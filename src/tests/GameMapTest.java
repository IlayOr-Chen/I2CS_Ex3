package tests;
import org.junit.jupiter.api.Test;
import utils.Index2D;
import server.GameMap;
import server.Game;

import static org.junit.jupiter.api.Assertions.*;

public class GameMapTest {
    private int[][] board = {
            {1,1,1},
            {1,0,2},
            {1,3,1}
    };

    private GameMap map = new GameMap(board);

    /**
     * Test constructor + getWidth + getHeight
     */
    @Test
    void testConstructorAndSize() {
        assertEquals(3, map.getWidth());
        assertEquals(3, map.getHeight());
    }

    /**
     * Test isInside
     */
    @Test
    void testIsInside() {
        assertTrue(map.isInside(new Index2D(0,0)));
        assertTrue(map.isInside(new Index2D(2,2)));
        assertFalse(map.isInside(new Index2D(-1,0)));
        assertFalse(map.isInside(new Index2D(3,1)));
        assertFalse(map.isInside(new Index2D(1,3)));
    }

    /**
     * Test isFree
     */
    @Test
    void testIsFree() {
        assertFalse(map.isFree(new Index2D(0,0)));
        assertTrue(map.isFree(new Index2D(1,1)));
        assertTrue(map.isFree(new Index2D(1,2)));
        assertFalse(map.isFree(new Index2D(5,5)));
    }

    /**
     * Test get
     */
    @Test
    void testGet() {
        assertEquals(GameMap.BLUE, map.get(new Index2D(0,0)));
        assertEquals(GameMap.EMPTY, map.get(new Index2D(1,1)));
        assertEquals(GameMap.PINK, map.get(new Index2D(1,2)));
        assertEquals(GameMap.GREEN, map.get(new Index2D(2,1)));
    }

    /**
     * Test set
     */
    @Test
    void testSet() {
        Index2D p = new Index2D(1,1);
        map.set(p, GameMap.GREEN);
        assertEquals(GameMap.GREEN, map.get(p));
    }


    /**
     * Test step
     */
    @Test
    void testStepDirections() {
        Index2D p = new Index2D(1,1);

        assertEquals(new Index2D(1,2), map.step(p, Game.UP));
        assertEquals(new Index2D(1,0), map.step(p, Game.DOWN));
        assertEquals(new Index2D(0,1), map.step(p, Game.LEFT));
        assertEquals(new Index2D(2,1), map.step(p, Game.RIGHT));
    }

    /**
     * Test step with cyclic behavior
     */
    @Test
    void testStepCyclic() {
        Index2D p1 = new Index2D(0,0);
        Index2D p2 = new Index2D(2,2);

        assertEquals(new Index2D(2,0), map.step(p1, Game.LEFT));
        assertEquals(new Index2D(0,2), map.step(p1, Game.DOWN));

        assertEquals(new Index2D(0,2), map.step(p2, Game.RIGHT));
        assertEquals(new Index2D(2,0), map.step(p2, Game.UP));
    }

    /**
     * Test getBoard
     */
    @Test
    void testGetBoard() {
        int[][] b = map.getBoard();
        assertNotNull(b);
        assertEquals(3, b.length);
        assertEquals(3, b[0].length);
        assertEquals(1, b[0][0]);
    }
}
