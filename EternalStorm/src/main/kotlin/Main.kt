package la.vok

import processing.core.PApplet
import java.io.PrintStream
import java.nio.charset.StandardCharsets

fun main() {
    System.setOut(PrintStream(System.`out`, true, StandardCharsets.UTF_8.name()))
    System.setErr(PrintStream(System.err, true, StandardCharsets.UTF_8.name()))

    PApplet.main("la.vok.Sketch.ProcessingSketch")
}
