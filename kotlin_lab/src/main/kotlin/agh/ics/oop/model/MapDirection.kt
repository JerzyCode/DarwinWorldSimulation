package agh.ics.oop.model

enum class MapDirection(val unitVector: Vector2d, val symbol: String) {
    NORTH(Vector2d(0, 1), "N"),
    SOUTH(Vector2d(0, -1), "S"),
    WEST(Vector2d(-1, 0), "W"),
    EAST(Vector2d(1, 0), "E");

    override fun toString() = when (this) {
        NORTH -> "Północ"
        SOUTH -> "Południe"
        WEST -> "Zachód"
        EAST -> "Wschód"
    }

    fun next() = when (this) {
        NORTH -> EAST
        SOUTH -> WEST
        WEST -> NORTH
        EAST -> SOUTH
    }

    fun previous() = when (this) {
        NORTH -> WEST
        SOUTH -> EAST
        WEST -> SOUTH
        EAST -> NORTH
    }

}