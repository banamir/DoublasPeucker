package douglas.peucker;

import java.awt.geom.Point2D;
import java.util.*;

public class DPAlgorithm extends VectorUtils {

    private Point2D[] polyline;

    public DPAlgorithm(Point2D[] polyline){
        if(polyline == null && polyline.length > 2)
            throw new IllegalArgumentException("Polyline can't be null and have at lest two point");
        this.polyline = polyline;
    }

    public final class PolylinePartInfo{

        private final int start, end;

        private double max_distance = -1.0;
        private int    max_point = -1;

        public PolylinePartInfo(int start, int end){

            if(0 > start || start > end || end >= polyline.length )
                throw new IllegalArgumentException("Wrong position of part start");

            this.start = start; this.end = end;
        }

        int getStart() {return  start;}

        int getEnd() { return  end;}

        int getMaxPoint(){
            return (max_point == -1)? findMaxPoint().max_point : max_point;
        }

        double getMaxDistance(){
            return (max_point == -1)? findMaxPoint().max_distance : max_distance;
        }

        private PolylinePartInfo findMaxPoint(){
            double disatance = 0;

            max_distance = 0;
            max_point = start;

            for(int i=start; i<end; i++){
                if((disatance = distanseToSigment(polyline[i],polyline[start],polyline[end])) >= max_distance) {
                    max_point = i;
                    max_distance = disatance;
                }
            }
            return this;
        }

    }


    public List<Point2D> simple(double eps){

        return simpleRecursive(new PolylinePartInfo(0,polyline.length-1),eps);
    }

    public List<Point2D> simpleRecursive(PolylinePartInfo info, double eps){

        LinkedList<Point2D> result = new LinkedList<Point2D>();

        if(info.getMaxDistance() < eps){
            result.add(polyline[info.getStart()]);
            result.add(polyline[info.getEnd()]);
        } else {
            result.addAll(simpleRecursive(new PolylinePartInfo(info.getStart(),    info.getMaxPoint()), eps));
            result.pollLast();
            result.addAll(simpleRecursive(new PolylinePartInfo(info.getMaxPoint(), info.getEnd()),      eps));
        }

        return result;

    }

    public List<Point2D> simple(int k){

        List<Point2D> result = new ArrayList<Point2D>();

        PriorityQueue<PolylinePartInfo> pq =
                new PriorityQueue<PolylinePartInfo>(polyline.length,
                        new Comparator<PolylinePartInfo>() {
                            public int compare(PolylinePartInfo info1, PolylinePartInfo info2) {
                                return -Double.compare(info1.getMaxDistance(), info2.getMaxDistance());
                            }
                        });
        pq.add(new PolylinePartInfo(0, polyline.length - 1));

        for(int i=2; i<k; i++){
            PolylinePartInfo info = pq.poll();
            if(info.getEnd() ==  info.getStart()) continue;
            pq.add(new PolylinePartInfo(info.getStart(),info.getMaxPoint()));
            pq.add(new PolylinePartInfo(info.getMaxPoint(),info.getEnd()));
        }

        PolylinePartInfo[] infoArray = {};
        infoArray = pq.toArray(infoArray);
        Arrays.sort(infoArray,
                new Comparator<PolylinePartInfo>() {
                    public int compare(PolylinePartInfo info1, PolylinePartInfo info2) {
                        return Integer.compare(info1.getStart(), info2.getStart());
                    }
                });

        for(PolylinePartInfo info :infoArray){
            result.add(polyline[info.getStart()]);
        }
        result.add(polyline[polyline.length-1]);

        return result;
    }

    public List<Point2D> iterative(double eps){

        List<Point2D> result = new ArrayList<Point2D>();

        result.add(polyline[0]);

        int start = 0;

        for(int i = 1; i < polyline.length; i++ ){
            PolylinePartInfo info = new PolylinePartInfo(start, i);
            if(info.getMaxDistance() >  eps){
                start = i-1;
                result.add(polyline[i-1]);
            }
        }

        result.add(polyline[polyline.length - 1]);

        return  result;
    }

    public List<Point2D> robust(double eps){
        //TODO: Implement robust variant of this method
        return null;
    }

    public double maxDistance(Point2D[] points){

        int i = 0;
        double max_distanse = 0, distanse = 0;

        for(Point2D point :this.polyline){

            if(i+1 < points.length && Double.compare(point.getX(), points[i+1].getX()) == 0
                                   && Double.compare(point.getY(), points[i+1].getY()) == 0)
            i++;
            distanse = distanseToSigment(point, points[i],points[i+1]);
            if(distanse > max_distanse) max_distanse = distanse;
        }

        return max_distanse;
    }



}
