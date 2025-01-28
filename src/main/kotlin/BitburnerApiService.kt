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
            response.body<JsonRpcResponse<T>>()
        } catch (e: Exception) {
            JsonRpcResponse(jsonrpc = "2.0", id = request.id, result = null, error = e.message)
        }
    }

    fun pushFile(id: Int, filename: String, content: String, server: String, authToken: String): JsonRpcResponse<String> = runBlocking {
        val params = mapOf("filename" to filename, "content" to content, "server" to server)
        sendRequest(JsonRpcRequest(id = id, method = "pushFile", params = params), authToken)
    }

    fun getFile(id: Int, filename: String, server: String, authToken: String): JsonRpcResponse<String> = runBlocking {
        val params = mapOf("filename" to filename, "server" to server)
        sendRequest(JsonRpcRequest(id = id, method = "getFile", params = params), authToken)
    }

    suspend fun deleteFile(id: Int, filename: String, server: String, authToken: String): JsonRpcResponse<String> {
        val response: HttpResponse = client.post("$serverUrl/deleteFile") {
            header("Authorization", "Bearer $authToken")
            contentType(ContentType.Application.Json)
            setBody(mapOf("jsonrpc" to "2.0", "id" to id, "method" to "deleteFile", "params" to mapOf("filename" to filename, "server" to server)))
        }
        return response.body()
    }

    // Other methods...
}