package com.q.life.clientcontroler;

import com.q.life.clientcontroller.BouncyMovement
import com.q.projects.datamodele.BasicCell;
import com.q.projects.metamodele.Position;
import kotlin.random.Random;

class BouncyCell(name: String, position: Position, size: Double) : BasicCell(name, position, size) {
    constructor() : this("default", Position(0.0, 0.0), 0.0)
    private var bouncyMovement:BouncyMovement

    init {
        bouncyMovement = BouncyMovement(this)
        bouncyMovement.targetPosition = Vector(generateRandomPosition())
    }

    fun generateRandomPosition(): Position {
        val x = Random.nextDouble(position.x - speed, position.x + speed)
        val y = Random.nextDouble(position.y - speed, position.y + speed)
        return Position(x, y)
    }

    override fun move() {
        super.move()
        if (bouncyMovement.isMovementComplete) {
            val newTargetPosition = generateRandomPosition()
            bouncyMovement.targetPosition = Vector(newTargetPosition.x, newTargetPosition.y)

            val direction = Vector(newTargetPosition.x - position.x, newTargetPosition.y - position.y).normalized()
            bouncyMovement.setTargetVelocityAndPosition(direction, Vector(newTargetPosition.x, newTargetPosition.y))
        }
        bouncyMovement.update()
    }
}
