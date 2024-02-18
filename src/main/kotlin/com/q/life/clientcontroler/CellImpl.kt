package com.q.life.clientcontroler

import com.q.projects.datamodele.BasicCell
import com.q.projects.metamodele.Position
import kotlin.random.Random

class CellImpl(name: String, position: Position, size: Double) : BasicCell(name, position, size) {
    constructor() : this("default", Position(0.0, 0.0), 0.0)
    private val bouncyMovement = BouncyMovement(Vector(position), maxSpeed = 1.0, slowdownDistance = 5.0)

    init {
         bouncyMovement.targetPosition = Vector(generateRandomPosition())
    }
    fun generateRandomPosition(): Position {
        val x = Random.nextDouble(position.x - speed, position.x + speed)
        val y =  Random.nextDouble(position.y - speed, position.y + speed)
        return Position(x, y)
    }

    override fun move() {
        super.move()
        if (bouncyMovement.isMovementComplete) {
            val newTargetPosition = generateRandomPosition()
            bouncyMovement.targetPosition = Vector(newTargetPosition)
        }
        bouncyMovement.update()
        this.position = bouncyMovement.position.toPosition()
    }
}
