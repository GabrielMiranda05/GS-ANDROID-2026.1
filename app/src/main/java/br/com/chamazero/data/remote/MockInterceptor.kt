package br.com.chamazero.data.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

class MockInterceptor : Interceptor {

    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        val method = request.method

        val (code, body) = when {
            url.contains("auth/login") && method == "POST" -> handleLogin(request)
            url.contains("auth/register") && method == "POST" -> handleRegister(request)
            url.contains("terrains") && method == "GET" -> handleGetTerrains()
            url.contains("terrains") && method == "POST" -> handleAddTerrain(request)
            url.contains("terrains") && method == "PUT" -> handleUpdateTerrain(request, url)
            url.contains("user/profile") && method == "GET" -> handleGetProfile()
            url.contains("user/profile") && method == "PUT" -> handleUpdateProfile(request)
            else -> 404 to """{"error":"Not found"}"""
        }

        return Response.Builder()
            .code(code)
            .message(if (code == 200) "OK" else "Error")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(body.toResponseBody("application/json".toMediaTypeOrNull()))
            .addHeader("content-type", "application/json")
            .build()
    }

    private fun handleLogin(request: okhttp3.Request): Pair<Int, String> {
        val bodyString = request.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: return 400 to """{"error":"Bad request"}"""

        val loginReq = gson.fromJson(bodyString, Map::class.java)
        val email = loginReq["email"] as? String ?: return 400 to """{"error":"Bad request"}"""
        val password = loginReq["password"] as? String ?: return 400 to """{"error":"Bad request"}"""

        val user = MockDataStore.findUser(email, password)
            ?: return 401 to """{"error":"Credenciais inválidas"}"""

        val token = "mock_token_${user.id}_${System.currentTimeMillis()}"
        MockDataStore.setCurrentUserId(user.id)

        return 200 to gson.toJson(mapOf(
            "token" to token,
            "user" to mapOf(
                "id" to user.id,
                "name" to user.name,
                "email" to user.email,
                "initials" to user.initials
            )
        ))
    }

    private fun handleRegister(request: okhttp3.Request): Pair<Int, String> {
        val bodyString = request.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: return 400 to """{"error":"Bad request"}"""

        val regReq = gson.fromJson(bodyString, Map::class.java)
        val name = regReq["name"] as? String ?: return 400 to """{"error":"Bad request"}"""
        val email = regReq["email"] as? String ?: return 400 to """{"error":"Bad request"}"""
        val password = regReq["password"] as? String ?: return 400 to """{"error":"Bad request"}"""

        if (MockDataStore.emailExists(email)) {
            return 409 to """{"error":"E-mail já cadastrado"}"""
        }

        val nameParts = name.trim().split(" ")
        val initials = if (nameParts.size >= 2) {
            "${nameParts.first().first()}${nameParts.last().first()}".uppercase()
        } else {
            name.take(2).uppercase()
        }

        val user = MockDataStore.createUser(name, email, password, initials)
        val token = "mock_token_${user.id}_${System.currentTimeMillis()}"
        MockDataStore.setCurrentUserId(user.id)

        return 200 to gson.toJson(mapOf(
            "token" to token,
            "user" to mapOf(
                "id" to user.id,
                "name" to user.name,
                "email" to user.email,
                "initials" to user.initials
            )
        ))
    }

    private fun handleGetTerrains(): Pair<Int, String> {
        val userId = MockDataStore.getCurrentUserId()
        val terrains = MockDataStore.getTerrains(userId)
        return 200 to gson.toJson(terrains)
    }

    private fun handleAddTerrain(request: okhttp3.Request): Pair<Int, String> {
        val bodyString = request.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: return 400 to """{"error":"Bad request"}"""

        val req = gson.fromJson(bodyString, Map::class.java)
        val userId = MockDataStore.getCurrentUserId()

        val terrain = MockDataStore.addTerrain(
            userId = userId,
            name = req["name"] as? String ?: "",
            hectares = (req["hectares"] as? Double) ?: 0.0,
            cropType = req["cropType"] as? String ?: "Soja",
            state = req["state"] as? String ?: "",
            city = req["city"] as? String ?: "",
            neighborhood = req["neighborhood"] as? String ?: "",
            street = req["street"] as? String ?: "",
            number = req["number"] as? String ?: "",
            latitude = (req["latitude"] as? Double) ?: 0.0,
            longitude = (req["longitude"] as? Double) ?: 0.0
        )

        return 200 to gson.toJson(terrain)
    }

    private fun handleUpdateTerrain(request: okhttp3.Request, url: String): Pair<Int, String> {
        val terrainId = url.substringAfterLast("/").toIntOrNull()
            ?: return 400 to """{"error":"ID inválido"}"""

        val bodyString = request.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: return 400 to """{"error":"Bad request"}"""

        val req = gson.fromJson(bodyString, Map::class.java)

        val terrain = MockDataStore.updateTerrain(
            terrainId = terrainId,
            name = req["name"] as? String ?: "",
            hectares = (req["hectares"] as? Double) ?: 0.0,
            cropType = req["cropType"] as? String ?: "Soja",
            state = req["state"] as? String ?: "",
            city = req["city"] as? String ?: "",
            neighborhood = req["neighborhood"] as? String ?: "",
            street = req["street"] as? String ?: "",
            number = req["number"] as? String ?: "",
            latitude = (req["latitude"] as? Double) ?: 0.0,
            longitude = (req["longitude"] as? Double) ?: 0.0
        ) ?: return 404 to """{"error":"Terreno não encontrado"}"""

        return 200 to gson.toJson(terrain)
    }

    private fun handleUpdateProfile(request: okhttp3.Request): Pair<Int, String> {
        val bodyString = request.body?.let {
            val buffer = Buffer()
            it.writeTo(buffer)
            buffer.readUtf8()
        } ?: return 400 to """{"error":"Bad request"}"""

        val req = gson.fromJson(bodyString, Map::class.java)
        val name = req["name"] as? String ?: return 400 to """{"error":"Bad request"}"""
        val email = req["email"] as? String ?: return 400 to """{"error":"Bad request"}"""
        val userId = MockDataStore.getCurrentUserId()

        val user = MockDataStore.updateUser(userId, name, email)
            ?: return 404 to """{"error":"Usuário não encontrado"}"""

        return 200 to gson.toJson(mapOf(
            "id" to user.id,
            "name" to user.name,
            "email" to user.email,
            "initials" to user.initials
        ))
    }

    private fun handleGetProfile(): Pair<Int, String> {
        val userId = MockDataStore.getCurrentUserId()
        val user = MockDataStore.getUserById(userId)
            ?: return 404 to """{"error":"Usuário não encontrado"}"""

        return 200 to gson.toJson(mapOf(
            "id" to user.id,
            "name" to user.name,
            "email" to user.email,
            "initials" to user.initials
        ))
    }
}
