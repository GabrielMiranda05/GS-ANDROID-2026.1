package br.com.windfyr.data.remote

import br.com.windfyr.data.model.AddTerrainRequest
import br.com.windfyr.data.model.AuthResponse
import br.com.windfyr.data.model.LoginRequest
import br.com.windfyr.data.model.RegisterRequest
import br.com.windfyr.data.model.Terrain
import br.com.windfyr.data.model.UpdateProfileRequest
import br.com.windfyr.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("terrains")
    suspend fun getTerrains(): Response<List<Terrain>>

    @POST("terrains")
    suspend fun addTerrain(@Body request: AddTerrainRequest): Response<Terrain>

    @PUT("terrains/{id}")
    suspend fun updateTerrain(
        @Path("id") id: Int,
        @Body request: AddTerrainRequest
    ): Response<Terrain>

    @GET("user/profile")
    suspend fun getProfile(): Response<User>

    @PUT("user/profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<User>
}
