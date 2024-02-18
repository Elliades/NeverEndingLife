package com.q.projects.metamodele

import com.q.life.clientcontroler.Vector
import kotlin.math.sqrt

data class Position(var x: Double, var y: Double){
    fun equals(other: Position, tolerance: Double = 0.01): Boolean {
        return Math.abs(this.x - other.x) < tolerance && Math.abs(this.y - other.y) < tolerance
    }

    fun add(other: Vector) {
        x += other.x
        y += other.y
    }

    fun distanceTo(other: Vector): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }
}


