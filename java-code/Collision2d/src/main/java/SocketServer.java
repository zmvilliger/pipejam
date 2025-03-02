
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer implements Runnable {

    // Creates a ServerSocket on port 7474, then calls connectClient()
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(7474)) {
            System.out.println("Server listening on port 7474");
            connectClient(serverSocket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // Tries to connect client and then calls handleMessages() to handle incoming json data
    private void connectClient(ServerSocket serverSocket) {
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                System.out.println("Connected with client!");
                handleMessages(socket);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Receives json messages with bounding box data, updates static jsonString variable
    private void handleMessages(Socket socket) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while ((CollisionChecker.jsonString = in.readLine()) != null) {
                synchronized (CollisionChecker.lock) {
                    CollisionChecker.lock.notify();
                    CollisionChecker.lock.wait();
                }
            }
        } catch (Exception e) {
            CollisionChecker.lock.notify();
        }
    }
}
