package Server

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.World
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.*
import java.net.ServerSocket


class WorldServer(val world: World) {
    var port = 9950

    fun start() = runBlocking {
        val serverSocket = ServerSocket(port)
        println("Server is running on port $port")

        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        CoroutineScope(Dispatchers.IO).launch {
            readNewCell(serverSocket, mapper)
        }
    }

    private fun readNewCell(serverSocket: ServerSocket, mapper: ObjectMapper) {
        while (true) {
            val clientSocket = serverSocket.accept()
            BufferedReader(InputStreamReader(clientSocket.getInputStream())).use { reader ->
                try {
                    val cellJson = reader.readLine()
                    val typeNode = mapper.readTree(cellJson).get("type")
                    if (typeNode == null) throw IllegalArgumentException("Invalid class type")
                    val typeName = typeNode.asText()
                    val cellClass = Class.forName(typeName)

                    if (!Cell::class.java.isAssignableFrom(cellClass)) throw IllegalArgumentException("Invalid class type")
                    val cell: Cell = mapper.treeToValue(mapper.readTree(cellJson), cellClass) as Cell
                    world.addCell(cell)

                } catch (e: Exception) {
                    println("Error: $e")
                }
            }
        }
    }


}