package br.com.windfyr.data.repository

import br.com.windfyr.data.model.AuthResponse
import br.com.windfyr.data.model.LoginRequest
import br.com.windfyr.data.model.RegisterRequest
import br.com.windfyr.data.remote.RetrofitClient
import br.com.windfyr.util.Resource
import com.google.gson.Gson
import com.google.gson.JsonObject

class AuthRepository {

    private val api = RetrofitClient.instance
    private val gson = Gson()

    suspend fun login(email: String, password: String): Resource<AuthResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string(), "Credenciais inválidas")
                Resource.Error(errorMsg)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    suspend fun register(name: String, email: String, password: String): Resource<AuthResponse> {
        return try {
            val response = api.register(RegisterRequest(name, email, password))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                val errorMsg = parseErrorMessage(response.errorBody()?.string(), "Erro ao criar conta")
                Resource.Error(errorMsg)
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    private fun parseErrorMessage(errorBody: String?, fallback: String): String {
        return try {
            val json = gson.fromJson(errorBody, JsonObject::class.java)
            json.get("error")?.asString ?: fallback
        } catch (e: Exception) {
            fallback
        }
    }
}
