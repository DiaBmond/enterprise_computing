package Ex1_2;

import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int serverPort = 12345;

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            // Send an empty message to the server
            byte[] sendBuffer = new byte[1];
            InetAddress serverAddress = InetAddress.getByName(serverHost);
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, serverPort);
            clientSocket.send(sendPacket);

            // Receive the server's response
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            // Print the response
            String serverTime = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server's date and time: " + serverTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
