package com.example.kotlin_firstlesson

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlin_firstlesson.ui.theme.Kotlin_firstlessonTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.setValue
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import  androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableStateOf
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.Row
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlin_firstlessonTheme {

                StudentScreen()
               // BatteryScreen()
               // GradeScreen()
                }
            }
        }
    }

//zad3
@Composable
fun GradeScreen() {
    var studentName by remember {
        mutableStateOf("")
    }

    var points1Text by remember {
        mutableStateOf("78")
    }

    var points2Text by remember {
        mutableStateOf("85")
    }

    var points3Text by remember {
        mutableStateOf("92")
    }

    var result by remember {
        mutableStateOf("Naciśnij Oblicz ocenę")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "System oceniania",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = studentName,
            onValueChange = {
                studentName = it
            },
            label = {
                Text("Imię i nazwisko")
            },
            singleLine = true,
            modifier = Modifier.padding(top = 16.dp)
        )
        OutlinedTextField(
            value = points1Text,
            onValueChange = {
                points1Text = it
            },
            label = {
                Text("Punkty - przedmiot 1")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.padding(top = 12.dp)
        )
        OutlinedTextField(
            value = points2Text,
            onValueChange = {
                points2Text = it
            },
            label = {
                Text("Punkty - przedmiot 2")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.padding(top = 12.dp)
        )
        OutlinedTextField(
            value = points3Text,
            onValueChange = {
                points3Text = it
            },
            label = {
                Text("Punkty - przedmiot 3")
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.padding(top = 12.dp)
        )

        Row(
            modifier = Modifier.padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Button(
                onClick = {

                    val points1 = points1Text.toIntOrNull()
                    val points2 = points2Text.toIntOrNull()
                    val points3 = points3Text.toIntOrNull()

                    if (
                        points1 == null ||
                        points2 == null ||
                        points3 == null ||
                        points1 !in 0..100 ||
                        points2 !in 0..100 ||
                        points3 !in 0..100
                    ) {

                        result = "Błąd: wszystkie punkty muszą być od 0 do 100"

                    } else {

                        val grade1 = calculateGrade(points1)
                        val grade2 = calculateGrade(points2)
                        val grade3 = calculateGrade(points3)

                        val average =
                            (grade1 + grade2 + grade3) / 3.0

                        val passed =
                            if (average >= 3.0) {
                                "ZALICZONE"
                            } else {
                                "NIEZALICZONE"
                            }

                        val veryGood =
                            if (average == 4.5 || average == 5.0) {
                                "\nBardzo dobry wynik"
                            } else {
                                ""
                            }

                        result =
                            "Student: $studentName\n" +
                                    "Przedmiot 1: $grade1\n" +
                                    "Przedmiot 2: $grade2\n" +
                                    "Przedmiot 3: $grade3\n" +
                                    "Średnia ocen: %.2f\n".format(average) +
                                    "Wynik: $passed" +
                                    veryGood
                    }
                }
            ) {
                Text("Oblicz ocenę")
            }

            Button(
                onClick = {
                    points1Text = "0"
                    points2Text = "0"
                    points3Text = "0"
                    result = ""
                }
            ) {
                Text("Wyczyść")
            }
        }

        Text(
            text = result,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}
fun calculateGrade(points: Int): Double {
    require(points in 0..100) {
        "Liczba punktów musi być od 0 do 100"
    }

    return when (points) {
        in 90..100 -> 5.0
        in 80..89 -> 4.5
        in 70..79 -> 4.0
        in 60..69 -> 3.5
        in 50..59 -> 3.0
        else -> 2.0
    }
}

//zad2
@Composable
fun BatteryScreen() {

    var batteryLevel by remember {
        mutableIntStateOf(100)
    }

    var warning by remember {
        mutableStateOf("Bateria jest pełna")
    }

    var isRunning by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isRunning) {

        if (isRunning) {

            while (batteryLevel > 0) {

                delay(500)

                batteryLevel -= 10

                if (batteryLevel == 20) {
                    warning = "⚠️ Ostrzeżenie: bateria ma tylko 20%!"
                }
            }

            if (batteryLevel == 0) {
                warning = "🔴 Bateria rozładowana!"
                isRunning = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Analiza poziomu baterii",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "$batteryLevel%",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 20.dp)
        )

        LinearProgressIndicator(
            progress = {
                batteryLevel / 100f
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .padding(top = 10.dp),
            color = if (batteryLevel <= 20) {
                Color.Red
            } else if (batteryLevel <= 50) {
                Color.Yellow
            } else {
                Color.Green
            }
        )

        Text(
            text = warning,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 20.dp)
        )

        Button(
            onClick = {
                batteryLevel = 100
                warning = "Bateria jest pełna"
                isRunning = true
            },
            enabled = !isRunning,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Rozładuj baterię")
        }

        Button(
            onClick = {
                batteryLevel = 100
                warning = "Bateria jest pełna"
                isRunning = false
            }
        ) {
            Text("Reset")
        }
    }
}

//zad1
@Composable
fun StudentScreen()
    {
        val name: String = "Liubomyr"

        var age by remember {
            mutableIntStateOf(20)
        }

        val premium: Boolean = false

        var balance by remember {
            mutableDoubleStateOf(1250.50)
        }

        var loginCount by remember {
            mutableIntStateOf(12)
        }

        var isActive by remember {
            mutableStateOf(true)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Profil użytkownika",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Użytkownik: $name"
            )

            Text(
                text = "Wiek: $age"
            )

            Text(
                text = "Premium: $premium"
            )

            Text(
                text = "Saldo konta: %.2f zł".format(balance)
            )

            Text(
                text = "Liczba logowań: $loginCount"
            )

            Text(
                text = "Status: ${
                    if (isActive) {
                        "Aktywny"
                    } else {
                        "Nieaktywny"
                    }
                }"
            )

            Text(
                text = "Wiek za rok: ${age + 1}",
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = {
                    age += 1
                },
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text("Zwiększ wiek o 1")
            }

            Button(
                onClick = {
                    loginCount += 1
                }
            ) {
                Text("Zaloguj użytkownika")
            }

            Button(
                onClick = {
                    isActive = !isActive
                }
            ) {
                Text("Zmień status")
            }
        }
    }

