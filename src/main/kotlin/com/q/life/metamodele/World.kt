package com.q.projects.metamodele

class World(val width: Int, val height: Int) {
    var cells: MutableList<Cell> = mutableListOf()

    fun addCell(cell: Cell) {
        this.cells.add(cell)
    }

    fun update() {
        cells.forEach { it.behavior(cells) }
        // Add logic here to manage interactions between species
    }

    fun draw() {
        // This method will be responsible for displaying the species in the UI
    }
}
