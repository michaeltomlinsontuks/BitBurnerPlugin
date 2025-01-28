import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import io.ktor.client.*
import kotlinx.coroutines.runBlocking

class GetAllFilesAction : AnAction("Get All Files from Bitburner") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val authToken = BitburnerSettings.getInstance().authToken
        val serverUrl = GameConfig.getServerUrl()
        val apiService = BitburnerApiService()

        val server = "home" // Replace with your actual server name

        try {
            val response = runBlocking {
                apiService.getAllFiles(1, server, authToken)
            }

            if (response.error == null) {
                val fileContents = response.result?.joinToString("\n") { "${it["filename"]}: ${it["content"]}" }
                Messages.showMessageDialog(project, "Files:\n$fileContents", "Success", Messages.getInformationIcon())
            } else {
                Messages.showMessageDialog(project, "Failed to get files: ${response.error}", "Error", Messages.getErrorIcon())
            }
        } catch (ex: Exception) {
            Messages.showMessageDialog(project, "An error occurred: ${ex.message}", "Error", Messages.getErrorIcon())
        } finally {
            // client.close() // Removed this line as client is no longer created
        }
    }
}