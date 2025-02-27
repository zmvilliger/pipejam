import refac.BoxFactory;
import refac.Box;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketServer implements Runnable {


    static String jsonLine;

    BoxFactory boxFactory;

    public SocketServer() {
        this.boxFactory = BoxFactory.getInstance();
    }

    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(7474)) {
            System.out.println("Server listening on port 7474");

            while (true) {
                try (Socket socket = serverSocket.accept()) {

                    System.out.println("Connected with client!");

                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    while((jsonLine = in.readLine()) != null) {

//                        CollisionChecker.jsonString = jsonLine;
//                        try {
//                            Thread.sleep(50);
//                        } catch (InterruptedException e) {
//                            throw new RuntimeException(e);
//                        }

                        List<Box> boxes = boxFactory.createBoxListFromJson(jsonLine);
                        System.out.println(Box.checkBoxesForCollision(boxes));
                    }
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        }
    }


}
