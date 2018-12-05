package com.android.yuhao_li.maze_android;

import android.support.v7.widget.GridLayout;
import android.util.Log;

import java.util.LinkedList;


public class Algorithm {
    MazeData data;
    GridLayout gridLayout;
    private AlgoVisHelper algoVisHelper;

    public enum Side {Left, Right, NotEndPoint}

    public enum Player {RED, BLUE}

    private BlueBlock[] blueBlock;
    private BlueBlock leftBlock, rightBlock;
    private boolean[][] curPath;
    private int REDScore = 0, BlueScore = 0;
    private cal play;
    private static final int[][] direction = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}}; //9点钟方向逆时针

    public Algorithm(AlgoVisHelper algoVisHelper) {
        this.algoVisHelper = algoVisHelper;
        data = algoVisHelper.getMazeData();
        Log.d("Maze", String.valueOf(data.getBlockNumbers()));
        gridLayout = algoVisHelper.getGridLayout();
        blueBlock = new BlueBlock[8];
        initBlueBlock();
        leftBlock = blueBlock[0];
        rightBlock = blueBlock[data.getBlockNumbers() - 1];
        curPath = new boolean[data.GetRow()][data.GetCol()];
    }

    public void run(int runMethodCount) {
        if (runMethodCount < data.getBlockNumbers()) {
            Player player;
            if (runMethodCount % 2 == 0) {
                player = Player.RED;
            } else {
                player = Player.BLUE;
            }
            play = new cal(player, leftBlock, rightBlock);
            play.clearPath(curPath);
            play.runNext();
            curPath = play.getPath();

            if (player == Player.RED) {
                REDScore += play.getScore();
            } else {
                BlueScore += play.getScore();
            }

            algoVisHelper.setPlayerScore(REDScore, BlueScore);
            if (play.getSide() == Side.Left) {
                leftBlock.cancelCurrentBlock(player);
                algoVisHelper.setBlueBlock(leftBlock.getX() + 1, leftBlock.getY(), leftBlock.getScore(), leftBlock.getPosition()); //刷新方块的分数
                leftBlock = leftBlock.nextBlock(Side.Left);
                leftBlock.init(algoVisHelper);
            } else if (play.getSide() == Side.Right) {
                rightBlock.cancelCurrentBlock(player);
                algoVisHelper.setBlueBlock(rightBlock.getX() + 1, rightBlock.getY(), rightBlock.getScore(), rightBlock.getPosition()); //刷新方块的分数
                rightBlock = rightBlock.nextBlock(Side.Right);
                rightBlock.init(algoVisHelper);
            }
        }
    }

    public int getREDScore() {
        return REDScore;
    }

    public int getBLUEScore() {
        return BlueScore;
    }

    public void initBlueBlock() {
        int count = 0; //记录蓝色方块在分数数组中的位置
        for (int i = 0; i < data.GetRow(); i++) {
            for (int j = 0; j < data.GetCol(); j++) {
                if (data.getMaze(i, j) == 'B') {
                    blueBlock[count] = new BlueBlock(i, j, data.getBlueScore()[count], count);
                    blueBlock[count].init(algoVisHelper);
                    algoVisHelper.setBlueBlock(i + 1, j, blueBlock[count].getScore(), blueBlock[count].getPosition()); //刷新初始时各方块的分数
                    count++;
                }
            }
        }
    }

    private class cal {
        int leftScore = 0, rightScore = 0;
        boolean[][] pathLeft = new boolean[data.GetRow()][data.GetCol()];
        boolean[][] pathRight = new boolean[data.GetRow()][data.GetCol()];
        BlueBlock exitLeft, exitRight;
        Player curPlayer;

        cal(Player curPlayer, BlueBlock exitLeft, BlueBlock exitRight) {
            this.curPlayer = curPlayer;
            this.exitLeft = exitLeft;
            this.exitRight = exitRight;
        }

        private void runNext() {
            leftScore = exitLeft.getScore() - calculate(Side.Left, exitLeft);
            rightScore = exitRight.getScore() - calculate(Side.Right, exitRight);
            drawPath();
        }

        private int calculate(Side side, BlueBlock exit) {
            int endX, endY;
            int score = 0;
            endX = exit.prevBlock(side).getX();
            endY = exit.prevBlock(side).getY();
            LinkedList<Position> queue = new LinkedList<Position>();
            Position entrance = new Position(data.getEnteranceX(), data.getEnteranceY());
            queue.addLast(entrance); //由队尾加入队列
            data.visited[entrance.getX()][entrance.getY()] = true;
            while (queue.size() != 0) {
                Position curPos = queue.pop();  //获取并自动删除队列中的第一个元素
                if (curPos.getX() == endX && curPos.getY() == endY) {
                    score = findPath(curPos, side);
                    //System.out.println("Solve "+isSolved);
                    break;
                }

                for (int i = 0; i < 4; i++) {
                    int newX = curPos.getX() + direction[i][0];
                    int newY = curPos.getY() + direction[i][1];

                    if (data.inArea(newX, newY)
                            && !data.visited[newX][newY]
                            && data.getMaze(newX, newY) == MazeData.ROAD) {
                        queue.addLast(new Position(newX, newY, curPos));
                        data.visited[newX][newY] = true;
                    }
                }
            }
            data.reset();
            return score;
        }

        private int findPath(Position des, Side side) {
            Position cur = des;
            int count = 0;
            while (cur != null) {
                if (side == Side.Left) {
                    pathLeft[cur.getX()][cur.getY()] = true; //代表左边路径
                } else if (side == Side.Right) {
                    pathRight[cur.getX()][cur.getY()] = true; //代表右边路径
                }
                cur = cur.getPrev();
                count++;
            }
            return count;
        }

        private int getScore() {
            return leftScore >= rightScore ? leftScore : rightScore;
        }

        private boolean[][] getPath() {
            if (leftScore >= rightScore) {
                return pathLeft;
            } else {
                return pathRight;
            }
        }

        //返回计算出的最短路径是那一边的，也就是该路径的终点在哪一边
        private Side getSide() {
            if (leftScore >= rightScore) {
                return Side.Left;
            } else {
                return Side.Right;
            }
        }

        private void drawPath() {
            for (int i = 0; i < data.GetRow(); i++) {
                for (int j = 0; j < data.GetCol(); j++) {
                    if (data.getMaze(i, j) != MazeData.StartPoint) {
                        if (curPlayer == Player.RED && this.getSide() == Side.Left) {
                            if (pathLeft[i][j]) {
                                algoVisHelper.addRectItem(i, j, AlgoVisHelper.PATHRed);
                            }
                        } else if (curPlayer == Player.RED && this.getSide() == Side.Right) {
                            if (pathRight[i][j]) {
                                algoVisHelper.addRectItem(i, j, AlgoVisHelper.PATHRed);
                            }
                        } else if (curPlayer == Player.BLUE && this.getSide() == Side.Left) {
                            if (pathLeft[i][j]) {
                                algoVisHelper.addRectItem(i, j, AlgoVisHelper.PATHBlue);
                            }
                        } else if (curPlayer == Player.BLUE && this.getSide() == Side.Right) {
                            if (pathRight[i][j]) {
                                algoVisHelper.addRectItem(i, j, AlgoVisHelper.PATHBlue);
                            }
                        }
                    }

                }
            }
        }

        private void clearPath(boolean path[][]) {
            for (int i = 0; i < data.GetRow(); i++) {
                for (int j = 0; j < data.GetCol(); j++) {
                    if (data.getMaze(i, j) != MazeData.StartPoint) {
                        if (path[i][j]) {
                            algoVisHelper.addRectItem(i, j, AlgoVisHelper.white);
                        }
                        path[i][j] = false;
                    }
                }
            }
            algoVisHelper.addRectItem(leftBlock.prevBlock(Side.Left).getX(), leftBlock.prevBlock(Side.Left).getY(), AlgoVisHelper.white);
            algoVisHelper.addRectItem(rightBlock.prevBlock(Side.Right).getX(), rightBlock.prevBlock(Side.Right).getY(), AlgoVisHelper.white);
        }

    }

}

