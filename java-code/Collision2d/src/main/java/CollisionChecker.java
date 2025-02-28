import refac.Box;
import refac.BoxFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CollisionChecker {

    public static final Object lock = new Object();

    public static String jsonString;

    public static void main(String[] args) {

        BoxFactory boxFactory = BoxFactory.getInstance();

        Thread socketThread = Thread.startVirtualThread(new SocketServer());

        List<Box> boxes;

        while (true) {

            synchronized (lock) {

                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Wait for jsonString update from socketServer
                if (jsonString == null) {
                    continue;
                }

                boxes = boxFactory.createBoxListFromJson(jsonString);

                List<List<Box>> boxMatchups = getBoxMatchups(boxes);

                ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();

                compareAllMatchups(boxMatchups, exec);

                lock.notify();
            }
        }
    }

    public static List<List<Box>> getBoxMatchups(List<Box> boxes) {

        List<List<Box>> boxMatchups = new ArrayList<>();

        for (int i = 0; i < boxes.size(); i++) {
            for (int j = i + 1; j < boxes.size(); j++) {

                List<Box> matchup = new ArrayList<>();
                matchup.add(boxes.get(i));
                matchup.add(boxes.get(j));

                boxMatchups.add(matchup);
            }
        }

        return boxMatchups;
    }

    // Take in list of all box pairs, then run their comparisons concurrently.
    public static void compareAllMatchups(List<List<Box>> matchups, ExecutorService exec) {

        long begin = System.nanoTime();

        try (exec) {

            List<Callable<Boolean>> tasks = new ArrayList<>();

            for (List<Box> matchup : matchups) {
                tasks.add(() -> Box.checkBoxesForCollision(matchup));
            }

            List<Future<Boolean>> results = exec.invokeAll(tasks);

            for (Future<Boolean> result : results) {
                if (result.get()) {

                    System.out.println("----------------------- C O L L I S I O N ------------------------");
                    return;
                }
            }

            System.out.println("~~~~~~~ no collisions ~~~~~~~");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
