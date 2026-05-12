package com.example.brain_trainer;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView questionText, scoreText;
    Button btn0, btn1, btn2, btn3;

    int correctAnswer;
    int winsInRow = 0;
    int bestScore = 0;

    Random random = new Random();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.questionText);
        scoreText = findViewById(R.id.scoreText);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);

        generateQuestion();
    }

    public void answerSelected(View view) {
        Button selected = (Button) view;
        int selectedAnswer = Integer.parseInt(selected.getText().toString());

        if (selectedAnswer == correctAnswer) {
            winsInRow++;
            if (winsInRow > bestScore) {
                bestScore = winsInRow;
            }
            scoreText.setText("Streak: " + winsInRow + " | Best: " + bestScore);
            generateQuestion();
        } else {
            scoreText.setText("Błąd! Streak: " + winsInRow + " | Best: " + bestScore);
            winsInRow = 0;
            generateQuestion();
        }
    }

    private void generateQuestion() {
        int a = random.nextInt(201) - 100;
        int b = random.nextInt(201) - 100;

        boolean addition = random.nextBoolean();

        if (addition) {
            correctAnswer = a + b;
            questionText.setText(a + " + " + b);
        } else {
            correctAnswer = a - b;
            questionText.setText(a + " - " + b);
        }

        ArrayList<Integer> answers = new ArrayList<>();
        answers.add(correctAnswer);

        while (answers.size() < 4) {
            int wrong = random.nextInt(401) - 200;
            if (!answers.contains(wrong)) {
                answers.add(wrong);
            }
        }

        Collections.shuffle(answers);

        btn0.setText(String.valueOf(answers.get(0)));
        btn1.setText(String.valueOf(answers.get(1)));
        btn2.setText(String.valueOf(answers.get(2)));
        btn3.setText(String.valueOf(answers.get(3)));
    }
}

