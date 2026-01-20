package tests;
import org.junit.jupiter.api.Test;
import server.Ghost;
import server.GameMap;
import server.GhostCL;
import utils.Index2D;

import static org.junit.jupiter.api.Assertions.*;

public class GhostTest {
    private int[][] board = {
            {1,1,1},
            {1,0,1},
            {1,0,1}
    };

    private GameMap map = new GameMap(board);

    /**
     * Test constructor
     */
    @Test
    void testConstructor() {
        Index2D start = new Index2D(1,1);
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, start);

        assertEquals(GhostCL.RANDOM_WALK0, g.getType());
        assertEquals("1,1", g.getPos(0));
        assertFalse(g.isEatable());
    }

    /**
     * Test setEatable + isEatable
     */
    @Test
    void testSetEatable() {
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1,1));
        long future = System.currentTimeMillis() + 2000;

        g.setEatable(future);
        assertTrue(g.isEatable());
    }

    /**
     * Test remainTimeAsEatable
     */
    @Test
    void testRemainTimeAsEatable() {
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1,1));
        long future = System.currentTimeMillis() + 3000;

        g.setEatable(future);
        double time = g.remainTimeAsEatable(0);

        assertTrue(time > 0 && time <= 3);
    }

    /**
     * Test reset
     */
    @Test
    void testReset() {
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1,1));
        Index2D newStart = new Index2D(1,2);

        g.setEatable(System.currentTimeMillis() + 2000);
        g.reset(newStart);

        assertEquals("1,2", g.getPos(0));
        assertFalse(g.isEatable());
    }

    /**
     * Test getStatus
     */
    @Test
    void testGetStatus() {
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1,1));
        assertEquals(GhostCL.PLAY, g.getStatus());
    }

    /**
     * Test getPos2D
     */
    @Test
    void testGetPos2D() {
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1,1));
        Index2D p = g.getPos2D();

        assertEquals(1, p.getX());
        assertEquals(1, p.getY());
    }

    /**
     * Test moveRandom
     */
    @Test
    void testMoveRandom() {
        Ghost g = new Ghost(GhostCL.RANDOM_WALK0, new Index2D(1,1));

        g.moveRandom(map);
        Index2D p = g.getPos2D();

        assertTrue(map.isFree(p));
    }
}
