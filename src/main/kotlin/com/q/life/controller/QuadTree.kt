package com.q.projects.controller

import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Rectangle

class Quadtree(
    private val boundary: Rectangle,
    private val capacity: Int
) {
    private var cells: MutableList<Cell> = mutableListOf()
    private var divided: Boolean = false
    private var topLeft: Quadtree? = null
    private var topRight: Quadtree? = null
    private var bottomLeft: Quadtree? = null
    private var bottomRight: Quadtree? = null

    fun insert(cell: Cell): Boolean {
        if (!boundary.contains(cell.position)) return false

        if (cells.size < capacity) {
            cells.add(cell)
            return true
        }

        if (!divided) {
            subdivide()
        }

        return (topLeft?.insert(cell) ?: false) ||
                (topRight?.insert(cell) ?: false) ||
                (bottomLeft?.insert(cell) ?: false) ||
                (bottomRight?.insert(cell) ?: false)
    }

    private fun subdivide() {
        val x = boundary.x
        val y = boundary.y
        val w = boundary.width / 2
        val h = boundary.height / 2

        topLeft = Quadtree(Rectangle(x, y, w, h), capacity)
        topRight = Quadtree(Rectangle(x + w, y, w, h), capacity)
        bottomLeft = Quadtree(Rectangle(x, y + h, w, h), capacity)
        bottomRight = Quadtree(Rectangle(x + w, y + h, w, h), capacity)

        divided = true
    }

    // Fonction pour rechercher des cellules potentiellement en collision
    fun query(range: Rectangle, found: MutableList<Cell> = mutableListOf()): MutableList<Cell> {
        if (!boundary.intersects(range)) {
            return found
        }

        for (cell in cells) {
            if (range.contains(cell.position)) {
                found.add(cell)
            }
        }

        if (divided) {
            topLeft?.query(range, found)
            topRight?.query(range, found)
            bottomLeft?.query(range, found)
            bottomRight?.query(range, found)
        }

        return found
    }
    fun clear() {
        cells.clear()
        if (divided) {
            topLeft?.clear()
            topRight?.clear()
            bottomLeft?.clear()
            bottomRight?.clear()
            divided = false
        }
    }

}
