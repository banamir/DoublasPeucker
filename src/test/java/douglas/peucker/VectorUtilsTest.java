package douglas.peucker;

import static douglas.peucker.VectorUtils.*;
import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;



public class VectorUtilsTest  {

    public static void assertPoint2DEquals(Point2D expected, Point2D actual, double delta) {
        assertEquals(expected.getX(),actual.getX(), delta);
        assertEquals(expected.getY(),actual.getY(), delta);
    }

    private static final double DELTA = 1e-15;

    @Test
    public void testDistanseToSigment() throws Exception {
        Point2D a,b,p;

        a = new Point2D.Double(1., 1.);
        b = new Point2D.Double(1., 1.);

        p = new Point2D.Double(1., 1.);
        assertEquals(0.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(0., 1.);
        assertEquals(1.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(1., 3.);
        assertEquals(2.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(4., 1.);
        assertEquals(3.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(1., -3.);
        assertEquals(4.,distanseToSigment(p,a,b),DELTA);

        p = new Point2D.Double(0., 2.);
        assertEquals(Math.sqrt(2.),distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(2., 3.);
        assertEquals(Math.sqrt(5.),distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(4., 0.);
        assertEquals(Math.sqrt(10.),distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(0., -3.);
        assertEquals(Math.sqrt(17.),distanseToSigment(p,a,b),DELTA);

        a = new Point2D.Double(2., 2.);
        b = new Point2D.Double(8., 10.);

        p = new Point2D.Double(5., 6.);
        assertEquals(0.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(9., 3.);
        assertEquals(5.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(1., 9.);
        assertEquals(5.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(2., -2.);
        assertEquals(4.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(-2., 2.);
        assertEquals(4.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(0., 0.);
        assertEquals(2.*Math.sqrt(2.),distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(11., 10.);
        assertEquals(3.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(8., 13.);
        assertEquals(3.,distanseToSigment(p,a,b),DELTA);
        p = new Point2D.Double(10., 12.);
        assertEquals(2.*Math.sqrt(2.),distanseToSigment(p,a,b),DELTA);


    }

    @Test
    public void testVectDif() throws Exception {
        Point2D a,b,c;

        a = new Point2D.Double(0., 0.);
        b = new Point2D.Double(0., 0.);
        c = vectDif(a,b);
        assertEquals(0.,c.getX(),DELTA);
        assertEquals(0.,c.getY(),DELTA);

        a = new Point2D.Double(0., 0.);
        b = new Point2D.Double(5.8, 6.7);
        c = vectDif(a,b);
        assertEquals(-5.8,c.getX(),DELTA);
        assertEquals(-6.7,c.getY(),DELTA);

        a = new Point2D.Double(1., 2.);
        b = new Point2D.Double(3., 4.);
        c = vectDif(a,b);
        assertEquals(-2.,c.getX(),DELTA);
        assertEquals(-2.,c.getY(),DELTA);

        a = new Point2D.Double(0.2, 2.);
        b = new Point2D.Double(0.1, 1.);
        c = vectDif(a,b);
        assertEquals(0.1,c.getX(),DELTA);
        assertEquals(1.0,c.getY(),DELTA);

        a = new Point2D.Double(0.7, 0.3);
        b = new Point2D.Double(0.5, 0.1);
        c = vectDif(a,b);
        assertEquals(0.2,c.getX(),DELTA);
        assertEquals(0.2,c.getY(),DELTA);

    }

    @Test
    public void testVectSquare() throws Exception {
        Point2D a;

        a = new Point2D.Double(0.,0.);
        assertEquals(0.,vectSquare(a),DELTA);

        a = new Point2D.Double(-1.,1.);
        assertEquals(2.,vectSquare(a),DELTA);


        a = new Point2D.Double(0.5,0.1);
        assertEquals(0.26,vectSquare(a),DELTA);

        a = new Point2D.Double(0.,0.1);
        assertEquals(0.01,vectSquare(a),DELTA);

        a = new Point2D.Double(0.1,0.);
        assertEquals(0.01,vectSquare(a),DELTA);
    }

    @Test
    public void testVectLength() throws Exception {
        Point2D a;

        a = new Point2D.Double(0.,0.);
        assertEquals(0.,vectLength(a),DELTA);

        a = new Point2D.Double(-1.,1.);
        assertEquals(Math.sqrt(2.),vectLength(a),DELTA);


        a = new Point2D.Double(0.5,0.1);
        assertEquals(Math.sqrt(0.26),vectLength(a),DELTA);

        a = new Point2D.Double(0.,0.1);
        assertEquals(0.1,vectLength(a),DELTA);

        a = new Point2D.Double(0.1,0.);
        assertEquals(0.1,vectLength(a),DELTA);
    }

    @Test
    public void testScalarProduct() throws Exception {
        Point2D a,b;

        a = new Point2D.Double(0., 0.);
        b = new Point2D.Double(0., 0.);
        assertEquals(0.0,scalarProduct(a,b),DELTA);

        a = new Point2D.Double(0., 0.);
        b = new Point2D.Double(5.8, 6.7);
        assertEquals(0.0,scalarProduct(a,b),DELTA);

        a = new Point2D.Double(1., 2.);
        b = new Point2D.Double(-2, 1.);
        assertEquals(0.0,scalarProduct(a,b),DELTA);

        a = new Point2D.Double(1., 2.);
        b = new Point2D.Double(3., 4.);
        assertEquals(11.0,scalarProduct(a,b),DELTA);

        a = new Point2D.Double(0.2, 2.);
        b = new Point2D.Double(0.1, 1.);
        assertEquals(2.02,scalarProduct(a,b),DELTA);

        a = new Point2D.Double(0.7, 0.3);
        b = new Point2D.Double(0.5, 0.1);
        assertEquals(0.38,scalarProduct(a,b),DELTA);
    }

    @Test
    public void testVectProduct() throws Exception {
        Point2D a,b;

        a = new Point2D.Double(0., 0.);
        b = new Point2D.Double(0., 0.);
        assertEquals(0.0,vectProduct(a, b),DELTA);

        a = new Point2D.Double(0., 0.);
        b = new Point2D.Double(5.8, 6.7);
        assertEquals(0.0,vectProduct(a, b),DELTA);

        a = new Point2D.Double(1., 2.);
        b = new Point2D.Double(-2, 1.);
        assertEquals(5.0,vectProduct(a, b),DELTA);


        a = new Point2D.Double(1., 2.);
        b = new Point2D.Double(3., 4.);
        assertEquals(-2.,vectProduct(a, b),DELTA);

        a = new Point2D.Double(0.2, 2.);
        b = new Point2D.Double(0.1, 1.);
        assertEquals(0.,vectProduct(a, b),DELTA);

        a = new Point2D.Double(0.7, 0.3);
        b = new Point2D.Double(0.5, 0.1);
        assertEquals(-0.08,vectProduct(a, b),DELTA);
    }

    @Test
    public void testIntersect() throws Exception {
        Point2D a, b, c ,d;

        a = new Point2D.Double(1,1); b = new Point2D.Double(5,5);

        c = new Point2D.Double(2,2); d = new Point2D.Double(2,2);
        assertTrue(intersect(a,b,c,d));

        c = new Point2D.Double(4,2); d = new Point2D.Double(7,3);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(1,7); d = new Point2D.Double(2,4);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(9,8); d = new Point2D.Double(7,4);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(5,7); d = new Point2D.Double(5,7);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(6,6); d = new Point2D.Double(3,3);
        assertTrue(intersect(a, b, c, d));

        c = new Point2D.Double(3,5); d = new Point2D.Double(5,3);
        assertTrue(intersect(a, b, c, d));

        c = new Point2D.Double(5,7); d = new Point2D.Double(5,7);
        assertFalse(intersect(a, b, c, d));


        c = new Point2D.Double(0,0); d = new Point2D.Double(5,0);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(7,0); d = new Point2D.Double(9,0);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(7,2); d = new Point2D.Double(7,4);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(0,2); d = new Point2D.Double(0,6);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(2,3); d = new Point2D.Double(2,5);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(7,1); d = new Point2D.Double(9,1);
        assertFalse(intersect(a, b, c, d));

        c = new Point2D.Double(3,6); d = new Point2D.Double(3,2);
        assertTrue(intersect(a, b, c, d));

        c = new Point2D.Double(4,4); d = new Point2D.Double(6,4);
        assertTrue(intersect(a, b, c, d));

    }

    @Test
    public void testIntersect1() throws Exception {

        double a = 0., b = 2., c = 4., d = 6.;

        assertFalse(intersect(a, b, c, d));
        assertFalse(intersect(c, d, a, b));
        assertFalse(intersect(b, a, d, c));
        assertTrue(intersect(a, c, b, d));
        assertTrue(intersect(a, d, b, c));
        assertTrue(intersect(b, c, a, d));
        assertTrue(intersect(b,c,c,d));
        assertTrue(intersect(b,c,d,c));
    }

    @Test
    public void testIntersectPoint() throws Exception {
        Point2D a, b, c ,d,p;

        a = new Point2D.Double(1,1); b = new Point2D.Double(5,5);

        c = new Point2D.Double(2,2); d = new Point2D.Double(2,2);
        assertNotNull(p = intersectPoint(a,b,c,d));
        assertEquals(2., p.getX(), DELTA);
        assertEquals(2., p.getY(), DELTA);

        c = new Point2D.Double(4,2); d = new Point2D.Double(7,3);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(1,7); d = new Point2D.Double(2,4);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(9,8); d = new Point2D.Double(7,4);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(5,7); d = new Point2D.Double(5,7);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(6,6); d = new Point2D.Double(3,3);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(3,5); d = new Point2D.Double(5,3);
        assertNotNull(p = intersectPoint(a, b, c, d));
        assertEquals(4., p.getX(), DELTA);
        assertEquals(4., p.getY(), DELTA);


        c = new Point2D.Double(5,7); d = new Point2D.Double(5,7);
        assertNull(intersectPoint(a, b, c, d));


        c = new Point2D.Double(0,0); d = new Point2D.Double(5,0);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(7,0); d = new Point2D.Double(9,0);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(7,2); d = new Point2D.Double(7,4);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(0,2); d = new Point2D.Double(0,6);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(2,3); d = new Point2D.Double(2,5);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(7,1); d = new Point2D.Double(9,1);
        assertNull(intersectPoint(a, b, c, d));

        c = new Point2D.Double(3,6); d = new Point2D.Double(3,2);
        assertNotNull(p = intersectPoint(a, b, c, d));
        assertEquals(3., p.getX(),DELTA);
        assertEquals(3., p.getY(), DELTA);

        c = new Point2D.Double(4,4); d = new Point2D.Double(6,4);
        assertNotNull(p = intersectPoint(a, b, c, d));
        assertEquals(4., p.getX(),DELTA);
        assertEquals(4., p.getY(), DELTA);
    }
}