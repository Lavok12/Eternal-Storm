package la.vok.Game.GameSystems.Map

import la.vok.Game.GameContent.Map.MapApi
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Map.MapSystem
import la.vok.Game.GameContent.TilesList
import la.vok.LavokLibrary.Vectors.p
import la.vok.State.AppState

class MapGenerator(var mapController: MapController) {
    val mapApi: MapApi get() = mapController.mapApi
    fun create(mapSystem: MapSystem) {
        AppState.logger.debug("GenerateMap")
        for (x in 0 until mapSystem.width) {
            for (y in 0 until mapSystem.height) {
                mapApi.placeTile(mapApi.getRegisteredTile(TilesList.stone), x p y)
            }
        }
        for (x in 0 until mapSystem.width) {
            mapApi.placeTile(mapApi.getRegisteredTile(TilesList.grass), x p 99)
        }
        for (x in 0 until mapSystem.width) {
            mapApi.placeTile(mapApi.getRegisteredTile(TilesList.dirt), x p 98)
        }
        for (x in 0 until mapSystem.width) {
            mapApi.placeTile(mapApi.getRegisteredTile(TilesList.dirt), x p 97)
        }
        for (x in 0 until mapSystem.width) {
            mapApi.placeTile(mapApi.getRegisteredTile(TilesList.dirt), x p 96)
        }
        for (x in 0 until mapSystem.width) {
            if (AppState.main.random(100f) < 50f) {
                mapApi.placeTile(mapApi.getRegisteredTile(TilesList.dirt), x p 95)
            }
        }
    }
}