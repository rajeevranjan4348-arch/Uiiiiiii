package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.ui.components.SideDrawerContent
import com.example.ui.screens.AddCommonPhraseScreen
import com.example.ui.screens.MainChatScreen
import com.example.ui.screens.VoiceModeScreen
import com.example.ui.theme.KimiBackgroundElevated
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.KimiViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: KimiViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        KimiApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun KimiApp(viewModel: KimiViewModel) {
  val uiState by viewModel.uiState.collectAsState()
  val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
  val coroutineScope = rememberCoroutineScope()

  // Sync drawer state with ViewModel
  LaunchedEffect(uiState.isSideDrawerOpen) {
    if (uiState.isSideDrawerOpen && drawerState.isClosed) {
      drawerState.open()
    } else if (!uiState.isSideDrawerOpen && drawerState.isOpen) {
      drawerState.close()
    }
  }

  LaunchedEffect(drawerState.isOpen) {
    if (drawerState.isOpen != uiState.isSideDrawerOpen) {
      viewModel.toggleSideDrawer(drawerState.isOpen)
    }
  }

  // Handle Back Navigation smoothly
  BackHandler(
    enabled = drawerState.isOpen ||
      uiState.isModelPopupOpen ||
      uiState.isAttachmentSheetOpen ||
      uiState.isVoiceSettingsOpen ||
      uiState.currentScreen != AppScreen.MAIN_CHAT
  ) {
    when {
      drawerState.isOpen -> {
        coroutineScope.launch { drawerState.close() }
      }
      uiState.isModelPopupOpen -> {
        viewModel.toggleModelPopup(false)
      }
      uiState.isAttachmentSheetOpen -> {
        viewModel.toggleAttachmentSheet(false)
      }
      uiState.isVoiceSettingsOpen -> {
        viewModel.toggleVoiceSettings(false)
      }
      uiState.currentScreen == AppScreen.VOICE_MODE -> {
        viewModel.setScreen(AppScreen.MAIN_CHAT)
      }
      uiState.currentScreen == AppScreen.ADD_COMMON_PHRASE -> {
        viewModel.setScreen(AppScreen.MAIN_CHAT)
      }
    }
  }

  ModalNavigationDrawer(
    drawerState = drawerState,
    gesturesEnabled = uiState.currentScreen == AppScreen.MAIN_CHAT,
    drawerContent = {
      ModalDrawerSheet(
        drawerContainerColor = KimiBackgroundElevated,
        drawerShape = RectangleShape,
        modifier = Modifier.fillMaxSize(fraction = 0.86f)
      ) {
        SideDrawerContent(
          historyItems = uiState.historyItems,
          searchQuery = uiState.activeSearchQuery,
          onSearchChange = { viewModel.updateSearchQuery(it) },
          onNewChat = {
            viewModel.startNewChat()
            coroutineScope.launch { drawerState.close() }
          },
          onHistoryItemClick = { item ->
            viewModel.selectHistoryItem(item)
            coroutineScope.launch { drawerState.close() }
          }
        )
      }
    }
  ) {
    AnimatedContent(
      targetState = uiState.currentScreen,
      transitionSpec = {
        when (targetState) {
          AppScreen.VOICE_MODE -> {
            (slideInVertically(tween(320)) { it / 4 } + fadeIn(tween(320)))
              .togetherWith(fadeOut(tween(200)))
          }
          AppScreen.ADD_COMMON_PHRASE -> {
            (slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)))
              .togetherWith(slideOutHorizontally(tween(250)) { -it / 3 } + fadeOut(tween(250)))
          }
          AppScreen.MAIN_CHAT -> {
            fadeIn(tween(250)).togetherWith(fadeOut(tween(200)))
          }
        }
      },
      label = "ScreenTransition"
    ) { screen ->
      when (screen) {
        AppScreen.MAIN_CHAT -> {
          MainChatScreen(
            selectedModel = uiState.selectedModel,
            isModelPopupOpen = uiState.isModelPopupOpen,
            thinkingEffort = uiState.thinkingEffort,
            isAttachmentSheetOpen = uiState.isAttachmentSheetOpen,
            isMuted = uiState.isMuted,
            inputText = uiState.inputText,
            messages = uiState.messages,
            onMenuClick = {
              coroutineScope.launch { drawerState.open() }
            },
            onModelSelectorClick = { viewModel.toggleModelPopup() },
            onSpeakerClick = { viewModel.toggleMute() },
            onSelectModel = { viewModel.selectModel(it) },
            onCycleThinkingEffort = { viewModel.cycleThinkingEffort() },
            onDismissModelPopup = { viewModel.toggleModelPopup(false) },
            onToggleAttachmentSheet = { viewModel.toggleAttachmentSheet(it) },
            onAttachmentSelect = { name ->
              viewModel.sendMessage(attachmentName = name)
              viewModel.toggleAttachmentSheet(false)
            },
            onCommonPhrasesClick = {
              viewModel.toggleAttachmentSheet(false)
              viewModel.setScreen(AppScreen.ADD_COMMON_PHRASE)
            },
            onVoiceClick = {
              viewModel.setScreen(AppScreen.VOICE_MODE)
            },
            onInputChange = { viewModel.updateInputText(it) },
            onSendMessage = { viewModel.sendMessage() },
            onChipClick = { chip -> viewModel.triggerActionChip(chip) }
          )
        }

        AppScreen.VOICE_MODE -> {
          VoiceModeScreen(
            voiceStatus = uiState.voiceStatus,
            responseText = uiState.voiceResponseText,
            isVoiceSettingsOpen = uiState.isVoiceSettingsOpen,
            isKeyboardOpen = uiState.isVoiceKeyboardOpen,
            keyboardInput = uiState.voiceKeyboardInput,
            voiceSettings = uiState.voiceSettings,
            syncState = uiState.syncState,
            onBackToChat = { viewModel.setScreen(AppScreen.MAIN_CHAT) },
            onToggleSettings = { viewModel.toggleVoiceSettings() },
            onUpdateSettings = { updater -> viewModel.updateVoiceSettings(updater) },
            onForceSync = { viewModel.forceSyncCloudSettings() },
            onTogglePause = { viewModel.toggleVoicePause() },
            onEndCall = { viewModel.setScreen(AppScreen.MAIN_CHAT) },
            onInterrupt = { viewModel.interruptVoice() },
            onToggleKeyboard = { viewModel.toggleVoiceKeyboard(it) },
            onKeyboardInputChange = { viewModel.updateVoiceKeyboardInput(it) },
            onSendKeyboardMessage = { text -> viewModel.sendVoiceKeyboardMessage(text) }
          )
        }

        AppScreen.ADD_COMMON_PHRASE -> {
          AddCommonPhraseScreen(
            onBack = { viewModel.setScreen(AppScreen.MAIN_CHAT) },
            onSavePhrase = { content, trigger ->
              viewModel.addNewCommonPhrase(content, trigger)
            }
          )
        }
      }
    }
  }
}
