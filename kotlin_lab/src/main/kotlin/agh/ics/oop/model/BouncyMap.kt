package agh.ics.oop.model

import agh.ics.oop.model.exceptions.IncorrectPositionException

class BouncyMap(width: Int, height: Int) : WorldMap {
    private val animals = HashMap<Vector2d, Animal>()
    private val boundary: Boundary = Boundary(Vector2d(0, 0), Vector2d(width - 1, height - 1))


    override fun place(animal: Animal) {
        val position = animal.getPosition

        if (isOccupied(position)) {
            val freePosition =
                animals.randomFreePosition(Vector2d(boundary.rightTopCorner.x, boundary.rightTopCorner.y))
            if (freePosition != null) {
                animals[freePosition] = animal
            } else {
                val positionToEvict = animals.randomPosition()
                if (positionToEvict != null) {
                    animals.remove(positionToEvict)
                }
                animals[position] = animal
            }
        } else {
            if (!canMoveTo(position)) {
                throw IncorrectPositionException("Can't place animal at: $position")
            }
            animals[position] = animal
        }
    }

    override fun move(animal: Animal, moveDirection: MoveDirection) {
        if (animals.containsValue(animal)) {
            animals.remove(animal.getPosition)
            animal.move(moveDirection, this)
            animals[animal.getPosition] = animal
        }
    }

    override fun canMoveTo(position: Vector2d): Boolean =
        position >= boundary.leftBottomCorner && position <= boundary.rightTopCorner

    override fun isOccupied(position: Vector2d): Boolean = animals.containsKey(position)

    override fun objectAt(position: Vector2d): WorldElement? = animals[position]

    override fun getElements(): Collection<WorldElement> = animals.values

    override fun getCurrentBounds(): Boundary = boundary
}