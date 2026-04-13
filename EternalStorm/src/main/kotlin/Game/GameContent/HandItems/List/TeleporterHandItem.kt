package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.ContentList.DimensionsList
import la.vok.Game.GameContent.ContentList.TilesList
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Items.TeleporterItem
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

class TeleporterHandItem(item: TeleporterItem, component: HandItemComponent) : HandItem(
    item,
    component,
    HandItemDescriptor(
        spriteName = "t4.png",
        spriteSize = 1.5f v 1.5f,
        useDuration = 100f,
        animationType = AnimationType.Swing(),
        leftAction = UseAction.Custom(
            onEnd = {
                val currentDim = entity.dimension ?: return@Custom
                val targetDimTag = if (currentDim.dimensionTag == DimensionsList.main) {
                    DimensionsList.stone_world
                } else {
                    DimensionsList.main
                }
                
                val targetDim = gameCycle.dimensionsController.dimensions[targetDimTag] ?: return@Custom
                
                // Teleportation logic
                val oldDim = entity.dimension
                gameCycle.entityApi.teleport(entity, targetDim, entity.position)
                gameCycle.dimensionsApi.changeRenderDimension(oldDim!!, targetDim)
                // Particles in target dimension
                val stoneType = gameController.coreController.objectRegistration.getTileType(TilesList.stone_block)
                gameCycle.particlesApi.buildTile(targetDim, stoneType)
                    .at(entity.position)
                    .count(25)
                    .randomSpeed(2f)
                    .randomOffset(0.5f)
                    .spawn()
            }
        )
    )
)
