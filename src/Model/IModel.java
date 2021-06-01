package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Observer;

public interface IModel {

    public void generateMaze(int row, int col);

    public Maze getMaze();

    public void movePlayer(KeyCode whereToMove);

    public int getColPlayer();

    public int getRowPlayer();

    public void assignObserver(Observer o);

    public void reset();

    public Solution getSolve();

    void solveMaze();
}
