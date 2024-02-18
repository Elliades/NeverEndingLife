package com.q.life.datamodele

import com.q.projects.metamodele.Cell
import com.q.projects.metamodele.Position

class CellImpl (name:String, position:Position, size:Double): Cell(name, position, size){
    constructor() : this("default", Position(0.0, 0.0), 0.0)

    override fun move() {
        System.out.println("NOT YET IMPLEMENTED")
    }

    override fun behavior(species: List<Cell>) {
        System.out.println("NOT YET IMPLEMENTED")
    }





}