package com.q.life.clientcontroler

import com.q.projects.metamodele.Position
import kotlin.math.sqrt


class Vector(var x: Double, var y: Double) {
    constructor(position: Position) : this(position.x, position.y)

    fun add(other: Vector) {
        x += other.x
        y += other.y
    }

    fun multiply(scalar: Double): Vector {
        x *= scalar
        y *= scalar
        return this
    }

    val magnitude: Double
        get() = sqrt(x * x + y * y)

    fun normalized(): Vector {
        val mag = magnitude
        return Vector(x / mag, y / mag)
    }

    val isZero: Boolean
        get() = x == 0.0 && y == 0.0

    fun setZero() {
        x = 0.0
        y = 0.0
    }

    fun distanceTo(other: Vector): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }

    fun toPosition(): Position {
        return Position(x, y)
    }

    fun posEquals(other: Vector, tolerance: Double = 0.01): Boolean {
        return Math.abs(this.x - other.x) < tolerance && Math.abs(this.y - other.y) < tolerance
    }

}
