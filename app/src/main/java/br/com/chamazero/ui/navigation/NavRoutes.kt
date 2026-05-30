package br.com.windfyr.ui.navigation

object NavRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val ADD_TERRAIN = "add_terrain"
    const val EDIT_TERRAIN = "edit_terrain/{terrainId}"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"

    fun editTerrain(terrainId: Int) = "edit_terrain/$terrainId"
}
