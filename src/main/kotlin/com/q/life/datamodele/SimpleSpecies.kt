package com.q.projects.datamodele

import com.q.projects.metamodele.Position
import com.q.projects.metamodele.Cell

class SimpleSpecies(name: String, position: Position, size: Double) : Cell(name, position, size) {
    override fun move() {
        // Implement movement logic here
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
