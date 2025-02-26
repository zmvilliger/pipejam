import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class SocketServer {


    static String jsonLine;
    private BoxData boxData;

    public SocketServer(BoxData boxData) {
        this.boxData = boxData;
    }

    public void runServer() {

        try (ServerSocket serverSocket = new ServerSocket(7474)) {
            System.out.println("Server listening on port 7474");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Connected with client!");
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    StringBuilder jsonBuilder = new StringBuilder();

                    while((jsonLine = in.readLine()) != null) {
//                        System.out.println("received data: " + jsonLine);
                        boxData = new BoxData();
                        boxData.printCoords();


                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
