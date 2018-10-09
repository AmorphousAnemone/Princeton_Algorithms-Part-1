import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
   private int n;       // number of elements in data structure
   private Node first;  // first node in the deque
   private Node last;   // last node in the deque
    
   private class Node {
       private Item item;
       private Node next;
       private Node previous;
   }
   
   public Deque()                           // construct an empty deque
   {
       first = null;
       last = null;
       n = 0;
   }
   
   public boolean isEmpty()                 // is the deque empty?
   {
       return n == 0;
   }
   
   public int size()                        // return the number of items on the deque
   {
       return n;
   }
   
   public void addFirst(Item item)          // add the item to the front
   {
       if (item == null) throw new java.lang.IllegalArgumentException();
       
       Node oldFirst = first;
       first = new Node();
       first.item = item;
       first.next = oldFirst;
       
       if (first.next != null)
       {
           first.next.previous = first;
       }
       else
       {
           last = first;
       }
       n++;
   }
   
   public void addLast(Item item)           // add the item to the end
   {
       if (item == null) throw new java.lang.IllegalArgumentException();
       
       Node oldLast = last;
       last = new Node();
       last.item = item;
       last.next = null;
       
       if (oldLast != null)
       {
           last.previous = oldLast;
           last.previous.next = last;
       } 
       else 
       {
           first = last;
       }
       n++;
   }
   
   public Item removeFirst()                // remove and return the item from the front
   {
       if (isEmpty()) throw new java.util.NoSuchElementException();
       Item firstItem = first.item;
       first = first.next;
       
       if (first != null)
       {
           first.previous = null;
       } 
       else
       {
           first = null;
           last = null;
       }
       n--;
       
       return firstItem;
   }
   
   public Item removeLast()                 // remove and return the item from the end
   {
       if (isEmpty()) throw new java.util.NoSuchElementException();
       Item lastItem = last.item;
       last = last.previous;
       
       
       if (last != null)
       {
           last.next = null;
       }
       else
       {
           last = null;
           first = null;
       }
       n--;
       
       return lastItem;
   }
   
   public Iterator<Item> iterator()         // return an iterator over items in order from front to end
   {
       return new ListIterator();
   }
   
   private class ListIterator implements Iterator<Item> 
   {
       private Node current = first;
       public boolean hasNext()
       {
           return current != null;
       }
       
       public void remove()
       {
           throw new UnsupportedOperationException();
       }
       
       public Item next()
       {
           if (!hasNext()) throw new NoSuchElementException();
           Item item = current.item;
           current = current.next; 
           return item;
       }
   }
   
   public static void main(String[] args)   // unit testing (optional)
   {
       Deque<String> deque = new Deque<String>();
       System.out.println("Deque of Strings created");
       System.out.println("deque is " + deque.size() + " node(s) long!");
       deque.addLast("1");
       System.out.println("..1");
       deque.removeFirst();
       System.out.println("..2");
       deque.addLast("2");
       System.out.println("..3");
       deque.removeFirst();
       System.out.println("..4");

       deque.addLast("3");
       deque.addLast("4");
       
       System.out.println(deque.first.next.item + " <-- the last string");

       deque.removeFirst();
       System.out.println(deque.first.next.item + " <-- the last string");

       deque.addLast("6");
       System.out.println(deque.first.item + " <-- the last string");

       deque.removeFirst();
      
       System.out.println(deque.last.item + " <-- the first string");
       
       System.out.println(deque.first.item + " <-- the last string");
       System.out.println("deque is " + deque.size() + " node(s) long!");
   }
}
