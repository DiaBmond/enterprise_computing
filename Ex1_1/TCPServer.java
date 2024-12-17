package Ex1_1;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 1667; // Server port
        System.out.println("Server waiting for client connection at port number " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Handle client connection in a new thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
            while (true) {
                // Prompt for the first number
                output.println("enter number 1 (to end just press enter): ");
                String number1 = input.readLine();

                // If the client presses enter without entering a number
                if (number1 == null || number1.trim().isEmpty()) {
                    output.println("Connection closed.");
                    break;
                }

                // Prompt for the second number
                output.println("enter number 2 (to end just press enter): ");
                String number2 = input.readLine();

                // If the client presses enter without entering a number
                if (number2 == null || number2.trim().isEmpty()) {
                    output.println("Connection closed.");
                    break;
                }

                // Convert numbers and calculate the sum
                try {
                    int num1 = Integer.parseInt(number1.trim());
                    int num2 = Integer.parseInt(number2.trim());
                    int sum = num1 + num2;

                    // Send result back to the client
                    output.println("The result is " + sum);
                } catch (NumberFormatException e) {
                    output.println("Invalid input. Please enter valid numbers.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client disconnected: " + clientSocket);
        }
    }
}
