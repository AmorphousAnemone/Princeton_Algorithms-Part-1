import java.util.Comparator;
import java.util.Stack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class Solver {
    private final Stack<Board> shortestPath = new Stack<Board>();
    // private int steps = 0;
    private int minimumMoves = 0;
    private boolean solvable = true;
    
    // create private class searchNode with attributes
    
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        int steps = 0;
        if (initial == null) throw new IllegalArgumentException();
        Board twin = initial.twin();
        
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>(new NodeComparator()); // compares nodes by by the priority field
        MinPQ<SearchNode> twinPQ= new MinPQ<SearchNode>(new NodeComparator()); // include comparator in parameter
        
        // create initial search node
        SearchNode initialNode = new SearchNode(initial, steps, null);
        SearchNode twinNode = new SearchNode(twin, steps, null);
        
        // add node into minPQ
        minPQ.insert(initialNode);
        twinPQ.insert(twinNode);
        
        // initialize currentNode
        SearchNode currentNode = null;
        SearchNode currentNodeTwin = null;

        while (!minPQ.isEmpty()) {
            // if (steps > 40000) break;
            
            // delete (dequeue) node of minimum priority
            currentNode = minPQ.delMin();
            currentNodeTwin = twinPQ.delMin();
            
            // determine if dequeued node is Goal
            if (currentNode.board.isGoal()) break;
             
            if (currentNodeTwin.board.isGoal()) {
                solvable = false;
                break;
            }
            
           
            // steps++;  // increment steps before adding new set of neighbors to minPQ
            for (Board b : currentNode.board.neighbors()) {
                if (currentNode.parent != null && b.equals(currentNode.parent.board)) continue;  // equal to parent?
                minPQ.insert(new SearchNode(b, currentNode.steps + 1, currentNode));
            }
            
            /* twin */
            for (Board b : currentNodeTwin.board.neighbors()) {
                if (currentNodeTwin.parent != null && b.equals(currentNodeTwin.parent.board)) continue;  // equal to parent?
                twinPQ.insert(new SearchNode(b, currentNodeTwin.steps + 1, currentNodeTwin));
            }
            
        }
        
        Stack<Board> temp = new Stack<Board>();
        while (currentNode != null) {   // find shortest path
            
            temp.push(currentNode.board);
            currentNode = currentNode.parent;
            minimumMoves++;
        }
        
        /* stack again. Iterator for stacks has bug - Java */
        while (!temp.isEmpty()) {
           Board b = temp.pop();
           shortestPath.push(b);
        }
    }
    
    private class SearchNode {
        private Board board;
        private int manhattan;
        private int priority;
        private int steps;
        private SearchNode parent;
        
        public SearchNode(Board board, int steps, SearchNode parent) {
            this.board = board;
            this.manhattan = board.manhattan();
            this.steps = steps;
            this.priority = manhattan + steps;
            this.parent = parent;
        }
        
        /*
        public boolean addParent(SearchNode node) {
            if (node != null) {
                this.parent = node;
                return true;
            }
            return false;
        }
        */
    }
    
    private class NodeComparator implements Comparator<SearchNode>{
        public int compare(SearchNode sn1, SearchNode sn2) {
            if (sn1.priority > sn2.priority) return 1;
            if (sn1.priority < sn2.priority) return -1;
            return 0;
        }
    }
    
    public boolean isSolvable()            // is the initial board solvable?
    {
        return solvable;
    }
    
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        if (solvable == false) return -1;
        return minimumMoves - 1;    // removes initial board from count
    }
    
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        if (solvable == false) return null;
        return shortestPath;
    }
    
    
    public static void main(String[] args) // solve a slider puzzle (given below)
    {
         int[][] blocks = new int[][] { {1, 0, 2}, {7, 5, 4}, {8, 6, 3} }; // puzzle11
        // int[][] blocks = new int[][] { {2,  3,  4,  8}, {1,  6,  0, 12}, {5, 10,  7, 11}, {9, 13, 14, 15} }; // puzzle13
        // int[][] blocks = new int[][] { {2, 0}, {1, 3} }; // puzzle3
        // int[][] blocks = new int[][] { {1, 2, 3}, {4, 6, 5}, {7, 8, 0} }; // unsolvable

        
        Board board = new Board(blocks);
        Solver solver = new Solver(board);
        
        
        System.out.println("Moves: " + solver.moves());
        
        for (Board b : solver.shortestPath) {
            System.out.println(b.toString());
        }
        
         System.out.println(solver.isSolvable());
    }
    
}