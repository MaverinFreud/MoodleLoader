package moodleloader;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
//Autor Marcel: Ich bin nicht verantwortlich für eventuelle Schäden oder nachteile die aus der Nutzung dieses Programms resultieren

public class MoodleLoader extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(new Group(), 450, 250);
        primaryStage.setTitle("Download your stuff");

        TextField url = new TextField();
        TextField username = new TextField();
        TextField password = new TextField();
        TextField foldername = new TextField();
        TextField moodleUrl = new TextField();
        url.setText("URL to the moodle course you want to download");

        url.clear();

        Button btn = new Button();
        btn.setText("Start download");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                HttpClient httpClient = HttpClientBuilder.create().build();

                List<String> nameList = new ArrayList<String>();
                List<String> resourceList = new ArrayList<String>();
                String html = null;

                System.out.println(url.textProperty().getValue().toString());
                try {
                    html = Connection.loginAndGetHTML(httpClient, username.textProperty().getValue().toString(), password.textProperty().getValue().toString(), url.textProperty().getValue().toString(), moodleUrl.textProperty().getValue());
                } catch (Exception ex) {
                    Logger.getLogger(MoodleLoader.class.getName()).log(Level.SEVERE, null, ex);
                }
                HTMLParser.parse(html, resourceList, nameList);
                Downloader.download(httpClient, resourceList, nameList, foldername.textProperty().getValue().toString());

                System.exit(0);
            }
        });

        GridPane grid = new GridPane();
        grid.setVgap(4);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(btn, 0, 5);
        grid.add(new Label("URL to the moodle course you want to download"), 0, 0);
        grid.add(url, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(username, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(password, 1, 2);
        grid.add(new Label("foldername:"), 0, 3);
        grid.add(foldername, 1, 3);
        grid.add(new Label("Your moodle url, as Example: www.moodle.your-university.com \nor just copy it witouth beeing logged in:"), 0, 4);
        grid.add(moodleUrl, 1, 4);

        Group root = (Group) scene.getRoot();
        root.getChildren().add(grid);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
