package com.example.nr6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText display;

    double firstNumber = 0;
    String operator = "";
    boolean isNewInput = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
    }

    public void onNumberClick(View view) {
        Button button = (Button) view;
        String number = button.getText().toString();

        if (isNewInput) {
            display.setText(number);
            isNewInput = false;
        } else {
            display.append(number);
        }
    }

    public void onOperatorClick(View view) {
        Button button = (Button) view;

        firstNumber = Double.parseDouble(display.getText().toString());
        operator = button.getText().toString();
        isNewInput = true;
    }

    public void onEqualClick(View view) {
        double secondNumber = Double.parseDouble(display.getText().toString());
        double result = 0;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "*":
                result = firstNumber * secondNumber;
                break;
            case "/":
                result = secondNumber != 0 ? firstNumber / secondNumber : 0;
                break;
        }

        display.setText(String.valueOf(result));
        isNewInput = true;
    }

    public void onClearClick(View view) {
        display.setText("0");
        firstNumber = 0;
        operator = "";
        isNewInput = true;
    }
}