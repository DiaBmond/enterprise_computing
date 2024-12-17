package Ex1_1;

import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1667;

        try (Socket socket = new Socket(host, port)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            String serverMessage;

            while (true) {
                // Read message from server
                serverMessage = input.readLine();
                if (serverMessage == null)
                    break;
                System.out.print(serverMessage + " ");

                // Send user input to server
                String userInput = console.readLine();
                output.println(userInput);

                // Read and display the server response
                serverMessage = input.readLine();
                if (serverMessage == null)
                    break;
                System.out.println(serverMessage);

                // If the connection is closed
                if (serverMessage.contains("Connection closed")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
