package Ex1_1;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1667;

        try (Socket socket = new Socket(host, port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                System.out.println(serverResponse);
                String userInput = consoleInput.readLine();
                out.println(userInput);

                if (userInput == null || userInput.isEmpty()) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
