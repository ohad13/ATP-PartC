package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    private Maze maze;
    private int isValid;
    private int row;
    private int col;
    private Solution solution;

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

    public void reset(){
        model.reset();
    }

    public Solution getSol(){
        return solution;
    }

    public int getIsValid() {
        return isValid;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if ("move".equals(arg)) {
                row = model.getRowPlayer();
                col = model.getColPlayer();
                isValid = model.getIsValid();
                setChanged();
                notifyObservers("move");
            }
            if ("generate".equals(arg)) {
                maze = model.getMaze();
                setChanged();
                notifyObservers("generate");
            }
            if ("solve".equals(arg)) {
                setChanged();
                notifyObservers("solve");
            }
            if ("reset".equals(arg)) {
                row = model.getRowPlayer();
                col = model.getColPlayer();
                setChanged();
                notifyObservers("reset");
            }
            if ("getSolve".equals(arg)) {
                this.solution = model.getSolve();
                setChanged();
                notifyObservers("getSolve");
            }
        }
    }

    public void saveSettings() {
        model.saveSettings();
    }

    public void exit() throws InterruptedException {
        model.exit();
    }
}