package la.vok.Game.GameContent.Map

import la.vok.Core.CoreControllers.Intergaces.Controller
import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

class MapController(var dimension: AbstractDimension) : Controller {
    init {
        create()
    }
    var mapSystem = MapSystem(this)
}