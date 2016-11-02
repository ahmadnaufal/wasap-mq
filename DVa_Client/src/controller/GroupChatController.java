package controller;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.ChatReceiver;
import model.ChatSender;
import model.Context;
import model.DVaRPCClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class GroupChatController implements Initializable {

    @FXML
    private TextArea chatStreamArea;

    @FXML
    private TextArea chatInputArea;

    @FXML
    private Button enterChatButton;

    @FXML
    private ListView<String> memberList;

    @FXML
    private Button addMemberButton;

    @FXML
    private ChoiceBox<String> friendChoiceBox;

    private ObservableList<String> oMemberList;

    private ObservableList<String> oFriendList;

    private String groupName;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public void startRPCTask(String type)
    {
        // Create a Runnable
        Runnable task = null;
        if (type == "get_members") {
            task = () -> runGetMemberTask();
        } else if (type == "add_member"){
            task = () -> runAddMemberTask();
        }

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runGetMemberTask() {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");

            JSONObject request = new JSONObject();
            request.put("method","get_members");
            request.put("username", Context.getInstance().currentUser().getUsername());

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    ArrayList<String> responseMemberList = (JSONArray) response.get("members");
                    oMemberList =  FXCollections.observableArrayList(responseMemberList);
                });
            } else {
                Platform.runLater(() -> {
                });
            }
            client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void runAddMemberTask(){
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");
            String memberName = friendChoiceBox.getSelectionModel().getSelectedItem();

            JSONObject request = new JSONObject();
            request.put("method","add_member");
            request.put("username", Context.getInstance().currentUser().getUsername());
            request.put("groupname", groupName);
            request.put("member", memberName);

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    oMemberList.add(memberName);
                });
            } else {
                Platform.runLater(() -> {
                });
            }
            client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        oMemberList = FXCollections.observableArrayList();
        if (Context.getInstance().currentUser().getFriendList() == null){
            ArrayList<String> tempList = new ArrayList<String>();
            tempList.add("Teman 1");
            tempList.add("Teman 2");
            Context.getInstance().currentUser().setFriendList(tempList);
        }
        if (oMemberList.isEmpty()){
            oMemberList.add("A");
            oMemberList.add("B");
        }
        oFriendList = FXCollections.observableArrayList(Context.getInstance().currentUser().getFriendList());
        memberList.setItems(oMemberList);
        friendChoiceBox.setItems(oFriendList);

        if (groupName == null){
            groupName = "Dummy";
        }

        ChatReceiver receiver = new ChatReceiver("localhost", "direct", "group."+groupName, "message_exchange");
        ChatSender sender = new ChatSender("localhost", "direct", "group."+groupName, "message_exchange");
        ArrayList<ChatSender> memberNotifSender = new ArrayList<>();
        for (String member : oMemberList){
            ChatSender notifSender = new ChatSender("localhost", "topic", "notif.group."+groupName+"."+member, "notification_topic_exchange");
            memberNotifSender.add(notifSender);
        }

        startRPCTask("get_members");

        startReceiveTask(receiver);

        enterChatButton.setOnAction(event -> {
            try {
                sender.send(Context.getInstance().currentUser().getUsername()+" : "+chatInputArea.getText());
                for (ChatSender notifSender : memberNotifSender){
                    notifSender.send("New Message in Group : "+groupName);
                }
                chatInputArea.clear();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        addMemberButton.setOnAction(event -> {

        });
    }

}
