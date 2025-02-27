import refac.Box;
import refac.BoxFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CollisionChecker {

    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_RESET = "\u001B[0m";

    public static String jsonString;
    public static void main(String[] args) {

//                String test = "[" +
//                "[[523.92236328125, 209.69586181640625]," +
//                " [528.2108154296875, 211.55337524414062], " +
//                "[576.489501953125, 100.0909423828125], " +
//                "[572.2010498046875, 98.23342895507812]], " +
//                "[[575.1689453125, 198.86827087402344], " +
//                "[576.5592041015625, 193.8419952392578]," +
//                " [447.2027282714844, 158.0636749267578], " +
//                "[445.8125305175781, 163.08995056152344]]" +
//                "]";

        BoxFactory boxFactory = BoxFactory.getInstance();

        Thread t1 = new Thread(new SocketServer());
        t1.start();


//        List<Box> boxes = null;
//        while(true) {
//
//
//            if (jsonString == null) {
//                continue;
//            }
//
//            boxes = boxFactory.createBoxListFromJson(jsonString);
//
//            List<List<Box>> boxMatchups = new ArrayList<>();
//
//            for (int i = 0; i < boxes.size(); i++) {
//                for (int j = i; j < boxes.size(); j++) {
//                    if (i == j) {
//                        continue;
//                    }
//
//                    List<Box> matchup = new ArrayList<>();
//                    matchup.add(boxes.get(i));
//                    matchup.add(boxes.get(j));
//
//                    boxMatchups.add(matchup);
//                }
//            }
//
//            System.out.println(boxMatchups);
//
//
//            // TODO 1 adding 3rd pipe f------ fixed
//             // TODO 2 find out why not printing here.  Debug looks good for the checks.
//            // TODO 3  make collision checks multithreaded and time them
//            for (List<Box> pairOfBoxes : boxMatchups) {
//                System.out.println(Box.checkBoxesForCollision(pairOfBoxes));
//                System.out.flush();
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@");
//                System.out.println("@@@@@@@@@@@@@@@@@@@@@");
//
//            }
//        }







    }
    //new idea, get all pair matchups of boxes, their axis lists, then have each pair sent to Box.isOverlapping()
    // with each on a different thread, to speed up calculation.
}
