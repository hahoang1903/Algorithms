import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.next = first;
        newFirst.prev = null;
        if (isEmpty())
            last = newFirst;
        else
            newFirst.next.prev = newFirst;
        first = newFirst;
        size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node newLast = new Node();
        newLast.item = item;
        newLast.next = null;
        newLast.prev = last;
        if (isEmpty())
            first = newLast;
        else
            last.next = newLast;
        last = newLast;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item firstItem = first.item;
        first = first.next;
        size--;
        if (first == null)
            last = null;
        else
            first.prev = null;
        return firstItem;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Item lastItem = last.item;
        last = last.prev;
        size--;
        if (last == null)
            first = null;
        else
            last.next = null;
        return lastItem;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();

            Item item = current.item;
            current = current.next;
            return item;
        }
    }


    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        deque.addFirst("hey");
        deque.addFirst("you");
        deque.addLast("yo");
        deque.addLast("perfect");
        for (String s : deque)
            StdOut.println(s);
        StdOut.println(deque.size());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.removeLast());
        for (String s : deque)
            StdOut.println(s);
        StdOut.println(deque.size());
        StdOut.println(deque.removeLast());
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.isEmpty());
    }
}