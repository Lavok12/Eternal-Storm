package la.vok.Game.GameContent.TileTypes.Crops

import la.vok.Game.GameContent.Dimensions.Dimensions.AbstractDimension

data class CropConfig(
    val maxGrowthStages: Int,                
    val baseTicksPerStage: Int,              
    val texturePrefix: String,               
    
    // Function that determines drops at a specific growth stage
    val getDrops: (growth: Int) -> List<Pair<String, IntRange>>, 
    
    // Additional condition for growth
    val canGrow: (dimension: AbstractDimension, x: Int, y: Int) -> Boolean = { _, _, _ -> true }
)
