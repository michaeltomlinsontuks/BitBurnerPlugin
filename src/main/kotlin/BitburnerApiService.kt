import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

class BitburnerApiService(private val client: HttpClient) {

    data class JsonRpcRequest(val jsonrpc: String = "2.0", val id: Int, val method: String, val params: Any? = null)
    data class JsonRpcResponse<T>(val jsonrpc: String, val id: Int, val result: T?, val error: String?)

    private suspend inline fun <reified T> sendRequest(request: JsonRpcRequest, authToken: String): JsonRpcResponse<T> {
        return try {
            val response: HttpResponse = client.post(GameConfig.getServerUrl()) {
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
        val params = mapOf("filename" to filename, "server" to server)
        return sendRequest(JsonRpcRequest(id = id, method = "deleteFile", params = params), authToken)
    }

    suspend fun getFileNames(id: Int, server: String, authToken: String): JsonRpcResponse<List<String>> {
        val params = mapOf("server" to server)
        return sendRequest(JsonRpcRequest(id = id, method = "getFileNames", params = params), authToken)
    }

    suspend fun getAllFiles(id: Int, server: String, authToken: String): JsonRpcResponse<List<Map<String, String>>> {
        val params = mapOf("server" to server)
        return sendRequest(JsonRpcRequest(id = id, method = "getAllFiles", params = params), authToken)
    }

    suspend fun calculateRam(id: Int, filename: String, server: String, authToken: String): JsonRpcResponse<Int> {
        val params = mapOf("filename" to filename, "server" to server)
        return sendRequest(JsonRpcRequest(id = id, method = "calculateRam", params = params), authToken)
    }

    suspend fun getDefinitionFile(id: Int, authToken: String): JsonRpcResponse<String> {
        return sendRequest(JsonRpcRequest(id = id, method = "getDefinitionFile"), authToken)
    }

    // Other methods...
}