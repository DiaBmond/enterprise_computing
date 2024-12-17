package Ex1_1;

import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 1667;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Waiting for client connection at port number " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
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
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            while (true) {
                out.println("Enter number 1 (to end just press enter):");
                String input1 = in.readLine();
                if (input1 == null || input1.isEmpty()) {
                    break;
                }

                out.println("Enter number 2 (to end just press enter):");
                String input2 = in.readLine();
                if (input2 == null || input2.isEmpty()) {
                    break;
                }

                try {
                    int num1 = Integer.parseInt(input1);
                    int num2 = Integer.parseInt(input2);
                    int result = num1 + num2;
                    out.println("The result is " + result);
                } catch (NumberFormatException e) {
                    out.println("Invalid input. Please enter valid numbers.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
