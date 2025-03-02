package agh.ics.oop.model

import agh.ics.oop.model.exceptions.IncorrectPositionException
import java.util.*

interface WorldMap : MoveValidator {
    @Throws(IncorrectPositionException::class)
    fun place(animal: Animal)
    fun move(animal: Animal, moveDirection: MoveDirection)
    fun isOccupied(position: Vector2d): Boolean
    fun objectAt(position: Vector2d): WorldElement?
    fun getElements(): Collection<WorldElement>
    fun getCurrentBounds(): Boundary
}