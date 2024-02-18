package com.q.life.clientcontroler

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Position
import com.q.projects.metamodele.World
import com.q.projects.metamodele.WorldHolder
import java.io.*
import java.net.Socket


class WorldBuilder(val host: String = "localhost", val port: Int = 9950) {

    companion object {
        @JvmStatic
        fun initServer(): WorldBuilder {
            return WorldBuilder()
        }
    }
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
    fun createRandomCell() {
        var randomInt = (5..20).random()
        var width = (0..WorldHolder.world!!.width.toInt()).random().toDouble()
        var height = (0..WorldHolder.world!!.height.toInt()).random().toDouble()
        addCell(BasicCell("randomCell"+ randomInt, Position(width, height), randomInt.toDouble()))
    }

    fun createXRandomCell(x:Int) {
        for (i in 0..x) {
            createRandomCell()

        }
    }

}
fun main(args: Array<String>) {
    val builder = WorldBuilder()
    // Exemple : "x,y,radius"
    builder.createRandomCell()
}


