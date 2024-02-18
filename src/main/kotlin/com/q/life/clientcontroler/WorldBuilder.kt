package com.q.life.clientcontroler

import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Position
import java.io.*
import java.net.Socket


class WorldBuilder(val host: String, val port: Int) {
    fun addCell(cell: Cell) {
        val gson = Gson()
        val cellJson = gson.toJson(cell)

        Socket(host, port).use { socket ->
            PrintWriter(socket.getOutputStream(), true).use { out ->
                out.println(cellJson)
            }
        }
    }
}

fun main(args: Array<String>) {
    val builder = WorldBuilder("localhost", 9999)
    // Exemple : "x,y,radius"
    builder.addCell(CellImpl("BouncyCell", Position(100.0, 100.0), 10.0))
}
