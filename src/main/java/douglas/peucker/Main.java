package douglas.peucker;


import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        if(args.length < 1) return;

        File file = new File(args[0]);
        FileInputStream is = null;

        Point2D[] polyline= {}, simplified = {};
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

                String name;
                while ((name = scanner.nextLine()).isEmpty()) {};

                m = scanner.nextInt();
                eps = scanner.nextDouble();
                k = scanner.nextInt();

                polyline = new Point2D.Double[m];

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
                    polyline[j] = new Point2D.Double(x,y);
                }

                DPAlgorithm dpa = new DPAlgorithm(polyline);

                offestX = (max_x-min_x)*0.2;
                offestY = (max_y-min_y)*0.2;

                Draw draw = new Draw(name);
                draw.setCanvasSize(800, 800);

                draw.setXscale(min_x - offestX, max_x + offestX);
                draw.setYscale(min_y - offestY, max_y + offestY);

                draw.setPenRadius(0.002);
                draw.setPenColor(Color.blue);
                draw.setPenDash(new float[]{2.f, 5.f}, 0.f);

                draw.polyline(polyline);
                draw.filledCircles(polyline, 0.01);
                draw.setPenDash();

                //simplified = dpa.simple(eps);
//                simplified = dpa.iterative(eps);
//                draw.polyline(simplified);
                draw.setPenColor(Color.RED);
                //simplified = dpa.simple(k);
                simplified = dpa.robust(eps);
//                for(int l = 0; l < simplified.length; l++) {
//                    draw.text(simplified[l].getX(),simplified[l].getY()," "  + simplified[l].getX() +"_"  + simplified[l].getY());
//                }
                draw.polyline(simplified);
            }

        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if(is != null) try {
                is.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
