package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Crafts.CraftRegistrator
import la.vok.Game.GameContent.Crafts.craft
import la.vok.Game.GameContent.Entities.EntitiTypes.*
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.*
import la.vok.Game.GameContent.Items.ItemTypes.*
import la.vok.Game.GameContent.TileTypes.DirtTileType
import la.vok.Game.GameContent.TileTypes.StoneTileType

class MainContentRegistration(var coreController: CoreController) {
    fun regObjects(objectRegistration: ObjectRegistration) {

        objectRegistration.registrationItemType(AxeItemType())
        objectRegistration.registrationItemType(SpearItemType())
        objectRegistration.registrationItemType(PickaxeItemType())

        objectRegistration.registrationItemType(GrassItemType())
        objectRegistration.registrationItemType(DirtItemType())
        objectRegistration.registrationItemType(StoneItemType())

        objectRegistration.registrationItemType(MostCommonStickItemType())


        objectRegistration.registrationTileType(StoneTileType())
        objectRegistration.registrationTileType(GrassTileType())
        objectRegistration.registrationTileType(DirtTileType())

        objectRegistration.registrationEntityType(PlayerEntityType())
        objectRegistration.registrationEntityType(SlimeEntityType())
        objectRegistration.registrationEntityType(BossEntityType())

        CraftRegistrator.registration(objectRegistration)

        objectRegistration.resolveAllCrafts()
    }
}