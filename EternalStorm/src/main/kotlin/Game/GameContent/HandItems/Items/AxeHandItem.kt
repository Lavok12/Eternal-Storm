package la.vok.Game.GameContent.HandItems.Items

import la.vok.Game.GameContent.EntityTags
import la.vok.Game.GameContent.HandItems.AnimationType
import la.vok.Game.GameContent.HandItems.HandItem
import la.vok.Game.GameContent.HandItems.HandItemDescriptor
import la.vok.Game.GameContent.HandItems.UseAction
import la.vok.Game.GameSystems.Entities.DamageData
import la.vok.Game.GameSystems.EntityComponents.HandItemComponent
import la.vok.Game.GameSystems.Entities.TagFilter
import la.vok.LavokLibrary.Vectors.v

class AxeHandItem(component: HandItemComponent) : HandItem(
    component,
    HandItemDescriptor(
        spriteName = "axe.png",
        spriteSize = 3 v 3,
        useDuration = 120f,
        useStageStep = 8f,
        animationType = AnimationType.Swing(),
        leftAction = UseAction.Custom(onStart = {entityApi.damageZone(entity.position + (component.entity.facing*2f v 0),
            4 v 5,
            DamageData(
                10,
                (component.entity.facing*0.2f v 0.1f),
                component.entity.systemId,
                this),
            TagFilter.HasAll(
            listOf(EntityTags.enemy)
        ))}),
    )
)