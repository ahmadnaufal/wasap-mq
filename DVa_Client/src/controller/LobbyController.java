package controller;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.ChatReceiver;
import model.Context;
import model.DVaRPCClient;
import model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    @FXML
    private Tab groupTab;

    @FXML
    private ListView<String> groupList;

    @FXML
    private Button createGroupButton;

    @FXML
    private Button leftGroupButton;

    @FXML
    private Button chatGroupButton;

    @FXML
    private TextField createGroupField;

    @FXML
    private Tab friendTab;

    @FXML
    private ListView<String> friendList;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button chatFriendButton;

    @FXML
    private TextField addFriendField;

    private ObservableList<String> oGroupList;
    private ObservableList<String> oFriendList;

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
                    //receiver.startConsume();
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            });
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void startRPCTask(String type)
    {
        // Create a Runnable
        Runnable task = null;
        if (type == "create_group") {
            task = () -> runCreateGroupTask();
        } else if (type == "left_group") {
            task = () -> runLeftGroupTask();
        } else if (type == "add_friend") {
            task = () -> runAddFriendTask();
        } else if (type == "get_groups") {
            task = () -> runGetGroupsTask();
        } else if (type == "get_friends") {
            task = () -> runGetFriendsTask();
        }

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }

    public void runCreateGroupTask()
    {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");
            String groupName = createGroupField.getText();

            JSONObject request = new JSONObject();
            request.put("method","create_group");
            request.put("username", Context.getInstance().currentUser().getUsername());
            request.put("groupname", groupName);

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    Context.getInstance().currentUser().addGroupList(groupName);
                    refreshGroupList();
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

    public void runLeftGroupTask() {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");
            String groupName = createGroupField.getText();

            JSONObject request = new JSONObject();
            request.put("method","left_group");
            request.put("username", Context.getInstance().currentUser().getUsername());
            request.put("groupname", groupName);

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    Context.getInstance().currentUser().removeGroupList(groupName);
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

    public void runAddFriendTask() {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");
            String friendName = addFriendField.getText();

            JSONObject request = new JSONObject();
            request.put("method","add_friend");
            request.put("username", Context.getInstance().currentUser().getUsername());
            request.put("friend", friendName);

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    Context.getInstance().currentUser().addFriendList(friendName);
                    refreshFriendList();
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

    public void runGetGroupsTask() {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");

            JSONObject request = new JSONObject();
            request.put("method","get_groups");
            request.put("username", Context.getInstance().currentUser().getUsername());

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    JSONArray list = new JSONArray();
                    ArrayList<String> responseGroupList = (JSONArray) response.get("groups");
                    Context.getInstance().currentUser().setGroupList(responseGroupList);
                    refreshGroupList();
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

    public void runGetFriendsTask() {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");

            JSONObject request = new JSONObject();
            request.put("method","get_friends");
            request.put("username", Context.getInstance().currentUser().getUsername());

            JSONObject response = client.call(request);
            if(response.get("status") == "ok"){
                Platform.runLater(() -> {
                    ArrayList<String> responseFriendList = (JSONArray) response.get("friends");
                    Context.getInstance().currentUser().setFriendList(responseFriendList);
                    refreshFriendList();
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

    public void refreshGroupList() {
        oGroupList = FXCollections.observableArrayList(Context.getInstance().currentUser().getGroupList());
    }

    public void refreshFriendList() {
        oFriendList = FXCollections.observableArrayList(Context.getInstance().currentUser().getFriendList());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Handle Create group, add friend, left group (RPC)
        // Handle notifikasi chat dan pemberitahuan penambahan teman?? << gimana caranya?
        // Bikin receiver notifikasi?

//        oFriendList = FXCollections.observableArrayList("cobain","ah");
//        oGroupList =  FXCollections.observableArrayList("group1","group2");

        friendList.setItems(oFriendList);
        groupList.setItems(oGroupList);

        ChatReceiver notifReceiver = new ChatReceiver("localhost", "direct", "notification"+"."+ Context.getInstance().currentUser().getUsername(), "notification_exchange");
        startReceiveTask(notifReceiver);

        startRPCTask("get_groups");
        startRPCTask("get_friends");

        createGroupButton.setOnAction(event -> {
            startRPCTask("create_group");
        });

        leftGroupButton.setOnAction(event -> {
            startRPCTask("left_group");
        });

        addFriendButton.setOnAction(event -> {
            startRPCTask("add_friend");
        });

        chatGroupButton.setOnAction(event -> {
            String groupName = groupList.getSelectionModel().getSelectedItem();
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/groupchatroom.fxml"));
                GroupChatController controller = new GroupChatController();
                controller.setGroupName(groupName);
                loader.setController(controller);
                root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Chat Group : "+groupName);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        chatFriendButton.setOnAction(event -> {
            String friendName = friendList.getSelectionModel().getSelectedItem();
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/chatroom.fxml"));
                FriendChatController controller = new FriendChatController();
                controller.setFriendName(friendName);
                loader.setController(controller);
                root = loader.load();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setTitle("Chat Friend : "+friendName);
                stage.setScene(scene);
                stage.show();

            } catch (Exception e){
                e.printStackTrace();
            }
        });



        // Bikin windows baru jika memencet button chat, kirim parameter username/nama group,
        // biar jadi parameter untuk chatreceiver dan chatsender di windows selanjutnya

    }

}
