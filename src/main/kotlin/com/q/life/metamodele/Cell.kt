package com.q.projects.metamodele

import kotlinx.coroutines.*


abstract class Cell(val name: String, var position: Position, var size: Double) {
    constructor() : this("default", Position(0.0, 0.0), 0.0)

    init {
    }
    abstract fun move()
    abstract fun eat(other: Cell)
    abstract fun behavior(species: List<Cell>)
    abstract fun die()
    fun birth(){
        startRoutine()
    }

    private fun startRoutine() {
        CoroutineScope(Dispatchers.Default).launch() {
            while (isActive) { // Boucle tant que la coroutine est active
                move()
                delay(10L) // refresh rate of 100Hz
            }
        }
    }

    var speed = 5.0 /size
    var direction = Math.random() * 2 * Math.PI //direction in radians


    fun calculateDistance(position1: Position, position2: Position): Double {
        return Math.sqrt(Math.pow(position2.x - position1.x, 2.0) + Math.pow(position2.y - position1.y, 2.0))
    }
    fun detectCollision(cell1: Cell, cell2: Cell): Boolean {
        val distance = calculateDistance(cell1.position, cell2.position)
        return distance < (cell1.size + cell2.size)
    }

}
