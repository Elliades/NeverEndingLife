package com.q.projects.metamodele

data class Rectangle(val x: Double, val y: Double, val width: Double, val height: Double) {
    fun contains(point: Position): Boolean =
        point.x >= x && point.x <= x + width && point.y >= y && point.y <= y + height

    fun intersects(range: Rectangle): Boolean =
        !(range.x > x + width || range.x + range.width < x || range.y > y + height || range.y + range.height < y)
}
