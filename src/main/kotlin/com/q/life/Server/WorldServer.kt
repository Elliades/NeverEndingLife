package Server

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.q.life.clientcontroler.BouncyCell
import com.q.life.datamodele.CellImpl
import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.World
import java.io.*
import java.lang.reflect.Type
import java.net.ServerSocket


class WorldServer(val world: World) {
    var port = 9950

    fun start() {
        val serverSocket = ServerSocket(port)
        println("Server is running on port $port")

        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        while (true) {
            val clientSocket = serverSocket.accept()
            BufferedReader(InputStreamReader(clientSocket.getInputStream())).use { reader ->
                try {
                    val cellJson = reader.readLine()
                    val typeNode = mapper.readTree(cellJson).get("type")
                    if (typeNode == null)  throw IllegalArgumentException("Invalid class type")
                    val typeName = typeNode.asText()
                    val cellClass = Class.forName(typeName)

                    if (!Cell::class.java.isAssignableFrom(cellClass))  throw IllegalArgumentException("Invalid class type")
                    val cell: Cell = mapper.treeToValue(mapper.readTree(cellJson), cellClass) as Cell
                    world.addCell(cell)

                } catch (e: Exception) {
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
                "CellImpl" -> Gson().fromJson(json, BouncyCell::class.java)
                "BasicCell" -> Gson().fromJson(json, BasicCell::class.java)
                else -> throw IllegalArgumentException("Unknown type")
            }
        }
    }
}