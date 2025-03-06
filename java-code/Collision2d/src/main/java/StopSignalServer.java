import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.io.OutputStream;

public class StopSignalServer implements Runnable {
    public void run() {
        try {
            // Create an HTTP server on port 8080
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            // Define a single context for the /stop endpoint
            server.createContext("/stop", exchange -> {
                String response = "running";
                if (CollisionChecker.isJam) {
                    response = "stop";
                    System.out.println("STOP signal sent!");
                } else {
                    response = "running";
                }

                exchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            // Start the server
            server.setExecutor(null); // Use the default executor
            server.start();
            System.out.println("Server is running on http://localhost:8080");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
