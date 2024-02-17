package com.q.life;

import com.q.projects.metamodele.Cell;
import com.q.projects.metamodele.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class GameApplication extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Cell Simulation");

        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);

        primaryStage.setScene(scene);
        primaryStage.show();

        GraphicsContext gc = canvas.getGraphicsContext2D();
        World world = new World(WIDTH, HEIGHT); // Assume you have a world class ready

        // Animation loop
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                // Update the world and cells

                // Clear the canvas
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Draw the world (for example, background)
                gc.setFill(Color.LIGHTGRAY);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Draw cells
                for (Cell cell : world.getCells()) {
                    gc.setFill(Color.BLUE);
                    gc.fillOval(cell.getPosition().getX() - cell.getSize(),
                            cell.getPosition().getY() - cell.getSize(),
                            cell.getSize() * 2, cell.getSize() * 2);
                }
            }
        }.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}