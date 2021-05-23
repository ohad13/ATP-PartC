package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private int row_player;
    private int col_player;

    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void setPlayerPos(int row, int col) {
        row_player = row;
        col_player = col;
        draw();
    }


    public void drawMaze(Maze maze) {
        this.maze = maze;
        row_player = maze.getStartPosition().getRowIndex();
        col_player = maze.getStartPosition().getColumnIndex();

        draw();
    }

    private void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double x, y, x_player, y_player;
            int rows = maze.getNumOfRow();
            int cols = maze.getNumOfCol();

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            Image wallImage = null; //TODO: String protpertis
            Image goalImage = null; //TODO: String protpertis
            Image playerImage = null;
            try {
                wallImage = new Image(new FileInputStream("./src/Resources/Image/wall.png"));
                playerImage = new Image(new FileInputStream("./src/Resources/Image/man.png"));
                goalImage = new Image(new FileInputStream("./src/Resources/Image/goal.png"));
            } catch (FileNotFoundException e) {
                System.out.println("no image bitches");
            }
            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.RED);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (maze.getMazeArr()[i][j] == 1) {
                        //if it is a wall:
                        x = j * cellWidth;
                        y = i * cellHeight;
                        if (wallImage == null) {
                            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                        } else {
                            graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                        }
                    }
                }
            }

            x_player = getRow_player() * cellHeight;
            y_player = getCol_player() * cellWidth;
            graphicsContext.drawImage(playerImage, y_player, x_player, cellWidth, cellHeight);
            int a = maze.getGoalPosition().getColumnIndex();
            int b = maze.getGoalPosition().getRowIndex();
            graphicsContext.drawImage(goalImage, a*cellWidth, b*cellHeight, cellWidth, cellHeight);
        }
    }
}