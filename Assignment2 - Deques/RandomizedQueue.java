import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] data;
    private int size;

    public RandomizedQueue() {
        size = 0;
        data = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = data[i];
        data = copy;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (size == data.length) {
            if (size != 0)
                resize(size * 2);
            else
                resize(1);
        }
        data[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();

        int index = StdRandom.uniform(size);

        Item item = data[index];

        data[index] = data[size - 1];
        data[--size] = null;

        if (size == data.length / 4)
            resize(data.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int index = StdRandom.uniform(size);
        return data[index];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int current;
        private RandomizedQueue<Item> rq;

        public RandomizedQueueIterator() {
            current = 0;
            rq = new RandomizedQueue<Item>();
            rq.data = Arrays.copyOf(data, size);
            rq.size = size;
        }

        public boolean hasNext() {
            return current < rq.size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return rq.dequeue();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(4);
        rq.enqueue(9);
        rq.enqueue(7);
        rq.enqueue(3);
        rq.enqueue(15);

        StdOut.println(rq.sample());
        StdOut.println(rq.size());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.size());

        for (int s : rq)
            StdOut.println(s);

        StdOut.println(rq.isEmpty());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.dequeue());
        StdOut.println(rq.isEmpty());
    }
}
