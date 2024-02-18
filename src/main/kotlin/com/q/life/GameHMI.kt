package com.q.life

import com.q.projects.metamodele.WorldHolder
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import javafx.stage.Stage
import kotlin.random.Random

class GameHMI: Application(){
    companion object {
        const val WIDTH = 800.0
        const val HEIGHT = 600.0
        val cellColors = mutableMapOf<String, Color>()
    }
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Cell Simulation"

        val root = Group()
        val scene = Scene(root, WIDTH, HEIGHT)
        val canvas = Canvas(WIDTH, HEIGHT)

        root.children.add(canvas)
        primaryStage.scene = scene
        primaryStage.show()

        val gc = canvas.graphicsContext2D
        val world = WorldHolder.world ?: return

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                // Update the world and cells
                world.cells.forEach { cell ->
                    cell.move()
                }

                // Clear the canvas
                gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

                // Draw the world (e.g., background)
                gc.fill = Color.LIGHTGRAY
                gc.fillRect(0.0, 0.0, canvas.width, canvas.height)

                // Draw cells
                // Draw cells
                world.cells.forEach { cell ->
                    val color = cellColors.getOrPut(cell.name) { generateRandomColor() }
                    gc.fill = color
                    gc.fillOval(cell.position.x - cell.size,
                        cell.position.y - cell.size,
                        cell.size * 2, cell.size * 2)
                }
            }
        }.start()
    }

    private fun generateRandomColor(): Color {
        return Color.rgb(
            Random.nextInt(256),
            Random.nextInt(256),
            Random.nextInt(256)
        )
    }
}

