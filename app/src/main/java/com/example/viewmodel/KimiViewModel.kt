package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CloudSettingsRepository
import com.example.data.SyncStateInfo
import com.example.model.ActionChipType
import com.example.model.ChatHistoryItem
import com.example.model.ChatMessage
import com.example.model.CommonPhrase
import com.example.model.MessageSender
import com.example.model.ModelOption
import com.example.model.ThinkingEffort
import com.example.model.VoiceModeStatus
import com.example.model.VoiceSettings
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

enum class AppScreen {
  MAIN_CHAT,
  VOICE_MODE,
  ADD_COMMON_PHRASE
}

data class KimiUiState(
  val currentScreen: AppScreen = AppScreen.MAIN_CHAT,
  val selectedModel: ModelOption = ModelOption.INSTANT,
  val isModelPopupOpen: Boolean = false,
  val thinkingEffort: ThinkingEffort = ThinkingEffort.STANDARD,
  val isAttachmentSheetOpen: Boolean = false,
  val isSideDrawerOpen: Boolean = false,
  val isMuted: Boolean = false,
  val inputText: String = "",
  val activeSearchQuery: String = "",
  val messages: List<ChatMessage> = emptyList(),
  val historyItems: List<ChatHistoryItem> = listOf(
    ChatHistoryItem(
      id = "1",
      title = "AI UI Design",
      previewText = "Mobile assistant interface exploration",
      hasUnreadDot = false,
      previewTheme = 0
    ),
    ChatHistoryItem(
      id = "2",
      title = "Show me UI preview",
      previewText = "Glowing orb and dark mode components",
      hasUnreadDot = true,
      previewTheme = 1
    ),
    ChatHistoryItem(
      id = "3",
      title = "UI Design Preview",
      previewText = "Voice conversation flow & bottom sheet",
      hasUnreadDot = true,
      previewTheme = 2
    )
  ),
  val commonPhrases: List<CommonPhrase> = listOf(
    CommonPhrase(
      id = "cp_1",
      content = "Kimi. Please summarize the main point of the article in one concise sentence.",
      triggerWord = "summarize"
    )
  ),
  // Voice Mode State
  val voiceStatus: VoiceModeStatus = VoiceModeStatus.SPEAKING,
  val voiceResponseText: String = "Good to hear your voice! How are you?",
  val isVoiceSettingsOpen: Boolean = false,
  val isVoiceKeyboardOpen: Boolean = false,
  val voiceKeyboardInput: String = "",
  val voiceSettings: VoiceSettings = VoiceSettings(),
  val syncState: SyncStateInfo = SyncStateInfo()
)

class KimiViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

  private val _uiState = MutableStateFlow(KimiUiState())
  val uiState: StateFlow<KimiUiState> = _uiState.asStateFlow()

  private val cloudSettingsRepository = CloudSettingsRepository(application.applicationContext)

  private var textToSpeech: TextToSpeech? = null
  private var isTtsReady = false
  private var streamingJob: Job? = null
  private var voiceLoopJob: Job? = null

  init {
    try {
      textToSpeech = TextToSpeech(application.applicationContext, this)
    } catch (_: Exception) {}

    // Load initial cached local settings
    val initialSettings = cloudSettingsRepository.loadLocalSettings()
    _uiState.update { it.copy(voiceSettings = initialSettings) }

    // Start real-time Firebase cloud settings sync
    cloudSettingsRepository.startCloudSync { syncedSettings ->
      _uiState.update { it.copy(voiceSettings = syncedSettings) }
    }

    viewModelScope.launch {
      cloudSettingsRepository.syncState.collect { state ->
        _uiState.update { it.copy(syncState = state) }
      }
    }
  }

  override fun onInit(status: Int) {
    if (status == TextToSpeech.SUCCESS) {
      textToSpeech?.let { tts ->
        tts.language = Locale.US
        isTtsReady = true
      }
    }
  }

  fun setScreen(screen: AppScreen) {
    _uiState.update { it.copy(currentScreen = screen) }
    if (screen == AppScreen.VOICE_MODE) {
      startVoiceSession()
    } else {
      stopVoiceSpeaking()
    }
  }

  fun toggleModelPopup(open: Boolean? = null) {
    _uiState.update {
      it.copy(isModelPopupOpen = open ?: !it.isModelPopupOpen)
    }
  }

  fun selectModel(model: ModelOption) {
    _uiState.update {
      it.copy(selectedModel = model, isModelPopupOpen = false)
    }
  }

  fun cycleThinkingEffort() {
    _uiState.update {
      val next = when (it.thinkingEffort) {
        ThinkingEffort.STANDARD -> ThinkingEffort.EXTENDED
        ThinkingEffort.EXTENDED -> ThinkingEffort.QUICK
        ThinkingEffort.QUICK -> ThinkingEffort.STANDARD
      }
      it.copy(thinkingEffort = next)
    }
  }

  fun toggleAttachmentSheet(open: Boolean? = null) {
    _uiState.update {
      it.copy(isAttachmentSheetOpen = open ?: !it.isAttachmentSheetOpen)
    }
  }

  fun toggleSideDrawer(open: Boolean? = null) {
    _uiState.update {
      it.copy(isSideDrawerOpen = open ?: !it.isSideDrawerOpen)
    }
  }

  fun toggleMute() {
    _uiState.update {
      it.copy(isMuted = !it.isMuted)
    }
  }

  fun updateInputText(text: String) {
    _uiState.update { it.copy(inputText = text) }
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { it.copy(activeSearchQuery = query) }
  }

  fun sendMessage(overrideText: String? = null, attachmentName: String? = null) {
    val textToSend = overrideText ?: _uiState.value.inputText.trim()
    if (textToSend.isEmpty() && attachmentName == null) return

    val userMessage = ChatMessage(
      sender = MessageSender.USER,
      text = textToSend,
      attachmentName = attachmentName
    )

    _uiState.update {
      it.copy(
        messages = it.messages + userMessage,
        inputText = ""
      )
    }

    generateAiResponse(textToSend, attachmentName)
  }

  fun triggerActionChip(chip: ActionChipType) {
    val prompt = when (chip) {
      ActionChipType.SLIDES -> "Create a modern 5-slide presentation on AI product design"
      ActionChipType.SWARM -> "Run deep swarm analysis on the latest market trends"
      ActionChipType.WEBSITES -> "Design and generate a responsive dark mode website layout"
    }
    sendMessage(prompt)
  }

  private fun generateAiResponse(query: String, attachmentName: String? = null) {
    streamingJob?.cancel()
    streamingJob = viewModelScope.launch {
      val messageId = "kimi_${System.currentTimeMillis()}"
      val fullResponse = buildAiReply(query, attachmentName)

      val initialBotMessage = ChatMessage(
        id = messageId,
        sender = MessageSender.KIMI,
        text = "",
        isStreaming = true
      )

      _uiState.update { it.copy(messages = it.messages + initialBotMessage) }

      val words = fullResponse.split(" ")
      var currentText = ""
      for (i in words.indices) {
        currentText += if (i == 0) words[i] else " ${words[i]}"
        _uiState.update { state ->
          val updated = state.messages.map { msg ->
            if (msg.id == messageId) msg.copy(text = currentText) else msg
          }
          state.copy(messages = updated)
        }
        delay(35)
      }

      _uiState.update { state ->
        val updated = state.messages.map { msg ->
          if (msg.id == messageId) msg.copy(isStreaming = false) else msg
        }
        state.copy(messages = updated)
      }
    }
  }

  private fun buildAiReply(prompt: String, attachment: String?): String {
    val modelName = _uiState.value.selectedModel.displayPill
    return when {
      attachment != null ->
        "I've received your attachment **$attachment**. I analyzed the contents and can help extract insights, summarize key data points, or format the information for your project."
      prompt.contains("presentation", ignoreCase = true) || prompt.contains("slide", ignoreCase = true) ->
        "Here is the slide structure generated by $modelName:\n\n1. **Title**: The Future of Ambient AI\n2. **Architecture**: Multimodal Voice & Neural Swarms\n3. **Experience**: Dark Minimalist UI & Responsive Interactions\n4. **Performance**: Low Latency Engine (60 FPS Native)\n5. **Summary**: Production Readiness & Next Steps"
      prompt.contains("swarm", ignoreCase = true) ->
        "$modelName Swarm activated. Concurrent sub-agents dispatched across web datasets. Synthesizing cross-domain insights with standard thinking depth."
      prompt.contains("website", ignoreCase = true) ->
        "Drafting a modern responsive web layout featuring high-contrast typography, deep obsidian background (#0B0B0B), and floating interactive components."
      else ->
        "I'm Kimi, your intelligent AI assistant. I can assist with creative workflows, coding, data analysis, deep search, and voice interactions. How can I help you take this further?"
    }
  }

  // VOICE MODE LOGIC
  fun startVoiceSession() {
    val initialGreeting = if (_uiState.value.voiceSettings.opening) {
      "Good to hear your voice! How are you?"
    } else {
      "Start speaking"
    }

    _uiState.update {
      it.copy(
        voiceStatus = if (initialGreeting.startsWith("Good")) VoiceModeStatus.SPEAKING else VoiceModeStatus.LISTENING,
        voiceResponseText = initialGreeting,
        isVoiceSettingsOpen = false,
        isVoiceKeyboardOpen = false
      )
    }

    if (initialGreeting.startsWith("Good")) {
      speakVoiceText(initialGreeting)
    }

    runVoiceCycle()
  }

  fun toggleVoicePause() {
    _uiState.update {
      val nextStatus = if (it.voiceStatus == VoiceModeStatus.PAUSED) {
        VoiceModeStatus.LISTENING
      } else {
        VoiceModeStatus.PAUSED
      }
      it.copy(voiceStatus = nextStatus)
    }
  }

  fun interruptVoice() {
    if (_uiState.value.voiceSettings.voiceInterrupt) {
      stopVoiceSpeaking()
      _uiState.update {
        it.copy(
          voiceStatus = VoiceModeStatus.LISTENING,
          voiceResponseText = "Start speaking"
        )
      }
    }
  }

  fun sendVoiceKeyboardMessage(text: String) {
    if (text.isBlank()) return
    _uiState.update {
      it.copy(
        isVoiceKeyboardOpen = false,
        voiceKeyboardInput = "",
        voiceStatus = VoiceModeStatus.THINKING,
        voiceResponseText = "Thinking..."
      )
    }

    viewModelScope.launch {
      delay(700)
      val reply = when {
        text.contains("hello", ignoreCase = true) || text.contains("hi", ignoreCase = true) ->
          "Hello Mamta! I'm listening. What would you like to explore today?"
        text.contains("how are you", ignoreCase = true) ->
          "I'm feeling energized and ready to assist you with anything you need!"
        else ->
          "Got it. Processing \"$text\". Everything is looking sharp and seamless on Kimi!"
      }

      _uiState.update {
        it.copy(
          voiceStatus = VoiceModeStatus.SPEAKING,
          voiceResponseText = reply
        )
      }
      speakVoiceText(reply)
    }
  }

  private fun runVoiceCycle() {
    voiceLoopJob?.cancel()
    voiceLoopJob = viewModelScope.launch {
      delay(4000)
      if (_uiState.value.currentScreen == AppScreen.VOICE_MODE &&
        _uiState.value.voiceStatus == VoiceModeStatus.SPEAKING
      ) {
        _uiState.update {
          it.copy(
            voiceStatus = VoiceModeStatus.LISTENING,
            voiceResponseText = "Start speaking"
          )
        }
      }
    }
  }

  private fun speakVoiceText(text: String) {
    if (isTtsReady && !_uiState.value.isMuted) {
      try {
        val rate = when (_uiState.value.voiceSettings.speechRate) {
          "0.8x" -> 0.8f
          "1.2x" -> 1.2f
          "1.5x" -> 1.5f
          else -> 1.0f
        }
        textToSpeech?.setSpeechRate(rate)
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "KIMI_VOICE_ID")
      } catch (_: Exception) {}
    }
  }

  private fun stopVoiceSpeaking() {
    try {
      textToSpeech?.stop()
    } catch (_: Exception) {}
  }

  fun toggleVoiceSettings(open: Boolean? = null) {
    _uiState.update {
      it.copy(isVoiceSettingsOpen = open ?: !it.isVoiceSettingsOpen)
    }
  }

  fun updateVoiceSettings(updater: (VoiceSettings) -> VoiceSettings) {
    val updatedSettings = updater(_uiState.value.voiceSettings)
    _uiState.update {
      it.copy(voiceSettings = updatedSettings)
    }
    cloudSettingsRepository.syncSettingsToCloud(updatedSettings)
  }

  fun forceSyncCloudSettings() {
    cloudSettingsRepository.syncSettingsToCloud(_uiState.value.voiceSettings)
  }

  fun toggleVoiceKeyboard(open: Boolean? = null) {
    _uiState.update {
      it.copy(isVoiceKeyboardOpen = open ?: !it.isVoiceKeyboardOpen)
    }
  }

  fun updateVoiceKeyboardInput(text: String) {
    _uiState.update { it.copy(voiceKeyboardInput = text) }
  }

  fun addNewCommonPhrase(content: String, triggerWord: String) {
    if (content.isBlank()) return
    val newPhrase = CommonPhrase(
      id = "cp_${System.currentTimeMillis()}",
      content = content.trim(),
      triggerWord = triggerWord.trim()
    )
    _uiState.update {
      it.copy(
        commonPhrases = it.commonPhrases + newPhrase,
        currentScreen = AppScreen.MAIN_CHAT,
        isAttachmentSheetOpen = false
      )
    }
  }

  fun selectHistoryItem(item: ChatHistoryItem) {
    _uiState.update {
      it.copy(
        isSideDrawerOpen = false,
        messages = listOf(
          ChatMessage(
            sender = MessageSender.USER,
            text = item.title
          ),
          ChatMessage(
            sender = MessageSender.KIMI,
            text = "Here is the conversation for **${item.title}** (${item.previewText}). Feel free to ask more details."
          )
        )
      )
    }
  }

  fun startNewChat() {
    _uiState.update {
      it.copy(
        isSideDrawerOpen = false,
        messages = emptyList(),
        inputText = ""
      )
    }
  }

  override fun onCleared() {
    super.onCleared()
    cloudSettingsRepository.detachListener()
    try {
      textToSpeech?.shutdown()
    } catch (_: Exception) {}
  }
}
