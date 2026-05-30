package br.com.chamazero.data.repository

import br.com.chamazero.data.model.UpdateProfileRequest
import br.com.chamazero.data.model.User
import br.com.chamazero.data.remote.RetrofitClient
import br.com.chamazero.util.Resource

class UserRepository {

    private val api = RetrofitClient.instance

    suspend fun getProfile(): Resource<User> {
        return try {
            val response = api.getProfile()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erro ao carregar perfil")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    suspend fun updateProfile(name: String, email: String): Resource<User> {
        return try {
            val response = api.updateProfile(UpdateProfileRequest(name, email))
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erro ao atualizar perfil")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }
}
