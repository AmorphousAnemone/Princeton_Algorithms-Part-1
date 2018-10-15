import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    
    private final TreeSet<Point2D> tree;
   
    public PointSET()                            // construct an empty set of points 
   {
        tree = new TreeSet<Point2D>();
   }
   
   public boolean isEmpty()                     // is the set empty? 
   {
       return tree.isEmpty();
   }
   
   public int size()                            // number of points in the set 
   {
       return tree.size();
   }
   
   public void insert(Point2D p)                // add the point to the set (if it is not already in the set)
   {
       if (p == null) throw new IllegalArgumentException();
       tree.add(p);
   }
   
   public boolean contains(Point2D p)           // does the set contain point p? 
   {
       if (p == null) throw new IllegalArgumentException();
       return tree.contains(p);
   }
   
   public void draw()                           // draw all points to standard draw 
   {
       // empty because optional
   }
   
   public Iterable<Point2D> range(RectHV rect)  // all points that are inside the rectangle (or on the boundary) 
   {
       if (rect == null) throw new IllegalArgumentException();
       Queue<Point2D> range = new LinkedList<Point2D>();

       for (Point2D point : tree) {
           if (rect.contains(point)) { 
               range.add(point);
           }
       }
       
       return range;
   }
   
   public Point2D nearest(Point2D p)            // a nearest neighbor in the set to point p; null if the set is empty 
   {
       if (p == null) throw new IllegalArgumentException();
       if (tree.isEmpty()) return null;
       
       Point2D nearPoint = p;   // Point nearest to p, initialize as p
       double dist = 1;         // Set initial distance to unit square's length
       
       for (Point2D fromTree : tree) {
           double tempDist = p.distanceSquaredTo(fromTree);
           if (tempDist <= dist) {
               dist = tempDist;
               nearPoint = fromTree;
           }
       }
       
       return nearPoint;
   }
   
   public static void main(String[] args)       // unit testing of the methods (optional) 
   {
        PointSET ps = new PointSET();
        
        ps.insert(new Point2D(0.206107, 0.095492));
        ps.insert(new Point2D(0.206107, 0.095492));
        ps.insert(new Point2D(0.975528, 0.654508));
        ps.insert(new Point2D(0.024472, 0.345492));
        ps.insert(new Point2D(0.793893, 0.095492));
        ps.insert(new Point2D(0.793893, 0.904508));
        ps.insert(new Point2D(0.975528, 0.345492));
        ps.insert(new Point2D(0.206107, 0.904508));
        ps.insert(new Point2D(0.500000, 0.000000));
        ps.insert(new Point2D(0.024472, 0.654508));
        ps.insert(new Point2D(0.500000, 1.000000));
        
        
        for (Point2D p : ps.range(new RectHV(-0.5, -0.5, 0.5, 0.5))) {
            System.out.println("x: " + p.x() + ", y: " + p.y());
        }
        System.out.println("\n");
        System.out.println("x: " + ps.nearest(new Point2D(0.5, 0.5)).x() + ", y: " + ps.nearest(new Point2D(0.5, 0.5)).y());
   }
   
}