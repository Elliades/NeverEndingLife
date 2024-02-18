package com.q.life.clientcontroller;

import com.q.life.clientcontroler.Vector
import com.q.projects.metamodele.Cell;
import com.q.projects.metamodele.WorldHolder;
import kotlin.random.Random;

class BouncyMovement (val cell: Cell) {
    private enum class State { STOP, PREPARE, ACCELERATE, SLOWDOWN }

    private var currentState = State.STOP
    private var velocity = Vector(0.0, 0.0)
    private var targetVelocity = Vector(0.0, 0.0)
    var targetPosition = Vector(0.0, 0.0)
    var maxSpeed = Random.nextDouble(1.5, 2.5) * cell.speed

    val isMovementComplete: Boolean
        get() = currentState == State.STOP && cell.position.equals(targetPosition.toPosition(), 0.5)

    fun setTargetVelocityAndPosition(targetVel: Vector, targetPos: Vector) {
        targetVelocity = targetVel
        targetPosition = targetPos
        if (targetVelocity.magnitude > 0) currentState = State.PREPARE
    }

    fun update() {
        when (currentState) {
            State.STOP -> handleStopState()
            State.PREPARE -> handlePrepareState()
            State.ACCELERATE -> handleAccelerateState()
            State.SLOWDOWN -> handleSlowdownState()
        }
        handleBorders()
        addPosition(velocity)
    }

    private fun handleStopState() {
        if (!velocity.isZero) {
            velocity.multiply(0.95)
            if (velocity.magnitude < 0.01) {
                velocity.setZero()
                currentState = State.PREPARE
                targetVelocity = generateRandomDirection()
            }
        }
    }

    private fun handlePrepareState() {
        velocity = targetVelocity.normalized().multiply(-maxSpeed * 0.3)
        currentState = State.ACCELERATE
    }

    private fun handleAccelerateState() {
        velocity.add(targetVelocity.normalized().multiply(maxSpeed * 0.8))
        if (velocity.magnitude > maxSpeed) {
            velocity = targetVelocity.normalized().multiply(maxSpeed)
            currentState = State.SLOWDOWN
        }
    }

    private fun handleSlowdownState() {
        if (cell.position.distanceTo(targetPosition) < 2) {
            velocity.multiply(0.5)
            if (velocity.magnitude < maxSpeed * 0.2) {
                currentState = State.STOP
            }
        }
    }

    private fun handleBorders() {
        val worldWidth = WorldHolder.world!!.width
        val worldHeight = WorldHolder.world!!.height
        var hitBorder = false

        if (cell.position.x - cell.size < 0) {
            cell.position.x = cell.size
            velocity.x = -velocity.x
            hitBorder = true
        } else if (cell.position.x + cell.size > worldWidth) {
            cell.position.x = worldWidth - cell.size
            velocity.x = -velocity.x
            hitBorder = true
        }

        if (cell.position.y - cell.size < 0) {
            cell.position.y = cell.size
            velocity.y = -velocity.y
            hitBorder = true
        } else if (cell.position.y + cell.size > worldHeight) {
            cell.position.y = worldHeight - cell.size
            velocity.y = -velocity.y
            hitBorder = true
        }

        if (hitBorder) {
            velocity = velocity.normalized().multiply(maxSpeed)
        }
    }

    private fun addPosition(other: Vector) {
        cell.position.add(other)
    }

    private fun generateRandomDirection(): Vector {
        val angle = Math.random() * 2 * Math.PI
        return Vector(Math.cos(angle), Math.sin(angle)).normalized().multiply(maxSpeed)
    }
}
