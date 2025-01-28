import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.ui.Messages
import io.ktor.client.*
import kotlinx.coroutines.runBlocking

class PushFileAction : AnAction("Push File to Bitburner") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFile = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE) ?: return

        val authToken = BitburnerSettings.getInstance().authToken
        val serverUrl = "http://localhost:8080" // Replace with your actual server URL
        val client = HttpClient()
        val apiService = BitburnerApiService(client, serverUrl)

        val filename = virtualFile.name
        val content = String(virtualFile.contentsToByteArray())
        val server = "home" // Replace with your actual server name

        val response = runBlocking {
            apiService.pushFile(1, filename, content, server, authToken)
        }

        if (response.error == null) {
            Messages.showMessageDialog(project, "File pushed successfully!", "Success", Messages.getInformationIcon())
        } else {
            Messages.showMessageDialog(project, "Failed to push file: ${response.error}", "Error", Messages.getErrorIcon())
        }
    }
}