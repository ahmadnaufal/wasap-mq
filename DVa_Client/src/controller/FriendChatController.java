package controller;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.ChatReceiver;
import model.ChatSender;
import model.Context;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class FriendChatController implements Initializable {

    @FXML
    private TextArea chatStreamArea;

    @FXML
    private TextArea chatInputArea;

    @FXML
    private Button enterChatButton;

    @FXML
    private Label chatName;

    private String friendName;

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void startReceiveTask(ChatReceiver receiver)
    {
        // Create a Runnable
        Runnable task = () -> runReceiveTask(receiver);


        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }
    public void runReceiveTask(ChatReceiver receiver)
    {
        try {
            Platform.runLater(() -> {
                try {
                    startConsume(receiver);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            });
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void startConsume(ChatReceiver chatReceiver) throws Exception {

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(chatReceiver.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                chatStreamArea.appendText(message+'\n');
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        chatReceiver.getChannel().basicConsume(chatReceiver.getQueueName(), true, consumer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (friendName == null){
            friendName = "Dummy";
        }
        String username = Context.getInstance().currentUser().getUsername();
        ChatReceiver receiver = new ChatReceiver("localhost", "direct", friendName+"."+username, "message_exchange");
        ChatSender sender = new ChatSender("localhost", "direct", username+"."+friendName, "message_exchange");
        ChatSender selfSender = new ChatSender("localhost", "direct", friendName+"."+username, "message_exchange");
        ChatSender notifSender = new ChatSender("localhost", "topic", "notif.friend."+username+"."+friendName, "notification_topic_exchange");

        startReceiveTask(receiver);
        enterChatButton.setOnAction(event -> {
            try {
                sender.send(Context.getInstance().currentUser().getUsername()+" : "+chatInputArea.getText());
                selfSender.send(Context.getInstance().currentUser().getUsername()+" : "+chatInputArea.getText());
                notifSender.send("New Message From : "+Context.getInstance().currentUser().getUsername());
                chatInputArea.clear();
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

}
