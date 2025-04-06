package kmitl.cs.entp.kodchamon;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        InitialContext initialContext;
        Connection connection = null;
        Queue requestQueue;
        TextListener listener = null;

        try {
            initialContext = new InitialContext();
            requestQueue = (Queue) initialContext.lookup("RequestQueue");

        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext(JMSContext.CLIENT_ACKNOWLEDGE)){

                JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
                listener = new TextListener(jmsContext);
                consumer.setMessageListener(listener);

                Scanner inp = new Scanner(System.in);
                String ch;
                do {
                    System.out.print("Press 'q' to quit: ");
                    ch = inp.nextLine();
                } while (!ch.equalsIgnoreCase("q"));

            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (JMSException ignored) {

                    }
                }
            }
        }
    }