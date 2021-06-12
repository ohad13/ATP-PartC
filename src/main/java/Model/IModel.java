package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.Observer;

public interface IModel {

     void generateMaze(int row, int col);

     Maze getMaze();

     void movePlayer(KeyCode whereToMove);

     int getColPlayer();

     int getRowPlayer();

     void assignObserver(Observer o);

     void reset();

     Solution getSolve();

    void solveMaze();

    void saveSettings(String gen, String ser, int nThreads);

    int getIsValid();

    void exit() throws InterruptedException;

    void load();

    void setLoadFile(File loadFile);
}
