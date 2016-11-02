package controller;

/**
 * Created by Julio Savigny on 11/1/2016.
 */
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.stage.Stage;
import model.Context;
import model.DVaRPCClient;
import model.User;
import org.json.simple.JSONObject;

public class AuthController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Label duplicateUsernameLabel;

    @FXML
    private Label wrongAuthLabel;

    @FXML
    private Label registerSuccessLabel;

    public void startTask(Event e, String type)
    {
        // Create a Runnable
        Runnable task = null;
        if (type == "register") {
            task = () -> runRegisterTask();
        } else if (type == "login") {
            task = () -> runLoginTask(e);
        }

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }
    public void runLoginTask(Event e)
    {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");
            String username = usernameField.getText();
            JSONObject registerRequest = new JSONObject();
            registerRequest.put("method", "login");
            registerRequest.put("username", username);
            registerRequest.put("password", passwordField.getText());

            JSONObject response = client.call(registerRequest);
            if(response.get("status") == "wrong"){
                Platform.runLater(() -> {
                    wrongAuthLabel.setVisible(true);
                    duplicateUsernameLabel.setVisible(false);
                    registerSuccessLabel.setVisible(false);
                });
            } else {
                // New FXML
                Platform.runLater(() -> {
                    Context.getInstance().currentUser().setUsername(username);
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("../view/lobby.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setTitle("Lobby");
                        stage.setScene(scene);
                        stage.show();
                        ((Node)(e.getSource())).getScene().getWindow().hide();
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                });
            }

            client.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public void runRegisterTask()
    {
        try {
            DVaRPCClient client = new DVaRPCClient("rpc_queue");

            JSONObject registerRequest = new JSONObject();
            registerRequest.put("method","register");
            registerRequest.put("username", usernameField.getText());
            registerRequest.put("password", passwordField.getText());

            JSONObject response = client.call(registerRequest);
            if(response.get("status") == "duplicate"){
                // Update the Label on the JavaFx Application Thread
                Platform.runLater(() -> {
                    wrongAuthLabel.setVisible(false);
                    duplicateUsernameLabel.setVisible(true);
                    registerSuccessLabel.setVisible(false);
                });
            } else {
                Platform.runLater(() -> {
                    duplicateUsernameLabel.setVisible(false);
                    wrongAuthLabel.setVisible(false);
                    registerSuccessLabel.setVisible(true);
                });
            }
            client.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        duplicateUsernameLabel.setVisible(false);
        wrongAuthLabel.setVisible(false);
        registerSuccessLabel.setVisible(false);

        registerButton.setOnAction(event -> {
            startTask(event, "register");

        });
        loginButton.setOnAction(event -> {
            startTask(event, "login");

        });
    }

}

