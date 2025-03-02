package agh.ics.oop.model


fun <T : WorldElement> Map<Vector2d, T>.randomPosition(): Vector2d? {
    return if (this.isNotEmpty()) {
        this.keys.random()
    } else {
        null
    }
}

fun <T : WorldElement> Map<Vector2d, T>.randomFreePosition(mapSize: Vector2d): Vector2d? {
    val allPositions = mutableSetOf<Vector2d>()

    for (x in 0 until mapSize.x) {
        for (y in 0 until mapSize.y) {
            allPositions.add(Vector2d(x, y))
        }
    }

    val freePositions = allPositions.filter { position -> !this.containsKey(position) }

    return if (freePositions.isNotEmpty()) {
        freePositions.random()
    } else {
        null
    }
}
