package controller;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.ChatReceiver;
import model.ChatSender;

import java.net.URL;
import java.util.ResourceBundle;


public class ChatController implements Initializable {

    @FXML
    private TextArea chatStreamArea;

    @FXML
    private TextArea chatInputArea;

    @FXML
    private Button enterChatButton;

    @FXML
    private Label chatName;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChatReceiver receiver = new ChatReceiver("localhost", "direct", "usernameOrangLain / Group", "message_exchange");
        ChatSender sender = new ChatSender("localhost", "direct", "usernameOrangLain / Group", "message_exchange");
    }

}
