import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int counter;
    private ArrayList<LineSegment> lineSegment = new ArrayList<LineSegment>();
    
    public FastCollinearPoints(Point[] points) // finds all line segments containing 4 or more points
    {
        if (points == null) throw new java.lang.IllegalArgumentException();
        for (Point p : points) // check for null points
        {
            if (p == null) throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < points.length - 1; i++) // check for duplicates
        {
            for (int j = i + 1; j < points.length; j++)
            {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException();
            }
        }
        
        
        for (int i = 0; i < points.length - 1; i++)
        {
            Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());
            
            int collinearPointsCount = 0;
            double currentSlope = points[i].slopeTo(points[i + 1]);
            Point[] pointArr = new Point[4]; // hold potential collinear points
            pointArr[collinearPointsCount++] = points[i];
            pointArr[collinearPointsCount++] = points[i + 1];
            
            for (int j = i + 2; j < points.length; j++)
            {
                
                if (points[i].slopeTo(points[j]) == currentSlope)
                {
                    pointArr[collinearPointsCount++] = points[j];
                }
                else
                {
                    collinearPointsCount = 0;
                    pointArr = new Point[4];
                    pointArr[collinearPointsCount++] = points[i];
                    pointArr[collinearPointsCount++] = points[j];
                    
                    currentSlope = points[i].slopeTo(points[j]);
                }
                
                if (collinearPointsCount == 4)
                {
                    Arrays.sort(pointArr);
                    // if both forward and reverse is not in arraylist... add
                    if (!lineSegment.contains(new LineSegment(pointArr[0], pointArr[3])))
                    {
                        lineSegment.add(new LineSegment(pointArr[0], pointArr[3]));
                        counter++;
                    }
                    
                    pointArr = new Point[4];
                    collinearPointsCount = 0;
                }
            }
        }
    }
    
    public int numberOfSegments() // the number of line segments
    {
        return counter;
    }
    
    public LineSegment[] segments() // the line segments
    {
        int size = lineSegment.size();
        LineSegment[] newArray = new LineSegment[size];
        for (int i = 0; i < size; i++)
        {
            newArray[i] = lineSegment.get(i);
        }
        return newArray;
    }
    
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In("input10.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        /* for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show(); */
    }
}