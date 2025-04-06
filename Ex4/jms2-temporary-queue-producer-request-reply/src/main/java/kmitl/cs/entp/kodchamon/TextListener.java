package kmitl.cs.entp.kodchamon;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TextListener implements MessageListener {
    TextMessage msg = null;
    @Override
    public void onMessage(Message message){

        try{
            if(message instanceof TextMessage){
                msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText() + " " + msg.getJMSCorrelationID());
            } else {
                System.out.println("Message is not a TextMessage");
            }
        } catch (JMSException e){
            System.out.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t){
            System.out.println("Exception in on Message(): " + t.getMessage());
        }
    }
}
