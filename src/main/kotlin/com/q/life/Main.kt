package com.q.life
import kotlinx.coroutines.*
import Server.WorldServer
import com.q.life.clientcontroler.BouncyCell
import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Position
import com.q.projects.metamodele.World
import com.q.projects.metamodele.WorldHolder
import javafx.application.Application
import kotlinx.coroutines.*


fun main(args: Array<String>) = runBlocking {
    val world = initWorld()

    launch(Dispatchers.IO) {
        WorldServer(world).start()
    }
    Application.launch(GameHMI::class.java, *args)
}

fun initWorld(): World {
    val world = World(800.0, 600.0)
    WorldHolder.world = world
    world.initialize()
    world.addCell(BasicCell("species1", Position(10.0, 10.0), 5.0))
    world.addCell(BasicCell("species2", Position(20.0, 20.0), 5.0))
    world.addCell(BasicCell("species3", Position(00.0, 20.0), 10.0))
    world.addCell(BouncyCell("species3", Position(200.0, 200.0), 10.0))
    return world;

}
