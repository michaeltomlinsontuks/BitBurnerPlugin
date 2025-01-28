package com.github.michaeltomlinsontuks.bitburnerplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import io.ktor.client.*
import kotlinx.coroutines.runBlocking

class GetDefinitionFileAction : AnAction("Get Definition File from Bitburner") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val authToken = BitburnerSettings.getInstance().authToken
        val serverUrl = GameConfig.getServerUrl()
        val apiService = BitburnerApiService()

        try {
            val response = runBlocking {
                apiService.getDefinitionFile(1, authToken)
            }

            if (response.error == null) {
                Messages.showMessageDialog(project, "Definition file content: ${response.result}", "Success", Messages.getInformationIcon())
            } else {
                Messages.showMessageDialog(project, "Failed to get definition file: ${response.error}", "Error", Messages.getErrorIcon())
            }
        } catch (ex: Exception) {
            Messages.showMessageDialog(project, "An error occurred: ${ex.message}", "Error", Messages.getErrorIcon())
        } finally {
            // client.close() is removed because the client is no longer created
        }
    }
}