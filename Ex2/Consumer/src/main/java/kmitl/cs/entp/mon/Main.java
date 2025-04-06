package kmitl.cs.entp.mon;

import javax.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String brokerURL = "tcp://localhost:61616"; // Artemis broker URL
        Connection connection = null;
        Destination destination = null;

        try {
            // สร้าง ConnectionFactory และเชื่อมต่อกับ broker
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection();
            connection.start();

            // สร้าง Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // สร้าง Queue Destination
            destination = session.createQueue("FootballUpdates");

            // สร้าง MessageConsumer
            MessageConsumer consumer = session.createConsumer(destination);

            // ตั้งค่า MessageListener
            consumer.setMessageListener(new TextListener());

            System.out.println("Waiting for messages...");

            // รอให้ผู้ใช้พิมพ์ 'q' หรือ 'Q' เพื่อหยุดโปรแกรม
            System.out.println("Type 'q' or 'Q' to quit.");
            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("q")) {
                    break; // จบการทำงานเมื่อผู้ใช้ป้อน 'q' หรือ 'Q'
                }
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
