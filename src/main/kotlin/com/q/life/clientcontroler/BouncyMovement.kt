package com.q.life.clientcontroler


class BouncyMovement(position: Vector, maxSpeed: Double, slowdownDistance: Double) {
    private enum class State {
        STOP, PREPARE, ACCELERATE, SLOWDOWN
    }

    val isMovementComplete: Boolean
        get() = currentState == State.STOP && position.posEquals(targetPosition, 0.5)

    private var currentState = State.STOP
    private var velocity = Vector(0.0, 0.0)
    private var targetVelocity = Vector(0.0, 0.0)
    var targetPosition = Vector(0.0, 0.0)

    val position: Vector
    private val maxSpeed: Double
    private val slowdownDistance: Double

    init {
        this.position = position
        this.maxSpeed = maxSpeed
        this.slowdownDistance = slowdownDistance
        velocity = Vector(0.0, 0.0) // Supposons que Vector est une classe avec des champs x, y.
        targetVelocity = Vector(0.0, 0.0)
    }
    fun setTargetVelocityAndPosition(targetVel: Vector, targetPos: Vector) {
        targetVelocity = targetVel
        targetPosition = targetPos
        currentState = State.PREPARE
    }

    fun update() {
        when (currentState) {
            State.STOP -> {
                if (!velocity.isZero) {
                    velocity.multiply(0.95)
                    if (velocity.magnitude < 0.01) {
                        velocity.setZero()
                    }
                }
            }
            State.PREPARE -> {
                velocity.add(targetVelocity.normalized().multiply(-0.1))
                currentState = State.ACCELERATE
            }
            State.ACCELERATE -> {
                velocity.add(targetVelocity.normalized().multiply(0.2))
                if (velocity.magnitude > maxSpeed) {
                    velocity = targetVelocity.normalized().multiply(maxSpeed)
                    currentState = State.SLOWDOWN
                }
            }
            State.SLOWDOWN -> {
                if (position.distanceTo(targetPosition) < slowdownDistance || velocity.magnitude > targetVelocity.magnitude) {
                    velocity.multiply(0.9)
                    if (velocity.magnitude < targetVelocity.magnitude) {
                        currentState = State.STOP
                    }
                }
            }
        }
        position.add(velocity)
    }
}