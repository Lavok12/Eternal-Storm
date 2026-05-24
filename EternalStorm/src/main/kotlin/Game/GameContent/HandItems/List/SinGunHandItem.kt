package la.vok.Game.GameContent.HandItems.List

import la.vok.Game.GameContent.ContentList.EntityTags
import la.vok.Game.GameContent.Entities.Entities.Projectiles.ProjectileEntity
import la.vok.Game.GameContent.Entities.Entities.Projectiles.SinWave
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.SpeedMultiplierType
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.LavokLibrary.Vectors.v

class SinGunHandItem(item: Item, component: HandItemComponent) : HandItem(
    item,
    component,
    HandItemDescriptor(
        speedType = SpeedMultiplierType.Ranged,
        spriteName = "sin_gun.png",
        spriteSize = 3 v 3,
        useDuration = 10f,
        autoRepeat = true,
        animationType = AnimationType.DirectionalThrust(1.4f, 0.8f),
        changeFacingToTarget = true,
        leftAction = UseAction.Custom(
            onStart = {
                gameCycle.entityApi.spawnProjectile(
                    entity.dimension!!,
                    SinWave(
                        gameCycle,
                        30,
                        entity.systemId
                    ),
                    pos = component.getHandPos() + component.targetDirection() * 2.0f,
                    direction = component.targetDirection(),
                    speed = 1f,
                    targetTags = listOf(EntityTags.enemy),
                )
            },
        ),
    )
)