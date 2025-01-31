package agh.ics.oop.model

class Animal(
    private var position: Vector2d = Vector2d(2, 2),
    private var orientation: MapDirection = MapDirection.NORTH
) : WorldElement {

    fun isAt(position: Vector2d) = this.position == position

    fun move(moveDirection: MoveDirection, validator: MoveValidator) {
        when (moveDirection) {
            MoveDirection.FORWARD -> updatePosition(position + orientation.unitVector, validator)
            MoveDirection.BACKWARD -> updatePosition(position - orientation.unitVector, validator)
            MoveDirection.LEFT -> orientation = orientation.previous()
            MoveDirection.RIGHT -> orientation = orientation.next()
        }
    }

    private fun updatePosition(newPosition: Vector2d, validator: MoveValidator) {
        if (validator.canMoveTo(newPosition)) {
            position = newPosition
        }
    }

    override fun toString() = orientation.symbol

    override val getPosition: Vector2d
        get() = position

    val getOrientation: MapDirection
        get() = orientation
}
