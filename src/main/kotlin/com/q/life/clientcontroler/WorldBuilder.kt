package com.q.life.clientcontroler

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Position
import java.io.*
import java.net.Socket


class WorldBuilder(val host: String, val port: Int) {
    fun addCell(cell: Cell) {
        val mapper = jacksonObjectMapper()
        val cellNode = mapper.valueToTree<ObjectNode>(cell)
        cellNode.put("type", cell::class.java.name)
        val cellJson = mapper.writeValueAsString(cellNode)

        Socket(host, port).use { socket ->
            PrintWriter(socket.getOutputStream(), true).use { out ->
                out.println(cellJson)
            }
        }
    }
}

fun main(args: Array<String>) {
    val builder = WorldBuilder("localhost", 9950)
    // Exemple : "x,y,radius"
    builder.addCell(BouncyCell("BouncyCell", Position(100.0, 100.0), 10.0))
}
