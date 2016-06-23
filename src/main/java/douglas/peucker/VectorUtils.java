package douglas.peucker;

import java.awt.geom.Point2D;
import static java.lang.Math.*;


public class VectorUtils {

    public static double distanseToSigment(Point2D p, Point2D start, Point2D end){
        Point2D r  = vectDif(end, start),
                r1 = vectDif(p, start),
                r2 = vectDif(p, end);

        double r_2  = vectSquare(r),
               r1_2 = vectSquare(r1),
               r2_2 = vectSquare(r2);

        return (r2_2 >= r_2 + r1_2)? Math.sqrt(r1_2) :
               (r1_2 >= r_2 + r2_2)? Math.sqrt(r2_2) :
                       Math.abs(vectProduct(r,r1)/sqrt(r_2));
    }

    public static Point2D vectDif(Point2D a, Point2D b){
        return new Point2D.Double(a.getX()-b.getX(), a.getY()-b.getY());
    }

    public static double vectSquare(Point2D a){
        return scalarProduct(a,a);
    }

    public static double vectLength(Point2D a){
        return sqrt(scalarProduct(a,a));
    }

    public static double scalarProduct(Point2D a, Point2D b){
        return a.getX()*b.getX() +  a.getY()*b.getY();
    }

    public static double vectProduct(Point2D a, Point2D b) {
        return a.getX()*b.getY() - a.getY()*b.getX();
    }
}
