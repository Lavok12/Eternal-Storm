# Liquid System Implementation Guide

This guide provides a step-by-step technical plan for implementing a high-performance, realistic liquid system in Eternal Storm.

## 1. Core Architecture & Registration

### AbstractLiquidType
Create `AbstractLiquidType` in `la.vok.Game.GameContent.LiquidTypes`.
```kotlin
abstract class AbstractLiquidType {
    abstract val tag: String
    abstract val color: LColor
    abstract val viscosity: Int // Steps between flows (1 = fast, 5 = slow)
    abstract val density: Int   // For liquid layering (heavier stays at bottom)
    
    open fun render(lg: LGraphics, x: Float, y: Float, w: Float, h: Float, amount: Byte) {
        lg.fill(color)
        val heightNormalized = (amount.toInt() and 0xFF) / 255f
        lg.setBlock(x, y + (1f - heightNormalized) * 0.5f * h, w, h * heightNormalized)
    }
}
```

### ObjectRegistration Updates
- Add `val liquids = HashMap<String, AbstractLiquidType>()`
- Add `val liquidCollisions = HashMap<Pair<String, String>, LiquidCollisionRule>()`
- `LiquidCollisionRule(val resultTile: String, val costMain: Byte, val costSec: Byte)`

---

## 2. Liquid Storage (Per Dimension)

### Bit-Packed Keys
To store liquids efficiently in a `HashMap<Long, Byte>`, use bit-packing for the key:
```kotlin
fun pack(x: Int, y: Int): Long = (x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFFL)
fun unpackX(key: Long): Int = (key shr 32).toInt()
fun unpackY(key: Long): Int = key.toInt()
```

### LiquidSystem Class
Each `AbstractDimension` should have its own `LiquidSystem`:
- `val amounts = HashMap<Long, Byte>()`
- `val types = HashMap<Long, AbstractLiquidType>()`
- `val activeCells = HashSet<Long>()` (Cells that need processing)

---

## 3. Physics & Flow Logic (Thread-Ready)

To allow off-thread processing, use **Double Buffering**:
1. Create `pendingAmounts` and `pendingTypes` maps.
2. In each update, iterate over `activeCells`.
3. Read values from `amounts`/`types`.
4. Write changes to `pendingMaps`.
5. After update, swap `pendingMaps` with `currentMaps`.

### Flow Algorithm (Logical Order)
1. **Down**: Check `(x, y + 1)`. If total liquid < 255, move `min(current, 255 - target)` down.
2. **Sides**: If bottom is blocked or full, check `(x - 1, y)` and `(x + 1, y)`.
3. **Equalization**: Average out the amounts between horizontal neighbors to create a flat surface.
4. **Collision**: If target cell contains a DIFFERENT liquid type, check `liquidCollisions` table.
   - If rule exists: Decrease both amounts by `cost`, spawn `resultTile` at target.

---

## 4. Liquid API

Create `LiquidApi` in `la.vok.Game.GameSystems.WorldSystems.Liquid`:
- `spawnLiquid(dimension, type, x, y, amount)`
- `getAmount(dimension, x, y)`
- `getType(dimension, x, y)`

---

## 5. Integration

### GameCycle.kt
- In `physicTick()`, call `dimension.liquidSystem.update()`.
- Ensure it's called after `mapSystem` update but before `collisionSystem`.

### GameRender.kt
Insert liquid rendering between Walls and Tiles:
```kotlin
// 1. Render Walls
// ...
// 1.5 Render Liquids
forEachInArea(p1, p2, 1) { ix, iy ->
    val type = liquidSystem.getType(ix, iy) ?: return@forEachInArea
    val amount = liquidSystem.getAmount(ix, iy)
    type.render(lg, camera.useCameraPosX(ix), camera.useCameraPosY(iy), blockSizeX, blockSizeY, amount)
}
// 2. Render Tiles
```

---

## 6. Content Registration

In `MainContentRegistration.kt`:
1. Register **Water** (Blue, fast).
2. Register **Lava** (Orange, slow).
3. Register **Acid** (Green, medium).
4. Register rules:
   - `(Lava, Water) -> (Stone, 10, 10)`
   - `(Acid, Water) -> (Stone, 5, 5)` (just for example)

## Threading Implementation Tip
To make it thread-ready, use a `synchronized` block or a `ConcurrentHashMap` during the map swap at the end of the update, but the processing itself can run on a separate fixed-rate thread worker.
