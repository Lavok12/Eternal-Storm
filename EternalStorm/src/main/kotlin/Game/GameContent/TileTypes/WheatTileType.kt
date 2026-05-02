package la.vok.Game.GameContent.TileTypes

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.ContentList.ItemsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.Game.GameContent.TileData.WheatTileData
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.Game.GameController.CollisionType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.State.AppState
import kotlin.random.Random

class WheatTileType : AbstractTileType() {
    override val tag: String = TilesList.wheat_block
    override var collisionType = CollisionType.NONE
    override val blockStrength: Int = 0
    override val maxHp: Int = 1
    override val texture: String = AppState.res("seed_1.png")
    override val drop: DropEntry = NothingDrop
    override val placeType = TilePlaceType.CUSTOM
    override val breakIfInvalid = true
    override val tags: Set<String> = setOf(la.vok.Game.GameContent.ContentList.BlockTags.PLANT)
    override val renderConfig = la.vok.Game.GameContent.Tiles.System.TileRenderConfig(AOShadow = false)

    override fun createTileData(x: Int, y: Int, dimension: AbstractDimension): AbstractTileData? {
        return WheatTileData(x, y, dimension)
    }

    override fun canPlace(x: Int, y: Int, dimension: AbstractDimension, mapController: MapController): Boolean {
        val typeBelow = dimension.gameCycle.mapApi.getTileType(dimension, x, y - 1)
        return typeBelow?.tag == TilesList.farmland_block
    }

    override fun render(
        pointX: Int,
        pointY: Int,
        lg: LGraphics,
        positionX: Float,
        positionY: Float,
        sizeX: Float,
        sizeY: Float,
        dimension: AbstractDimension,
        gameController: GameController
    ) {
        val tileData = dimension.mapSystem.getTileData(pointX, pointY) as? WheatTileData
        val growth = tileData?.growth ?: 0
        
        val texName = AppState.res("seed_${growth + 1}.png")

        val baseW = sizeX * width
        val baseH = sizeY * height
        
        var finalW = baseW * renderConfig.sizeMultiplier
        var finalH = baseH * renderConfig.sizeMultiplier
        
        val centerX = positionX + renderConfig.renderDelta.x * sizeX
        val centerY = positionY + renderConfig.renderDelta.y * sizeY

        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texName),
            centerX,
            centerY,
            finalW,
            finalH,
            renderConfig.flipX
        )
    }

    override fun onMined(x: Int, y: Int, mineData: MineData, dimension: AbstractDimension, mapController: MapController) {
        // Only drop if broken by a Hoe
        if (mineData.item?.itemType?.hasTag(ItemTags.HOE) == true) {
            val tileData = dimension.mapSystem.getTileData(x, y) as? WheatTileData
            val growth = tileData?.growth ?: 0
            
            // Drop only if fully grown
            if (growth >= 3) {
                val pos = dimension.gameCycle.mapApi.getBlockPos(x, y)
                val spawnPos = la.vok.LavokLibrary.Vectors.Vec2(pos.x + 0.5f, pos.y + 0.5f)
                
                // Drop seeds
                val seedsCount = Random.nextInt(1, 3) // 1 to 2
                dimension.gameCycle.itemsApi.spawnItemEntity(dimension, ItemsList.wheat_seeds, spawnPos, seedsCount, true)
                
                // Drop wheat
                val wheatCount = Random.nextInt(2, 4) // 2 to 3
                dimension.gameCycle.itemsApi.spawnItemEntity(dimension, ItemsList.wheat_item, spawnPos, wheatCount, true)
            }
        }
        
        super.onMined(x, y, mineData, dimension, mapController)
    }
}
