import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item>
        implements Iterable<Item> {

    private static final String CANNOT_ADD_NULL_VALUE = "Cannot add null value";

    private Node first;
    private Node last;
    private int size;

    public Deque() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(CANNOT_ADD_NULL_VALUE);
        }

        size++;
        Node oldFirst = first;

        first = new Node();
        first.item = item;

        if (oldFirst == null) {
            last = first;
        } else {
            first.next = oldFirst;
            oldFirst.previous = first;
        }
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(CANNOT_ADD_NULL_VALUE);
        }

        size++;
        Node oldLast = last;

        last = new Node();
        last.item = item;

        if (oldLast == null) {
            first = last;
        } else {
            last.previous = oldLast;
            oldLast.next = last;
        }
    }

    public Item removeFirst() {
        validateRemove();
        Item result = first.item;

        if (size == 1) {
            first = null;
            last = null;
        } else {
            Node oldFirst = first;
            first = oldFirst.next;

            oldFirst.next = null;
            first.previous = null;
        }

        size--;

        return result;
    }

    public Item removeLast() {
        validateRemove();
        Item result = last.item;

        if (size == 1) {
            first = null;
            last = null;
        } else {
            Node oldLast = last;
            last = oldLast.previous;

            oldLast.previous = null;
            last.next = null;
        }

        size--;

        return result;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator(this);
    }

    private void validateRemove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    private class Node {
        private Node previous;
        private Item item;
        private Node next;
    }

    private class DequeIterator
            implements Iterator<Item> {

        private Node next;

        private DequeIterator(Deque<Item> deque) {
            next = deque.first;
        }

        @Override
        public boolean hasNext() {
            return (next != null);
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item result = next.item;
            next = next.next;
            return result;
        }
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        assertIsEmpty(deque, true);

        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        String f = "f";

        assertIterator(deque, "");

        deque.addFirst(c);
        assertFirst(deque, c);
        assertLast(deque, c);
        assertIsEmpty(deque, false);
        assertIterator(deque, "c");

        deque.addLast(d);
        assertFirst(deque, c);
        assertLast(deque, d);
        assertIsEmpty(deque, false);
        assertIterator(deque, "cd");

        deque.addFirst(b);
        assertFirst(deque, b);
        assertLast(deque, d);
        assertIsEmpty(deque, false);
        assertIterator(deque, "bcd");

        deque.addFirst(a);
        assertFirst(deque, a);
        assertLast(deque, d);
        assertIsEmpty(deque, false);

        assertIterator(deque, "abcd");

        deque.addLast(e);
        assertFirst(deque, a);
        assertLast(deque, e);
        assertIsEmpty(deque, false);
        assertIterator(deque, "abcde");

        deque.addLast(f);
        assertFirst(deque, a);
        assertLast(deque, f);
        assertIsEmpty(deque, false);
        assertIterator(deque, "abcdef");

        deque.removeLast();
        assertFirst(deque, a);
        assertLast(deque, e);
        assertIsEmpty(deque, false);
        assertIterator(deque, "abcde");

        deque.removeFirst();
        assertFirst(deque, b);
        assertLast(deque, e);
        assertIsEmpty(deque, false);
        assertIterator(deque, "bcde");

        deque.removeFirst();
        assertFirst(deque, c);
        assertLast(deque, e);
        assertIsEmpty(deque, false);
        assertIterator(deque, "cde");

        deque.removeLast();
        assertFirst(deque, c);
        assertLast(deque, d);
        assertIsEmpty(deque, false);
        assertIterator(deque, "cd");

        deque.removeLast();
        assertFirst(deque, c);
        assertLast(deque, c);
        assertIsEmpty(deque, false);
        assertIterator(deque, "c");

        deque.removeLast();
        assertFirstNull(deque);
        assertLastNull(deque);
        assertIsEmpty(deque, true);
        assertIterator(deque, "");
    }

    private static void assertIterator(Deque<String> deque, String expected) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = deque.iterator();

        while (it.hasNext()) {
            sb.append(it.next());
        }

        String actual = sb.toString();
        assertObjectEquals(expected, actual);
    }

    private static <T> void assertFirst(Deque<T> deque, T expected) {
        T actual = deque.first.item;
        assertObjectEquals(expected, actual);
    }

    private static void assertFirstNull(Deque<String> deque) {
        Object actual = deque.first;
        String message = String.format("%s should be null", actual);
        System.out.println(message);

        if (actual != null) {
            throw new AssertionError();
        }
    }

    private static <T> void assertLast(Deque<T> deque, T expected) {
        T actual = deque.last.item;
        assertObjectEquals(expected, actual);
    }

    private static void assertLastNull(Deque<String> deque) {
        Object actual = deque.last;
        String message = String.format("%s should be null", actual);
        System.out.println(message);

        if (actual != null) {
            throw new AssertionError();
        }
    }

    private static void assertIsEmpty(Deque<String> deque, boolean expected) {
        boolean actual = deque.isEmpty();
        String message = String.format("isEmpty %s should be %s", actual, expected);
        System.out.println(message);

        if (actual != expected) {
            throw new AssertionError();
        }
    }

    private static <T> void assertObjectEquals(T expected, T actual) {
        String message = String.format("%s should be %s", actual, expected);
        System.out.println(message);

        if (!actual.equals(expected)) {
            throw new RuntimeException(message);
        }
    }
}
// Throw a java.util.NoSuchElementException if the client calls the next()
// method in the iterator when there are no more items to return.
