import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;


public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int n;
    
    public RandomizedQueue()                 // construct an empty randomized queue
    {
        a = (Item[]) new Object[1];
        n = 0;
    }
    
    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return n == 0;
    }
    
    public int size()                        // return the number of items on the randomized queue
    {
        return n;
    }
    
    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (a.length == 0)
        {
            resize(1);
        }
        if (n == a.length) resize(2 * a.length);
        a[n++] = item;
    }
    
    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        
        int randInt = StdRandom.uniform(n);
        Item randItem = a[randInt];
        a[randInt] = null;
        n--;
        
        resize(a.length);
        
        if (n * 4 == a.length) 
        {
            resize(a.length / 2);
        }
        
        return randItem;
    }
    
    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int randInt = StdRandom.uniform(n);
        Item randItem = a[randInt];
        
        return randItem;
    }
    
    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new ArrayIterator();
    }
    
   private void resize(int capacity)       // gathers all non null entries to front of the array & resizes
    {
        Item[] temp = (Item[]) new Object[capacity];
        
        int j = 0;
        for (int i = 0; i < a.length; i++)
        {    
              if (a[i] != null)
              {
                  temp[j] = a[i];
                  j++;
              }
              if (j == n) break;
        }
        
        a = temp;
    }
    
    private class ArrayIterator implements Iterator<Item> 
    {
        private int i;
        private Item[] arr;
        
        public ArrayIterator() {
            i = 0;
            arr = a;
            StdRandom.shuffle(arr, 0, n);
        }
 
        public boolean hasNext() {
            return i != n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return arr[i++];
        }
    }
   
    public static void main(String[] args)   // unit testing (optional)
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);
        for (int i = 0; i < rq.a.length; i++)
        {
            System.out.println(rq.a[i]);
        }
        System.out.println(rq.a.length);
    }
}