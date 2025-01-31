import agh.ics.oop.model.Animal
import agh.ics.oop.model.BouncyMap
import agh.ics.oop.model.Vector2d
import agh.ics.oop.model.exceptions.IncorrectPositionException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BouncyMapTest : FunSpec({
    lateinit var map: BouncyMap

    beforeEach {
        map = BouncyMap(5, 5)
    }

    test("should be initialized properly") {
        val boundary = map.getCurrentBounds()

        boundary.leftBottomCorner shouldBe Vector2d(0, 0)
        boundary.rightTopCorner shouldBe Vector2d(4, 4)
        map.getElements().size shouldBe 0
    }

    test("should place animal") {
        val animal = Animal(Vector2d(2, 1))

        map.place(animal)

        map.isOccupied(animal.getPosition) shouldBe true
        map.objectAt(animal.getPosition) shouldBe animal
    }

    test("should throw when animal position out of bounds") {
        val animal1 = Animal(Vector2d(100, 100))
        val animal2 = Animal(Vector2d(2, 100))
        val animal3 = Animal(Vector2d(100, 2))
        val animal4 = Animal(Vector2d(-100, 3))

        shouldThrow<IncorrectPositionException> {
            map.place(animal1)
            map.place(animal2)
            map.place(animal3)
            map.place(animal4)
        }
    }

    test("should bounce animal on other position when place at occupied") {
        val animal1 = Animal(Vector2d(2, 1))
        val animal2 = Animal(Vector2d(2, 1))

        map.place(animal1)
        map.place(animal2)

        map.isOccupied(Vector2d(2, 1)) shouldBe true
        map.objectAt(Vector2d(2, 1)) shouldBe animal1
        map.getElements().size shouldBe 2
    }

    test("should remove animal when there is no free positions") {
        val map = BouncyMap(1, 1)
        val animal1 = Animal(Vector2d(0, 0))
        val animal2 = Animal(Vector2d(0, 0))

        map.place(animal1)
        map.place(animal2)

        map.isOccupied(Vector2d(0, 0)) shouldBe true
        map.objectAt(Vector2d(0, 0)) shouldBe animal2
        map.getElements().size shouldBe 1
    }

})