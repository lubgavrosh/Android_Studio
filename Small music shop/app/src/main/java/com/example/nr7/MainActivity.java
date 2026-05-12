package com.example.nr7;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    Spinner spinnerProducts;
    Button buttonNext, buttonPlus, buttonMinus;
    TextView textQuantity;
    TextView textPrice;

    int quantity = 0;
    int pricePerItem = 500; // 🔥 цена по умолчанию

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textPrice = findViewById(R.id.textPrice);
        editName = findViewById(R.id.editName);
        spinnerProducts = findViewById(R.id.spinnerProducts);
        buttonNext = findViewById(R.id.buttonNext);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);
        textQuantity = findViewById(R.id.textQuantity);

        // 🔹 Продукты
        String[] products = {"Gitara", "Pianino", "Perkusja"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                products
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducts.setAdapter(adapter);

        // 🔥 обработчик выбора товара (цена зависит от товара)
        spinnerProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0: // Gitara
                        pricePerItem = 500;
                        break;
                    case 1: // Pianino
                        pricePerItem = 1000;
                        break;
                    case 2: // Perkusja
                        pricePerItem = 800;
                        break;
                }

                updatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 🔹 Кнопка +
        buttonPlus.setOnClickListener(v -> {
            quantity++;
            textQuantity.setText(String.valueOf(quantity));
            updatePrice();
        });

        // 🔹 Кнопка -
        buttonMinus.setOnClickListener(v -> {
            if (quantity > 0) {
                quantity--;
                textQuantity.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        // 🔹 Кнопка перехода
        buttonNext.setOnClickListener(v -> {

            String name = editName.getText().toString();
            String product = spinnerProducts.getSelectedItem().toString();

            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Wpisz imię!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (quantity == 0) {
                Toast.makeText(MainActivity.this, "Wybierz ilość!", Toast.LENGTH_SHORT).show();
                return;
            }

            String quantityStr = String.valueOf(quantity);

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("product", product);
            intent.putExtra("quantity", quantityStr);

            startActivity(intent);
        });
    }

    // 🔥 функция пересчёта цены
    private void updatePrice() {
        int total = pricePerItem * quantity;
        textPrice.setText(total + " $");
    }
}