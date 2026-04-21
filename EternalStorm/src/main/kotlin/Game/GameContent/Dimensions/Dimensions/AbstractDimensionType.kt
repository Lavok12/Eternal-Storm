package la.vok.Game.GameContent.Dimensions.Dimensions

import la.vok.Game.GameController.GameCycle

abstract class AbstractDimensionType {
    abstract val tag: String
    
    abstract fun createDimension(gameCycle: GameCycle): AbstractDimension
}
