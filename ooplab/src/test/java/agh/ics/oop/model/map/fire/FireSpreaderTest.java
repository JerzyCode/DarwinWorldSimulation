package agh.ics.oop.model.map.fire;

import agh.ics.oop.model.Boundary;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.elements.Fire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireSpreaderTest {

    private FireValidator fireValidator;
    private FireSpreader fireSpreader;

    @BeforeEach
    void setUp() {
        fireValidator = mock(FireValidator.class);
        fireSpreader = new FireSpreader(10);
    }


    @Test
    void shouldReturnEmptyFireOptionalWhenInvalidPositions() {
        //given
        var availablePositions = new ArrayList<>(List.of(new Vector2d(2, 2), new Vector2d(3, 4)));
        availablePositions.forEach(pos -> when(fireValidator.canPlaceFire(pos)).thenReturn(false));

        //when
        var createdFire = fireSpreader.createNewFire(availablePositions, fireValidator);

        //then
        assertTrue(createdFire.isEmpty());
        verify(fireValidator, times(2)).canPlaceFire(any());
    }

    @Test
    void shouldReturnNewFire() {
        //given
        var availablePositions = new ArrayList<Vector2d>();
        availablePositions.add(new Vector2d(2, 2));
        when(fireValidator.canPlaceFire(any())).thenReturn(true);

        //when
        var createdFire = fireSpreader.createNewFire(availablePositions, fireValidator);

        //then
        assertTrue(createdFire.isPresent());
        assertEquals(new Vector2d(2, 2), createdFire.get().getPosition());
        assertFalse(createdFire.get().isBurned());
        verify(fireValidator, times(1)).canPlaceFire(any());
    }

    @Test
    void shouldReturnEmptySpreadFires() {
        //given
        var boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        var existingFires = List.of(
                new Fire(new Vector2d(1, 0), 1),
                new Fire(new Vector2d(5, 2), 1),
                new Fire(new Vector2d(1, 0), 1));

        when(fireValidator.canPlaceFire(new Vector2d(5, 1))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(5, 3))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(4, 2))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(6, 2))).thenReturn(false);

        when(fireValidator.canPlaceFire(new Vector2d(2, 0))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(0, 0))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(1, 10))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(1, 1))).thenReturn(false);

        //when
        var spreadFires = fireSpreader.getSpreadFires(existingFires, boundary, fireValidator);

        //then
        assertTrue(spreadFires.isEmpty());
        verify(fireValidator, times(8)).canPlaceFire(any());
    }

    @Test
    void shouldReturnOneSpreadFire() {
        //given
        var boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        var existingFires = List.of(
                new Fire(new Vector2d(3, 3), 1),
                new Fire(new Vector2d(5, 2), 1),
                new Fire(new Vector2d(3, 3), 1));

        when(fireValidator.canPlaceFire(new Vector2d(5, 1))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(5, 3))).thenReturn(true);
        when(fireValidator.canPlaceFire(new Vector2d(4, 2))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(6, 2))).thenReturn(false);

        when(fireValidator.canPlaceFire(new Vector2d(2, 3))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(4, 3))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(3, 4))).thenReturn(false);
        when(fireValidator.canPlaceFire(new Vector2d(3, 2))).thenReturn(false);

        //when
        var spreadFires = fireSpreader.getSpreadFires(existingFires, boundary, fireValidator);

        //then
        assertEquals(1, spreadFires.size());
        assertEquals(new Vector2d(5, 3), spreadFires.iterator().next().getPosition());
        verify(fireValidator, times(8)).canPlaceFire(any());
    }

    @Test
    void shouldReturnAllFiresAroundSpreadFire() {
        //given
        var boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        var existingFires = List.of(new Fire(new Vector2d(3, 3), 1));

        when(fireValidator.canPlaceFire(new Vector2d(2, 3))).thenReturn(true);
        when(fireValidator.canPlaceFire(new Vector2d(4, 3))).thenReturn(true);
        when(fireValidator.canPlaceFire(new Vector2d(3, 4))).thenReturn(true);
        when(fireValidator.canPlaceFire(new Vector2d(3, 2))).thenReturn(true);

        //when
        var spreadFires = fireSpreader.getSpreadFires(existingFires, boundary, fireValidator);

        //then
        assertEquals(4, spreadFires.size());
        var occupiedByFirePositions = spreadFires.stream()
                .map(Fire::getPosition)
                .collect(Collectors.toSet());

        assertTrue(occupiedByFirePositions.contains(new Vector2d(2, 3)));
        assertTrue(occupiedByFirePositions.contains(new Vector2d(4, 3)));
        assertTrue(occupiedByFirePositions.contains(new Vector2d(3, 4)));
        assertTrue(occupiedByFirePositions.contains(new Vector2d(3, 2)));

        verify(fireValidator, times(4)).canPlaceFire(any());
    }


    @Test
    void shouldSpreadFireOppositeSide() {
        //given
        var boundary = new Boundary(new Vector2d(0, 0), new Vector2d(10, 10));
        var existingFires = List.of(new Fire(new Vector2d(0, 5), 1), new Fire(new Vector2d(10, 7), 1));

        when(fireValidator.canPlaceFire(new Vector2d(10, 5))).thenReturn(true);
        when(fireValidator.canPlaceFire(new Vector2d(0, 7))).thenReturn(true);

        //when
        var spreadFires = fireSpreader.getSpreadFires(existingFires, boundary, fireValidator);

        //then
        var occupiedByFirePositions = spreadFires.stream()
                .map(Fire::getPosition)
                .collect(Collectors.toSet());

        assertTrue(occupiedByFirePositions.contains(new Vector2d(10, 5)));
        assertTrue(occupiedByFirePositions.contains(new Vector2d(0, 7)));
    }


}