package com.q.life.datamodele

import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Position

class CellFactory {

    var factory: CellFactory? = null


    fun createCell(name: String): Cell {
        return createCell(name, Position(0.0, 0.0), 1.0)
    }

    fun createCell(name: String, position: Position, size: Double): Cell {
        return BasicCell(name, position, size)
    }
}