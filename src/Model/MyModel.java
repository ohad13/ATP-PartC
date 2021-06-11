package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    public Solution solution;
    public File loadFile;
    Logger logger = Logger.getLogger(MyModel.class.getName());
    private Maze maze;
    private int rowPlayer;
    private int colPlayer;
    private Server mazeGeneratorServer;
    private Server mazeSolverServer;
    private int isValid = 0;
    private boolean isSolved = false;

    public MyModel() {
        mazeSolverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        mazeGeneratorServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        this.maze = null;
        rowPlayer = 0;
        colPlayer = 0;
    }

    public void startServers() {
        mazeGeneratorServer.start();
        logger.info("Start generator server");
        mazeSolverServer.start();
        logger.info("Start searcher server");
    }

    public void stopServers() throws InterruptedException {
        mazeGeneratorServer.stop();
        logger.info("Stop generator server");
        mazeSolverServer.stop();
        logger.info("Stop searcher server");
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public int getRowPlayer() {
        return rowPlayer;
    }

    public int getColPlayer() {
        return colPlayer;
    }

    public Maze getMaze() {
        return maze;
    }

    private void setMaze(Maze mazeObj) {
        this.maze = mazeObj;
    }

    public void movePlayer(KeyCode whereToMove) {
        int player_row_pos = rowPlayer;
        int player_col_pos = colPlayer;
        isValid = 0;
        switch (whereToMove) {
            case UP:
            case NUMPAD8:
                if (maze.possibleToGo(player_row_pos - 1, player_col_pos))
                    player_row_pos -= 1;
                else isValid = 1;
                break;
            case DOWN:
            case NUMPAD2:
                if (maze.possibleToGo(player_row_pos + 1, player_col_pos))
                    player_row_pos += 1;
                else isValid = 1;
                break;
            case LEFT:
            case NUMPAD4:
                if (maze.possibleToGo(player_row_pos, player_col_pos - 1))
                    player_col_pos -= 1;
                else isValid = 1;
                break;
            case RIGHT:
            case NUMPAD6:
                if (maze.possibleToGo(player_row_pos, player_col_pos + 1))
                    player_col_pos += 1;
                else isValid = 1;
                break;
            case NUMPAD7:
                if (maze.possibleToGo(player_row_pos - 1, player_col_pos - 1)) {
                    player_col_pos += -1;
                    player_row_pos += -1;
                } else isValid = 1;
                break;
            case NUMPAD9:
                if (maze.possibleToGo(player_row_pos - 1, player_col_pos + 1)) {
                    player_col_pos += 1;
                    player_row_pos += -1;
                } else isValid = 1;
                break;
            case NUMPAD3:
                if (maze.possibleToGo(player_row_pos + 1, player_col_pos + 1)) {
                    player_col_pos += 1;
                    player_row_pos += 1;
                } else isValid = 1;
                break;
            case NUMPAD1:
                if (maze.possibleToGo(player_row_pos + 1, player_col_pos - 1)) {
                    player_col_pos += -1;
                    player_row_pos += 1;
                } else isValid = 1;
                break;
            default:
                isValid = 2;
                break;
        }
        rowPlayer = player_row_pos;
        colPlayer = player_col_pos;
        setChanged();
        notifyObservers("move");

        // when maze is solved
        if (player_row_pos == maze.getGoalPosition().getRowIndex() && player_col_pos == maze.getGoalPosition().getColumnIndex() && !isSolved) {
            isSolved = true;//if drag-make no more than 1 alert.
            setChanged();
            notifyObservers("solve");
            logger.info("Client solve the maze !");
        }
    }

    private void generateMazeThroughGeneratorServer(int numOfRows, int numOfCols) {
        try {
            /* Code from part-B test: "RunCommunicateWithServers" */
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{numOfRows, numOfCols};
                        /* write the desired Maze dimensions to the OutStream */
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        /* get compressed Maze from the InStream */
                        byte[] compressedMaze = (byte[]) fromServer.readObject();
                        /* Decompress the compressed-maze read from server */
                        InputStream decompressorIS = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[numOfCols * numOfRows + 24];
                        /*Fill decompressedMaze with bytes*/
                        decompressorIS.read(decompressedMaze);
                        /*create new Maze */
                        Maze newMaze = new Maze(decompressedMaze);
                        /* update maze data member */
                        setMaze(newMaze);
                    } catch (Exception e) {
                        logger.info(e);
                        //e.printStackTrace();
                    }
                }
            });
            /* invoking the anonymous "clientStrategy" implemented above */
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            logger.info(e);
            //e.printStackTrace();
        }
    }

    public int getIsValid() {
        return isValid;
    }

    private void solveMazeThroughSolverServer() {
        try {
            /* Code from part-B test: "RunCommunicateWithServers" */
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        /*update solution so that maze Displayer can use getter to take it*/
                        solution = (Solution) fromServer.readObject();
                    } catch (Exception e) {
                        logger.info(e);
//                        e.printStackTrace();
                    }
                }
            });
            int x = maze.getNumOfRow();
            int y = maze.getNumOfCol();
            /* invoking the anonymous "clientStrategy" implemented above */
            logger.info("Client ask to solve maze " + x + "X" + y);
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            logger.info(e);
//            e.printStackTrace();
        }
    }

    public void generateMaze(int row, int col) {
        try {
            generateMazeThroughGeneratorServer(row, col);
            rowPlayer = maze.getStartPosition().getRowIndex();
            colPlayer = maze.getStartPosition().getColumnIndex();
            setChanged();
            notifyObservers("generate");
            logger.info("Client ask for maze " + maze.getNumOfRow() + "X" + maze.getNumOfCol());
        } catch (Exception e) {
            //errorSound();
            Alert a = new Alert(Alert.AlertType.NONE);
            a.setAlertType(Alert.AlertType.WARNING);
            a.setContentText("Wrong Parameters, Please insert 2 numbers bigger then 2");
            a.show();
        }
    }

    public void reset() {
        rowPlayer = maze.getStartPosition().getRowIndex();
        colPlayer = maze.getStartPosition().getColumnIndex();
        isSolved = false;
        setChanged();
        notifyObservers("reset");
        logger.info("Client ask to restart the current maze");
    }

    @Override
    public void saveSettings(String gen, String ser, int nThreads) {
        mazeGeneratorServer.setServerStrategy(new ServerStrategyGenerateMaze());
        mazeSolverServer.setServerStrategy(new ServerStrategySolveSearchProblem());
        mazeGeneratorServer.setExecutor(nThreads);
        mazeSolverServer.setExecutor(nThreads);
        logger.info("Client changed the properties.");
        logger.info("The new properties is:");
        logger.info("Maze Generator: " + gen);
        logger.info("Maze Searcher: " + ser);
        logger.info("Num of Threads: " + nThreads);
    }

    public void solveMaze() {
        solveMazeThroughSolverServer();
        setChanged();
        notifyObservers("getSolve");
    }

    @Override
    public void load() {
        byte[] bArr = new byte[0];
        try {
            bArr = Files.readAllBytes(loadFile.toPath());
        } catch (IOException e) {
            logger.info(e);
            //e.printStackTrace();
        }
        int l = 24 + (bArr.length - 32) / 4;
        byte[] shorty = new byte[l];
        int j = 24;// he previous before the pos was 24 instead the 32
        System.arraycopy(bArr, 8, shorty, 0, l);// MetaData copy
        for (int i = 0; i < (bArr.length - 32); i += 4) {
            byte b = bArr[35 + i];
            shorty[j] = b;
            j++;
        }
        this.maze = new Maze(shorty);
        byte[] first = Arrays.copyOfRange(bArr, 0, 8);
        IntBuffer intBuf = ByteBuffer.wrap(first).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        rowPlayer = array[0];
        colPlayer = array[1];
        setChanged();
        notifyObservers("load");
        logger.info("Client ask to load a maze");
    }

    public void setLoadFile(File loadFile) {
        this.loadFile = loadFile;
    }

    @Override
    public void exit() throws InterruptedException {
        logger.info("Client closed all..");
        stopServers();
        Platform.exit();
    }

    public Solution getSolve() {
        return solution;
    }
}