package Ex1_2;

import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1668;

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(host);
            byte[] requestBytes = "Request Date and Time".getBytes();

            DatagramPacket requestPacket = new DatagramPacket(requestBytes, requestBytes.length, serverAddress, port);
            socket.send(requestPacket);

            byte[] responseBuffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length);
            socket.receive(responsePacket);

            String serverResponse = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Server Date and Time: " + serverResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
