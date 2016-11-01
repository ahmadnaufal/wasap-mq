package controller;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LobbyController implements Initializable {

    @FXML
    private Tab groupsTab;

    @FXML
    private Button createGroupButton;

    @FXML
    private Button leftGroupButton;

    @FXML
    private Button chatGroupButton;

    @FXML
    private Tab friendsTab;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button chatFriendButton;

    private ArrayList<String> groupList; //Diisi sama group punya user ini (RPC)
    private ArrayList<String> friendList; //Diisi dengan friend user ini (RPC)
    private ObservableList<String> oGroupList;
    private ObservableList<String> oFriendList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Handle Create group, add friend, left group (RPC)
        // Handle notifikasi chat dan pemberitahuan penambahan teman?? << gimana caranya?

        // Bikin windows baru jika memencet button chat, kirim parameter username/nama group,
        // biar jadi parameter untuk chatreceiver dan chatsender di windows selanjutnya (ChatController.java)

    }

}
