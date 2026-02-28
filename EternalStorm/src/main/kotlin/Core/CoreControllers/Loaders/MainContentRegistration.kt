package Core.CoreControllers.Loaders

import Core.CoreControllers.ObjectRegistration
import Game.GameContent.TileTypes.GrassTileType
import la.vok.Core.CoreControllers.CoreController
import la.vok.Game.GameContent.Entities.EntitiTypes.PlayerEntityType
import la.vok.Game.GameContent.Entities.EntitiTypes.SlimeEntityType
import la.vok.Game.GameContent.Items.ItemTypes.AxeItemType
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.DirtItemType
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.GrassItemType
import la.vok.Game.GameContent.Items.ItemTypes.Blocks.StoneItemType
import la.vok.Game.GameContent.Items.ItemTypes.PickaxeItemType
import la.vok.Game.GameContent.Items.ItemTypes.SpearItemType
import la.vok.Game.GameContent.TileTypes.DirtTileType
import la.vok.Game.GameContent.TileTypes.StoneTileType

class MainContentRegistration(coreController: CoreController) {
    fun regObjects(objectRegistration: ObjectRegistration) {

        objectRegistration.registrationItemType(AxeItemType())
        objectRegistration.registrationItemType(SpearItemType())
        objectRegistration.registrationItemType(PickaxeItemType())

        objectRegistration.registrationItemType(GrassItemType())
        objectRegistration.registrationItemType(DirtItemType())
        objectRegistration.registrationItemType(StoneItemType())

        objectRegistration.registrationTileType(StoneTileType())
        objectRegistration.registrationTileType(GrassTileType())
        objectRegistration.registrationTileType(DirtTileType())

        objectRegistration.registrationEntityType(PlayerEntityType())
        objectRegistration.registrationEntityType(SlimeEntityType())
    }
}