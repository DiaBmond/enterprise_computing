package kmitl.cs.entp.mon;

import javax.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String brokerURL = "tcp://localhost:61616";
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        Destination destination = null;

        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            connection = factory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("FootballUpdates");

            producer = session.createProducer(destination);

            while (true) {
                System.out.print("Enter football Live scores (or press Enter to exit): ");
                String score = scanner.nextLine();
                if (score.isEmpty()) {
                    break;
                }

                TextMessage message = session.createTextMessage(score);
                producer.send(message);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            try {
                if (producer != null) producer.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
