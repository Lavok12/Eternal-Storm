package la.vok.Game.GameContent.Tiles.System

import la.vok.Core.GameControllers.GameController
import la.vok.Core.GameControllers.GameRender
import la.vok.Game.GameContent.Items.Other.DropEntry
import la.vok.Game.GameContent.Items.Other.DropTable
import la.vok.Game.GameContent.Items.Other.Item
import la.vok.Game.GameContent.Items.Other.NothingDrop
import la.vok.Game.GameContent.Items.Other.dropTable
import la.vok.Game.GameContent.Map.MapController
import la.vok.Game.GameSystems.WorldSystems.Map.MineData
import la.vok.Game.GameSystems.WorldSystems.Particles.Particles.TileParticle
import la.vok.LavokLibrary.LGraphics.LGraphics
import la.vok.LavokLibrary.Vectors.Vec2
import la.vok.LavokLibrary.Vectors.p
import la.vok.LavokLibrary.Vectors.v
import la.vok.State.AppState

abstract class AbstractTileType {

    open val tag: String = ""
    open val blockStrength: Int = 0
    open val maxHp: Int = 0
    open val texture: String = ""
    open val consumed: Boolean = true
    open val drop: DropEntry = NothingDrop

    open fun render(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameController: GameController
    ) {
        lg.setImage(
            gameController.coreController.spriteLoader.getValue(texture),
            position,
            size
        )
    }

    open fun renderHighlight(
        tileContext: TileContext,
        lg: LGraphics,
        position: Vec2,
        size: Vec2,
        gameRender: GameRender
    ) {
        lg.stroke(0f)
        lg.strokeWeight(3f)
        lg.fill(0f, 0f)
        lg.setBlock(position, size-3f)
        lg.noStroke()
    }

    open fun place(x: Int, y: Int, item: Item, mapController: MapController) {}

    open fun damage(
        x: Int,
        y: Int,
        damage: Int,
        tileContext: TileContext,
        mapController: MapController
    ) {
        // TODO ЭТО ПОЛНОЕ ГОВНО ПЕРЕДЕЛАТЬ
        for (i in 0 .. 4) {
            mapController.gameCycle.particleController.particleSystem.addParticle(
                TileParticle(
                    mapController.gameCycle,
                    this,
                    mapController.gameCycle.mapApi.getTilePos(x, y) + (AppState.main.random(
                        -0.5f,
                        0.5f
                    ) v AppState.main.random(-0.5f, 0.5f)),
                    (AppState.main.random(-1f, 1f) v AppState.main.random(-1f, 1f)) * AppState.main.random(0.025f, 0.05f)
                )
            )
        }
    }

    open fun onMined(
        x: Int,
        y: Int,
        mineData: MineData,
        tileContext: TileContext, mapController: MapController
    ) {
        mapController.gameCycle.itemsApi.spawnDropTable(
            drop,
            mapController.mapApi.getTilePos(x p y),
            true
        )
            // TODO ЭТО ПОЛНОЕ ГОВНО ПЕРЕДЕЛАТЬ
        for (i in 0 .. 8) {
            mapController.gameCycle.particleController.particleSystem.addParticle(
                TileParticle(
                    mapController.gameCycle,
                    this,
                    mapController.gameCycle.mapApi.getTilePos(x, y) + (AppState.main.random(
                        -0.5f,
                        0.5f
                    ) v AppState.main.random(-0.5f, 0.5f)),
                    (AppState.main.random(-1f, 1f) v AppState.main.random(-1f, 1f)) * AppState.main.random(0.025f, 0.05f)
                )
            )
        }
    }
    open fun onRemoved(
        x: Int,
        y: Int,
        tileContext: TileContext,
        reason: Any? = null
    ) {
    }
}