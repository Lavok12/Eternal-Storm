package la.vok.LavokLibrary.LGraphics

import la.vok.LavokLibrary.Geometry.FrameRect
import la.vok.LavokLibrary.Vectors.*
import la.vok.State.AppState
import processing.core.PApplet
import processing.core.PGraphics
import processing.core.PImage
import processing.opengl.PGraphicsOpenGL

class LGraphics() : FrameRect {
    var baseVirtualWidth = 2000f
    var baseVirtualHeight = 1000f

    var windowWidth: Int = 0
    var windowHeight: Int = 0

    lateinit var pg: PGraphics

    var disW: Float = 0f
    var disH: Float = 0f
    val disV: Vec2
        get() {return Vec2(disW, disH) }
    var disW2: Float = 0f
    var disH2: Float = 0f
    val disV2: Vec2
        get() {return Vec2(disW2, disH2) }

    var canvasPixelX = 0f;
    var canvasPixelY = 0f;

    var M: Float = 0f
    var M2: Float = 0f

    override val frameLeftTop: Vec2
        get() = Vec2(-disW2, disH2)

    override val frameRightBottom: Vec2
        get() = Vec2(disW2, -disH2)

    // Default constructor for adaptive resolution
    init {
        updateResolution()
    }

    constructor(fixedWidth: Int, fixedHeight: Int, baseVirtualWidth: Float, baseVirtualHeight : Float, mp: Float = 1f) : this() {
        this.baseVirtualWidth = baseVirtualWidth
        this.baseVirtualHeight = baseVirtualHeight

        initFixedResolution(fixedWidth, fixedHeight, mp)
    }
    constructor(fixedWidth: Int, fixedHeight: Int, mp: Float = 1f) : this() {
        initFixedResolution(fixedWidth, fixedHeight, mp)
    }

    private fun initFixedResolution(width: Int, height: Int, mp: Float = 1f) {
        windowWidth = width
        windowHeight = height

        val screenW = width.toFloat()
        val screenH = height.toFloat()

        // 1. Считаем правильный коэффициент масштабирования относительно базы
        val screenAspect = screenW / screenH
        val baseAspect = baseVirtualWidth / baseVirtualHeight

        if (screenAspect < baseAspect) {
            M = screenW / baseVirtualWidth
        } else {
            M = screenH / baseVirtualHeight
        }

        // 2. Теперь логические координаты disW/disH ВСЕГДА будут
        // пропорциональны вашей базе (2000x1000)
        disW = screenW / M
        disH = screenH / M
        disW2 = disW / 2
        disH2 = disH / 2

        // Учитываем плотность пикселей и множитель
        M2 = AppState.pixelDensityFactor * mp
        M *= M2

        canvasPixelX = width * AppState.pixelDensityFactor * mp
        canvasPixelY = height * AppState.pixelDensityFactor * mp

        pg = AppState.main.createGraphics(
            canvasPixelX.toInt(),
            canvasPixelY.toInt(),
            PApplet.P2D
        )
        (pg as PGraphicsOpenGL).textureSampling(3)
    }


    fun checkResolution() {
        if (windowWidth != AppState.main.width ||
            windowHeight != AppState.main.height) {
            //updateResolution()
        }
    }

    fun updateResolution() {
        windowWidth = AppState.main.width
        windowHeight = AppState.main.height

        val screenW = windowWidth.toFloat()
        val screenH = windowHeight.toFloat()
        val screenAspect = screenW / screenH
        val baseAspect = baseVirtualWidth / baseVirtualHeight

        if (screenAspect < baseAspect) {
            M = screenW / baseVirtualWidth
        } else {
            M = screenH / baseVirtualHeight
        }

        disW = screenW/M
        disH = screenH/M
        disW2 = disW/2
        disH2 = disH/2

        /*PApplet.println(
            "${windowWidth}--${windowHeight}",
            "${screenW}--${screenH}",
            "Resolution updated:",
            "Logical (disW, disH): ${disW}x${disH}",
            "Actual (windowWidth, windowHeight): ${windowWidth}x${windowHeight}",
            "Scale factor M: ${M}"
        )*/

        M2 = AppState.pixelDensityFactor
        M *= M2
        canvasPixelX = windowWidth * AppState.pixelDensityFactor;
        canvasPixelY = windowHeight * AppState.pixelDensityFactor;

        pg = AppState.main.createGraphics(
            canvasPixelX.toInt(),
            canvasPixelY.toInt(),
            PApplet.P2D
        )
        (pg as PGraphicsOpenGL).textureSampling(3)
    }


    fun setTextAlign(x: Int, y: Int) {
        val hAlign = when (x) {
            -1 -> PApplet.LEFT
            1 -> PApplet.RIGHT
            else -> PApplet.CENTER
        }
        val vAlign = when (y) {
            -1 -> PApplet.TOP
            1 -> PApplet.BOTTOM
            else -> PApplet.CENTER
        }
        pg.textAlign(hAlign, vAlign)
    }
    fun setTextAlign(lPoint: LPoint) {
        val hAlign = when (lPoint.x) {
            -1 -> PApplet.LEFT
            1 -> PApplet.RIGHT
            else -> PApplet.CENTER
        }
        val vAlign = when (lPoint.y) {
            -1 -> PApplet.TOP
            1 -> PApplet.BOTTOM
            else -> PApplet.CENTER
        }
        pg.textAlign(hAlign, vAlign)
    }

    fun setText(txt: String, xPos: Float, yPos: Float, size: Float) {
        val lsize = size.coerceIn(1f, 100000f)
        pg.textSize(lsize * M)
        pg.text(txt, (disW2 + xPos) * M, (disH2 - yPos) * M)
    }

    fun setTextWH(txt: String, xPos: Float, yPos: Float, size: Float, w: Float, h: Float) {
        val nx = xPos - w/2f
        var ny = yPos + h/2f
        pg.textSize(size * M)
        pg.text(txt, (disW2 + nx) * M, (disH2 - ny) * M, w * M, h * M)
    }
    fun noStroke() {
        pg.noStroke()
    }

    fun stroke(red: Float) {
        pg.stroke(red)
    }

    fun stroke(red: Float, a: Float) {
        pg.stroke(red, a)
    }

    fun stroke(red: Float, green: Float, blue: Float) {
        pg.stroke(red, green, blue)
    }

    fun stroke(red: Float, green: Float, blue: Float, alpha: Float) {
        pg.stroke(red, green, blue, alpha)
    }

    fun stroke(lColor: LColor) {
        pg.stroke(lColor.r, lColor.g, lColor.b, lColor.a)
    }

    fun strokeWeight(weight: Float) {
        pg.strokeWeight(weight * M)
    }

    fun getTextWidth(txt: String, size: Float): Float {
        val lsize = size.coerceIn(1f, 100000f)
        pg.textSize(lsize * M)
        return pg.textWidth(txt) / M
    }

    fun noFill() {
        pg.noFill()
    }

    fun fill(red: Float) {
        pg.fill(red)
    }

    fun fill(red: Float, a: Float) {
        pg.fill(red, a)
    }

    fun fill(red: Float, green: Float, blue: Float) {
        pg.fill(red, green, blue)
    }

    fun fill(red: Float, green: Float, blue: Float, alpha: Float) {
        pg.fill(red, green, blue, alpha)
    }

    fun fill(lColor: LColor) {
        pg.fill(lColor.r, lColor.g, lColor.b, lColor.a);
    }

    fun fill(vec2: Vec2) {
        pg.fill(vec2.x, vec2.y);
    }

    fun fill(vec3: Vec3) {
        pg.fill(vec3.x, vec3.y, vec3.z);
    }

    fun fill(vec4: Vec4) {
        pg.fill(vec4.x, vec4.y, vec4.z, vec4.w);
    }

    fun bg(white: Float) {
        pg.background(white)
    }
    fun bg(lColor: LColor) {
        pg.background(lColor.r, lColor.g, lColor.b)
    }

    fun bg(red: Float, green: Float, blue: Float) {
        pg.background(red, green, blue)
    }

    fun setTint(n1: Float, n2: Float, n3: Float, n4: Float) {
        pg.tint(n1, n2, n3, n4)
    }

    fun setTint(n1: Float, n2: Float, n3: Float) {
        pg.tint(n1, n2, n3)
    }

    fun setTint(lColor: LColor) {
        pg.tint(lColor.r, lColor.g, lColor.b, lColor.a)
    }

    fun setTint(n1: Float, n2: Float) {
        pg.tint(n1, n2)
    }

    fun setTint(n1: Float) {
        pg.tint(n1)
    }

    fun noTint() {
        pg.tint(255f, 255f, 255f, 255f)
    }

    fun rect(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.rect(xPos * M, yPos * M, xSize * M, ySize * M)
    }

    fun setBlock(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.rect((disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M)
    }

    fun setBlockNB(xPos: Float, yPos: Float, xSize: Float, ySize: Float, k: Float) {
        pg.rect((disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M, k * M)
    }

    fun setPolygon(angles: Array<FloatArray>, xPos: Float, yPos: Float, mpx: Float, mpy: Float) {
        pg.beginShape()
        for (angle in angles) {
            val x = angle[0]
            val y = angle[1]
            pg.vertex((disW2 + x * mpx + xPos) * M, (disH2 - y * mpy - yPos) * M)
        }
        pg.endShape(PApplet.CLOSE)
    }

    fun setRotateBlock(xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.rotate(Rotate)
        setBlock(0f, 0f, xSize, ySize)
        pg.popMatrix()
    }

    fun setEps(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.ellipse((disW2 + xPos) * M, (disH2 - yPos) * M, xSize * M, ySize * M)
    }

    fun setRotateEps(xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.rotate(Rotate)
        setEps(0f, 0f, xSize, ySize)
        pg.popMatrix()
    }

    fun setLine(xPos: Float, yPos: Float, xPos2: Float, yPos2: Float) {
        pg.line((disW2 + xPos) * M, (disH2 - yPos) * M, (disW2 + xPos2) * M, (disH2 - yPos2) * M)
    }

    fun setLine(xPos: Float, yPos: Float, xPos2: Float, yPos2: Float, w: Float, r: Float, g: Float, b: Float) {
        stroke(r, g, b, 255f)
        strokeWeight(w)
        pg.line((disW2 + xPos) * M, (disH2 - yPos) * M, (disW2 + xPos2) * M, (disH2 - yPos2) * M)
        noStroke()
    }

    fun setImage(img: PImage?, x: Float, y: Float, w: Float, h: Float) {
        if (img == null) return
        pg.image(img, (disW2 + x - w / 2f) * M, (disH2 - y - h / 2f) * M, w * M, h * M)
    }

    fun setImage(img: PImage?, x: Float, y: Float, w: Float, h: Float, sx: Int, sy: Int, sw: Int, sh: Int) {
        if (img == null) return
        pg.image(img, (disW2 + x - w / 2f) * M, (disH2 - y - h / 2f) * M, w * M, h * M, sx, sy, (sx + sw), (sy + sh))
    }

    fun setImage(img: PImage?, pos: Vec2, size: Vec2) {
        if (img == null) return
        pg.image(img, (disW2 + pos.x - size.x / 2f) * M, (disH2 - pos.y - size.y / 2f) * M, size.x * M, size.y * M)
    }

    fun setImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float) {
        val ySize = xSize / fImage.width * fImage.height
        pg.image(fImage, (disW2 + xPos - xSize / 2) * M, (disH2 - yPos - ySize / 2) * M, xSize * M, ySize * M)
    }

    fun setRotateImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M) // центр вращения
        pg.rotate(Rotate)
        pg.image(fImage, -xSize/2 * M, -ySize/2 * M, xSize * M, ySize * M) // рисуем с сдвигом к центру
        pg.popMatrix()
    }


    fun beginDraw() {
        pg.beginDraw()
    }

    fun endDraw() {
        pg.endDraw()
    }

    fun restartDraw() {
        pg.endDraw()
        pg.beginDraw()
    }

    fun getPI(): PImage {
        return pg
    }

    fun setRect(xPos: Float, yPos: Float, xSize: Float, ySize: Float) {
        pg.rect((disW2 + xPos) * M, (disH2 - yPos) * M, xSize * M, ySize * M)
    }

    fun setRect(xPos: Float, yPos: Float, xSize: Float, ySize: Float, k: Float) {
        pg.rect((disW2 + xPos) * M, (disH2 - yPos) * M, xSize * M, ySize * M, k * M)
    }

    fun setRect(pos: Vec2, size: Vec2) =
        setRect(pos.x, pos.y, size.x, size.y)

    fun setRect(pos: Vec2, size: Vec2, radius: Float) =
        setRect(pos.x, pos.y, size.x, size.y, radius)


    fun setBlock(pos: Vec2, size: Vec2) =
        setBlock(pos.x, pos.y, size.x, size.y)

    fun setBlockNB(pos: Vec2, size: Vec2, radius: Float) =
        setBlockNB(pos.x, pos.y, size.x, size.y, radius)
    fun setEps(pos: Vec2, size: Vec2) =
        setEps(pos.x, pos.y, size.x, size.y)

    fun setRotateEps(pos: Vec2, size: Vec2, angle: Float) =
        setRotateEps(pos.x, pos.y, size.x, size.y, angle)
    fun setLine(from: Vec2, to: Vec2) =
        setLine(from.x, from.y, to.x, to.y)

    fun setLine(from: Vec2, to: Vec2, w: Float, r: Float, g: Float, b: Float) =
        setLine(from.x, from.y, to.x, to.y, w, r, g, b)

    fun setImage(image: PImage, pos: Vec2, xSize: Float) =
        setImage(image, pos.x, pos.y, xSize)
    fun setRotateImage(image: PImage, pos: Vec2, size: Vec2, angle: Float) =
        setRotateImage(image, pos.x, pos.y, size.x, size.y, angle)

    fun setText(txt: String, pos: Vec2, size: Float) =
        setText(txt, pos.x, pos.y, size)

    fun setTextWH(txt: String, pos: Vec2, size: Float, box: Vec2) =
        setTextWH(txt, pos.x, pos.y, size, box.x, box.y)

    fun logicalToScreen(logicalPos: Vec2): Vec2 {
        val screenX = (disW2 + logicalPos.x) * (M / M2)
        val screenY = (disH2 - logicalPos.y) * (M / M2)
        return Vec2(screenX, screenY)
    }

    fun logicalToCanvas(logicalPos: Vec2): Vec2 {
        val canvasX = (disW2 + logicalPos.x) * M
        val canvasY = (disH2 - logicalPos.y) * M
        return Vec2(canvasX, canvasY)
    }

    fun logicalDimToScreenDim(logicalDim: Float): Float {
        return logicalDim * (M / M2)
    }

    fun logicalDimToCanvasDim(logicalDim: Float): Float {
        return logicalDim * M
    }

    fun screenToLogical(screenPos: Vec2): Vec2 {
        val logicalX = (screenPos.x / (M / M2)) - disW2
        val logicalY = disH2 - (screenPos.y / (M / M2))
        return Vec2(logicalX, logicalY)
    }

    fun screenToCanvas(screenPos: Vec2): Vec2 {
        return Vec2(screenPos.x * M2, screenPos.y * M2)
    }

    fun screenDimToLogicalDim(screenDim: Float): Float {
        return screenDim / (M / M2)
    }

    fun screenDimToCanvasDim(screenDim: Float): Float {
        return screenDim * M2
    }

    fun canvasToLogical(canvasPos: Vec2): Vec2 {
        val logicalX = (canvasPos.x / M) - disW2
        val logicalY = disH2 - (canvasPos.y / M)
        return Vec2(logicalX, logicalY)
    }

    fun canvasToScreen(canvasPos: Vec2): Vec2 {
        return Vec2(canvasPos.x / M2, canvasPos.y / M2)
    }

    fun canvasDimToLogicalDim(canvasDim: Float): Float {
        return canvasDim / M
    }

    fun canvasDimToScreenDim(canvasDim: Float): Float {
        return canvasDim / M2
    }

    fun setHexTop(pos: Vec2, size: Float) {
        setHexTop(pos.x, pos.y, size)
    }

    fun setHexTop(xPos: Float, yPos: Float, size: Float) {
        pg.beginShape()
        for (i in 0 until 6) {
            val vx = xPos + size * HEX_COS[i]
            val vy = yPos + size * HEX_SIN[i]
            pg.vertex((disW2 + vx) * M, (disH2 - vy) * M)
        }
        pg.endShape(PApplet.CLOSE)
    }

    companion object {
        // Предрассчитанные координаты для flat-top шестиугольника (0°, 60°, 120° ...)
        private val HEX_COS = FloatArray(6) { i ->
            PApplet.cos(PApplet.radians(60f * i))
        }
        private val HEX_SIN = FloatArray(6) { i ->
            PApplet.sin(PApplet.radians(60f * i))
        }
    }

    fun logicalSizeToScreen(size: Vec2): Vec2 {
        val sx = size.x * (M / M2)
        val sy = size.y * (M / M2)
        return Vec2(sx, sy)
    }

    fun logicalSizeToCanvas(size: Vec2): Vec2 {
        val sx = size.x * M
        val sy = size.y * M
        return Vec2(sx, sy)
    }

    fun screenSizeToLogical(size: Vec2): Vec2 {
        val lx = size.x / (M / M2)
        val ly = size.y / (M / M2)
        return Vec2(lx, ly)
    }

    fun screenSizeToCanvas(size: Vec2): Vec2 {
        val cx = size.x * M2
        val cy = size.y * M2
        return Vec2(cx, cy)
    }

    fun canvasSizeToLogical(size: Vec2): Vec2 {
        val lx = size.x / M
        val ly = size.y / M
        return Vec2(lx, ly)
    }

    fun canvasSizeToScreen(size: Vec2): Vec2 {
        val sx = size.x / M2
        val sy = size.y / M2
        return Vec2(sx, sy)
    }

    fun getTextLogicalSize(text: String, size: Float): Vec2 {
        val lsize = size.coerceIn(1f, 100000f)
        pg.textSize(lsize * M)

        val lines = text.split('\n')
        val width = lines.maxOf { pg.textWidth(it) } / M
        val lineHeight = (pg.textAscent() + pg.textDescent()) / M
        val height = lines.size * lineHeight

        return Vec2(width, height)
    }

    fun setShaderParams(shader: processing.opengl.PShader) {
        shader.set("u_resolution", canvasPixelX, canvasPixelY)
        shader.set("u_disV2", disW, disH)
        shader.set("u_M", M)
    }

    fun applyShader(shader: processing.opengl.PShader) {
        setShaderParams(shader)
        pg.shader(shader)
    }

    fun resetShader() {
        pg.resetShader()
    }


    fun setRotateImageAround(
        fImage: PImage,
        xPos: Float, yPos: Float,
        xSize: Float, ySize: Float,
        rotate: Float,
        pivotX: Float, pivotY: Float
    ) {
        pg.pushMatrix()

        pg.translate((disW2 + pivotX) * M, (disH2 - pivotY) * M)

        pg.rotate(rotate)

        val offsetX = xPos - pivotX
        val offsetY = pivotY - yPos

        pg.image(
            fImage,
            (offsetX - xSize / 2f) * M,
            (offsetY - ySize / 2f) * M,
            xSize * M,
            ySize * M
        )

        pg.popMatrix()
    }


    fun setRotateImageAround(image: PImage, pos: Vec2, size: Vec2, rotate: Float, pivot: Vec2) =
        setRotateImageAround(image, pos.x, pos.y, size.x, size.y, rotate, pivot.x, pivot.y)


    fun setImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float, ySize: Float, flipX: Boolean) {
        if (!flipX) {
            setImage(fImage, xPos, yPos, xSize, ySize)
            return
        }

        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.scale(-1f, 1f)
        pg.image(fImage, -xSize / 2 * M, -ySize / 2 * M, xSize * M, ySize * M)
        pg.popMatrix()
    }

    fun setImage(image: PImage, pos: Vec2, size: Vec2, flipX: Boolean) =
        setImage(image, pos.x, pos.y, size.x, size.y, flipX)

    fun setRotateImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float, ySize: Float, Rotate: Float, flipX: Boolean) {
        pg.pushMatrix()
        pg.translate((disW2 + xPos) * M, (disH2 - yPos) * M)
        pg.rotate(Rotate)
        if (flipX) pg.scale(-1f, 1f)
        pg.image(fImage, -xSize / 2 * M, -ySize / 2 * M, xSize * M, ySize * M)
        pg.popMatrix()
    }

    // --- Отрисовка блоков по вектору направления ---

    fun setRotateBlock(xPos: Float, yPos: Float, xSize: Float, ySize: Float, dir: Vec2) {
        val angle = dir.normalized().angle() // Предполагаем, что у твоего Vec2 есть angle()
        setRotateBlock(xPos, yPos, xSize, ySize, angle)
    }

    fun setRotateBlock(pos: Vec2, size: Vec2, dir: Vec2) =
        setRotateBlock(pos.x, pos.y, size.x, size.y, dir)

    // --- Отрисовка эллипсов по вектору направления ---

    fun setRotateEps(pos: Vec2, size: Vec2, dir: Vec2) {
        val angle = dir.normalized().angle()
        setRotateEps(pos.x, pos.y, size.x, size.y, angle)
    }

    // --- Отрисовка изображений по вектору направления ---

    fun setRotateImage(fImage: PImage, xPos: Float, yPos: Float, xSize: Float, ySize: Float, dir: Vec2, flipX: Boolean = false) {
        val normalizedDir = dir.normalized()
        val angle = kotlin.math.atan2(normalizedDir.y.toDouble(), normalizedDir.x.toDouble()).toFloat()

        // ВАЖНО: В твоем LGraphics инвертирована ось Y (disH2 - yPos),
        // поэтому угол вектора может потребовать инверсии знака в зависимости от реализации Vec2.
        // Если Vec2.angle() уже это учитывает, используй его.

        setRotateImage(fImage, xPos, yPos, xSize, ySize, angle, flipX)
    }

    fun setRotateImage(image: PImage, pos: Vec2, size: Vec2, dir: Vec2, flipX: Boolean = false) =
        setRotateImage(image, pos.x, pos.y, size.x, size.y, dir, flipX)

    // --- Вращение вокруг точки по вектору направления ---

    fun setRotateImageAround(image: PImage, pos: Vec2, size: Vec2, dir: Vec2, pivot: Vec2) {
        val angle = dir.normalized().angle()
        setRotateImageAround(image, pos.x, pos.y, size.x, size.y, angle, pivot.x, pivot.y)
    }


    fun setRotateImage(image: PImage, pos: Vec2, size: Vec2, dir: Float, flipX: Boolean = false) =
        setRotateImage(image, pos.x, pos.y, size.x, size.y, dir, flipX)

    fun setRotateImageAround(
        fImage: PImage,
        xPos: Float, yPos: Float,
        xSize: Float, ySize: Float,
        rotate: Float,
        pivotX: Float, pivotY: Float,
        flipX: Boolean
    ) {
        pg.pushMatrix()

        // 1. Переносим начало координат в точку пивота
        pg.translate((disW2 + pivotX) * M, (disH2 - pivotY) * M)

        // 2. Поворачиваем
        pg.rotate(rotate)

        // 3. Отражаем по горизонтали, если нужно
        if (flipX) {
            pg.scale(-1f, 1f)
        }

        // 4. Вычисляем смещение от пивота до центра картинки
        // Если мы отразили систему координат через scale,
        // то положительный offsetX будет рисовать картинку "впереди" пивота в обоих случаях.
        val offsetX = xPos - pivotX
        val offsetY = pivotY - yPos

        pg.image(
            fImage,
            (offsetX - xSize / 2f) * M,
            (offsetY - ySize / 2f) * M,
            xSize * M,
            ySize * M
        )

        pg.popMatrix()

        if (setRotateImageAroundDebug) {
            fill(200f, 50f, 50f)
            setEps(pivotX, pivotY, 20f, 20f)
        }
    }
    var setRotateImageAroundDebug = false

    // Удобная перегрузка для Vec2 с поддержкой flipX
    fun setRotateImageAround(
        image: PImage,
        pos: Vec2,
        size: Vec2,
        rotate: Float,
        pivot: Vec2,
        flipX: Boolean
    ) = setRotateImageAround(image, pos.x, pos.y, size.x, size.y, rotate, pivot.x, pivot.y, flipX)
}