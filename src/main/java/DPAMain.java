import douglas.peucker.DPAlgorithm;

import java.awt.geom.Point2D;

/**
 * Created by banamir on 07.11.16.
 */
public class DPAMain extends AbstractMain {


    public Point[] solve(Point[] polyline,double eps) {

        DPAlgorithm dpa = new DPAlgorithm(toPoint2D(polyline));

        return fromPoint2D(dpa.robust(eps));
    }

    Point2D[] toPoint2D(Point[] polyline){
        Point2D[] points =  new Point2D.Double[polyline.length];

        for(int i = 0; i < polyline.length; i++){
            points[i] = new Point2D.Double(polyline[i].x,polyline[i].y);
        }
        return points;
    }

    Point[] fromPoint2D(Point2D[] polyline){
        Point[] points =  new Point[polyline.length];

        for(int i = 0; i < polyline.length; i++){
            points[i] = new Point();
            points[i].x = polyline[i].getX() ;
            points[i].y = polyline[i].getY();
        }
        return points;
    }

    public static void main(String[] args){

        if(args.length == 0) return;

        new DPAMain().startProblem(args[0]);
    }


}
