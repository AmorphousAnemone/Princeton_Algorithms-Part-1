import java.util.Arrays;
import java.util.LinkedList;

public class Board {
    private final int n;            // board dimension
    private final int[][] newBlocks;
    
    private final int zeroRow;        // zero's (empty space's) position
    private final int zeroCol;
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks

    {
        n = blocks.length;
        newBlocks = new int[n][n];
        int row = -1;
        int col = -1;
        
        // find zero's position and create new block array
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newBlocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }
        
        zeroRow = row;
        zeroCol = col;
        
    }
    
    public int dimension()                  // board dimension n
    {
        return n;
    }
    
    public int hamming()                    // number of blocks out of place
    {
        int hamDist = 0;                 // out of order counter will increment for every...
        int counter = 0;                    // ...number out of place
        
        // count numbers out of position, except the last position
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) break; // break if this the last "space"
                counter++;
                if (newBlocks[i][j] == 0) continue;
                if (newBlocks[i][j] != counter) hamDist++;
            }
        }
        
        // check last position
        if (newBlocks[n - 1][n - 1] != 0) hamDist++;
        
        return hamDist;
    }
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int manDist = 0;
        // Board board = new Board(newBlocks());

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (newBlocks[i][j] == 0) continue;

                // gives correct position of k
                int k = newBlocks[i][j];

                int row = (k - 1) / n;
                int col = (k - 1) % n;
                
                // absolute value of current position minus correct
                // in both row and column gives total Manhattan distance
                manDist += Math.abs(row - i) + Math.abs(col - j);
            }
        }
        
        return manDist;
    }
    
    public boolean isGoal()                         // is this board the goal board?
    {
        Board board = new Board(newBlocks());
        
        if (board.newBlocks[n - 1][n - 1] != 0) return false;    // final integer needs to be 0 (empty space);
        
        // compare newBlocks with counter
        int counter = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board.newBlocks[i][j] != counter) return false;
                counter++;
                if (counter == n*n - 1) break; // change back to n*n?
            }
            if (counter == n*n - 1) break; // change back to n*n?
        }
        
        return true;
    }
    
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int twinRow = -1;
        int twinCol = -1;
        
        // instantiate a twin board
        Board twinBoard = new Board(newBlocks());
        
        // get new positions for numbers; guaranteed not zero
        if (zeroRow < n - 1) {
            twinRow = zeroRow + 1;
        } else {
            twinRow = zeroRow - 1;
        }
        if (zeroCol < n - 1) {
            twinCol = zeroCol + 1;
        } else {
            twinCol = zeroCol - 1;
        }
        
        // swap the two positions on the board
        int temp = twinBoard.newBlocks[twinRow][zeroCol];
        twinBoard.newBlocks[twinRow][zeroCol] = twinBoard.newBlocks[twinRow][twinCol];
        twinBoard.newBlocks[twinRow][twinCol] = temp;
        
        return twinBoard;
    }
    
    public boolean equals(Object y)        // does this board equal y?
    {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false; // compares data type

        Board that = (Board) y;

        // compares instance variables 
        // update if new instance variables are added
        return Arrays.deepEquals(newBlocks, that.newBlocks); // compares size and content
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        LinkedList<Board> neighbors = new LinkedList<Board>();
        int newRow, newCol = 0;
        
        // try 4 scenarios of switching 0 with its (up to) 4 neighbors
        // checks which neighbors can be swapped
        // swaps, then adds to linked list
        if (zeroRow < dimension() - 1) {  // bottom neighbor
            int[][] copy = newBlocks();
            newRow = zeroRow + 1;
            newCol = zeroCol;
            int temp = copy[newRow][newCol];
            copy[newRow][newCol] = copy[zeroRow][zeroCol];
            copy[zeroRow][zeroCol] = temp;
            neighbors.add(new Board(copy));
        }
        if (zeroRow > 0) {  // top neighbor
            int[][] copy = newBlocks();
            newRow = zeroRow - 1;
            newCol = zeroCol;
            int temp = copy[newRow][newCol];
            copy[newRow][newCol] = copy[zeroRow][zeroCol];
            copy[zeroRow][zeroCol] = temp;
            neighbors.add(new Board(copy));
        }
        if (zeroCol < dimension() - 1) {  // right neighbor
            int[][] copy = newBlocks();
            newRow = zeroRow;
            newCol = zeroCol + 1;
            int temp = copy[newRow][newCol];
            copy[newRow][newCol] = copy[zeroRow][zeroCol];
            copy[zeroRow][zeroCol] = temp;
            neighbors.add(new Board(copy));
        }
        if (zeroCol > 0) {  // left neighbor
            int[][] copy = newBlocks();
            newRow = zeroRow;
            newCol = zeroCol - 1;
            int temp = copy[newRow][newCol];
            copy[newRow][newCol] = copy[zeroRow][zeroCol];
            copy[zeroRow][zeroCol] = temp;
            neighbors.add(new Board(copy));
        }
        
        return neighbors;
    }

    private int[][] newBlocks() {    // deep copy
        int[][] arr = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = newBlocks[i][j];
            }
        }
        
        return arr;
    }
    
    
    public String toString()               // string representation of this board
    {
        StringBuilder s = new StringBuilder();
        int[] arr = new int[n*n];
        int index = 0;
        
        // create 1-D array of newBlocks
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[index] = newBlocks[i][j];
                index++;
            }
        }
        
        // create string
        s.append(n + "\n");

        // starts a new row when loop reaches n
        int i = 0;
        while (i < n * n) {
            s.append(" " + arr[i]);
            if ((double) (i + 1) % (double) n == 0) {
                s.append("\n");
            }
            i++;
        }
        
        return s.toString();
    }
  
    public static void main(String[] args) // unit tests (not graded)
    {
        int[][] blocks = new int[3][3];
        // int[][] blocks = new int[][] { {1, 0}, {2, 3} };
        
        /*
        blocks[0][0] = 1;
        blocks[0][1] = 2;
        blocks[0][2] = 3;
        blocks[1][0] = 4;
        blocks[1][1] = 5;
        blocks[1][2] = 6;
        blocks[2][0] = 7;
        blocks[2][1] = 8;
        blocks[2][2] = 0; // return true
        */
        // /*
        blocks[0][0] = 8;
        blocks[0][1] = 1;
        blocks[0][2] = 3;
        blocks[1][0] = 4;
        blocks[1][1] = 0;
        blocks[1][2] = 2;
        blocks[2][0] = 7;
        blocks[2][1] = 6;
        blocks[2][2] = 5; // return false
        // */
        
        
        
        Board board = new Board(blocks);
        
        // System.out.println("Hamming: " + board.hamming);
        // System.out.println("Manhattan: " + board.manhattan);

        // System.out.println("twin: \n" + board.twin().toString());
        
        // /*
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println(board.toString());
        for (Board b : board.neighbors()) {
            System.out.println("Manhattan: " + b.manhattan());
            System.out.println(b.toString());
        }
        // */
        
        /*
        Board board1;
        int counter = 0;
        
        for (Board b : board.neighbors()) {
            board1 = new Board(b.newBlocks);
            for (Board b1 : board1.neighbors()) {
                System.out.println(b1);
                counter++;
            }
        }
        System.out.println("Neighbors of neighbors: " + counter);
        // */
        // System.out.println(board.toString());
        // System.out.println("\n");
        // System.out.println(board.equals(board));
    }

}