package Server

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.q.life.clientcontroler.CellImpl
import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.World
import java.io.*
import java.lang.reflect.Type
import java.net.ServerSocket
import java.net.Socket


class WorldServer(val world: World) {
    var port = 9950

    fun start() {
        val serverSocket = ServerSocket(port)
        println("Server is running on port $port")

        while (true) {
            val clientSocket = serverSocket.accept()
            val gson = Gson()

            BufferedReader(InputStreamReader(clientSocket.getInputStream())).use { reader ->
                try {
                    val cellJson = reader.readLine()
                    JsonDeserializer<Cell> { json, typeOfT, context ->
                        val jsonObject = json.asJsonObject
                        val name = jsonObject.get("name").asString
                        val position = jsonObject.get("position").asJsonObject
                        val x = position.get("x").asDouble
                        val y = position.get("y").asDouble
                        val size = jsonObject.get("size").asDouble
                        CellImpl(name, com.q.projects.metamodele.Position(x, y), size)
                    }
                    val cell = gson.fromJson(cellJson, Cell::class.java)
                    world.addCell(cell)
                }catch (e: Exception){
                    println("Error: $e")
                }
            }
        }
    }

    class CellDeserializer : JsonDeserializer<Cell> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Cell {
            val jsonObject = json.asJsonObject
            val type = jsonObject.get("type").asString
            return when (type) {
                "CellImpl" -> Gson().fromJson(json, CellImpl::class.java)
                "BasicCell" -> Gson().fromJson(json, BasicCell::class.java)
                else -> throw IllegalArgumentException("Unknown type")
            }
        }
    }
}