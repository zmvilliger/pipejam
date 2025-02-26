import java.util.ArrayList;
import java.util.List;

public class Box {

    Double[] x = new Double[4];
    Double[] y = new Double[4];
    boolean isColliding = false;

//    public static List<Double> axisList = new ArrayList<>();

    public Box(Double[] x, Double[] y){
        this.x = x;
        this.y = y;
    }


    public List<Double[]> getProjections(List<Double[]> axisList) {


//        System.out.println("Getting min and max");
        Double p1min = null;
        Double p1max = null;
        List<Double[]> projections = new ArrayList<>();
        int projectionNumber = 0;

//        List<Double[]> axisList = getAxisList(x, y);

        for (Double[] axisN : axisList) {
            p1min = getDotProduct(axisN[0], axisN[1], x[0], y[0]);
            p1max = p1min;
//            System.out.println(p1min);

            // Project each point onto the normalized axis
            for (int i = 1; i < x.length; i++) {
                Double dot = getDotProduct(axisN[0], axisN[1], x[i], y[i]);
                p1min = Math.min(p1min, dot);
                p1max = Math.max(p1max, dot);
            }
//            System.out.println("***********Projection " + projectionNumber + " min: " + p1min + " max: " + p1max);
//            System.out.println("***********Projection " + projectionNumber + " length: " + (p1max - p1min));
            projections.add(new Double[]{p1min, p1max});
            projectionNumber++;
        }
        return projections;
    }

    // get dot product :-}
    public Double getDotProduct(Double axisX, Double axisY, Double shapeX, Double shapeY) {
        return (axisX * shapeX) + (axisY * shapeY);

    }

    // Take in array of vertex x and y values for a shape, return list of axis vectors of the normal to that face
    public List<Double[]> getAxisList(Double[] xVals, Double[] yVals) {
        List<Double[]> axisList = new ArrayList<>();
        Double[] axis = new Double[2];

        //for each face, get the axis perpendicular to it
        for (int i = 0; i < xVals.length; i++) {
            if (i == 0) {
                axis[0] = -(yVals[i] - yVals[yVals.length - 1]);
                axis[1] = (xVals[i] - xVals[xVals.length - 1]);
//delete the if block if it dont work, just a test
                if (axis[0] < 0 || (axis[0] == 0 && axis[1] < 0)) {
                    axis[0] *= -1;
                    axis[1] *= -1;
                }
            } else {
                    axis[0] = -(yVals[i] - yVals[i - 1]);
                    axis[1] = (xVals[i] - xVals[i - 1]);
//added , delete if needed the whole if block
                    if (axis[0] < 0 || (axis[0] == 0 && axis[1] < 0)) {
                        axis[0] *= -1;
                        axis[1] *= -1;
                    }
                }

            Double mag = Math.sqrt(Math.pow(axis[0], 2) + Math.pow(axis[1], 2));
//            System.out.println(mag);

            Double[] axisN = new Double[2];

            // normalize the axis vector
            axisN[0] = axis[0] * (1.0 / mag);
            axisN[1] = axis[1] * (1.0 / mag);

            double magN = Math.sqrt(Math.pow(axisN[0], 2) + Math.pow(axisN[1], 2));
//            System.out.println("normalized axis magnitude: " + magN);
//            System.out.println("should see this 8 times: \n normalized axis coords: x: " + axisN[0] + " y: " + axisN[1]);

            axisList.add(axisN);
        }

        int index = 0;
        for (Double[] item : axisList) {
//            System.out.println("axis " + index + " x: " + item[0] + " axis 0 y: " + item[1] );
            index++;
        }
        return axisList;
    }
}
