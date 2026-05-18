package la.vok.Game.GameContent.Entities.Entities.Special

import la.vok.Core.CoreContent.Camera.Camera
import la.vok.Core.GameContent.RenderSystem.RenderLayers.LayersRenderContainer
import la.vok.Core.GameContent.RenderSystem.RenderLayers.Objects.RenderObjectInterface
import la.vok.Game.GameContent.Entities.EntitiTypes.AbstractEntityType
import la.vok.Game.GameContent.Entities.EntityRender.BaseRenderEntity
import la.vok.Game.GameContent.Entities.EntityRender.HpRender
import la.vok.Game.GameContent.Tiles.System.AbstractTileType
import la.vok.Game.GameSystems.WorldSystems.Map.IBlockType
import la.vok.Game.GameController.GameCycle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class BlockEntity(
    val blockType: IBlockType,
    entityType: AbstractEntityType,
    gameCycle: GameCycle
) : Entity(entityType, gameCycle) {

    override var renderEntity: RenderObjectInterface? = null
    override var hpRender: HpRender? = null

    init {
        hpBody = null
        hpRender?.hide()
        hpRender = null
    }

    override fun spawn() {
        val w = if (blockType is AbstractTileType) blockType.width else 1
        val h = if (blockType is AbstractTileType) blockType.height else 1
        size = w.toFloat() v h.toFloat()

        super.spawn()

        renderEntity = BlockRender(getRenderLayer())
    }

    override fun createBaseHitbox() {
        val main = addHitbox("main")
        main.size = (size.x - 0.05f) v (size.y - 0.05f)
        if (hasCollisionDetector) createBaseCollisionDetector()
    }

    inner class BlockRender(layersRenderContainer: LayersRenderContainer) : BaseRenderEntity(layersRenderContainer) {
        override fun draw(lg: LGraphics, pos: Vec2, size: Vec2, camera: Camera) {
            blockType.renderBlockEntity(
                lg,
                pos.x,
                pos.y,
                size.x,
                size.y,
                dimension!!,
                gameController
            )
        }
    }
}
