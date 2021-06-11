package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private int row_player;
    private int col_player;

    /**
     * Constructor
     */
    public MazeDisplayer() {
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    /**
     * This function draw all the maze, 0 as roads and 1 as walls.
     */
    private void draw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double x, y, x_player, y_player;
            int rows = maze.getNumOfRow();
            int cols = maze.getNumOfCol();
            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            Image wallImage = null;
            Image goalImage = null;
            Image playerImageR = null;
            //Image playerImageL = null;
            try {
                wallImage = new Image(new FileInputStream("./src/Resources/Image/coronaWall2.png"));
                playerImageR = new Image(new FileInputStream("./src/Resources/Image/coronaPlayer1R.png"));
                //playerImageL = new Image(new FileInputStream("./src/Resources/Image/coronaPlayer1L.png"));
                goalImage = new Image(new FileInputStream("./src/Resources/Image/coronaGoal1.png"));
            } catch (FileNotFoundException e) {
                System.out.println("no image..");
            }
            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.WHITE);
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
                    } else {
                        x = j * cellWidth;
                        y = i * cellHeight;
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    }
                }
            }

            x_player = getRow_player() * cellHeight;
            y_player = getCol_player() * cellWidth;
            graphicsContext.drawImage(playerImageR, y_player, x_player, cellWidth, cellHeight);
            int a = maze.getGoalPosition().getColumnIndex();
            int b = maze.getGoalPosition().getRowIndex();
            graphicsContext.drawImage(goalImage, a * cellWidth, b * cellHeight, cellWidth, cellHeight);
        }
    }

    /**
     * draw the player in his new position.
     * clean his prev pos and draw at the new one.
     *
     * @param oldRow - the previous row pos of the player
     * @param oldCol - the previous column pos of the player
     */
    public void drawPlayer(double oldRow, double oldCol) {
        double x_player, y_player, x, y;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();

        double cellHeight = canvasHeight / maze.getNumOfRow();
        double cellWidth = canvasWidth / maze.getNumOfCol();

        // clean the player last step
        GraphicsContext graphicsContext = getGraphicsContext2D();
        x = oldCol * cellWidth;
        y = oldRow * cellHeight;
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(x, y, cellWidth, cellHeight);

        //draw the new player step
        Image playerImageR = null;
        x_player = getRow_player() * cellHeight;
        y_player = getCol_player() * cellWidth;
        try {
            playerImageR = new Image(new FileInputStream("./src/Resources/Image/coronaPlayer1R.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        graphicsContext.drawImage(playerImageR, y_player, x_player, cellWidth, cellHeight);
    }

    /**
     * draw the player after loading an existing game.
     * no need to clean his last position so there is no old row/col
     */
    public void drawPlayer() {
        double x_player, y_player;//,x,y;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / maze.getNumOfRow();
        double cellWidth = canvasWidth / maze.getNumOfCol();

        // clean the player last step
        GraphicsContext graphicsContext = getGraphicsContext2D();

        //draw the new player step
        Image playerImageR = null;
        x_player = getRow_player() * cellHeight;
        y_player = getCol_player() * cellWidth;
        try {
            playerImageR = new Image(new FileInputStream("./src/Resources/Image/coronaPlayer1R.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        graphicsContext.drawImage(playerImageR, y_player, x_player, cellWidth, cellHeight);
    }

    /**
     * get a maze, save it to the class and than call to the draw function.
     * @param maze - the maze we want to draw.
     */
    public void drawMaze(Maze maze) {
        this.maze = maze;
        row_player = maze.getStartPosition().getRowIndex();
        col_player = maze.getStartPosition().getColumnIndex();
        draw();
    }

    /**
     * This function gets a solution and draw it on the canvas so the client will be able to see and use it.
     * @param solution - the solution for the specific maze.
     */
    public void drawSol(ArrayList<AState> solution) throws FileNotFoundException {
        Image maskImage = new Image(new FileInputStream("./src/Resources/Image/mask.png"));
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int rows = maze.getNumOfRow();
        int cols = maze.getNumOfCol();
        double cellHeight = canvasHeight / rows;
        double cellWidth = canvasWidth / cols;
        double x, y;

        GraphicsContext graphicsContext = getGraphicsContext2D();
        //clear the canvas:
        graphicsContext.setFill(Color.BLUE);

        for (AState aState : solution) {
            x = ((MazeState) aState).getPos().getColumnIndex() * cellWidth;
            y = ((MazeState) aState).getPos().getRowIndex() * cellHeight;
            graphicsContext.drawImage(maskImage, x, y, cellWidth / 2, cellHeight / 2);
        }
    }

    /**
     * clean the canvas - erase all image or something on the canvas.
     */
    public void cleanCanvas() {
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
    //-------------- getters and setters---------------
    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void setPlayerPos(int row, int col) {
        double x = row_player;
        double y = col_player;
        row_player = row;
        col_player = col;
        drawPlayer(x, y);
    }
}