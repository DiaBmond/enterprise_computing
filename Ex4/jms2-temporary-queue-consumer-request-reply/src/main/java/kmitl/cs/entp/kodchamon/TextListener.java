package kmitl.cs.entp.kodchamon;

import javax.jms.*;

public class TextListener implements MessageListener {
    private final JMSContext jmsContext;

    public TextListener(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText());

                if (message.getJMSReplyTo() != null) {
                    TextMessage response = jmsContext.createTextMessage("Hello back");
                    response.setJMSCorrelationID(message.getJMSCorrelationID());

                    System.out.println("Sending reply: " + response.getText());
                    jmsContext.createProducer().send(message.getJMSReplyTo(), response);
                } else {
                    System.out.println("No reply destination found");
                }
            } else {
                System.out.println("Received message is not a TextMessage");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.getMessage());
        }
    }
}
