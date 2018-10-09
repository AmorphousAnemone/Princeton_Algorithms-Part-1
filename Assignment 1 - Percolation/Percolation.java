// import edu.princeton.cs.algs4.StdRandom;
// import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[][] grid;							// global variable
	private final WeightedQuickUnionUF weightedFind;      // 1st union find object
	private final WeightedQuickUnionUF weightedFind2;
	private int counter;							// counts number of open sites
	// private boolean perc;                           // true if site on the last row is full
	
	
    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
    	if (n < 1) throw new IllegalArgumentException("n must be greater than 0");
    	grid = new int[n+1][n+1];
        weightedFind = new WeightedQuickUnionUF(n*n + 2); 
        weightedFind2 = new WeightedQuickUnionUF(n*n + 2);
        // perc = false;
    }
    
    public void open(int row, int col) // open site (row, col) if it is not open already
    {
        argumentCheck(row, col);
    	
    	if (grid[row][col] == 0) 
    	{
    		grid[row][col] = 1; // open the site
    		counter++;
    	}
    	
    	/* check if newly opened site can be connected to adjacent sites */
    	int site1 = xyTo1D(row, col);
    	
    	if (col < grid.length - 1 && isOpen(row, col + 1)) // union with right site
    	{
    		int newCol = col + 1;
            weightedFind.union(site1, xyTo1D(row, newCol));
            weightedFind2.union(site1, xyTo1D(row, newCol));
    	}
    		
    	if (col > 1  && isOpen(row, col - 1)) // union with left site
    	{
    		int newCol = col - 1;
            weightedFind.union(site1, xyTo1D(row, newCol));
            weightedFind2.union(site1, xyTo1D(row, newCol));
    	}
    	
    	if (row > 1  && isOpen(row - 1, col)) // union with top site
    	{
    		int newRow = row - 1;
    		weightedFind.union(site1, xyTo1D(newRow, col));
    		weightedFind2.union(site1, xyTo1D(newRow, col));
    	}
    	
    	if (row < grid.length - 1  && isOpen(row + 1, col)) // union with bottom site
    	{
    		int newRow = row + 1;
            weightedFind.union(site1, xyTo1D(newRow, col));
            weightedFind2.union(site1, xyTo1D(newRow, col));
    	}

	    if (row == 1)  
        {
            weightedFind.union(0, xyTo1D(row, col));
            weightedFind2.union(0, xyTo1D(row, col));
        }
    	
	    if (row == grid.length - 1)
        {
	        weightedFind.union(xyTo1D(row, col), ((grid.length - 1) * (grid.length - 1) + 1)); // virt2
        }
    	
    }
    
    public boolean isOpen(int row, int col)  // is site (row, col) open?
    {
        argumentCheck(row, col);
    	
    	if (grid[row][col] == 1)
    	{
    		return true;
    	}

        return false;
    }
    
    public boolean isFull(int row, int col)
    {
        argumentCheck(row, col);
    	
        if (weightedFind.connected(0, xyTo1D(row, col)) && weightedFind2.connected(0, xyTo1D(row, col)))
        {
            return true;
        }

        return false;
    }
    
    public int numberOfOpenSites()     // number of open sites		   
    {
        return counter;
    }
    
    public boolean percolates()        // does the system percolate?
    {
        if (weightedFind.connected(0, (((grid.length - 1) * (grid.length - 1)) + 1)))
        {
            return true;
        }

        return false; 
    }
    
    private int xyTo1D(int row, int col)
    {
        argumentCheck(row, col);
    	
    	// convert row and column into a single number assigned in WQUF data structure
    	int component = 0;
    	if (row == 1)
    	{
    		component = col;
    	}
    	else
    	{
    	    component = ((row - 1) * (grid.length - 1)) + col;
    	}
    	
    	return component;
    }
    
    private void argumentCheck(int row, int col)
    {
        if (row > grid.length - 1 || row < 1)   // if input is greater than grid size
            throw new IllegalArgumentException();
        
        if (col > grid.length - 1 || col < 1)   // if input is greater than grid size
            throw new IllegalArgumentException();
    }
    
    public static void main(String[] args)   // test client (optional)
    {
    	// left blank
    }
    	
}
