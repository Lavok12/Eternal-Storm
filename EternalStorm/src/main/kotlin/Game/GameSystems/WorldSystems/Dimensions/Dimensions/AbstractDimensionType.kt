package la.vok.Game.GameSystems.WorldSystems.Dimensions.Dimensions

import la.vok.Game.GameController.GameCycle

abstract class AbstractDimensionType {
    abstract val tag: String
    
    abstract fun createDimension(gameCycle: GameCycle): AbstractDimension
}
