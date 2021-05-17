package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeDisplayer extends Canvas {
    private Maze maze;

    public void drawMaze(Maze maze) {
        this.maze = maze;
        draw();
    }

    private void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.getNumOfRow();
            int cols = maze.getNumOfCol();

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.RED);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (maze.getMazeArr()[i][j] == 1) {
                        //if it is a wall:
                        double x = j * cellWidth;
                        double y = i * cellHeight;
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    }
                }
            }
        }
    }
}