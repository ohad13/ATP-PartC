package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze maze;
    private Solution solution;
    private int isValid;
    private int row;
    private int col;
    private int rowPlayer;
    private int colPlayer;
    public File loadFile;

    /**
     * constructor
     * @param model - model that will be our observable.
     */
    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }

    /**
     * tell the model to generate new maze with this row/col
     * @param row - row
     * @param col - column
     */
    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    /**
     * tell the model to solve the maze.
     */
    public void solveMaze() {
        model.solveMaze();
    }

    /**
     * tell the model to move the player.
     * @param move - the direction to move to.
     */
    public void movePlayer(KeyCode move) {
        this.model.movePlayer(move);
    }

    /**
     * tell the model to reset the game.
     */
    public void reset() {
        model.reset();
    }

    /**
     * tell the model to save the new properties settings.
     * @param gen - maze generator
     * @param ser - maze searcher
     * @param nThreads - number of threads to use in the servers.
     */
    public void saveSettings(String gen, String ser, int nThreads) {
        model.saveSettings(gen,ser,nThreads);
    }

    public void exit() throws InterruptedException {
        model.exit();
    }

    public void load() {
        model.setLoadFile(loadFile);
        model.load();
    }

    @Override
    public void update(Observable o, Object arg) {
        // after being notify, act as needed:
        if (o == model) {
            if (arg.equals("move")) {
                row = model.getRowPlayer();
                col = model.getColPlayer();
                isValid = model.getIsValid();
                setChanged();
                notifyObservers("move");
            }
            if (arg.equals("generate")) {
                maze = model.getMaze();
                setChanged();
                notifyObservers("generate");
            }
            if (arg.equals("solve")) {
                setChanged();
                notifyObservers("solve");
            }
            if (arg.equals("reset")) {
                row = model.getRowPlayer();
                col = model.getColPlayer();
                setChanged();
                notifyObservers("reset");
            }
            if (arg.equals("getSolve")) {
                this.solution = model.getSolve();
                setChanged();
                notifyObservers("getSolve");
            }
            if (arg.equals("load")) {
                rowPlayer = model.getRowPlayer();
                colPlayer = model.getColPlayer();
                maze = model.getMaze();
                setChanged();
                notifyObservers("load");
            }
        }
    }

    // -------------- getters and setters -----------------------
    public Maze getMaze() {
        return this.maze;
    }

    public Solution getSol() {
        return solution;
    }

    public int getRowPlayer() {
        return rowPlayer;
    }

    public int getColPlayer() {
        return colPlayer;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setLoadFile(File loadFile) {
        this.loadFile = loadFile;
    }
}