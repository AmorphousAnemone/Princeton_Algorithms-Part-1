import java.util.ArrayList;
import java.util.Arrays;


public class BruteCollinearPoints {
   private int counter;
   private ArrayList<LineSegment> lineSegment = new ArrayList<LineSegment>();
   private Point[] pointArray = new Point[4];
   
   public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
   {
        if (points == null) throw new java.lang.IllegalArgumentException();
        for (Point p : points)
        {
            if (p == null) throw new java.lang.IllegalArgumentException();
        }
        for (int i = 0; i < points.length - 1; i++)
        {
            for (int j = i + 1; j < points.length; j++)
            {
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException();
            }

        }
        
        for (int i = 0; i < points.length - 3; i++) 
        {
            Point p = points[i];
            pointArray[0] = p;
            for (int j = i + 1; j < points.length - 2; j++)
            {
                Point q = points[j];
                if (p.compareTo(q) == 0) throw new java.lang.IllegalArgumentException();
                pointArray[1] = q;
                for (int k = j + 1; k < points.length - 1; k++)
                {
                    Point r = points[k];
                    if (r.compareTo(p) == 0 || r.compareTo(q) == 0) throw new java.lang.IllegalArgumentException();
                    if (p.slopeOrder().compare(q, r) != 0) continue;
                    pointArray[2] = r;
                    
                    for (int m = k + 1; m < points.length; m++)
                    {
                        Point s = points[m];
                        if (r.compareTo(s) == 0) throw new java.lang.IllegalArgumentException();
                        if (q.slopeOrder().compare(p, s) != 0) continue;
                        pointArray[3] = s;
                        Arrays.sort(pointArray);
                        lineSegment.add(new LineSegment(pointArray[0], pointArray[3]));
                        counter++;
                    }
                   
                } 
               
            }

        }
        // iterate through all points comparing them to every other point
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
       Point[] point = new Point[8];
       Point p = new Point(10000,      0);
       Point q = new Point(    0,  10000);
       Point r = new Point(3000,   7000);
       Point s = new Point( 7000,   3000);
       Point t = new Point(20000,  21000);
       Point u = new Point( 3000,   4000);
       Point v = new Point(14000,  15000);
       Point w = new Point( 6000,   7000);
       point[0] = p;
       point[1] = q;
       point[2] = r;
       point[3] = s;
       point[4] = t;
       point[5] = u;
       point[6] = v;
       point[7] = w;
       BruteCollinearPoints brute = new BruteCollinearPoints(point);
       }
}