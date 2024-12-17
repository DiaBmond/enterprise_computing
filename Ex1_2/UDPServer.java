package Ex1_2;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPServer {
    public static void main(String[] args) {
        int port = 1668;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("UDP Server is running at port " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket);

                InetAddress clientAddress = requestPacket.getAddress();
                int clientPort = requestPacket.getPort();

                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                byte[] responseBytes = currentTime.getBytes();

                DatagramPacket responsePacket = new DatagramPacket(responseBytes, responseBytes.length, clientAddress,
                        clientPort);
                socket.send(responsePacket);

                System.out.println("Sent current time to client: " + currentTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
