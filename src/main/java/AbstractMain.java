import douglas.peucker.Draw;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import java.util.List;

import static douglas.peucker.VectorUtils.distanseToSegment;

public abstract class AbstractMain {

    public class Point{
        double x,y;
    }

    private static final double EPS = 1e-15;

    private static final int canvasSizeX = 800,
            canvasSizeY = 800;

    private static final double offsetScale = 0.2;

    private static final double penRadius = 0.002;
    private static final double markerRadius = 0.01;


    private String name = null;

    private Draw draw = null;


    public void startProblem(String filename){

        File file = new File(filename);
        FileInputStream is = null;

        Point[] polyline= {}, simplified = {};
        int n = 0, m = 0, k = 0;

        double eps;
        double max_x, min_x, max_y, min_y;
        double offestX, offestY;

        try{

            is = new FileInputStream(file);
            Scanner scanner = new Scanner(is);
            scanner.useLocale(Locale.US);

            n = scanner.nextInt();

            for(int i = 0; i < n; i++){

                while ((name = scanner.nextLine()).isEmpty()) {};
                draw = new Draw(name);

                m = scanner.nextInt();
                eps = scanner.nextDouble();
                k = scanner.nextInt();

                polyline = new Point[m];

                double x,y;
                max_x = Double.MIN_VALUE; min_x = Double.MAX_VALUE;
                max_y = Double.MIN_VALUE; min_y = Double.MAX_VALUE;
                for(int j = 0; j < m; j++){
                    x = scanner.nextDouble();
                    y = scanner.nextDouble();
                    if(x > max_x) max_x = x;
                    if(x < min_x) min_x = x;
                    if(y > max_y) max_y = y;
                    if(y < min_y) min_y = y;
                    polyline[j] = new Point();
                    polyline[j].x = x;
                    polyline[j].y = y;
                }


                offestX = (max_x-min_x)*offsetScale;
                offestY = (max_y-min_y)*offsetScale;

                draw.setCanvasSize(canvasSizeX, canvasSizeY);

                draw.setXscale(min_x - offestX, max_x + offestX);
                draw.setYscale(min_y - offestY, max_y + offestY);

                draw.setPenRadius(penRadius);
                draw.setPenColor(Color.blue);
                draw.setPenDash(new float[]{2.f, 5.f}, 0.f);

                polyline(polyline);
                filledCircles(polyline, markerRadius);
                draw.setPenDash();

                draw.setPenColor(Color.RED);

                Long time = System.currentTimeMillis();
                simplified = this.solve(polyline, eps);
                time = System.currentTimeMillis() - time;

                this.polyline(simplified);

                draw.setPenColor(Color.BLACK);
                double charSize = 4. * (max_y - min_y  + 2 * offestY) * draw.getFontMetric().getDescent()/canvasSizeY;
                double lineXPos = min_x - offestX;
                double lineYPos = max_y + offestY -  charSize;
                draw.textLeft(lineXPos, lineYPos, " Polyline name: " + name);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Number of point: " + m);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Tolerance: " + eps);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Simplification:");
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Number of point: " + simplified.length);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, String.format(" Max distance: %10.5f", maxDistance(polyline, simplified)));
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, String.format(" Time: %d ms", time));

                System.out.println("Simplifed Polyline:" + name);
                for(Point p : simplified){
                    System.out.println(p.x + " " + p.y);
                }
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            if(is != null) try {
                is.close();
            } catch (IOException e){
                System.out.println(e.getMessage());
            }
        }

    }

    abstract public Point[] solve(Point[] polyline, double eps);


    public void polyline(Point points[]) {
        for(int i = 0; i<points.length - 1; i++){
            draw.line(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y);
        }
    }

    public void filledCircles(Point[] points, double r){
        for(int i = 0; i < points.length; i++) {
            draw.filledCircle(points[i].x, points[i].y, r);
        }

    }

    public double maxDistance(Point[] polyline, Point[] simplified){

        int i = 0;
        double max_distanse = 0, distanse = 0;

        for(Point point : polyline){
            Point2D.Double p = new Point2D.Double(point.x,point.y);
            Point2D.Double A = new Point2D.Double(simplified[i].x,simplified[i].y);
            Point2D.Double B = new Point2D.Double(simplified[i+1].x,simplified[i+1].y);

            if(i+1 < simplified.length - 1 && p.distance(B) < EPS) i++;

            distanse = distanseToSegment(p, A, B);
            if(distanse > max_distanse) max_distanse = distanse;
        }

        return max_distanse;
    }


    public static void main(String[] args){

        if(args.length == 0) return;

        Map<String,String> env =  System.getenv();

        Thread cur = Thread.currentThread();
        String name = cur.getName();

        try{
            String  clsName =Thread.currentThread().getStackTrace()[1].getClassName();

            Class<AbstractMain> cls = (Class<AbstractMain>) Class.forName(clsName);

             ((AbstractMain) cls.getConstructor().newInstance()).startProblem(args[0]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }





}
