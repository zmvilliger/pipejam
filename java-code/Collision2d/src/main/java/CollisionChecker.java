import bbox.Box;
import bbox.BoxFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CollisionChecker {

    // collision and iteration counts to smooth out collision warnings
    //TODO maybe need volatile
    public static int collisionConfidence = 0;
    public static boolean isJam = false;
    public static String jamStatusString = "Waiting for data...";

    // Higher decrement value means the program is more quick to decrease jam confidence.
    public static int CONFIDENCE_DECREMENT_VALUE = 2;

    // Higher increment value means the program gains more confidence of a jam being present per frame.
    public static int CONFIDENCE_INCREMENT_VALUE = 10;

    /* variables that control how "deep" the confidence can go that a jam is occurring, and also
       how high the confidence has to get before a jam warning triggers.  This smoothes out
       the twitchiness of the object detection, where sometimes a few frames here and there don't detect both of
       the colliding pipes.  Higher upper limit means more collision-free frames will need to be seen before
       the jam warning is called off.
    */
    public static int CONFIDENCE_UPPER_LIMIT = 100;
    public static int CONFIDENCE_UPPER_THRESHOLD = 50;
    public static int CONFIDENCE_LOWER_THRESHOLD = 25;


    // Synchronization object lock for SocketServer and CollisionChecker (main) functions
    public static final Object lock = new Object();
    public static String jsonString;

    public static void main(String[] args) {

        BoxFactory boxFactory = BoxFactory.getInstance();

        Thread socketThread = Thread.startVirtualThread(new SocketServer());

        Thread stopSignalThread = Thread.startVirtualThread(new StopSignalServer());

        MyFrame myFrame = new MyFrame();
        myFrame.run();

        List<Box> boxes;

        while (true) {

            synchronized (lock) {

                // Wait for jsonString update from socketServer
                if (jsonString == null) {
                    lock.notifyAll();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }

                boxes = boxFactory.createBoxListFromJson(jsonString);
                List<List<Box>> boxMatchups = getBoxMatchups(boxes);

                ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();

                boolean collisionFound = compareAllMatchups(boxMatchups, exec);
                adjustCollisionConfidence(collisionFound);
                
                jsonString = null;
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
    public static boolean compareAllMatchups(List<List<Box>> matchups, ExecutorService exec) {
        long begin = System.nanoTime();
        boolean collisionFound = false;

        /* Create a list of tasks.  Each task is to compare a pair of boxes on one thread.  The tasks
           each return a Callable<Boolean>.  The ExecutorService invokes all of these tasks, and if any
           of them return true, there is a collision in view.  To smooth out collision detection, a
           value is increased when collisions are found in a frame.  If a collision is NOT found, the
           value is decremented.  If this value goes over a threshold, it's likely a collision really exists,
           as opposed to there just being a bad detection by the detection model.
         */
        try (exec) {
            List<Callable<Boolean>> tasks = new ArrayList<>();

            // fills the tasks list
            for (List<Box> matchup : matchups) {
                tasks.add(() -> Box.checkBoxesForCollision(matchup));
            }

            // Starts executing the comparison tasks concurrently
            List<Future<Boolean>> results = exec.invokeAll(tasks);

            // Checks the boolean values returned by the tasks
            for (Future<Boolean> result : results) {
                if (result.get()) {
                    collisionFound = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return collisionFound;
    }

    public static void adjustCollisionConfidence(boolean collisionFound) {
        if (collisionFound) {
            collisionConfidence += CONFIDENCE_INCREMENT_VALUE;

            if (collisionConfidence > CONFIDENCE_UPPER_LIMIT) {
                collisionConfidence = CONFIDENCE_UPPER_LIMIT;
            }
        } else {
            collisionConfidence -= CONFIDENCE_DECREMENT_VALUE;

            if (collisionConfidence < 0) {
                collisionConfidence = 0;
            }
        }

        isJam = (collisionConfidence > CONFIDENCE_UPPER_THRESHOLD);
        jamStatusString = (isJam) ? "!!! jam detected !!!" : "--- clear ---";
    }
}
