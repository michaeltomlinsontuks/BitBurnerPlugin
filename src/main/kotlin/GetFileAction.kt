package com.github.michaeltomlinsontuks.bitburnerplugin

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.ui.Messages
import io.ktor.client.*
import kotlinx.coroutines.runBlocking

class GetFileAction : AnAction("Get File from Bitburner") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFile = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return

        val authToken = BitburnerSettings.getInstance().authToken
        val serverUrl = GameConfig.getServerUrl()
        val apiService = BitburnerApiService()

        val filename = virtualFile.name
        val server = "home" // Replace with your actual server name

        try {
            val response = runBlocking {
                apiService.getFile(1, filename, server, authToken)
            }

            if (response.error == null) {
                Messages.showMessageDialog(project, "File content: ${response.result}", "Success", Messages.getInformationIcon())
            } else {
                Messages.showMessageDialog(project, "Failed to get file: ${response.error}", "Error", Messages.getErrorIcon())
            }
        } catch (ex: Exception) {
            Messages.showMessageDialog(project, "An error occurred: ${ex.message}", "Error", Messages.getErrorIcon())
        } finally {
            // client.close() is removed because the new constructor does not create a client
        }
    }
}