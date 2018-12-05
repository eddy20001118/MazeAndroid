package com.android.yuhao_li.maze_android;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AlgoVisHelper extends ContextWrapper {
    private MazeData data;
    private DisplayMetrics dm;
    private ImageView[][] block;
    private MainActivity activity;
    private GridLayout gridLayout;
    private EditText[] BlueBlockScore;
    private TextView REDScore,BLUEScore;
    private int ScreenWidth;
    private int RectangleStroke = 2;
    public static int gray = Color.parseColor("#aaaaaa");
    public static int blue = Color.parseColor("#4169FF");
    public static int orange = Color.parseColor("#FF6D00");
    public static int red = Color.parseColor("#FF1744");
    public static int white = Color.parseColor("#eeeeee");
    public static int PATHRed = Color.parseColor("#CE93D8");
    public static int PATHBlue = Color.parseColor("#00BCD4");
    public static int TextBlack = Color.parseColor("#212121");

    public AlgoVisHelper(Context base) {
        super(base);
        dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        data = new MazeData(getResources().openRawResource(R.raw.map));
        BlueBlockScore = new EditText[data.getBlockNumbers()];
        ScreenWidth = dm.widthPixels;
    }

    public ImageView getRectangle(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        ImageView imageView = new ImageView(this);
        gradientDrawable.setCornerRadius(5);
        gradientDrawable.setColor(color);
        imageView.setBackground(gradientDrawable);
        return imageView;
    }

    public void CreateMap() {
        int color;
        block = new ImageView[data.GetRow()][data.GetCol()];
        for (int i = 0; i < data.GetRow(); i++) {
            for (int j = 0; j < data.GetCol(); j++) {
                if (data.getMaze(i, j) == MazeData.WALL) {
                    color = AlgoVisHelper.orange;
                } else if (data.getMaze(i, j) == MazeData.ROAD) {
                    color = AlgoVisHelper.white;
                } else if (data.getMaze(i, j) == MazeData.StartPoint) {
                    color = AlgoVisHelper.red;
                } else {
                    color = AlgoVisHelper.blue;
                }
                block[i][j] = getRectangle(color);
                gridLayout.addView(block[i][j], createParams(i, j));
            }
        }
    }

    public void addRectItem(int row, int col, int color) {
        gridLayout.removeView(block[row][col]);
        block[row][col] = getRectangle(color);
        gridLayout.addView(block[row][col], createParams(row, col));
    }

    public void setBlueBlock(int row, int col, int score, int position) {
        gridLayout.removeView(BlueBlockScore[position]);
        BlueBlockScore[position] = new EditText(this);
        BlueBlockScore[position].setText(String.valueOf(score));
        BlueBlockScore[position].setTextColor(AlgoVisHelper.TextBlack);
        BlueBlockScore[position].setTextSize(13);
        GridLayout.LayoutParams params = createParams(row,col);
        gridLayout.addView(BlueBlockScore[position], params);
    }

    public GridLayout.LayoutParams createParams(int row, int col) {
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec columnSpec = GridLayout.spec(col);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams(rowSpec, columnSpec);
        params.width = (ScreenWidth / gridLayout.getColumnCount()) - (2 * RectangleStroke);
        params.height = (ScreenWidth / gridLayout.getColumnCount()) - (2 * RectangleStroke);
        params.leftMargin = RectangleStroke;
        params.rightMargin = RectangleStroke;
        params.topMargin = RectangleStroke;
        params.bottomMargin = RectangleStroke;
        params.setGravity(Gravity.START);
        return params;
    }

    public GridLayout getGridLayout() {
        return gridLayout;
    }

    public void setGridLayout(GridLayout gridLayout) {
        this.gridLayout = gridLayout;
    }

    public void activityCallback(MainActivity activity) {
        this.activity = activity;
    }

    public void setPlayerScore(int RED, int BLUE){
        REDScore.setText(String.valueOf(RED));
        BLUEScore.setText(String.valueOf(BLUE));
    }

    public void setPlayerScoreView(TextView REDScore, TextView BLUEScore){
        this.REDScore  = REDScore;
        this.BLUEScore = BLUEScore;
    }

    public MazeData getMazeData() {
        return data;
    }
}
