package la.vok.Game.GameContent.TileTypes

import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.State.AppState

class SmallGrassTileType : AbstractTileType() {
    override val tag: String = TilesList.small_grass
    override var collisionType = CollisionType.NONE
    override val texture: String = AppState.res("small_grass.png")
    override val placeType = TilePlaceType.ON_TILE
    override val canBeReplaced = true
    override val renderConfig = TileRenderConfig(AOShadow = false)
    
    override val blockStrength: Int = 0
    override val maxHp: Int = 1
    override val breakIfInvalid = true
    override val tags: Set<String> = setOf(la.vok.Game.GameContent.ContentList.BlockTags.PLANT)

    override fun onMined(x: Int, y: Int, mineData: la.vok.Game.GameSystems.WorldSystems.Map.MineData, dimension: AbstractDimension, mapController: MapController) {
        if (mineData.item?.itemType?.hasTag(la.vok.Game.GameContent.ContentList.ItemTags.HOE) == true) {
            if (kotlin.random.Random.nextFloat() <= 0.2f) { // 20% chance
                val pos = dimension.gameCycle.mapApi.getBlockPos(x, y)
                val spawnPos = la.vok.LavokLibrary.Vectors.Vec2(pos.x + 0.5f, pos.y + 0.5f)
                dimension.gameCycle.itemsApi.spawnItemEntity(dimension, la.vok.Game.GameContent.ContentList.ItemsList.wheat_seeds, spawnPos, 1, true)
            }
        }
        super.onMined(x, y, mineData, dimension, mapController)
    }
}
