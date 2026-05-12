package com.example.lk1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    TextView[] cells = new TextView[9];
    boolean isX = true;
    int moveCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < 9; i++) {
            int id = getResources().getIdentifier("c" + i, "id", getPackageName());
            cells[i] = findViewById(id);
            int finalI = i;
            cells[i].setOnClickListener(v -> onClick(finalI));
        }
    }
    void onClick(int i) {
        if (!cells[i].getText().toString().equals("")) return;
        cells[i].setText(isX ? "X" : "O");
        moveCount++;
        if (checkWin()) {
            Toast.makeText(this, (isX ? "X" : "O") + " WIN!", Toast.LENGTH_SHORT).show();
            resetGame();
            return;
        }
        if (moveCount == 9) {
            Toast.makeText(this, "NO WIN!", Toast.LENGTH_SHORT).show();
            resetGame();
            return;
        }
        isX = !isX;
    }
    boolean checkWin() {
        int[][] winPositions = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };
        for (int[] pos : winPositions) {
            String a = cells[pos[0]].getText().toString();
            String b = cells[pos[1]].getText().toString();
            String c = cells[pos[2]].getText().toString();
            if (!a.equals("") && a.equals(b) && b.equals(c)) {
                return true;
            }
        }
        return false;
    }
    void resetGame() {
        for (TextView cell : cells) {
            cell.setText("");
        }
        isX = true;
        moveCount = 0;
    }
}