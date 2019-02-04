package com.maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Maze {

    private char [][] maze = null;
    private File file;
    private static int startX = 0;
    private static int startY = 0;
    private int endX = 0;
    private int endY = 0;
    private int height = 0;
    private int width = 0;

    public static void main(String[] args) {

        Maze maze = new Maze();

        String fileName;

        System.out.println("Enter file path: ");
        Scanner scanner = new Scanner(System.in);
        fileName = scanner.nextLine();
        Path path = Paths.get(fileName);

        //path validation (it does not validate empty entry)
        while (isPathExist(path)) {
            System.out.println("\nFile does not exist/or is invalid.");
            System.out.println("Enter correct file path:");
            scanner = new Scanner(System.in);
            fileName = scanner.nextLine();
            path = Paths.get(fileName);
        }

        maze.buildMaze(fileName);
        maze.refactorMaze();
        if(maze.solve(startX, startY)) {
            maze.printMaze();
        }
        else {
            System.out.println("The maze is unsolvable");
        }
    }

    private static boolean isPathExist(Path path) {
        try {
            Paths.get(path.toUri());
        } catch (InvalidPathException | NullPointerException ex) {
            return true;
        }
        return !Files.exists(path);
    }


    //Populates the maze
    private void buildMaze(String file) {

        char temp;
        String line;
        int count = 1;
        int heightCounter = 0;
        try {
            this.file = new File(file);
            if(!this.file.exists() || this.file.isDirectory()) {
                throw new FileNotFoundException();
            }

            //Read file lines and set variable
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while((line = bufferedReader.readLine()) != null) {
                switch (count) {
                    case (1):
                        width = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                        height = Integer.parseInt((line.substring(line.indexOf(' ')+1)));
                        maze = new char[height][width];
                        break;
                    case (2):
                        temp = line.charAt(0);
                        startY = Character.getNumericValue(temp);
                        temp = line.charAt(2);
                        startX = Character.getNumericValue(temp);
                        break;
                    case (3):
                        endY = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                        endX = Integer.parseInt((line.substring(line.indexOf(' ') +1 )));
                        break;
                    default:
                        int counter = 0;
                        for (int i = 0; i < line.length(); i++){
                            if(line.charAt(i) != ' '){
                                maze[heightCounter][counter] = line.charAt(i);
                                counter++;
                            }
                        }
                        heightCounter++;
                        break;
                }
                count++;
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("The file : " + this.file.getName() + " does not exist" );
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }


    //refactor the maze
    private void refactorMaze() {

        maze[startX][startY] = 'S';
        maze[endX][endY] = 'E';

        for (int i = 0; i < height; i++) {
            for(int j = 0; j < width; j++) {

                if(maze[i][j] == '1') {
                    maze[i][j] = '#';
                }

                if(maze[i][j] == '0') {
                    maze[i][j] = ' ';
                }
            }
        }
    }


    //recursively calling till exit
    private boolean solve(int i, int j) {

        //wrap the maze rows
        if (i < 0) {
            i = (maze.length - 1);
        }

        if (i >= maze.length) {
            i = 0;
        }

        //wrap the maze column
        if (j < 0) {
            j = (maze[i].length - 1);
        }

        if (j >= maze[i].length) {
            j = 0;
        }

        //check around itself
        if (maze[i][j] == '#') {
            return false;
        }

        if (maze[i][j] == 'E') {
            return true;
        }

        if (maze[i][j] == 'X') {
            return false;
        }

        maze[i][j] = 'X';

        //Down
        if ((solve(i + 1, j))) {
            return true;
        }
        //Left
        if ((solve(i, j - 1))) {
            return true;
        }
        //Right
        if ((solve(i, j + 1))) {
            return true;
        }
        //Up
        if ((solve(i - 1, j))) {
            return true;
        }

        maze[i][j] = ' ';
        return false;
    }


    //print
    private void printMaze() {

        maze[startX][startY] = 'S';
        for (char[] chars : maze) {
            System.out.println(chars);
        }
    }
}