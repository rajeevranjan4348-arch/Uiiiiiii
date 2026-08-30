package com.example.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.model.VoiceSettings
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class CloudSyncStatus {
  SYNCED,
  SYNCING,
  OFFLINE
}

data class SyncStateInfo(
  val status: CloudSyncStatus = CloudSyncStatus.OFFLINE,
  val lastSyncedTimeText: String = "Not synced yet",
  val message: String = "Local Cache Active"
)

class CloudSettingsRepository(private val context: Context) {

  private val prefs: SharedPreferences = context.getSharedPreferences("kimi_voice_settings_prefs", Context.MODE_PRIVATE)

  private val _syncState = MutableStateFlow(SyncStateInfo())
  val syncState: StateFlow<SyncStateInfo> = _syncState.asStateFlow()

  private var firestoreListener: ListenerRegistration? = null
  private val scope = CoroutineScope(Dispatchers.IO)

  companion object {
    private const val TAG = "CloudSettingsRepo"
    private const val PREF_SPEECH_RATE = "speech_rate"
    private const val PREF_VOICE_PLAYBACK = "voice_playback"
    private const val PREF_OPENING = "opening"
    private const val PREF_VOICE_INTERRUPT = "voice_interrupt"
    private const val PREF_DARK_MODE = "dark_mode"
    private const val PREF_KEYBOARD_INPUT = "keyboard_input"
    private const val PREF_DEVICE_USER_ID = "device_user_id"
  }

  init {
    ensureFirebaseInitialized()
    ensureUserIdentity()
  }

  private fun ensureFirebaseInitialized(): Boolean {
    return try {
      if (FirebaseApp.getApps(context).isEmpty()) {
        FirebaseApp.initializeApp(context)
      }
      FirebaseApp.getApps(context).isNotEmpty()
    } catch (e: Throwable) {
      Log.w(TAG, "FirebaseApp initialization skipped/failed: ${e.message}")
      false
    }
  }

  private fun getFirebaseAuth(): FirebaseAuth? {
    return try {
      if (ensureFirebaseInitialized()) FirebaseAuth.getInstance() else null
    } catch (e: Throwable) {
      Log.w(TAG, "FirebaseAuth unavailable: ${e.message}")
      null
    }
  }

  private fun getFirestore(): FirebaseFirestore? {
    return try {
      if (ensureFirebaseInitialized()) FirebaseFirestore.getInstance() else null
    } catch (e: Throwable) {
      Log.w(TAG, "FirebaseFirestore unavailable: ${e.message}")
      null
    }
  }

  private fun getUserId(): String {
    val authUser = getFirebaseAuth()?.currentUser
    if (authUser != null) {
      return authUser.uid
    }

    var cachedId = prefs.getString(PREF_DEVICE_USER_ID, null)
    if (cachedId.isNull_or_blank()) {
      val newId = "user_" + java.util.UUID.randomUUID().toString().take(12)
      prefs.edit().putString(PREF_DEVICE_USER_ID, newId).apply()
      return newId
    }
    return cachedId!!
  }

  private fun String?.isNull_or_blank(): Boolean = this == null || this.trim().isEmpty()

  fun loadLocalSettings(): VoiceSettings {
    return VoiceSettings(
      speechRate = prefs.getString(PREF_SPEECH_RATE, "1.0x") ?: "1.0x",
      voicePlayback = prefs.getString(PREF_VOICE_PLAYBACK, "Tintin") ?: "Tintin",
      opening = prefs.getBoolean(PREF_OPENING, true),
      voiceInterrupt = prefs.getBoolean(PREF_VOICE_INTERRUPT, true),
      darkMode = prefs.getBoolean(PREF_DARK_MODE, true),
      keyboardInput = prefs.getBoolean(PREF_KEYBOARD_INPUT, true)
    )
  }

  fun saveLocalSettings(settings: VoiceSettings) {
    prefs.edit()
      .putString(PREF_SPEECH_RATE, settings.speechRate)
      .putString(PREF_VOICE_PLAYBACK, settings.voicePlayback)
      .putBoolean(PREF_OPENING, settings.opening)
      .putBoolean(PREF_VOICE_INTERRUPT, settings.voiceInterrupt)
      .putBoolean(PREF_DARK_MODE, settings.darkMode)
      .putBoolean(PREF_KEYBOARD_INPUT, settings.keyboardInput)
      .apply()
  }

  fun startCloudSync(onCloudSettingsUpdated: (VoiceSettings) -> Unit) {
    _syncState.value = SyncStateInfo(
      status = CloudSyncStatus.SYNCING,
      lastSyncedTimeText = getCurrentTimeFormatted(),
      message = "Connecting to Firebase..."
    )

    val userId = getUserId()
    val firestore = getFirestore()

    if (firestore == null) {
      _syncState.value = SyncStateInfo(
        status = CloudSyncStatus.OFFLINE,
        lastSyncedTimeText = getCurrentTimeFormatted(),
        message = "Offline (Local backup active)"
      )
      return
    }

    try {
      val docRef = firestore.collection("user_settings").document(userId)

      firestoreListener?.remove()
      firestoreListener = docRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
          Log.e(TAG, "Firestore sync error", error)
          _syncState.value = SyncStateInfo(
            status = CloudSyncStatus.OFFLINE,
            lastSyncedTimeText = getCurrentTimeFormatted(),
            message = "Offline (Local backup active)"
          )
          return@addSnapshotListener
        }

        if (snapshot != null && snapshot.exists()) {
          val data = snapshot.data
          if (data != null) {
            val cloudSpeechRate = data["speechRate"] as? String ?: "1.0x"
            val cloudVoicePlayback = data["voicePlayback"] as? String ?: "Tintin"
            val cloudOpening = data["opening"] as? Boolean ?: true
            val cloudVoiceInterrupt = data["voiceInterrupt"] as? Boolean ?: true
            val cloudDarkMode = data["darkMode"] as? Boolean ?: true
            val cloudKeyboardInput = data["keyboardInput"] as? Boolean ?: true

            val syncedSettings = VoiceSettings(
              speechRate = cloudSpeechRate,
              voicePlayback = cloudVoicePlayback,
              opening = cloudOpening,
              voiceInterrupt = cloudVoiceInterrupt,
              darkMode = cloudDarkMode,
              keyboardInput = cloudKeyboardInput
            )

            saveLocalSettings(syncedSettings)
            onCloudSettingsUpdated(syncedSettings)

            _syncState.value = SyncStateInfo(
              status = CloudSyncStatus.SYNCED,
              lastSyncedTimeText = getCurrentTimeFormatted(),
              message = "Firebase Cloud Synced"
            )
          }
        } else {
          // Document does not exist yet; create initial cloud copy from local settings
          val currentLocal = loadLocalSettings()
          syncSettingsToCloud(currentLocal)
        }
      }
    } catch (e: Throwable) {
      Log.e(TAG, "Firebase initialization error", e)
      _syncState.value = SyncStateInfo(
        status = CloudSyncStatus.OFFLINE,
        lastSyncedTimeText = getCurrentTimeFormatted(),
        message = "Local Cache Active"
      )
    }
  }

  fun syncSettingsToCloud(settings: VoiceSettings) {
    saveLocalSettings(settings)

    _syncState.value = SyncStateInfo(
      status = CloudSyncStatus.SYNCING,
      lastSyncedTimeText = getCurrentTimeFormatted(),
      message = "Uploading to Firebase..."
    )

    scope.launch {
      try {
        val userId = getUserId()
        val firestore = getFirestore()

        if (firestore == null) {
          _syncState.value = SyncStateInfo(
            status = CloudSyncStatus.OFFLINE,
            lastSyncedTimeText = getCurrentTimeFormatted(),
            message = "Saved locally"
          )
          return@launch
        }

        val map = hashMapOf<String, Any>(
          "speechRate" to settings.speechRate,
          "voicePlayback" to settings.voicePlayback,
          "opening" to settings.opening,
          "voiceInterrupt" to settings.voiceInterrupt,
          "darkMode" to settings.darkMode,
          "keyboardInput" to settings.keyboardInput,
          "updatedAt" to System.currentTimeMillis()
        )

        firestore.collection("user_settings")
          .document(userId)
          .set(map, SetOptions.merge())
          .addOnSuccessListener {
            _syncState.value = SyncStateInfo(
              status = CloudSyncStatus.SYNCED,
              lastSyncedTimeText = getCurrentTimeFormatted(),
              message = "Firebase Cloud Synced"
            )
          }
          .addOnFailureListener { e ->
            Log.e(TAG, "Failed to write settings to Firestore", e)
            _syncState.value = SyncStateInfo(
              status = CloudSyncStatus.OFFLINE,
              lastSyncedTimeText = getCurrentTimeFormatted(),
              message = "Offline (Saved locally)"
            )
          }
      } catch (e: Throwable) {
        Log.e(TAG, "Firestore write exception", e)
        _syncState.value = SyncStateInfo(
          status = CloudSyncStatus.OFFLINE,
          lastSyncedTimeText = getCurrentTimeFormatted(),
          message = "Saved locally"
        )
      }
    }
  }

  private fun ensureUserIdentity() {
    try {
      val auth = getFirebaseAuth() ?: return
      if (auth.currentUser == null) {
        // Authenticate anonymously if unauthenticated so Firebase Auth rules work seamlessly
        auth.signInAnonymously()
          .addOnCompleteListener { task ->
            if (task.isSuccessful) {
              Log.d(TAG, "Signed in with Firebase user ID: ${auth.currentUser?.uid}")
            }
          }
      }
    } catch (e: Throwable) {
      Log.w(TAG, "Firebase auth optional setup notice", e)
    }
  }

  private fun getCurrentTimeFormatted(): String {
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(Date())
  }

  fun detachListener() {
    firestoreListener?.remove()
    firestoreListener = null
  }
}
