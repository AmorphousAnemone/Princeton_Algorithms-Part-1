import java.util.LinkedList;
import java.util.Queue;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private Queue<Point2D> range = new LinkedList<Point2D>();
    private Node root = null;       // root of KD tree
    private boolean contains = false;
    // private Node subTreeRoot = null;
    private double minDist;
    private Point2D nearest;
    private int counter;
    
    private static class Node 
    {
        private Point2D p;          // the point
        private double x;
        private double y;
        private RectHV rect;        // the axis-aligned rectangle corresponding to this node
        private Node lb = null;     // the left/bottom subtree
        private Node rt = null;     // the right/top subtree
        private boolean vert;       // orientation
        private Node parent;
        
        public Node(Point2D point, Node parent) {
            this.p = point;
            this.vert = true;
            this.x = point.x();
            this.y = point.y();
            this.parent = parent;
            this.rect = getRect(p, parent);
        }
        
        private static RectHV getRect(Point2D point, Node parent) {
            
            // if root node
            if (parent == null) return new RectHV(0, 0, 1, 1);
            
            double xmin = 0, ymin = 0, xmax = 0, ymax = 0;
            
            // current node is horizontal and right/top
            if (parent.vert && point.x() >= parent.x) {    
                xmin = parent.x;
                ymin = parent.rect.ymin();
                xmax = parent.rect.xmax();
                ymax = parent.rect.ymax();
            }
            
            // current node is horizontal and left/bottom
            else if (parent.vert && point.x() <  parent.x) {
                xmin = parent.rect.xmin();
                ymin = parent.rect.ymin();
                xmax = parent.x;
                ymax = parent.rect.ymax();
            }
            
            // current node is vertical and right/top
            else if (!parent.vert && point.y() >= parent.y) {
                xmin = parent.rect.xmin();
                ymin = parent.y;
                xmax = parent.rect.xmax();
                ymax = parent.rect.ymax();
            }
            
            // current node is vertical and left/bottom
            else if (!parent.vert && point.y() <  parent.y) {
                xmin = parent.rect.xmin();
                ymin = parent.rect.ymin();
                xmax = parent.rect.xmax();
                ymax = parent.y;
            }
            
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            
            return rect;
        }
    }
    
    public KdTree()                            // construct an empty set of points 
    {
        // empty constructor
    }
   
    public boolean isEmpty()                     // is the set empty? 
    {
        return root == null;
    }
    
    public int size()                            // number of points in the set 
    {
        if (root == null) return 0;
        return counter;
    }
    
    public void insert(Point2D p)                // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        
        root = insert(root, p, null);
        counter++;
    }
    
    private Node insert(Node currentNode, Point2D p, Node parent)
    {
        if (currentNode == null) return new Node(p, parent);
        if (currentNode.p.equals(p)) {
            counter--;
            return currentNode;
        }
        
        int cmp = 0;
        if (currentNode.vert) {
            if (currentNode.p.x() <= p.x()) cmp =  1;
            if (currentNode.p.x() >  p.x()) cmp = -1;
        } else {
            if (currentNode.p.y() <= p.y()) cmp =  1;
            if (currentNode.p.y() >  p.y()) cmp = -1;
        }

        if (cmp < 0) {
            currentNode.lb = insert(currentNode.lb, p, currentNode);
            currentNode.lb.vert = !currentNode.vert;
        }
        
        if (cmp > 0) {
            currentNode.rt = insert(currentNode.rt, p, currentNode);
            currentNode.rt.vert = !currentNode.vert;
        }
        
        return currentNode;
    }
    
    public boolean contains(Point2D p)           // does the set contain point p? 
    {
        if (p == null) throw new IllegalArgumentException();
        
        contains = false;
        root = contains(root, p);
        return contains;
    }
    
    private Node contains(Node currentNode, Point2D p) 
    {
        if (currentNode == null) return currentNode;
        if (currentNode.p.equals(p)) {
            contains = true;
            return currentNode;                 // found. set instance variable to true
        }
        
        int cmp = 0;
        if (currentNode.vert) {
            if (currentNode.p.x() <= p.x()) cmp =  1;
            if (currentNode.p.x() >  p.x()) cmp = -1;
        } else {
            if (currentNode.p.y() <= p.y()) cmp =  1;
            if (currentNode.p.y() >  p.y()) cmp = -1;
        }
        
        if (cmp < 0) currentNode.lb = contains(currentNode.lb, p);
        if (cmp > 0) currentNode.rt = contains(currentNode.rt, p);

        return currentNode;
        
    }
    
    public void draw()                           // draw all points to standard draw 
    {
        // optional
    }
    
    public Iterable<Point2D> range(RectHV rect)  // all points that are inside the rectangle (or on the boundary) 
    {
        if (rect == null) throw new IllegalArgumentException();
        
        root = rangeSolver(root, rect);
        return range;
    }
    
    private Node rangeSolver(Node currentNode, RectHV rect)  // all points that are inside the rectangle (or on the boundary) 
    {
        if (rect == null) throw new IllegalArgumentException();
        
        if (currentNode == null) return currentNode;
        if (currentNode.rect.intersects(rect)) {
            if (rect.contains(currentNode.p)) range.add(currentNode.p);
            
            currentNode.lb = rangeSolver(currentNode.lb, rect);
            currentNode.rt = rangeSolver(currentNode.rt, rect);
        }
        
        return currentNode;
    }
    
    
    public Point2D nearest(Point2D p)            // a nearest neighbor in the set to point p; null if the set is empty 
    {
        if (p == null) throw new IllegalArgumentException();
        
        // reset instance variables
        // nearest = null;
        // minDist = root.p.distanceSquaredTo(p);
        minDist = 4;

        root = findNearest(root, p);

        return nearest;
    }
    
    private Node findNearest(Node currentNode, Point2D p) 
    {
        if (p == null) throw new IllegalArgumentException();
        if (currentNode == null) return currentNode;
        
        if (currentNode != root && currentNode.p.distanceSquaredTo(p) < minDist) { // || currentNode == root) {
            minDist = currentNode.p.distanceSquaredTo(p);
            nearest = currentNode.p;
        } 
        
        if (currentNode.lb != null) { 
            if (currentNode.lb.rect.contains(p)) {
                currentNode.lb = findNearest(currentNode.lb, p);
            } else {
                Point2D orientationPoint = orientationPoint(currentNode, p);
                if (orientationPoint.distanceSquaredTo(p) < minDist) {
                    currentNode.rt = findNearest(currentNode.rt, p);
                }
            }
        }
            
        if (currentNode.rt != null) {
            if (currentNode.rt.rect.contains(p)) {
                currentNode.rt = findNearest(currentNode.rt, p);
            } else {
                Point2D orientationPoint = orientationPoint(currentNode, p);
                if (orientationPoint.distanceSquaredTo(p) < minDist) {
                    currentNode.rt = findNearest(currentNode.rt, p);
                }
            }
        }

        return currentNode;
    }
    
    private Point2D orientationPoint(Node currentNode, Point2D p)
    {
        Point2D orientationPoint;
        
        if (currentNode.vert) {
            orientationPoint = new Point2D(currentNode.x, p.y());
        } else {
            orientationPoint = new Point2D(p.x(), currentNode.y);
        }
        
        return orientationPoint;
    }
    
    public static void main(String[] args)       // unit testing of the methods (optional) 
    {
        KdTree kdtree = new KdTree();
        Point2D pTest = new Point2D(0.955, 0.44);
        // Point2D pTest2 = new Point2D(0.616, 0.07);
        // Point2D pTest3 = new Point2D(0.495, 0.205);

        
        /*
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);
        */ 
        
        
        // /*
        Point2D p1 = new Point2D(0.372, 0.497);
        Point2D p2 = new Point2D(0.564, 0.413);
        Point2D p3 = new Point2D(0.226, 0.577);
        Point2D p4 = new Point2D(0.144, 0.179);
        Point2D p5 = new Point2D(0.083, 0.510);
        Point2D p6 = new Point2D(0.320, 0.708);
        Point2D p7 = new Point2D(0.417, 0.362);
        Point2D p8 = new Point2D(0.862, 0.825);
        Point2D p9 = new Point2D(0.785, 0.725);
        Point2D p10 = new Point2D(0.499, 0.208);
        // */
        
        
        kdtree.insert(p1);
        kdtree.insert(p2);
        kdtree.insert(p3);
        kdtree.insert(p4);
        kdtree.insert(p5);
        
        // /*
        kdtree.insert(p6);
        kdtree.insert(p7);
        kdtree.insert(p8);
        kdtree.insert(p9);
        kdtree.insert(p10);
        // */
        
        // RectHV rect = new RectHV(0.01, 0.16, 0.51, 0.24);
        
        System.out.println(kdtree.nearest(pTest));
        System.out.println(kdtree.nearest(pTest));
        System.out.println(kdtree.nearest(pTest));
        System.out.println(kdtree.nearest(pTest));
        /*
        System.out.println(kdtree.contains(pTest2));
        System.out.println(kdtree.contains(p9));
        System.out.println(kdtree.contains(p7));
        System.out.println(kdtree.contains(p2));
        System.out.println(kdtree.contains(p1));
        System.out.println(kdtree.contains(pTest3));
        */
         
    }
} 