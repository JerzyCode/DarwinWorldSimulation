package agh.ics.oop.model

data class Vector2d(val x: Int, val y: Int) : Comparable<Vector2d> {
    override fun compareTo(other: Vector2d): Int {
        return when {
            x == other.x && y == other.y -> 0 // equal
            x > other.x || (x == other.x && y > other.y) -> 1 // follows
            else -> -1 // precedes
        }
    }

    operator fun plus(other: Vector2d) = Vector2d(x + other.x, y + other.y)

    operator fun minus(other: Vector2d) = Vector2d(x - other.x, y - other.y)

    fun upperRight(other: Vector2d) = Vector2d(Math.max(x, other.x), Math.max(y, other.y))

    fun lowerLeft(other: Vector2d) = Vector2d(Math.min(x, other.x), Math.min(y, other.y))

    fun opposite() = Vector2d(-x, -y)

    override fun toString() = "($x, $y)"

}