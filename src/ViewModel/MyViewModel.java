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
    private int isValid;
    private int row;
    private int col;
    private int rowPlayer;
    private int colPlayer;
    private Solution solution;

    public int getRowPlayer() {
        return rowPlayer;
    }

    public int getColPlayer() {
        return colPlayer;
    }

    public File loadFile;

    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
    }

    public Maze getMaze() {
        return this.maze;
    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    public void solveMaze() {
        model.solveMaze();
    }

    public void movePlayer(KeyCode move) {
        this.model.movePlayer(move);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void reset() {
        model.reset();
    }

    public Solution getSol() {
        return solution;
    }

    public int getIsValid() {
        return isValid;
    }

    @Override
    public void update(Observable o, Object arg) {
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

    public void saveSettings() {
        model.saveSettings();
    }

    public void exit() throws InterruptedException {
        model.exit();
    }

    public void load() {
        model.setLoadFile(loadFile);
        model.load();
    }

    public void setLoadFile(File loadFile) {
        this.loadFile = loadFile;
    }
}