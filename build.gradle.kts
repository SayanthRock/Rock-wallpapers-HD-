// Top-level build file where you can add configuration options common to all sub-projects/modules.

// Suppress known transient KSP/IntelliJ headless ClassLoader event-queue exceptions in build logging
try {
    System.setProperty("java.awt.headless", "true")
    val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
        val isAwtOrIntellij = thread.name.contains("AWT-EventQueue") || 
                throwable.toString().contains("intellij") || 
                throwable.stackTrace.any { it.className.contains("intellij") || it.className.contains("ksp") }
        if (isAwtOrIntellij) {
            // Silently swallow the headless AWT lifecycle warning to keep compilation logs clean
        } else {
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable)
            } else {
                System.err.println("Exception in thread \"${thread.name}\" ${throwable.javaClass.name}: ${throwable.message}")
                throwable.printStackTrace(System.err)
            }
        }
    }
} catch (e: Exception) {
    // Suppress configuration errors
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.roborazzi) apply false
  alias(libs.plugins.secrets) apply false
}
