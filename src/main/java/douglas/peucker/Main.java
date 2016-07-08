    package douglas.peucker;


import java.awt.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    enum AlgorithmType {
        SIMPLE,
        SIMPLEN,
        ITERATIVE,
        ROBUST
    }

    private static final int canvasSizeX = 800,
                             canvasSizeY = 800;

    private static final double offsetScale = 0.2;

    private static final double penRadius = 0.002;
    private static final double markerRadius = 0.01;

    public static final String usageString =
            "Usage: java -jar dpa ALGORITHM FILE\n" +
            "\t ALGORITHM  \t\t select the polyline simplification algorithm: \n" +
            "\t\t simple  \t original Douglas-Peucker algorithm\n" +
            "\t\t simpleN \t modification of Douglas-Peucker algorithm\n" +
            "\t\t iterative \t non-recursive variant of Douglas-Peucker algorithm\n" +
            "\t\t robust \t non-recursive robus variant of Douglas-Peucker algorithm\n" +
            "\t FILE  \t\t\t path to the input file \n" +
            "Format of input file: \n" +
            "\t N\n\t PolylineName1 \n\t n eps k\n" +
            "\t x1 y1\n\t ... \n\t xn yn\n\t ...\n"  +
            "\t PolylineNameN \n\t n eps k\n" +
            "\t x1 y1\n\t ... \n\t xn yn\n\t ...\n" +
            "\t\t N \t number of polylines\n" +
            "\t\t n \t number of points \n " +
            "\t\t eps \t tolerance \n " +
            "\t\t k \t number of points of a simplified polyline";

    public static void main(String[] args) {

        if(args.length < 2) {
            System.out.println(usageString);
            return;
        }

        String algorithm = args[0],
               filename  = args[1];

        AlgorithmType type;
        try {
            type = AlgorithmType.valueOf(algorithm.toUpperCase());
        } catch (IllegalArgumentException e){
            System.out.println("The "+ algorithm +" algorithm isn't available");
            return;
        }

        File file = new File(filename);
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

                offestX = (max_x-min_x)*offsetScale;
                offestY = (max_y-min_y)*offsetScale;

                Draw draw = new Draw(name);
                draw.setCanvasSize(canvasSizeX, canvasSizeY);

                draw.setXscale(min_x - offestX, max_x + offestX);
                draw.setYscale(min_y - offestY, max_y + offestY);

                draw.setPenRadius(penRadius);
                draw.setPenColor(Color.blue);
                draw.setPenDash(new float[]{2.f, 5.f}, 0.f);

                draw.polyline(polyline);
                draw.filledCircles(polyline, markerRadius);
                draw.setPenDash();

                draw.setPenColor(Color.RED);

                Long time = System.currentTimeMillis();
                switch (type){
                    case SIMPLE:
                        simplified = dpa.simple(eps); break;
                    case SIMPLEN:
                        simplified = dpa.simpleN(k); break;
                    case ITERATIVE:
                        simplified = dpa.iterative(eps); break;
                    case ROBUST:
                        simplified = dpa.robust(eps); break;
                }
                time = System.currentTimeMillis() - time;

                draw.polyline(simplified);

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
                draw.textLeft(lineXPos, lineYPos, " Number of point: " + eps);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Algortithm: " + algorithm);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Simplification:");
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, " Number of point: " + simplified.length);
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, String.format(" Max distance: %10.5f", dpa.maxDistance(simplified)));
                lineYPos -=  charSize;
                draw.textLeft(lineXPos, lineYPos, String.format(" Time: %d ms", time));

                System.out.println("Simplifed Polyline:" + name);
                for(Point2D p : simplified){
                    System.out.println(p.getX() + " " + p.getY());
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
}
