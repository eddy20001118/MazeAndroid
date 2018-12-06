package com.android.yuhao_li.maze_android;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    String TAG = "USER_PRINT";
    AlgoVisHelper algoVisHelper;
    GridLayout gridLayout;
    MazeData data;
    Button goButton;
    Algorithm algorithm;
    TextView REDScore, BLUEScore;
    private int runMethodCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void goOnClick(View view) {
        if (runMethodCount < data.getBlockNumbers()) {
            algorithm.run(runMethodCount);
            if (runMethodCount == data.getBlockNumbers() - 1) {
                if (Integer.parseInt(REDScore.getText().toString()) >= Integer.parseInt(BLUEScore.getText().toString())) {
                    setScoreVis(REDScore, "Winner!", AlgoVisHelper.red);
                } else {
                    setScoreVis(BLUEScore,"Winner!", AlgoVisHelper.red);
                }
            }
        } else if (runMethodCount == data.getBlockNumbers()) {
            goButton.setText("Reset");
        } else {
            gridLayout.removeAllViews();
            init();
            runMethodCount = -1;
        }
        runMethodCount++;
    }

    public void init() {
        algoVisHelper = new AlgoVisHelper(this);
        goButton = findViewById(R.id.goButton);
        gridLayout = findViewById(R.id.gridLayout);
        REDScore = findViewById(R.id.REDScore);
        BLUEScore = findViewById(R.id.BLUEScore);
        data = algoVisHelper.getMazeData();
        goButton.setText("Go");
        setScoreVis(REDScore, "Red", AlgoVisHelper.gray);
        setScoreVis(BLUEScore, "Blue", AlgoVisHelper.gray);
        algoVisHelper.activityCallback(this);
        algoVisHelper.setGridLayout(gridLayout);
        algoVisHelper.CreateMap();
        algoVisHelper.setPlayerScoreView(REDScore, BLUEScore);
        algorithm = new Algorithm(algoVisHelper);
    }

    public void setScoreVis(TextView textView, String text, int color ){
        textView.setText(text);
        textView.setTextColor(color);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
    }
}

