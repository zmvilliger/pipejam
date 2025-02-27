package refac;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// Singleton factory class for receiving and handling JSON bounding box data, and creating boxes from the Data.
public class BoxFactory {

    private static BoxFactory instance;

    // public method for getting reference to single box factory instance
    public static BoxFactory getInstance() {
        if (instance == null) {
            instance = new BoxFactory();
        }
        return instance;
    }

    //Prevent construction with private constructor
    private BoxFactory() {}

    // Parses list of boxes, each box containing list of Points, each point containing list of two doubles
    private final Gson gson = new Gson();
    private final Type BoxListType = new TypeToken<List<List<List<Double>>>>(){}.getType();

    public List<Box> createBoxListFromJson(String boxesJson) {

        List<List<List<Double>>> boxesList = gson.fromJson(boxesJson, BoxListType);
        List<Box> boxObjectList = new ArrayList<>();

        for (List<List<Double>> boxPoints : boxesList) {

                Box box = new Box(boxPoints);
                boxObjectList.add(box);

        }
//        System.out.println(boxObjectList);
        return boxObjectList;
    }
}
