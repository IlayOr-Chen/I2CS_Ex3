package tests;

import org.junit.jupiter.api.Test;
import utils.Index2D;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Index2DTest {

    /**
     * Test the Index2D Constructor
     */
    @Test
    void testIndex2DConstructor(){
        Index2D p1 = new Index2D(3, 5);
        Index2D p2 = new Index2D(-2, -7);
        Index2D p3 = new Index2D(0, 0);

        assertTrue(p1.getX() == 3 && p1.getY() == 5);
        assertTrue(p2.getX() == -2 && p2.getY() == -7);
        assertTrue(p3.getX() == 0 && p3.getY() == 0);
    }

    /**
     * Test the Index2D Copy Constructor
     */
    @Test
    void testIndex2DCopyConstructor(){
        Index2D p1 = new Index2D(9, 15);
        Index2D p2 = new Index2D(p1);

        assertEquals(9, p2.getX());
        assertEquals(15, p2.getY());
    }

    /**
     * Test the getX function.
     */
    @Test
    void testGetX(){
        Index2D p1 = new Index2D(9, 15);
        Index2D p2 = new Index2D(0, 0);
        assertEquals(9, p1.getX());
        assertEquals(0, p2.getX());
    }

    /**
     * Test the getY function.
     */
    @Test
    void testGetY(){
        Index2D p1 = new Index2D(9, 15);
        Index2D p2 = new Index2D(0, 0);
        assertEquals(15, p1.getY());
        assertEquals(0, p2.getY());
    }

    /**
     * Test the distance function.
     */
    @Test
    void testDistance2D(){
        final double EPS = 0.01;
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(4, 3);
        Index2D p3 = new Index2D(2, -6);
        Index2D p4 = new Index2D(7, 3);

        assertEquals(5.0, p1.distance2D(p2), EPS);
        assertEquals(10.29, p3.distance2D(p4), EPS);

        assertThrows(RuntimeException.class, () -> p1.distance2D(null));
    }

    /**
     * Test the toString function.
     */
    @Test
    void testToString(){
        Index2D p1 = new Index2D(3, 2);
        Index2D p2 = new Index2D(-7, -12);
        Index2D p3 = new Index2D(0, 0);

        assertEquals("3,2", p1.toString());
        assertEquals("-7,-12", p2.toString());
        assertEquals("0,0", p3.toString());
    }

    /**
     * Test the equals function.
     */
    @Test
    void testEquals(){
        Index2D p1 = new Index2D(1, 3);
        Index2D p2 = new Index2D(1, 3);
        Index2D p3 = new Index2D(2, 4);

        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));

        assertFalse(p1.equals(p3));

        assertFalse(p1.equals(null));
        assertFalse(p1.equals(4));
    }
}
