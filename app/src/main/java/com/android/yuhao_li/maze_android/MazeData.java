package com.android.yuhao_li.maze_android;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Scanner;


public class MazeData {
    public static final char ROAD = '0';
    public static final char WALL = '1';
    public static final char BlueBlock = 'B';
    public static final char StartPoint = 'S';
    public boolean[][] visited;
    private int BlueBlockNumbers; //初始蓝色方块的数量
    private int[] BlueScore = {1000, 40, 40, 40, 40, 40, 40, 1000};
    private int row, col; //行(N)，列(M)
    private int enteranceX = 0, enteranceY = 0;
    private int exitX = 9, exitY = 1;
    private char[][] maze;

    public MazeData(InputStream inputStream) {
        Scanner scanner = null;
        try {
            InputStream fis = inputStream;
            if (fis == null) {
                throw new IllegalArgumentException("文件不存在");
            }
            scanner = new Scanner(new BufferedInputStream(fis), "UTF-8");
            //读取第一行（长宽）
            String rcLine = scanner.nextLine();
            String[] rc = rcLine.trim().split("\\s+");

            row = Integer.parseInt(rc[0]);
            col = Integer.parseInt(rc[1]);

            maze = new char[row][col];
            visited = new boolean[row][col];
            //读取后续的row行
            for (int i = 0; i < row; i++) {
                String line = scanner.nextLine();
                if (line.length() != col) {
                    throw new IllegalArgumentException("地图文件第" + row + "行有误");
                }
                for (int j = 0; j < col; j++) {
                    maze[i][j] = line.charAt(j);
                    visited[i][j] = false;
                    if (maze[i][j] == 'B') {
                        BlueBlockNumbers++;
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public int getBlockNumbers() {
        return BlueBlockNumbers;
    }

    public int[] getBlueScore() {
        return BlueScore;
    }

    public int GetRow() {
        return row;
    }

    public int GetCol() {
        return col;
    }

    public int getEnteranceX() {
        return enteranceX;
    }

    public int getEnteranceY() {
        return enteranceY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public void setEnterancePoint(int x, int y) {
        this.enteranceX = x;
        this.enteranceY = y;
    }

    public void setExitPoint(int x, int y) {
        this.exitX = x;
        this.exitY = y;
        maze[x][y] = '0';
    }

    public void setMaze(int x, int y, char value) {
        maze[x][y] = value;
    }

    public char getMaze(int x, int y) {
        if (!inArea(x, y)) {
            throw new IllegalArgumentException("传入坐标不在范围内");
        }
        return maze[x][y]; //返回迷宫里的点
    }

    public boolean inArea(int x, int y) {
        return x >= 0 && x < row && y >= 0 && y < col;
    }

    public void test() {
        System.out.println(row + "行" + col + "列");
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    public void reset() {
        for (int i = 0; i < GetRow(); i++) {
            for (int j = 0; j < GetCol(); j++) {
                visited[i][j] = false;
            }
        }
    }
}
