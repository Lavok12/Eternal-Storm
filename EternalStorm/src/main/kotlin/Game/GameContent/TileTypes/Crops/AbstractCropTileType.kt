package la.vok.Game.GameContent.TileTypes.Crops

import la.vok.Core.GameControllers.GameController
import la.vok.Game.GameContent.ContentList.BlockTags
import la.vok.Game.GameContent.ContentList.ItemTags
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameContent.TileData.AbstractTileData
import la.vok.Game.GameContent.TileData.CropTileData
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameContent.Tiles.System.TileRenderConfig
import la.vok.Game.GameController.CollisionType
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Map.TilePlaceType
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.State.AppState
import kotlin.random.Random

abstract class AbstractCropTileType(val config: CropConfig) : AbstractTileType() {
    override var collisionType = CollisionType.NONE
    override val blockStrength: Int = 0
    override val maxHp: Int = 1
    override val placeType = TilePlaceType.CUSTOM
    override val breakIfInvalid = true
    override val tags: Set<String> = setOf(BlockTags.PLANT)
    override val renderConfig = TileRenderConfig(AOShadow = false)
    
    // Provide a fallback texture for particles (uses the 1st stage of growth)
    override val texture: String = AppState.res("${config.texturePrefix}1.png")

    override fun createTileData(x: Int, y: Int, dimension: AbstractDimension): AbstractTileData? {
        return CropTileData(x, y, dimension, config)
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
        val tileData = dimension.mapSystem.getTileData(pointX, pointY) as? CropTileData
        val growth = tileData?.growth ?: 0
        
        val texName = AppState.res("${config.texturePrefix}${growth + 1}.png")

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
        if (mineData.item?.itemType?.hasTag(ItemTags.HOE) == true) {
            val tileData = dimension.mapSystem.getTileData(x, y) as? CropTileData
            val growth = tileData?.growth ?: 0
            
            val pos = dimension.gameCycle.mapApi.getBlockPos(x, y)
            val spawnPos = Vec2(pos.x + 0.5f, pos.y + 0.5f)
            
            config.getDrops(growth).forEach { (itemTag, countRange) ->
                val count = Random.nextInt(countRange.first, countRange.last + 1)
                dimension.gameCycle.itemsApi.spawnItemEntity(dimension, itemTag, spawnPos, count, true)
            }
        }
        super.onMined(x, y, mineData, dimension, mapController)
    }
}
