import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import streamviewer.DVStreamViewer;

import java.io.File;

public class VideoController extends Application {


    private GridPane controllerkeyPad;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        this.DVStream = DVStreamViewer.INSTANCE;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initVideoControllerScene();
        this.primaryStage.setScene(controllerScene);
        this.primaryStage.setTitle("Data Viewer Controller");
        this.primaryStage.setResizable(false);
        this.primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        this.DVStream.close();
    }

    private void initVideoControllerScene() {
        primaryStage.setOnCloseRequest(event -> Platform.exit());

        this.controllerkeyPad = new GridPane();
        controllerkeyPadInit();

        //TODO: start with a simple slider just to control the stream
//        mixerContoller = new VBox();
//        mixerContollerInit();

//        HBox videoControllerVbox = new HBox(controllerkeyPad, mixerContoller);
        HBox videoControllerVbox = new HBox(controllerkeyPad);

        this.controllerScene = new Scene(videoControllerVbox);

        this.controllerScene.getStylesheets().add("DatavyuView.css");
    }

    private void controllerkeyPadInit() {

        this.addVideoButton = new Button("Add Video");// Add media Button

        this.mediaTime = new Label("00:00:00:000");
        this.mediaTime.setId("mediatime-label");

        this.playButton = new Button();// Play Media button
        this.playButton.setId("play-button");

        this.stopButton = new Button();// Stop Media Button
        this.stopButton.setId("stop-button");

        this.pauseButton = new Button();// Pause Media Button
        this.pauseButton.setId("pause-button");

        this.fButton = new Button();// Shuttle Forward Button
        this.fButton.setId("shuttle-f-button");

        this.bButton = new Button();// Shuttle backward Button
        this.bButton.setId("shuttle-b-button");

        this.jogFButton = new Button();// Jog Forward Button
        this.jogFButton.setId("jog-f-button");

        this.jogBButton = new Button();// Jog Backward Button
        this.jogBButton.setId("jog-b-button");

        this.onsetButton = new Button();// Set Cell onset Button
        this.onsetButton.setId("onset-button");

        this.offsetButton = new Button();// Set Cell offset Button
        this.offsetButton.setId("offset-button");

        this.pointCellbutton = new Button();// Point a Cell button
        this.pointCellbutton.setId("point-cell-button");

        this.hideTrack = new Button();// Hide Track Button
        this.hideTrack.setId("hidetrack-button");

        this.backButton = new Button();// Back Button
        this.backButton.setId("back-button");

        this.findButton = new Button(); // Find Button
        this.findButton.setId("find-button");

        this.newCellButton = new Button(); // New Cell Button
        this.newCellButton.setId("newcell-button");

        this.newCellPrevOffsetbutton = new Button();// New Cell Button with previous offset
        this.newCellPrevOffsetbutton.setId("newcell-prevoffset-button");

        this.jumpBackLabel = new Label("Jump Back By");
        this.jumpBackText = new TextField("00:00:05:000");//Force the format

        this.jumpBackBox = new VBox(jumpBackLabel, jumpBackText);
        this.jumpBackBox.getStyleClass().add("vbox");

        this.stepPerSecondLabel = new Label("Steps Per Second");
        this.stepPerSecondText = new TextField();//Force the format

        this.stepPerSecondBox = new VBox(stepPerSecondLabel, stepPerSecondText);
        this.stepPerSecondBox.getStyleClass().add("vbox");

        this.onsetLabel = new Label("onset");
        this.onsetText = new TextField("00:00:00:000");//Force the format

        this.onsetBox = new VBox(onsetLabel, onsetText);
        this.onsetBox.getStyleClass().add("vbox");

        this.offsetLabel = new Label("onset");
        this.offsetText = new TextField("00:00:00:000");//Force the format

        this.offsetBox = new VBox(offsetLabel, offsetText);
        this.offsetBox.getStyleClass().add("vbox");

        //mediaTime.textProperty().bind(); // To be bind to the clock Timer
        setOnActions();

        this.controllerkeyPad.add(mediaTime, 2, 0, 3, 1);
        this.controllerkeyPad.add(addVideoButton, 1, 1,2,1);
        this.controllerkeyPad.add(pointCellbutton, 2, 2);
        this.controllerkeyPad.add(hideTrack, 3, 2);
        this.controllerkeyPad.add(onsetButton, 1, 3);
        this.controllerkeyPad.add(playButton, 2, 3);
        this.controllerkeyPad.add(offsetButton, 3, 3);
        this.controllerkeyPad.add(backButton, 4, 3);
        this.controllerkeyPad.add(jumpBackBox, 5, 3);
        this.controllerkeyPad.add(bButton, 1, 4);
        this.controllerkeyPad.add(stopButton, 2, 4);
        this.controllerkeyPad.add(fButton, 3, 4);
        this.controllerkeyPad.add(findButton, 4, 4);
        this.controllerkeyPad.add(stepPerSecondBox, 5, 4);
        this.controllerkeyPad.add(jogBButton, 1, 5);
        this.controllerkeyPad.add(pauseButton, 2, 5);
        this.controllerkeyPad.add(jogFButton, 3, 5);
        this.controllerkeyPad.add(newCellButton, 4, 5, 1, 3);
        this.controllerkeyPad.add(onsetBox, 5, 5);
        this.controllerkeyPad.add(newCellPrevOffsetbutton, 1, 6, 2, 1);
        this.controllerkeyPad.add(offsetBox, 5, 6);
    }

    private void setOnActions() {
        addVideoButton.setOnAction(event -> this.openStreams());
        playButton.setOnAction(event -> this.DVStream.play());
        pauseButton.setOnAction(event -> this.DVStream.play());
        stopButton.setOnAction(event -> this.DVStream.stop());
        fButton.setOnAction(event -> this.DVStream.shuttleBackward());
        bButton.setOnAction(event -> this.DVStream.shuttleBackward());
        jogFButton.setOnAction(event -> this.DVStream.jogForward());
        jogBButton.setOnAction(event -> this.DVStream.jogBackward());
        backButton.setOnAction(event -> this.DVStream.back(5000));
        onsetButton.setOnAction(event -> {  });
        offsetButton.setOnAction(event -> {  });
        pointCellbutton.setOnAction(event -> {  });
        hideTrack.setOnAction(event -> {  });
        findButton.setOnAction(event -> {  });
        newCellButton.setOnAction(event -> {  });
        newCellPrevOffsetbutton.setOnAction(event -> {  });
    }
    private void openStreams() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open A Video");
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if(selectedFile != null) {

//            this.DVStream.addStream();
        }
    }

    private DVStreamViewer DVStream;

    private Stage primaryStage;
    private Scene controllerScene;

    private Button addVideoButton;
    private Button playButton;
    private Button stopButton;
    private Button pauseButton;
    private Button fButton;
    private Button bButton;
    private Button jogFButton;
    private Button jogBButton;
    private Button onsetButton;
    private Button offsetButton;
    private Button pointCellbutton;
    private Button hideTrack;
    private Button backButton;
    private Button findButton;
    private Button newCellButton;
    private Button newCellPrevOffsetbutton;

    private VBox offsetBox;
    private VBox jumpBackBox;
    private VBox onsetBox;
    private VBox stepPerSecondBox;

    private Label mediaTime;
    private Label jumpBackLabel;
    private Label onsetLabel;
    private Label offsetLabel;
    private Label stepPerSecondLabel;

    private TextField jumpBackText;
    private TextField stepPerSecondText;
    private TextField onsetText;
    private TextField offsetText;

}
