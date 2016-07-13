package douglas.peucker;

import java.awt.geom.Point2D;
import java.util.*;

import static  douglas.peucker.VectorUtils.*;

public class DPAlgorithm  {

    private static final double EPS = 1e-15;

    private Point2D[] polyline;

    public DPAlgorithm(Point2D[] polyline){

        if(polyline == null && polyline.length > 2)
            throw new IllegalArgumentException("Polyline can't be null and have at lest two point");

        this.polyline = polyline;
    }


    public Point2D[] simple(double eps){

         return simpleRecursive(new BaseSegment(0,polyline.length-1),eps).toArray(new Point2D[0]);
    }

    protected List<Point2D> simpleRecursive(BaseSegment segment, double eps){

        LinkedList<Point2D> result = new LinkedList<Point2D>();

        if(segment.getMaxDistance() < eps){
            result.add(segment.startPoint());
            result.add(segment.endPoint());
        } else {
            result.addAll(simpleRecursive(new BaseSegment(segment.getStart(), segment.getMaxPoint()), eps));
            result.pollLast();
            result.addAll(simpleRecursive(new BaseSegment(segment.getMaxPoint(), segment.getEnd()),      eps));
        }

        return result;
    }

    public Point2D[] simpleN(int k){

        Point2D[] result = new Point2D[k];

        PriorityQueue<BaseSegment> pq = new PriorityQueue<BaseSegment>(polyline.length);
        pq.add(new BaseSegment(0, polyline.length - 1));

        for(int i=0; i < k - 2; i++){
            BaseSegment segment = pq.poll();

            if(segment.getStart() == segment.getEnd()) continue;

            pq.add(new BaseSegment(segment.getStart(), segment.getMaxPoint()));
            pq.add(new BaseSegment(segment.getMaxPoint(), segment.getEnd()));
        }

        List<BaseSegment> list = new ArrayList<BaseSegment>();
        list.addAll(pq);
        Collections.sort(list, new RangeStartComparator());

        int i=0;
        for(BaseSegment rang : list){
            result[i++] = rang.startPoint();
        }
        result[i] = polyline[polyline.length-1];

        return result;
    }

    public Point2D[] iterative(double eps){

        List<Point2D> result = new ArrayList<Point2D>();

        result.add(polyline[0]);

        int start = 0;

        for(int i = 1; i < polyline.length; i++ ){
            BaseSegment info = new BaseSegment(start, i);
            if(info.getMaxDistance() >  eps){
                start = --i;
                result.add(polyline[i]);
            }
        }

        result.add(polyline[polyline.length - 1]);

        return  result.toArray(new Point2D[0]);
    }

    public Point2D[] robust(double eps){
        List<Point2D> result = new ArrayList<Point2D>();

        result.add(polyline[0]);

        int start = 0;
        for(int i = 1; i < polyline.length; i++ ){
            BaseSegment info = new BaseSegment(start, i);

            if(info.getMaxDistance() >  eps ) {
                do { i--; }
                while (hasIntersection(start, i, result) && i>= start + 1);

                start = i;
                result.add(polyline[i]);

            } else if( i == polyline.length - 1) {
                while(hasIntersection(start, i, result) && i>= start + 1)
                i--;

                start = i;
                result.add(polyline[i]);
            }

        }

        return  result.toArray(new Point2D[0]);
    }

    public double maxDistance(Point2D[] simplified){

        int i = 0;
        double max_distanse = 0, distanse = 0;

        for(Point2D point : polyline){
            if(i+1 < simplified.length - 1 && point.distance(simplified[i+1]) < EPS) i++;
            distanse = distanseToSigment(point, simplified[i],simplified[i+1]);
            if(distanse > max_distanse) max_distanse = distanse;
        }

        return max_distanse;
    }

    private boolean hasIntersection(int start, int cur_pos, List<Point2D> simplified){
        for (int j = 1; j < simplified.size() - 1; j++) {
            if (intersect(simplified.get(j - 1), simplified.get(j), polyline[start], polyline[cur_pos])) {
                return true;
            }
        }
        for (int j = cur_pos + 2; j < polyline.length; j++) {
            if (intersect(polyline[j - 1], polyline[j], polyline[start], polyline[cur_pos])) {
                return true;
            }
        }
        return false;
    }

    public final class BaseSegment implements Comparable<BaseSegment> {

        private final int start, end;

        private double max_distance = -1.0;
        private int    max_point = -1;

        public BaseSegment(int start, int end){

            if(0 > start || start > end || end >= polyline.length )
                throw new IllegalArgumentException("Wrong position of part start");

            this.start = start; this.end = end;
        }

        public int getStart() {return  start;}

        public int getEnd() { return  end;}

        public Point2D startPoint() {return  polyline[start];}

        public Point2D endPoint() {return  polyline[end];}

        public int getMaxPoint(){
            return (max_point == -1)? findMaxPoint().max_point : max_point;
        }

        public double getMaxDistance(){
            return (max_point == -1)? findMaxPoint().max_distance : max_distance;
        }

        private BaseSegment findMaxPoint(){
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

        public int compareTo(BaseSegment segment) {
            return -Double.compare(this.getMaxDistance(), segment.getMaxDistance());
        }

    }

    public static class RangeStartComparator implements Comparator<BaseSegment> {

        public int compare(BaseSegment baseSegment1, BaseSegment baseSegment2) {
            return Integer.compare(baseSegment1.start, baseSegment2.start);
        }
    }

}
