import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.*

suspend fun main() {
    val authToken = "YOUR_AUTH_TOKEN" // Replace with your actual AUTH token
    val client = HttpClient {
        install(WebSockets)
    }

    client.webSocket(method = HttpMethod.Get, host = "localhost", port = 8080, path = "/ws") {
        // Send the AUTH token for authentication
        send(Frame.Text(authToken))

        // Handle incoming messages
        for (message in incoming) {
            message as? Frame.Text ?: continue
            println("Received: ${message.readText()}")
        }
    }

    client.close()
}