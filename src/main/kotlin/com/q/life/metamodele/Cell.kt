package com.q.projects.metamodele

import com.q.projects.controller.Quadtree
import kotlinx.coroutines.*


abstract class Cell(val name: String, var position: Position, var size: Double) {
    constructor() : this("default", Position(0.0, 0.0), 0.0)

    var isInvulnerable: Boolean = true
    var isAlive: Boolean = true

    init {
    }
    abstract fun move()
    fun eat(other: Cell) {
        if (this.size > other.size && !other.isInvulnerable && other.isAlive) {
            this.size += other.size*.4;
            other.die()
            System.out.println("Cell $name ate cell ${other.name}")
        }
    }
    abstract fun behavior(species: List<Cell>)
    fun die(){
        isAlive = false
        System.out.println("Cell $name died")
    }
    fun birth(){
        isAlive = true
        startRoutine()
        startInvulnerabilityPeriod()
    }

    private fun startInvulnerabilityPeriod() {
        CoroutineScope(Dispatchers.Default).launch {
            delay(3000) // Attend 3 secondes
            isInvulnerable = false // Rend la cellule vulnérable après 3 secondes
        }
    }
    private fun startRoutine() {
        CoroutineScope(Dispatchers.Default).launch() {
            while (isActive) { // Boucle tant que la coroutine est active
                move()
                checkCollisionsAndEat(this@Cell, WorldHolder.world!!.quadtree)
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

    fun checkCollisionsAndEat(cell: Cell, quadtree: Quadtree) {
        val range = Rectangle(cell.position.x - cell.size, cell.position.y - cell.size, cell.size * 2, cell.size * 2)
        val nearbyCells = quadtree.query(range)

        for (other in nearbyCells) {
            if (cell != other && cell.detectCollision(cell, other)) {
                cell.eat(other)
            }
        }
    }


}
