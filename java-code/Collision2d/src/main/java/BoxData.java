import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoxData {

    public List<Double> xVals = new ArrayList<>();
    public List<Double> yVals = new ArrayList<>();

    private List<List<List<Double>>> boxesList;
    private List<List<Double>> coordsList;
    private List<Double> xyList;

    public void printCoords() {

//        String theJson = "[" +
//                "           [" +
//                                 "[340.8923034667969, 223.36080932617188], " +
//                                "[343.9502258300781, 219.4539794921875], " +
//                                "[257.6163635253906, 151.87991333007812]," +
//                                " [254.55845642089844, 155.7867431640625]" +
//                "            ], " +
//
//                            "[" +
//                    "           [229.93397521972656, 233.86471557617188], " +
//                                "[233.24375915527344, 236.45361328125], " +
//                                "[298.2398681640625, 153.35870361328125], " +
//                                "[294.9300537109375, 150.76980590820312]" +
//                            "]" +
//                        "]";
        String theJson = SocketServer.jsonLine;

        Gson gson = new Gson();
        Type BoxesList = new TypeToken<List<List<List<Double>>>>(){}.getType();
        boxesList = gson.fromJson(theJson, BoxesList);

        for (int i = 0; i < boxesList.size(); i++) {
            coordsList = (boxesList.get(i));

            for (int j = 0; j < coordsList.size(); j++) {
                xyList = coordsList.get(j);
//                System.out.println("xyList: x is " + xyList.get(0));
//                System.out.println("xyList: y is " + xyList.get(1));
                xVals.add(xyList.get(0));
                yVals.add(xyList.get(1));

            }
        }



//        System.out.println(Arrays.toString(xVals.toArray()) + "xvals array 0000000000000000000000000000000000000000");


//        System.out.println("xvals: " + xVals + " y-vals: " + yVals);
        List<Box> boxesToCompare = new ArrayList<>();

        for (int i = 0; i < xVals.size(); i += 4) {
            Double[] xArray = new Double[4];
            Double[] yArray = new Double[4];
            for (int j = 0; j < 4; j++) {
                xArray[j] = xVals.get(i + j);
                yArray[j] = yVals.get(i + j);
            }
//            System.out.println("x array : " + Arrays.asList(xArray));
            boxesToCompare.add(new Box(xArray, yArray));

        }
        Double[] xArray = xVals.toArray(new Double[0]);
        Double[] yArray = yVals.toArray(new Double[0]);

        List<Double[]> axisListBothBoxes = boxesToCompare.get(0).getAxisList(xArray, yArray);

//        System.out.println(axisListBothBoxes);


        for (Box box : boxesToCompare) {
//            System.out.println("double array for x of box: " + Arrays.asList(box.x));
//            System.out.println("double array for y of box: " + Arrays.asList(box.y));
        }

        CollisionChecker.compareBoxes(boxesToCompare, axisListBothBoxes);


    }
}
