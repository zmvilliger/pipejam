package bbox;

import java.util.ArrayList;
import java.util.List;

public class Box {

    /* How sensitive to collisions the detection is.  Higher = more sensitivity, but too high will lead to
     collision alert when objects don't yet touch.  recommended range: .95-1.0 */
    public static float sensitivity = 1.00f;

    // Bounding box x and y point coordinates
    private final List<Point> points = new ArrayList<>();

    // Hold the normalized axis for each box to be projected onto
    private final List<Point> projectionAxisList = new ArrayList<>();

    // Constructor
    public Box(List<List<Double>> coords) {

        for (List<Double> coord : coords) {
            Point p = new Point(coord.get(0), coord.get(1));
            points.add(p);
        }

        populateAxisList();
    }

    // Populate list of normalized projection axes for this bounding box
    private void populateAxisList() {

        // Values for surface normal vector
        double projectionX;
        double projectionY;
        double magnitude;

        // To get surface normal vectors, swap x and y values of each point, and make one negative
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                projectionX = -(points.get(i).y - points.getLast().y);
                projectionY = points.get(i).x - points.getLast().x;
            } else {
                projectionX = -(points.get(i).y - points.get(i - 1).y);
                projectionY = points.get(i).x - points.get(i - 1).x;
            }

            // get magnitude of surface normal
            magnitude = (Math.sqrt(Math.pow(projectionX, 2) + Math.pow(projectionY, 2)));

            // normalize the axis to 0 - 1.0
            projectionX *= 1.0 / magnitude;
            projectionY *= 1.0 / magnitude;

            projectionAxisList.add(new Point(projectionX, projectionY));
        }
    }

    // Look for collisions between a list of boxes
    public static boolean checkBoxesForCollision(List<Box> boxes) {

        if (boxes.size() < 2) {
            return false;
        }

        /* Check every possible pair of boxes' projections against each other using the projectionAxisList of both
           boxes.  If separation is found between two boxes then they are not colliding.  If separation is not found
           between the projections of two boxes, a collision exists and this function should return true.
         */
        for (int i = 0; i < boxes.size(); i++) {

            // Get the axes from box 1
            Box box1 = boxes.get(i);
            List<Point> firstBoxAxes = boxes.get(i).getProjectionAxisList();

            for (int j = i + 1; j < boxes.size(); j++) {

                // Get the axes for box 2, then combine their axes lists.  They will both be projected onto all axes.
                Box box2 = boxes.get(j);

                List<Point> secondBoxAxes = box2.getProjectionAxisList();
                List<Point> combinedAxesList = new ArrayList<>(firstBoxAxes);

                combinedAxesList.addAll(secondBoxAxes);

                if (isOverlapping(box1, box2, combinedAxesList)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isOverlapping(Box box1, Box box2, List<Point> combinedAxesList) {

        // For each projection axis, project all box 1 points and box 2 points onto axis, and compare min and max
        for (Point axis : combinedAxesList) {
            boolean collisionFound = false;

            double box1min;
            double box1max;

            double box2min;
            double box2max;

            //initialize min projection value for each box
            box1min = getDotProduct(box1.points.getFirst(), axis);
            box1max = box1min;

            box2min = getDotProduct(box2.points.getFirst(), axis);
            box2max = box2min;

            for (Point p : box1.points) {

                // Project the point onto the axis, update min and max values
                Double dot = getDotProduct(p, axis);

                if (dot < box1min) {
                    box1min = dot;
                }

                if (dot > box1max) {
                    box1max = dot;
                }
            }

            for (Point p : box2.points) {

                // Project the point onto the axis, update min and max values
                Double dot = getDotProduct(p, axis);

                if (dot < box2min) {
                    box2min = dot;
                }

                if (dot > box2max) {
                    box2max = dot;
                }
            } //end of projection onto one axis

            // if true (boxes projections' don't overlap), boxes do NOT overlap, return false.
            if (box1min > box2max * sensitivity || box1max * sensitivity < box2min) {

                //return
                return false;
            }
        }
        return true;
    }

    private static Double getDotProduct(Point boxPoint, Point axisPoint) {
        return (boxPoint.x * axisPoint.x) + (boxPoint.y * axisPoint.y);
    }

    public List<Point> getProjectionAxisList() {
        return this.projectionAxisList;
    }
}
