package agh.ics.oop.model.util;

import agh.ics.oop.model.elements.Animal;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnimalComparatorTest {

    private final AnimalComparator sut = new AnimalComparator();

    @Test
    void compareAnimalByEnergy() {
        var greater = Animal.builder()
                .energy(120)
                .build();

        var lower = Animal.builder()
                .energy(10)
                .build();

        assertEquals(1, sut.compare(greater, lower));
        assertEquals(-1, sut.compare(lower, greater));
    }

    @Test
    void compareAnimalByEnergyAge() {
        var greater = Animal.builder()
                .energy(120)
                .startDay(5)
                .build();

        var lower = Animal.builder()
                .energy(120)
                .startDay(10)
                .build();

        assertEquals(1, sut.compare(greater, lower));
        assertEquals(-1, sut.compare(lower, greater));
    }


    @Test
    void compareAnimalByChildren() {
        var greater = Animal.builder()
                .energy(120)
                .startDay(5)
                .build();
        greater.addChild(Animal.builder().build());
        greater.addChild(Animal.builder().build());

        var lower = Animal.builder()
                .energy(120)
                .startDay(5)
                .build();
        lower.addChild(Animal.builder().build());

        assertEquals(1, sut.compare(greater, lower));
        assertEquals(-1, sut.compare(lower, greater));
    }

    @Test
    void streamSortShouldReturnStrongestAnimal() {
        // given
        var animalStrongest = Animal.builder()
                .energy(120)
                .startDay(5)
                .build();
        animalStrongest.addChild(Animal.builder().build());
        animalStrongest.addChild(Animal.builder().build());

        var animal2 = Animal.builder()
                .energy(120)
                .startDay(5)
                .build();
        animal2.addChild(Animal.builder().build());

        var animal3 = Animal.builder()
                .energy(50)
                .startDay(5)
                .build();


        var list = new ArrayList<>(List.of(animalStrongest, animal2, animal3));
        Collections.shuffle(list);

        // when
        var strongestOpt = list.stream().max(sut);

        //then
        assertTrue(strongestOpt.isPresent());
        assertEquals(animalStrongest, strongestOpt.get());
    }

}