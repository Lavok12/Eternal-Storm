package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.SpeedMultiplierType
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Items.HummerItem
import la.vok.Game.GameContent.Items.Items.PickaxeItem
import la.vok.Game.GameContent.VfxObjects.AxeSwingTraceVfxObject
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.WorldSystems.Entities.DamageData
import la.vok.Game.GameSystems.WorldSystems.Entities.TagFilter
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.LavokLibrary.Vectors.LPoint
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.v

open class HummerHandItem(item: HummerItem, component: HandItemComponent) : HandItem(
    item,
    component,
    HandItemDescriptor(
        speedType = SpeedMultiplierType.Digging,
        spriteName = item.handItemTexture,
        spriteSize = item.handItemSize v item.handItemSize,
        useDuration = item.handItemUseDuration,
        autoRepeat = true,
        animationType = AnimationType.Swing(),
        renderMineHighlight = true,
        leftAction = UseAction.Custom(
            onStart = {
                gameCycle.entityApi.damageZone(
                    entity.position + (component.entity.facing * 2f v 0),
                    (1.1 v 1.5) * item.handItemSize,
                    DamageData(
                        item.handItemDamage,
                        ((entity.facing v 1f) * item.handItemKnockback),
                        component.entity.systemId,
                        this
                    ),
                    TagFilter.HasAll(
                        listOf(EntityTags.enemy)
                    )
                )

                gameController.gameCycle.vfxObjectsApi.addInSystem(
                    AxeSwingTraceVfxObject(gameCycle, entity.facing),
                    entity.position + ((0.5f + descriptor.spriteSize.x / 2f) * entity.facing v 0.2f),
                    descriptor.spriteSize * 1.4f,
                    (entity.rigidBody?.speed ?: (Vec2.Companion.ZERO)) * (1f v 0.3f)
                )

                (this as HummerHandItem).onMineTile()
            },
        ),
    )
) {
    open fun onMineTile() {
        var placePos = entity.ai?.targetMapPos() ?: LPoint.Companion.ZERO
        var mineData =
            MineData((item as HummerItem).mineDamage, (item as HummerItem).minePower, entity.systemId, this)
        gameCycle.mapApi.mineWall(placePos.x, placePos.y, mineData)
    }
}