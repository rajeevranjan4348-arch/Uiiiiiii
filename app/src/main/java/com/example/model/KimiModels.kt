package com.example.model

enum class ModelOption(val title: String, val subtitle: String, val displayPill: String) {
  K3(
    title = "K3",
    subtitle = "Chat & Agent, flagship\nall-rounder",
    displayPill = "Kimi K3"
  ),
  K3_SWARM(
    title = "K3 Swarm",
    subtitle = "Massive search, batch\nprocessing, and more in one\ngo",
    displayPill = "Kimi Swarm"
  ),
  INSTANT(
    title = "Instant",
    subtitle = "Fast chat, quick replies",
    displayPill = "Kimi Instant"
  )
}

enum class ThinkingEffort(val displayName: String) {
  STANDARD("Standard"),
  EXTENDED("Extended"),
  QUICK("Quick")
}

enum class VoiceModeStatus {
  LISTENING,
  SPEAKING,
  THINKING,
  PAUSED
}

data class VoiceSettings(
  val speechRate: String = "1.0x",
  val voicePlayback: String = "Tintin",
  val opening: Boolean = true,
  val voiceInterrupt: Boolean = true,
  val darkMode: Boolean = true,
  val keyboardInput: Boolean = true
)

enum class MessageSender {
  USER, KIMI
}

data class ChatMessage(
  val id: String = System.currentTimeMillis().toString(),
  val sender: MessageSender,
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val attachmentName: String? = null,
  val isStreaming: Boolean = false
)

data class ChatHistoryItem(
  val id: String,
  val title: String,
  val previewText: String = "",
  val timestamp: String = "Recent",
  val hasUnreadDot: Boolean = false,
  val previewTheme: Int = 0 // 0: dark grid, 1: purple gradient, 2: blue gradient
)

data class CommonPhrase(
  val id: String,
  val content: String,
  val triggerWord: String = ""
)

enum class ActionChipType(val label: String, val iconType: String) {
  SLIDES("Slides", "slides"),
  SWARM("Swarm", "swarm"),
  WEBSITES("Websites", "websites")
}
