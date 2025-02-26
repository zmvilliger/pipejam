import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollisionChecker {

    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_RESET = "\u001B[0m";
    public static void main(String[] args) {

        BoxData boxData = new BoxData();

        //TEMP ---- REMOVE WHEN DONE TESTING JSON
//        boxData.printCoords();
//        boxData.printCoords();

        SocketServer server = new SocketServer(boxData);
        server.runServer();
        final int MIN = 0;
        final int MAX = 1;

        //test vals
//        Double[] x = {3.0, 5.0, 4.0, 2.0};
//        Double[] y = {1.0, 2.0, 4.0, 3.0};
//
//        Double[] x2 = {5.0, 7.0, 6.0, 4.0};
//        Double[] y2 = {1.0, 2.0, 4.0, 3.0};

//        Box box1 = new Box();
//        Box box2 = new Box();
//
//        List<Box> boxList = new ArrayList<>();
//        boxList.add(box1);
//        boxList.add(box2);
//
//        compareBoxes(boxList, x, y, x2, y2);

        // For each shape, get a list of each projection, each containing a min at [0], and max at [1]
//        List<Double[]> projections1 = box1.getProjections(x,y);
//        List<Double[]> projections2 = box2.getProjections(x2,y2);
//
//        for (int i = 0; i < projections1.size(); i++) {
//            //if box 1 projection min and max are both below box 2 min, boxes do not overlap
//            if (projections1.get(i)[MIN] < projections2.get(i)[MIN] && projections1.get(i)[MAX] < projections2.get(i)[MIN]) {
//                System.out.println("Boxes do not overlap on projection " + i);
//            }
//
//            if (projections1.get(i)[MIN] > projections2.get(i)[MAX] && projections1.get(i)[MAX] > projections2.get(i)[MAX]) {
//                System.out.println("Boxes do not overlap on projection " + i);
//            }
//        }


    } // End of main()

    public static void compareBoxes(List<Box> boxes, List<Double[]> axisList) {

//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++ BOX COUNT: " + boxes.size() + "+++++++");

        final int MIN = 0;
        final int MAX = 1;
        int separationMax = (boxes.size() - 1) * 2;
        int separations = 0;
        boolean clear = false;

        for (int i = 0; i < boxes.size(); i++) {
            int boxesCleared = 0;


            //RETURNS a list of min/max pairs for projection min and max of one box, on ITS OWN PROJECTION AXIS set.
            // NEEDS to return a list of min/max pairs that has 8 pairs, not 4.
            List<Double[]> projections1 = boxes.get(i).getProjections(axisList);

//            System.out.println("projections111111111111, should be 8 min/max pairs: " + projections1);

            //for each box to be compared
            for (int j = i; j < boxes.size(); j++) {
                boolean separationFoundOnBox = false;
                if (j == i) {
                    continue;
                }
                List<Double[]> projections2 = boxes.get(j).getProjections(axisList);
//                System.out.println("projections2222222222, should be 8 min/max pairs: " + projections1);

                // For a each projection angle k, test box i against box j
                for (int k = 0; k < projections1.size(); k++) {

//                  System.out.println("Projection 1  happening: projection1: " + Arrays.asList(projections1.get(k)));
//                  System.out.println("Projection 2 happening " + Arrays.asList(projections2.get(k)));
                    //if box 1 projection min and max are both below box 2 min, boxes do not overlap
                    if (projections1.get(k)[MAX] < projections2.get(k)[MIN] || projections2.get(k)[MAX] < projections1.get(k)[MIN]) {

                        separationFoundOnBox = true;
                        boxesCleared++;

                        break;
                    }
                }
            }

            if (boxesCleared >= (boxes.size() - i - 1)) {
                separations++;
            }
        }

        if (separations >= boxes.size()) {
            System.out.println(ANSI_RESET + "------------------- no collisions ----------------");
            System.out.println("::Separations found ::::: " + separations + "threshold: " + separationMax);
            System.out.println("[][][][][][][][][][] NUM OF BOXES DETECTED " + boxes.size());



        } else {
            System.out.println(ANSI_RED + "!!!!!!!!!!!!!!! WARNING JAM IMMINENT - COLLISION DETECTED !!!!!!!!!!!!!!!!!");
            System.out.println("Separations found::::: " + separations + "threshold: " + separationMax);
            System.out.println("[][][][][][][][][][] NUM OF BOXES DETECTED " + boxes.size());
        }

    } // End of compareBoxes
}
