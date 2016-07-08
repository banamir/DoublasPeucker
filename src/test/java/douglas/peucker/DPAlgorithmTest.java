package douglas.peucker;

import org.junit.Test;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

import static douglas.peucker.VectorUtils.distanseToSigment;
import static douglas.peucker.VectorUtils.intersect;
import static org.junit.Assert.*;


public class DPAlgorithmTest {

    private static final double EPS = 1e-15;

    @Test
    public void testSimple() throws Exception {

        TestPolyline[] polylines = readTestPolylines("test");

        for(TestPolyline polyline : polylines){

         Point2D[] simplifed =  new DPAlgorithm(polyline.points).simple(polyline.eps);

         assertTrue(maxDistance(polyline.points,simplifed) <= polyline.eps);
         assertNotEquals(polyline.points.length, simplifed.length);
        }
    }

    @Test
    public void testSimple1() throws Exception {

        TestPolyline[] polylines = readTestPolylines("test");

        for(TestPolyline polyline : polylines){

            Point2D[] simplifed =  new DPAlgorithm(polyline.points).simpleN(polyline.k);

            assertEquals(polyline.k, simplifed.length);
        }
    }

    @Test
    public void testIterative() throws Exception {

        TestPolyline[] polylines = readTestPolylines("test");

        for(TestPolyline polyline : polylines){

            Point2D[] simplifed =  new DPAlgorithm(polyline.points).iterative(polyline.eps);

            assertTrue(maxDistance(polyline.points,simplifed) <= polyline.eps);
            assertNotEquals(polyline.points.length, simplifed.length);
        }
    }

    @Test
    public void testRobust() throws Exception {

        TestPolyline[] polylines = readTestPolylines("robust");
        for(TestPolyline polyline : polylines){

            Point2D[] simplifed =  new DPAlgorithm(polyline.points).robust(polyline.eps);

            assertTrue(maxDistance(polyline.points, simplifed) < polyline.eps);
            //assertNotEquals(polyline.points.length, simplifed.length);
            for(int i = 0; i < simplifed.length - 1; i++ )
                for(int j = 0; j < simplifed.length - 1; j++){
                    if(j < i - 1 || j > i + 1)
                        assertTrue(!intersect(simplifed[i],simplifed[i+1],
                                              simplifed[j],simplifed[j+1]));
                }
        }
    }


    public TestPolyline[] readTestPolylines(String fileName) throws Exception{

        File file = new File("src/test/resources/" + fileName);
        InputStream in = null;
        TestPolyline[] polylines;
        try{
            in = new FileInputStream(file);
            Scanner scanner = new Scanner(in);
            scanner.useLocale(Locale.US);

            polylines =  new TestPolyline[scanner.nextInt()];

            for(int i = 0; i < polylines.length; i++){
                polylines[i] = new TestPolyline();
                String name;
                while ((name = scanner.nextLine()).isEmpty()) {};
                polylines[i].points = new Point2D.Double[scanner.nextInt()];
                polylines[i].eps = scanner.nextDouble();
                polylines[i].k = scanner.nextInt();
                for(int j = 0; j < polylines[i].points.length; j++) {
                    polylines[i].points[j] = new Point2D.Double(scanner.nextDouble(),scanner.nextDouble());
                }
            }

            return polylines;

        } finally {
            if(in != null) in.close();
        }

    }

    public double maxDistance(Point2D[] polyline, Point2D[] simplified){

        int i = 0;
        double max_distanse = 0, distanse = 0;

        for(Point2D point : polyline){
            if(i+1 < simplified.length - 1 && point.distance(simplified[i+1]) < EPS) i++;
            distanse = distanseToSigment(point, simplified[i],simplified[i+1]);
            if(distanse > max_distanse) max_distanse = distanse;
        }

        return max_distanse;
    }

    public class TestPolyline {
        public Point2D[] points;
        public int k;
        public double eps;

    }
}