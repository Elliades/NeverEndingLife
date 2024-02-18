package com.q.projects.datamodele

import com.q.projects.metamodele.Position
import com.q.projects.metamodele.Cell
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

open class BasicCell(name: String, position: Position, size: Double) : Cell(name, position, size) {

    override fun move() {
        // Mettez à jour la direction de manière aléatoire mais fluide.
        direction += (Math.random() - 0.5) * PI / 18 // Change la direction de max +/- 10 degrés.

        // Calcul des composantes de déplacement en x et y en fonction de la direction et de la vitesse.
        val dx = cos(direction) * speed
        val dy = sin(direction) * speed

        // Mettre à jour la position de la cellule avec une vérification des limites.
        val newX = position.x + dx
        val newY = position.y + dy

        // Vérifier les bords horizontaux et ajuster si nécessaire
        if (newX - size < 0) {
            position.x = size
            direction = PI - direction
        } else if (newX + size > worldWidth) {
            position.x = worldWidth - size
            direction = PI - direction
        } else {
            position.x = newX
        }

        // Vérifier les bords verticaux et ajuster si nécessaire
        if (newY - size < 0) {
            position.y = size
            direction = -direction
        } else if (newY + size > worldHeight) {
            position.y = worldHeight - size
            direction = -direction
        } else {
            position.y = newY
        }
    }

    override fun eat(other: Cell) {
        val samePosition = other.position.equals(this.position)
        if (samePosition){
            other.die();
            this.size += other.size*0.50;
        }
    }

    override fun behavior(species: List<Cell>) {
        // Implement the specific species' behavior here
    }

    override fun die() {
        TODO("Not yet implemented")
    }

    override fun birth() {
        TODO("Not yet implemented")
    }
}
