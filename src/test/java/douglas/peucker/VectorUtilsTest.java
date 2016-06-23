package douglas.peucker;

import static douglas.peucker.VectorUtils.*;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.awt.geom.Point2D;



public class VectorUtilsTest  {

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
}