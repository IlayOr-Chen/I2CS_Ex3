package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import utils.Index2D;
import utils.Map;
import utils.Map2D;
import utils.Pixel2D;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Intro2CS, 2026A, this is a very
 */
class MapTest {
    /**
     */
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private Map2D _m0, _m1, _m3_3;

    /**
     * Initializes maps before each test.
     */
    @BeforeEach
    public void setup() {
        _m3_3 = new Map(_map_3_3);
        _m1 = new Map(5);
        _m0 = new Map(5);
    }

    /**
     * Tests that initializing two maps with the same array produces equal maps.
     */
    @Test
    void testInit() {
        Map map = new Map(3,3,7);

        assertEquals(3, map.getWidth());
        assertEquals(3, map.getHeight());

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                assertEquals(7, map.getPixel(x,y));
            }
        }
    }

    /**
     * Tests all constructors of Map:
     * - Map(width, height, value)
     * - Map(size)
     * - Map(array)
     */
    @Test
    void testConstructors() {
        Map2D m1 = new Map(5,4,7);
        assertEquals(5, m1.getWidth());
        assertEquals(4, m1.getHeight());
        assertEquals(7, m1.getPixel(0,0));

        Map2D m2 = new Map(4);
        assertEquals(4, m2.getWidth());
        assertEquals(4, m2.getHeight());

        Map m3 = new Map(_map_3_3);
        assertEquals(3, m3.getWidth());
        assertEquals(3, m3.getHeight());
        assertEquals(1, m3.getPixel(1,0));

        assertThrows(RuntimeException.class, () -> new Map(0,3,1));
        assertThrows(RuntimeException.class, () -> new Map(-1));
        assertThrows(RuntimeException.class, () -> new Map((int[][]) null));
    }

    /**
     * Test getMap returns a deep copy.
     */
    @Test
    void testGetMap() {
        int[][] copy = _m3_3.getMap();
        assertEquals(0, copy[0][0]);
        copy[0][0] = 9;
        // ensure deep copy
        assertNotEquals(9, _m3_3.getPixel(0,0));
    }

    /**
     * Tests getPixel and setPixel methods.
     */
    @Test
    void testGetSetPixel() {
        Map2D map = new Map(2,2,0);
        map.setPixel(0,0,7);
        map.setPixel(new Index2D(1,1), 9);
        assertEquals(7, map.getPixel(0,0));
        assertEquals(9, map.getPixel(new Index2D(1,1)));
        assertThrows(RuntimeException.class, () -> map.getPixel(-1,0));
        assertThrows(RuntimeException.class, () -> map.setPixel(new Index2D(2,0),5));
    }

    /**
     * Tests the isInside method.
     * Verifies that the map correctly identifies whether a pixel is inside its bounds
     */
    @Test
    void testIsInside() {
        Map2D map = new Map(2, 3, 0);

        // Pixel inside the map
        assertTrue(map.isInside(new Index2D(1, 1)));

        // Pixel outside the map
        assertFalse(map.isInside(new Index2D(-1, 0)));

        // Pixel outside the map
        assertFalse(map.isInside(new Index2D(2, 0)));
    }

    /**
     * Tests fill method including cyclic behavior.
     */
    @Test
    void testFill() {
        Map2D map = new Map(3,3,0);
        map.setCyclic(false);

        int filled = map.fill(new Index2D(1,1),7);
        assertEquals(9, filled); // whole map should be filled
        assertEquals(7,map.getPixel(0,0));
    }

    /**
     * Tests fill throws exception on null.
     */
    @Test
    void testFillException() {
        assertThrows(RuntimeException.class, () -> _m0.fill(null, 1));
    }

    /**
     * Tests shortestPath method.
     */
    @Test
    void testShortestPath() {
        Map2D map = new Map(3,3,0);
        map.setPixel(1,1,-1);

        map.setCyclic(false);
        Pixel2D[] path = map.shortestPath(new Index2D(0,0), new Index2D(2,2), -1);
        assertEquals(new Index2D(0,0), path[0]);
        assertEquals(new Index2D(2,2), path[path.length-1]);
    }

    /**
     * Tests shortestPath throws exception on null.
     */
    @Test
    void testShortestPathException() {
        assertThrows(RuntimeException.class, () -> _m0.shortestPath(null, new Index2D(1,1), -1));
    }

    /**
     * Tests allDistance method.
     */
    @Test
    void testAllDistance() {
        Map2D map = new Map(3,3,0);
        map.setPixel(1,1,-1);

        map.setCyclic(false);
        Map2D dist = map.allDistance(new Index2D(0,0),-1);
        assertEquals(0, dist.getPixel(0,0));
        assertEquals(-1, dist.getPixel(1,1));
    }
}