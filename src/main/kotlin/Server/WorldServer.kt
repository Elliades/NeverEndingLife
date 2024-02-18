package Server

import com.q.life.clientcontroler.CellImpl
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.World
import java.io.*
import java.net.ServerSocket
import java.net.Socket


class WorldServer(val world: World) {
    val port = 9999
    private val serverSocket = ServerSocket(port) // Port arbitraire pour l'exemple

    fun start() {
        val serverSocket = ServerSocket(port)
        println("Server is running on port $port")

        while (true) {
            val clientSocket = serverSocket.accept()
            val gson = Gson()

            BufferedReader(InputStreamReader(clientSocket.getInputStream())).use { reader ->
                val cellJson = reader.readLine()
                val cell = gson.fromJson(cellJson, Cell::class.java)
                world.addCell(cell)
            }
        }
    }



fun main() {
    val world = World(800.0, 600.0)
    val server = WorldServer(world)
    server.start()
}
