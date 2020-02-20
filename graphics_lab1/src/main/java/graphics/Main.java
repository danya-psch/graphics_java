package graphics;

import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.shape.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 400, 250);
        scene.setFill(Color.rgb(138, 3, 0));

        Ellipse ellipse1 = new Ellipse(325, 125, 65, 85);
        root.getChildren().add(ellipse1);
        ellipse1.setFill(Color.rgb(250, 250, 250));

        Ellipse ellipse2 = new Ellipse(325, 125, 60, 80);
        root.getChildren().add(ellipse2);
        ellipse2.setFill(Color.rgb(192, 192, 192));

        Circle circle1 = new Circle(247,125,58);
        root.getChildren().add(circle1);
        Circle circle2 = new Circle(403,125,58);
        root.getChildren().add(circle2);
        circle1.setFill(Color.rgb(138, 3, 0));
        circle2.setFill(Color.rgb(138, 3, 0));

        Rectangle r = new Rectangle(60, 120, 300, 10);
        root.getChildren().add(r);
        r.setFill(Color.rgb(0, 0, 0));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
