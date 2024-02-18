package com.q.life
import javafx.application.Application
import kotlinx.coroutines.*
import com.q.life.clientcontroler.BouncyCell
import com.q.life.clientcontroler.WorldBuilder
import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Position
import com.q.projects.metamodele.World
import com.q.projects.metamodele.WorldHolder
import Server.WorldServer
import java.util.Random
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    startLife(args)
}

private fun startLife(args: Array<String>) = runBlocking {
    val world = initWorld()

    launch(Dispatchers.IO) {
        WorldServer(world).start()
    }

    // Lancer l'IHM dans son propre thread pour éviter l'erreur
    val uiThread = thread {
        Application.launch(GameHMI::class.java, *args)
    }

    launch(Dispatchers.Default) {
        repeat(10) {
            sleepAndCreateRandomCell()
        }
    }

    uiThread.join() // Attendre que le thread de l'IHM se termine
}


fun initWorld(): World {
    val world = World(800.0, 600.0)
    WorldHolder.world = world
    world.initialize()
    world.addCell(BasicCell("species1", Position(10.0, 10.0), 5.0))
    world.addCell(BasicCell("species2", Position(20.0, 20.0), 5.0))
    world.addCell(BasicCell("species3", Position(0.0, 20.0), 10.0))
    world.addCell(BouncyCell("species3", Position(200.0, 200.0), 10.0))
    return world
}

suspend fun sleepAndCreateRandomCell() {
    delay(5000) // Utilisation de delay au lieu de Thread.sleep pour une meilleure intégration avec les coroutines
    WorldBuilder.initServer().createXRandomCell(Random().nextInt(1, 10))
}
