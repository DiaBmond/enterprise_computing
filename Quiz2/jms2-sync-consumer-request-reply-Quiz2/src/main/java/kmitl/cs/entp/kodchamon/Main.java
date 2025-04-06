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

        try {
            initialContext = new InitialContext();
            requestQueue = (Queue) initialContext.lookup("RequestQueue");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSConsumer consumer = jmsContext.createConsumer(requestQueue);
            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof ObjectMessage) {
                        ObjectMessage objectMessage = (ObjectMessage) message;
                        Student student = (Student) objectMessage.getObject();

                        boolean isQualified = student.getGpa() >= 3.5;

                        MapMessage response = jmsContext.createMapMessage();
                        response.setBoolean("qualified", isQualified);
                        response.setJMSCorrelationID(message.getJMSCorrelationID());

                        Destination replyQueue = message.getJMSReplyTo();
                        JMSProducer replyProducer = jmsContext.createProducer();
                        replyProducer.send(replyQueue, response);

                        System.out.println("Processed student ID: " + student.getId() + " - Sending message: " + isQualified);
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            // กด 'q' เพื่อปิดโปรแกรม
            Scanner inp = new Scanner(System.in);
            while (true) {
                System.out.print("Press q to quit ");
                String ch = inp.nextLine();
                if (ch.equals("q")) {
                    break;
                }
            }
        }
    }
}