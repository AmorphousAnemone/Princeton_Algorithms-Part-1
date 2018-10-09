import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {
   private double thresh; // number of open sites at the time of percolation
   private double[] openSites;
   private double T;
   
   public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
   {
	   if (n < 1 || trials < 1) throw new java.lang.IllegalArgumentException();
	   
     openSites = new double[trials];
	   int elementsInGrid = n*n;
	   T = trials;
	   
     for (int i = 0; i < trials; i++)
	   {
	       Percolation perc = new Percolation(n);
         while (!perc.percolates())
         {
             int row = StdRandom.uniform(n);
             int col = StdRandom.uniform(n);
             perc.open(row + 1, col + 1);
         }

         openSites[i] = (double) perc.numberOfOpenSites() / (double) elementsInGrid;
	   }
	   
   }
   
   public double mean()                          // sample mean of percolation threshold
   {
	   double mean = StdStats.mean(openSites);
     return mean;
   }

   public double stddev()                        // sample standard deviation of percolation threshold
   {
	   double stddev = StdStats.stddev(openSites);
     return stddev;
   }

   public double confidenceLo()                  // low  endpoint of 95% confidence interval
   {
	   double lo = StdStats.mean(openSites) - ((1.96 * StdStats.stddev(openSites)) / Math.sqrt(T));
     return lo;
   }
   
   public double confidenceHi()                  // high endpoint of 95% confidence interval
   {
     double hi = StdStats.mean(openSites) + ((1.96 * StdStats.stddev(openSites)) / Math.sqrt(T));
     return hi;
   }
   
   public static void main(String[] args)        // test client (described below)
   {
	   // String n = args[0];
	   // String T = args[1];
	   PercolationStats pStats = new PercolationStats(200, 100);
	   System.out.println("mean: " + pStats.mean());
	   System.out.println("stddev: " + pStats.stddev());
	   System.out.println("confidenceLo: " + pStats.confidenceLo());
	   System.out.println("confidenceHi: " + pStats.confidenceHi());
   }
}
