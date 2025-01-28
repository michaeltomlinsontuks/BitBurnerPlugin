import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class BitburnerApiService(private val client: HttpClient, private val serverUrl: String) {

    data class JsonRpcRequest(val jsonrpc: String = "2.0", val id: Int, val method: String, val params: Any)
    data class JsonRpcResponse<T>(val jsonrpc: String, val id: Int, val result: T?, val error: String?)

    private suspend inline fun <reified T> sendRequest(request: JsonRpcRequest, authToken: String): JsonRpcResponse<T> {
        return try {
            val response: HttpResponse = client.post(serverUrl) {
                contentType(ContentType.Application.Json)
                header("Authorization", "Bearer $authToken")
                setBody(request)
            }
            response.body()
        } catch (e: Exception) {
            JsonRpcResponse(jsonrpc = "2.0", id = request.id, result = null, error = e.message)
        }
    }

    fun pushFile(id: Int, filename: String, content: String, server: String, authToken: String): JsonRpcResponse<String> = runBlocking {
        val params = mapOf("filename" to filename, "content" to content, "server" to server)
        sendRequest(JsonRpcRequest(id = id, method = "pushFile", params = params), authToken)
    }

    // Other methods...
}