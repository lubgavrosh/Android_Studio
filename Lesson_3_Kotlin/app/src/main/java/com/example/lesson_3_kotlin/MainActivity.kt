package com.example.lesson_3_kotlin

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lesson_3_kotlin.ui.theme.Lesson_3_KotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lesson_3_KotlinTheme {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen() {
    var selectedScreen by rememberSaveable { mutableIntStateOf(0) }
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (selectedScreen == 0) {
                    Button(onClick = { selectedScreen = 0 }, modifier = Modifier.weight(1f)) {
                        Text("TextLab Pro")
                    }
                    OutlinedButton(onClick = { selectedScreen = 1 }, modifier = Modifier.weight(1f)) {
                        Text("HabitPoints")
                    }
                } else {
                    OutlinedButton(onClick = { selectedScreen = 0 }, modifier = Modifier.weight(1f)) {
                        Text("TextLab Pro")
                    }
                    Button(onClick = { selectedScreen = 1 }, modifier = Modifier.weight(1f)) {
                        Text("HabitPoints")
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                if (selectedScreen == 0) TextLabScreen()
                else HabitPointsScreen()
            }
        }
    }
}

@Composable
fun TextLabScreen() {
    var editor by remember { mutableStateOf(EditorState()) }
    var author by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("Wpisz tekst i wybierz operację.") }
    val context = LocalContext.current

    // Lambda wyższego rzędu: każda operacja przechowuje poprzedni tekst.
    fun performEdit(operation: (String) -> String) {
        val previous = editor
        editor = editText(editor, operation)
        result = if (previous == editor) "Tekst nie uległ zmianie."
                 else "Tekst został zmieniony. Możesz cofnąć operację."
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("TextLab Pro", style = MaterialTheme.typography.headlineMedium)
        Text("Zaawansowany analizator i edytor tekstu")
        OutlinedTextField(
            value = editor.text,
            onValueChange = { editor = editor.copy(text = it) },
            label = { Text("Tekst do analizy") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Autor (opcjonalnie)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val stats = analyzeText(editor.text)
                result = """
                    STATYSTYKI
                    Wszystkie znaki (ze spacjami): ${stats.characters}
                    Znaki bez białych znaków: ${stats.nonWhitespaceCharacters}
                    Samogłoski: ${stats.vowels}
                    Cyfry: ${stats.digits}
                    Zdania: ${stats.sentences}
                    Najdłuższe słowo (długość): ${stats.longestWordLength}
                    Krótki tekst (mniej niż 5 znaków): ${if (stats.shortText) "TAK" else "NIE"}
                    Palindrom: ${if (stats.palindrome) "TAK" else "NIE"}
                    ${authorSignature(author)}
                """.trimIndent()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Analizuj tekst") }

        HorizontalDivider()
        Text("EDYCJA TEKSTU", style = MaterialTheme.typography.titleMedium)
        Button(onClick = { performEdit { it.uppercase() } }, modifier = Modifier.fillMaxWidth()) {
            Text("WIELKIE LITERY")
        }
        Button(onClick = { performEdit { it.lowercase() } }, modifier = Modifier.fillMaxWidth()) {
            Text("małe litery")
        }
        Button(onClick = { performEdit { it.reversed() } }, modifier = Modifier.fillMaxWidth()) {
            Text("Odwróć tekst")
        }
        Button(onClick = { performEdit(::removeExtraSpaces) }, modifier = Modifier.fillMaxWidth()) {
            Text("Usuń nadmiarowe spacje")
        }
        Button(onClick = { performEdit(::removeDigits) }, modifier = Modifier.fillMaxWidth()) {
            Text("Usuń cyfry")
        }

        HorizontalDivider()
        Text("HISTORIA OPERACJI", style = MaterialTheme.typography.titleMedium)
        Text("Operacje do cofnięcia: ${editor.history.size}")
        Button(
            onClick = {
                editor = undoTextEdit(editor)
                result = "Cofnięto ostatnią zmianę."
            },
            enabled = editor.history.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cofnij ostatnią zmianę") }
        OutlinedButton(
            onClick = {
                editor = EditorState()
                author = ""
                result = "Wpisz tekst i wybierz operację."
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Resetuj aplikację") }

        HorizontalDivider()
        Text("WYNIK", style = MaterialTheme.typography.titleMedium)
        Text(result)
        OutlinedButton(
            onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
                clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText("TextLab wynik", result)
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Kopiuj wynik do schowka") }
    }
}

@Composable
fun HabitPointsScreen() {
    var name by remember { mutableStateOf("") }
    var habits by remember { mutableStateOf(defaultHabits) }
    var newHabitName by remember { mutableStateOf("") }
    var newHabitPoints by remember { mutableStateOf("") }
    var nextId by remember { mutableIntStateOf(5) }
    var selectedBonus by remember { mutableStateOf("Standardowy") }
    var result by remember { mutableStateOf("Zaznacz wykonane aktywności.") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("HabitPoints", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Imię (opcjonalnie)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Text("Wykonane aktywności", style = MaterialTheme.typography.titleMedium)
        habits.forEach { habit ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(modifier = Modifier.weight(1f)) {
                    Checkbox(
                        checked = habit.completed,
                        onCheckedChange = { checked ->
                            habits = habits.map {
                                if (it.id == habit.id) it.copy(completed = checked) else it
                            }
                        }
                    )
                    Text(
                        "${habit.name} — ${habit.points} pkt",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
                if (!habit.isDefault) {
                    TextButton(onClick = { habits = habits.filter { it.id != habit.id } }) {
                        Text("Usuń")
                    }
                }
            }
        }

        HorizontalDivider()
        Text("Dodaj własny nawyk", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = newHabitName,
            onValueChange = { newHabitName = it; error = null },
            label = { Text("Nazwa nawyku") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = newHabitPoints,
            onValueChange = { newHabitPoints = it; error = null },
            label = { Text("Punkty (1–100)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }
        Button(
            onClick = {
                val validationError = validateHabit(newHabitName, newHabitPoints, habits)
                if (validationError != null) {
                    error = validationError
                } else {
                    habits = habits + Habit(nextId++, newHabitName.trim(), newHabitPoints.toInt())
                    newHabitName = ""
                    newHabitPoints = ""
                    error = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Dodaj nawyk") }

        HorizontalDivider()
        Text("Sposób punktowania", style = MaterialTheme.typography.titleMedium)
        listOf("Standardowy", "Podwójne punkty", "Premia +10").forEach { bonus ->
            Row {
                RadioButton(selected = selectedBonus == bonus, onClick = { selectedBonus = bonus })
                Text(bonus, modifier = Modifier.padding(top = 12.dp))
            }
        }
        Button(
            onClick = {
                val basePoints = totalHabitPoints(habits)
                val completedCount = habits.count { it.completed }
                val finalPoints = when (selectedBonus) {
                    "Podwójne punkty" -> applyBonus(basePoints) { it * 2 }
                    "Premia +10" -> applyBonus(basePoints) { it + 10 }
                    else -> applyBonus(basePoints) { it }
                }
                val warning = if (name.isNotBlank() && name.isShortName())
                    "\nUwaga: krótkie imię." else ""
                result = "${welcome(name)}\n" +
                    "Wykonane aktywności: $completedCount / ${habits.size}\n" +
                    "Punkty podstawowe: $basePoints\n" +
                    "Premia: $selectedBonus\n" +
                    "Punkty końcowe: $finalPoints\n" +
                    "Osiągnięcie: ${getAchievement(finalPoints)}" + warning
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Podsumuj dzień") }
        OutlinedButton(
            onClick = {
                name = ""
                habits = defaultHabits
                newHabitName = ""
                newHabitPoints = ""
                nextId = 5
                selectedBonus = "Standardowy"
                error = null
                result = "Zaznacz wykonane aktywności."
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Nowy dzień") }
        HorizontalDivider()
        Text("Wynik", style = MaterialTheme.typography.titleMedium)
        Text(result)
    }
}

@Preview(showBackground = true)
@Composable
fun TextLabPreview() {
    Lesson_3_KotlinTheme { TextLabScreen() }
}

@Preview(showBackground = true)
@Composable
fun HabitPointsPreview() {
    Lesson_3_KotlinTheme { HabitPointsScreen() }
}
