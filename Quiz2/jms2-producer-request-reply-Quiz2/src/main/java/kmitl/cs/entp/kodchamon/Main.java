package kmitl.cs.entp.kodchamon;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        Queue requestQueue;
        Queue replyQueue;

        try {
            initialContext = new InitialContext();
            requestQueue = (Queue) initialContext.lookup("RequestQueue");
            replyQueue = (Queue) initialContext.lookup("ReplyQueue");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
             JMSContext jmsContext = connectionFactory.createContext()) {

            JMSProducer producer = jmsContext.createProducer();
            JMSConsumer responseConsumer = jmsContext.createConsumer(replyQueue);

            Student[] students = {
                    new Student(12345, "Donal Trump", 2.5),
                    new Student(12346, "Alisa Manobal", 3.5),
                    new Student(12347, "Mo Salah", 3.6),
                    new Student(12348, "Lisa Blackpink", 2.9),
                    new Student(12349, "Elon Musk", 3.8),
                    new Student(12350, "Mark Zuckerberg", 2.7),
                    new Student(12351, "Bill Gates", 3.9)
            };

            Map<String, Student> studentMap = new HashMap<>();

            for (Student student : students) {
                ObjectMessage message = jmsContext.createObjectMessage(student);
                message.setJMSReplyTo(replyQueue);
                message.setJMSCorrelationID(String.valueOf(student.getId()));

                System.out.println("Sending student id: " + student.getId());
                producer.send(requestQueue, message);

                studentMap.put(String.valueOf(student.getId()), student);
            }

            responseConsumer.setMessageListener(message -> {
                try {
                    if (message instanceof MapMessage) {
                        MapMessage mapMessage = (MapMessage) message;
                        boolean isQualified = mapMessage.getBoolean("qualified");
                        String studentId = message.getJMSCorrelationID();

                        Student student = studentMap.get(studentId);
                        if (student != null) {
                            String result = student.getId() + " " + student.getName() +
                                    (isQualified ? " will get the scholarship" : " will not get the scholarship");
                            System.out.println(result);
                        }
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            // กด 'q' เพื่อปิดโปรแกรม
            Scanner inp = new Scanner(System.in);
            while (true) {
                System.out.println("Press q to quit ");
                String ch = inp.nextLine();
                if (ch.equals("q")) {
                    break;
                }
            }

        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
