/**
 * Created by Julio Savigny on 11/1/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class Main extends Application
{

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    public void start(Stage stage) throws IOException
    {
        // Create the FXMLLoader
        FXMLLoader loader = new FXMLLoader();

        // Create the Pane and all Details
        Pane root = (Pane) loader.load(Main.class.getResource("view/auth.fxml"));

        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        stage.setScene(scene);
        // Set the Title to the Stage
        stage.setTitle("myWekaExplorer");
        // Display the Stage
        stage.show();

    }
}