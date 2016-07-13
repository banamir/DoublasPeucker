package douglas.peucker;

import java.awt.geom.Point2D;
import static java.lang.Math.*;


public final class VectorUtils {

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

    public static boolean intersect(Point2D a, Point2D b, Point2D c, Point2D d){
        Point2D ab = vectDif(a,b), ac = vectDif(a,c), ad = vectDif(a,d);
        Point2D cd = vectDif(c, d), ca = vectDif(c, a), cb = vectDif(c, b);
        return intersect(a.getX(),b.getX(),c.getX(),d.getX()) &&
                vectProduct(ab,ac) *  vectProduct(ab,ad) <=0  &&
                vectProduct(cd,ca) *  vectProduct(cd,cb) <=0;
    }

    public static boolean intersect(double a, double b, double c, double d) {
        double p;
        if(a > b) { p = a; a = b; b = p; }
        if(c > d) { p = c; c = d; d = p;  }
        return max(a,c) <= min(b,d);
    }

    public static Point2D intersectPoint(Point2D a, Point2D b, Point2D c, Point2D d) {
        if(!intersect(a,b,c,d)) return  null;

        if(Double.compare(a.getX(),b.getX()) == 0 && Double.compare(a.getY(),b.getY()) == 0 )
            return a;
        if(Double.compare(c.getX(),d.getX()) == 0 && Double.compare(c.getY(),d.getY()) == 0 )
            return c;

        double A1 = b.getY() - a.getY(), A2 = d.getY() - c.getY(),
               B1 = a.getX() - b.getX(), B2 = c.getX() - d.getX(),
               C1 = A1 * a.getX() + B1 * a.getY(),
               C2 = A2 * c.getX() + B2 * c.getY(),
               D  = A1 * B2 - B1 * A2,
               D1 = C1 * B2 - B1 * C2,
               D2 = A1 * C2 - C1 * A2;
        if(Double.compare(D,0.) == 0) return null;

        return new Point2D.Double(D1/D,D2/D);
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
