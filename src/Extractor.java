import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Created by Tyler Wilding on 01/06/16.
 * TA-Code-Reviewer - COSC
 */
public class Extractor extends Application {

    String folderPath = "";

    Label assignmentPath = new Label();
    Label assignmentName = new Label();
    Hyperlink assignmentLink = new Hyperlink();


    @Override
    public void start(Stage stage) {

        Button chooseFile = new Button("Select Zip File...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Zip File Containing Assignments");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Zip Files", "*.zip"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));


        VBox info = new VBox(50);
        info.setPadding(new Insets(20));
        info.setAlignment(Pos.CENTER);
        info.getChildren().addAll(assignmentPath, assignmentName, assignmentLink);

        chooseFile.setOnAction( e -> getFileContents(fileChooser, stage));
        assignmentLink.setOnAction( e -> getHostServices().showDocument(assignmentLink.getText()));


        HBox controls = new HBox(50);
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(chooseFile);


        BorderPane layout = new BorderPane();
        layout.setCenter(info);
        layout.setBottom(controls);

        Scene scene = new Scene(layout, 1000, 500);
        stage.setTitle("TA Code Reviewer - Extractor");
        stage.setScene(scene);
        stage.show();
    }

    public void getFileContents(FileChooser fileChooser, Stage stage) {

        File selectedFile = fileChooser.showOpenDialog(stage);
        String[] path = selectedFile.getAbsolutePath().split(Pattern.quote("/"));

        String newFolder = "/";
        for(int i = 0; i < path.length-1; i++) {

            newFolder += path[i]+"/";
        }

        String[] assignmentInfo = path[path.length-1].split("-");
        String folderName = assignmentInfo[0] + "_" + assignmentInfo[1];

        newFolder += folderName;

        File dir = new File(newFolder);
        boolean folderCreated = dir.mkdir();
        if(folderCreated)
            System.out.println("Folder Successfully Created");
        else
            System.out.println("Folder already exists.");


        try {
            ZipFile mainZip = new ZipFile(selectedFile);
            mainZip.extractAll(newFolder+"/");
        }
        catch(ZipException e) {
            e.printStackTrace();
        }

        folderPath = newFolder;
        assignmentPath.setText("Working With: "+selectedFile.getAbsolutePath());
        assignmentName.setText(folderName);
        assignmentLink.setText("https://courses.algomau.ca/moodle/mod/assign/view.php?id="+assignmentInfo[2]);

    }

    public static void main(String[] args) {

        launch(args);
    }


}
