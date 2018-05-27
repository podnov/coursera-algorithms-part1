import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item>
        implements Iterable<Item> {

    private Item[] data;
    private int enqueueIndex = 0;

    public RandomizedQueue() {
        data = createArray(1);
    }

    private void autoSizeDown() {
        if (enqueueIndex == (data.length / 4)) {
            resize(data.length / 2);
        }
    }

    private void autoSizeUp() {
        if (enqueueIndex == data.length) {
            resize(data.length * 2);
        }
    }

    private Item[] createArray(int size) {
        return (Item[]) new Object[size];
    }

    public Item dequeue() {
        validateHasElement();
        int dequeueIndex = getRandomDataIndex();
        Item result = data[dequeueIndex];

        int lastElementIndex = enqueueIndex - 1;
        data[dequeueIndex] = data[lastElementIndex];
        data[lastElementIndex] = null;
        enqueueIndex--;

        autoSizeDown();

        return result;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot enqueue a null item");
        }
        data[enqueueIndex++] = item;
        autoSizeUp();
    }

    private int getRandomDataIndex() {
        return StdRandom.uniform(enqueueIndex);
    }

    public boolean isEmpty() {
        return enqueueIndex == 0;
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator(this);
    }

    private void resize(int newSize) {
        Item[] newData = createArray(newSize);
        for (int i = 0; i < enqueueIndex; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    public Item sample() {
        validateHasElement();
        int sampleIndex = getRandomDataIndex();
        return data[sampleIndex];
    }

    public int size() {
        return enqueueIndex;
    }

    private void validateHasElement() {
        if (enqueueIndex == 0) {
            throw new NoSuchElementException();
        }
    }

    private class RandomizedQueueIterator
            implements Iterator<Item> {

        private final RandomizedQueue<Item> queue;
        private final int[] order;
        private int orderIndex = 0;

        public RandomizedQueueIterator(RandomizedQueue<Item> queue) {
            this.queue = queue;
            this.order = StdRandom.permutation(queue.enqueueIndex);
        }

        @Override
        public boolean hasNext() {
            return order.length > orderIndex;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            int queueIndex = order[orderIndex++];
            return queue.data[queueIndex];
        }

    }

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();

        assertQueueSize(queue, 0);
        assertQueueDataLength(queue, 1);

        queue.enqueue("a");
        assertQueueSize(queue, 1);
        assertQueueDataLength(queue, 2);

        queue.enqueue("b");
        assertQueueSize(queue, 2);
        assertQueueDataLength(queue, 4);

        queue.enqueue("c");
        assertQueueSize(queue, 3);
        assertQueueDataLength(queue, 4);

        queue.enqueue("d");
        assertQueueSize(queue, 4);
        assertQueueDataLength(queue, 8);

        queue.enqueue("e");
        assertQueueSize(queue, 5);
        assertQueueDataLength(queue, 8);

        print(queue);

        queue.dequeue();
        print(queue);
        assertQueueSize(queue, 4);
        assertQueueDataLength(queue, 8);

        queue.dequeue();
        print(queue);
        assertQueueSize(queue, 3);
        assertQueueDataLength(queue, 8);

        queue.dequeue();
        print(queue);
        assertQueueSize(queue, 2);
        assertQueueDataLength(queue, 4);

        queue.dequeue();
        print(queue);
        assertQueueSize(queue, 1);
        assertQueueDataLength(queue, 2);

        queue.dequeue();
        print(queue);
        assertQueueSize(queue, 0);
        assertQueueDataLength(queue, 1);
    }

    private static void assertQueueDataLength(RandomizedQueue<String> queue, int expected) {
        int actual = ((Object[]) queue.data).length;
        String message = String.format("%s should be %s", actual, expected);
        System.out.println(message);

        if (actual != expected) {
            throw new AssertionError();
        }
    }

    private static void assertQueueSize(RandomizedQueue<String> queue, int expected) {
        int actual = queue.size();
        String message = String.format("%s should be %s", actual, expected);
        System.out.println(message);

        if (actual != expected) {
            throw new AssertionError();
        }
    }

    private static void print(RandomizedQueue<String> queue) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = queue.iterator();

        while (it.hasNext()) {
            sb.append(it.next());
        }

        System.out.println(sb.toString());
    }
}
