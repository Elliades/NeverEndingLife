package com.q.life.clientcontroller

import com.q.life.clientcontroler.Vector
import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.WorldHolder

class BouncyMovement(var cell: Cell, private val slowdownDistance: Double) {
    private enum class State { STOP, PREPARE, ACCELERATE, SLOWDOWN }

    private var currentState = State.STOP
    private var velocity = Vector(0.0, 0.0)
    private var targetVelocity = Vector(0.0, 0.0)
    var targetPosition = Vector(0.0, 0.0)
    var maxSpeed = 2*cell.speed

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
                // Générer une nouvelle direction aléatoire après un arrêt complet pour un rebond visible
                targetVelocity = generateRandomDirection()
            }
        }else{
            currentState = State.PREPARE
            targetVelocity = generateRandomDirection()
        }
    }

    private fun handlePrepareState() {
        // Assurer un mouvement perceptible en arrière pour marquer la préparation
        velocity = targetVelocity.normalized().multiply(-maxSpeed * 0.1)
        currentState = State.ACCELERATE
    }

    private fun handleAccelerateState() {
        // Appliquer une accélération plus significative pour marquer l'état
        velocity.add(targetVelocity.normalized().multiply(maxSpeed * 0.5))
        if (velocity.magnitude > maxSpeed) {
            velocity = targetVelocity.normalized().multiply(maxSpeed)
            currentState = State.SLOWDOWN
        }
    }

    private fun handleSlowdownState() {
        // Commencer le ralentissement plus tôt pour un arrêt plus naturel
        if (cell.position.distanceTo(targetPosition) < slowdownDistance) {
            velocity.multiply(0.8)
            if (velocity.magnitude < maxSpeed * 0.2) {
                currentState = State.STOP
            }
        }
    }

    private fun handleBorders() {
        val worldWidth = WorldHolder.world!!.width
        val worldHeight = WorldHolder.world!!.height
        var hitBorder = false

        // Vérifier et ajuster pour les bords horizontaux
        if (cell.position.x - cell.size < 0) {
            cell.position.x = cell.size
            velocity.x = -velocity.x // Inverser la composante x de la vitesse
            hitBorder = true
        } else if (cell.position.x + cell.size > worldWidth) {
            cell.position.x = worldWidth - cell.size
            velocity.x = -velocity.x
            hitBorder = true
        }

        // Vérifier et ajuster pour les bords verticaux
        if (cell.position.y - cell.size < 0) {
            cell.position.y = cell.size
            velocity.y = -velocity.y // Inverser la composante y de la vitesse
            hitBorder = true
        } else if (cell.position.y + cell.size > worldHeight) {
            cell.position.y = worldHeight - cell.size
            velocity.y = -velocity.y
            hitBorder = true
        }

        // Conserver la vitesse constante après le rebond
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
