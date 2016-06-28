package douglas.peucker;

import java.awt.geom.Point2D;
import java.util.*;

import static  douglas.peucker.VectorUtils.*;

public class DPAlgorithm  {

    private Point2D[] polyline;

    public DPAlgorithm(Point2D[] polyline){

        if(polyline == null && polyline.length > 2)
            throw new IllegalArgumentException("Polyline can't be null and have at lest two point");

        this.polyline = polyline;
    }

    public Point2D[] simple(double eps){

         return simpleRecursive(new Range(0,polyline.length-1),eps).toArray(new Point2D[0]);
    }

    protected List<Point2D> simpleRecursive(Range range, double eps){

        LinkedList<Point2D> result = new LinkedList<Point2D>();

        if(range.getMaxDistance() < eps){
            result.add(polyline[range.getStart()]);
            result.add(polyline[range.getEnd()]);
        } else {
            result.addAll(simpleRecursive(new Range(range.getStart(), range.getMaxPoint()), eps));
            result.pollLast();
            result.addAll(simpleRecursive(new Range(range.getMaxPoint(), range.getEnd()),      eps));
        }

        return result;
    }

    public Point2D[] simple(int k){

        Point2D[] result = new Point2D[k];

        PriorityQueue<Range> pq = new PriorityQueue<Range>(polyline.length);
        pq.add(new Range(0, polyline.length - 1));

        for(int i=2; i<k; i++){
            Range range = pq.poll();
            if(range.getEnd() ==  range.getStart()) continue;
            pq.add(new Range(range.getStart(), range.getMaxPoint()));
            pq.add(new Range(range.getMaxPoint(),range.getEnd()));
        }

        List<Range> rangeList = new ArrayList<Range>();
        rangeList.addAll(rangeList);
        Collections.sort(rangeList, new RangeStartComparator());

        int i=0;
        for(Range rang : rangeList){
            result[i++] = polyline[rang.getStart()];
        }
        result[i] = polyline[polyline.length-1];

        return result;
    }

    public List<Point2D> iterative(double eps){

        List<Point2D> result = new ArrayList<Point2D>();

        result.add(polyline[0]);

        int start = 0;

        for(int i = 1; i < polyline.length; i++ ){
            Range info = new Range(start, i);
            if(info.getMaxDistance() >  eps){
                start = i-1;
                result.add(polyline[i-1]);
            }
        }

        result.add(polyline[polyline.length - 1]);

        return  result;
    }

    public List<Point2D> robust(double eps){
        //TODO: Implement robust variant of the algorithm
        return null;
    }

    public final class Range implements Comparable<Range> {

        private final int start, end;

        private double max_distance = -1.0;
        private int    max_point = -1;

        public Range(int start, int end){

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

        private Range findMaxPoint(){
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

        public int compareTo(Range range) {
            return -Double.compare(this.getMaxDistance(), range.getMaxDistance());
        }

    }

    public static class RangeStartComparator implements Comparator<Range> {

        public int compare(Range range1, Range range2) {
            return 0;
        }
    }

}
