package br.com.windfyr.data.repository

import br.com.windfyr.data.model.AddTerrainRequest
import br.com.windfyr.data.model.Terrain
import br.com.windfyr.data.remote.RetrofitClient
import br.com.windfyr.util.Resource

class TerrainRepository {

    private val api = RetrofitClient.instance

    suspend fun getTerrains(): Resource<List<Terrain>> {
        return try {
            val response = api.getTerrains()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erro ao carregar terrenos")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    suspend fun addTerrain(request: AddTerrainRequest): Resource<Terrain> {
        return try {
            val response = api.addTerrain(request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erro ao cadastrar terreno")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }

    suspend fun updateTerrain(id: Int, request: AddTerrainRequest): Resource<Terrain> {
        return try {
            val response = api.updateTerrain(id, request)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Erro ao atualizar terreno")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Erro desconhecido")
        }
    }
}
