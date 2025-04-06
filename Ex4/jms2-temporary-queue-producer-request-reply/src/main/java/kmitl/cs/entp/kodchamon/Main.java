package kmitl.cs.entp.kodchamon;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Queue requestQueue;
        TextListener listener = null;

        try {
            initialContext = new InitialContext();
            requestQueue = (Queue) initialContext.lookup("RequestQueue");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup queues", e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            listener = new TextListener();

            Queue tempDest = jmsContext.createTemporaryQueue();
            JMSConsumer responseConsumer = jmsContext.createConsumer(tempDest);
            responseConsumer.setMessageListener(listener);

            TextMessage message = jmsContext.createTextMessage("Hello friend");
            message.setJMSReplyTo(tempDest);

            String correlationId = "12345";
            message.setJMSCorrelationID(correlationId);

            System.out.println("Sending message: " + message.getText());
            producer.send(requestQueue, message);
            System.out.println("message id:" + message.getJMSCorrelationID());

            Scanner inp = new Scanner(System.in);
            String ch;
            do {
                System.out.print("Press 'q' to quit: ");
                ch = inp.nextLine();
            } while (!ch.equalsIgnoreCase("q"));

        } catch (JMSException e) {
            throw new RuntimeException("JMS Error occurred", e);
        }
    }
}
