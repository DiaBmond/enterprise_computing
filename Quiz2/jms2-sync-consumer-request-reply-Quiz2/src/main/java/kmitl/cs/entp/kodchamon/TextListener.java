package kmitl.cs.entp.kodchamon;

import javax.jms.*;

public class TextListener implements MessageListener {

    private  JMSContext jmsContext;


    public TextListener(JMSContext jmsContext) {
        this.jmsContext = jmsContext;
    }
    @Override
    public void onMessage(Message message) {
        TextMessage msg = null;
        JMSProducer replyProducer = jmsContext.createProducer();

        try {
            if (message instanceof TextMessage) {
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText());
            } else {
                System.err.println("Message is not a TextMessage");
            }
            // set up the reply message
            TextMessage response = jmsContext.createTextMessage();
            response.setText("Hello back");
            //response.setJMSCorrelationID(message.getJMSCorrelationID());
            response.setJMSCorrelationID(message.getJMSMessageID());
            System.out.println("sending message " + response.getText());
            replyProducer.send(message.getJMSReplyTo(), response);
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.err.println("Exception in onMessage():" + t.getMessage());
        }
    }
}
