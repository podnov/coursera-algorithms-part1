import edu.princeton.cs.algs4.StdIn;

public class Permutation {

    public static void main(String[] args) {
        int count = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String string = StdIn.readString();
            queue.enqueue(string);
        }

        for (int i = 0; i < count; i++) {
            String dequeue = queue.dequeue();
            System.out.println(dequeue);
        }
    }
}
