package com.example.lesson2_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lesson2_kotlin.ui.theme.Lesson2_kotlinTheme
import kotlinx.coroutines.delay
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lesson2_kotlinTheme {
                MainMenu()
                }
            }
        }
    }


@Composable
fun MainMenu() {
    var currentScreen by remember { mutableStateOf("MENU") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (currentScreen != "MENU") {
            OutlinedButton(onClick = { currentScreen = "MENU" }) {
                Text("← Menu główne")
            }
        }
        when (currentScreen) {
            "QUIZ" -> QuizScreen()
            "FOOD" -> FoodOrderScreen()
            "TRIP" -> TripBudgetScreen()
            else -> {
                Text("Laboratorium Kotlin", style = MaterialTheme.typography.headlineMedium)

                Button(onClick = { currentScreen = "QUIZ" }, modifier = Modifier.fillMaxWidth()) {
                    Text("1. QuizMaster")
                }
                Button(onClick = { currentScreen = "FOOD" }, modifier = Modifier.fillMaxWidth()) {
                    Text("2. FoodOrder")
                }
                Button(onClick = { currentScreen = "TRIP" }, modifier = Modifier.fillMaxWidth()) {
                    Text("3. TripBudget")
                }
            }
        }
    }
}

// ====================== QUIZMASTER  ======================

data class Question(
    val category: String,
    val difficulty: String,
    val text: String,
    val answers: List<String>,
    val correctIndex: Int
)

fun start(): String = "Witaj w QuizMaster! Wybierz odpowiedź."
fun getFeedback(correct: Boolean): String =
    if (correct) "Poprawna odpowiedź!" else "Niestety, niepoprawna odpowiedź."
fun calculateScore(correct: Int, points: Int): Int = correct * points
fun isCorrect(selected: Int, expected: Int) = selected == expected
fun calculatePercentage(correct: Int, total: Int): Int =
    if (total > 0) correct * 100 / total else 0
fun pointsForDifficulty(difficulty: String): Int = when (difficulty) {
    "Łatwy" -> 10
    "Średni" -> 20
    "Trudny" -> 30
    else -> 10
}

private fun q(category: String, level: String, text: String,
              correct: String, wrong1: String, wrong2: String, wrong3: String): Question {

    val answers = listOf(correct, wrong1, wrong2, wrong3).shuffled()
    return Question(category, level, text, answers, answers.indexOf(correct))
}

fun createQuestions(): List<Question> = listOf(
    // KOTLIN — ŁATWY
    q("Kotlin", "Łatwy", "Jakim słowem deklaruje się funkcję?", "fun", "def", "function", "method"),
    q("Kotlin", "Łatwy", "Jak deklaruje się zmienną tylko do odczytu?", "val", "var", "let", "constvar"),
    q("Kotlin", "Łatwy", "Typ wyniku 5 > 3 to:", "Boolean", "Int", "String", "Double"),
    q("Kotlin", "Łatwy", "Który zapis oznacza typ tekstowy?", "String", "Int", "Boolean", "Float"),
    q("Kotlin", "Łatwy", "Który operator dodaje liczby?", "+", "*", "&&", "=="),
    q("Kotlin", "Łatwy", "Która deklaracja pozwala zmieniać wartość?", "var", "val", "fun", "class"),
    // KOTLIN — ŚREDNI
    q("Kotlin", "Średni", "Która funkcja jest jednowyrażeniowa?", "fun add(a:Int,b:Int)=a+b", "function add(a,b)", "def add(a,b)", "fun = add(a,b)"),
    q("Kotlin", "Średni", "Jak przekazać argument nazwany?", "greet(name = \"Anna\")", "greet->name", "greet:name", "greet('Anna')"),
    q("Kotlin", "Średni", "Co zwraca listOf(1,2,3).filter { it > 1 }?", "[2, 3]", "[1]", "[1, 2, 3]", "3"),
    q("Kotlin", "Średni", "Jak wywołać bezpiecznie metodę na nullable?", "?.", "!!.", "::", "->"),
    q("Kotlin", "Średni", "Co oznacza vararg?", "Dowolną liczbę argumentów", "Argument opcjonalny", "Typ generyczny", "Pętlę"),
    q("Kotlin", "Średni", "Co robi operator ?: ?", "Zwraca wartość zastępczą dla null", "Tworzy klasę", "Rozpoczyna pętlę", "Porównuje referencje"),
    // KOTLIN — TRUDNY
    q("Kotlin", "Trudny", "Która kolekcja zawiera unikatowe elementy?", "Set", "List", "Array", "String"),
    q("Kotlin", "Trudny", "Co zwraca listOf(1,2,3).fold(0) { a,b -> a+b }?", "6", "3", "0", "123"),
    q("Kotlin", "Trudny", "Do czego służy inline w Kotlinie?", "Umożliwia wstawienie kodu funkcji w miejscu wywołania", "Tworzy interfejs", "Wyłącza null safety", "Uruchamia wątek"),
    q("Kotlin", "Trudny", "Jaki wynik daje (1..5).map { it * 2 }.last()?", "10", "5", "8", "15"),
    q("Kotlin", "Trudny", "Który modyfikator ogranicza dziedziczenie klasy?", "final", "open", "abstract", "override"),
    q("Kotlin", "Trudny", "Co robi suspend w deklaracji funkcji?", "Pozwala zawieszać wykonanie w korutynie", "Tworzy singleton", "Zapewnia synchronizację", "Kończy proces"),
    // INFORMATYKA — ŁATWY
    q("Informatyka", "Łatwy", "Co oznacza CPU?", "Procesor", "Pamięć RAM", "Dysk", "Karta graficzna"),
    q("Informatyka", "Łatwy", "Ile bitów ma bajt?", "8", "4", "16", "32"),
    q("Informatyka", "Łatwy", "Co służy do wyświetlania stron WWW?", "Przeglądarka", "Kompilator", "Router", "Edytor dźwięku"),
    q("Informatyka", "Łatwy", "Jaki skrót oznacza pamięć operacyjną?", "RAM", "SSD", "CPU", "USB"),
    q("Informatyka", "Łatwy", "Który system jest mobilny?", "Android", "BIOS", "HTML", "SQL"),
    q("Informatyka", "Łatwy", "Jakie rozszerzenie często ma plik tekstowy?", ".txt", ".png", ".mp3", ".exe"),
    // INFORMATYKA — ŚREDNI
    q("Informatyka", "Średni", "Co oznacza HTTP?", "Protokół przesyłania hipertekstu", "System plików", "Format obrazu", "Język programowania"),
    q("Informatyka", "Średni", "Jaki kod statusu HTTP oznacza brak zasobu?", "404", "200", "201", "301"),
    q("Informatyka", "Średni", "Która struktura działa według LIFO?", "Stos", "Kolejka", "Graf", "Drzewo"),
    q("Informatyka", "Średni", "Do czego służy DNS?", "Zamienia nazwy domen na adresy IP", "Kompiluje kod", "Szyfruje dysk", "Przechowuje hasła"),
    q("Informatyka", "Średni", "Jaki język wykorzystuje zapytania SELECT?", "SQL", "CSS", "XML", "HTML"),
    q("Informatyka", "Średni", "Co oznacza O(n) przy rosnącym n?", "Złożoność liniową", "Złożoność stałą", "Złożoność kwadratową", "Brak złożoności"),
    // INFORMATYKA — TRUDNY
    q("Informatyka", "Trudny", "Jaka jest złożoność wyszukiwania binarnego?", "O(log n)", "O(n²)", "O(n)", "O(1)"),
    q("Informatyka", "Trudny", "Który protokół gwarantuje kolejność danych?", "TCP", "UDP", "ICMP", "ARP"),
    q("Informatyka", "Trudny", "Ile różnych wartości mieści 8 bitów?", "256", "8", "128", "1024"),
    q("Informatyka", "Trudny", "Czym jest deadlock?", "Wzajemnym blokowaniem zasobów", "Błędem składni", "Formatowaniem dysku", "Sortowaniem tablicy"),
    q("Informatyka", "Trudny", "Co zapewnia własność ACID: Atomicity?", "Transakcję w całości albo wcale", "Szyfrowanie", "Kompresję", "Indeksowanie"),
    q("Informatyka", "Trudny", "Który algorytm zwykle ma średnią złożoność O(n log n)?", "Merge sort", "Bubble sort", "Linear search", "Selection sort")
)

@Composable
fun QuizScreen() {
    var category by remember { mutableStateOf("Kotlin") }
    var difficulty by remember { mutableStateOf("Łatwy") }
    var questions by remember { mutableStateOf(emptyList<Question>()) }
    var questionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableIntStateOf(-1) }
    var correctAnswers by remember { mutableIntStateOf(0) }
    var answered by remember { mutableStateOf(false) }
    var finished by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(15) }
    var message by remember { mutableStateOf(start()) }


    LaunchedEffect(questions, questionIndex, answered, finished) {
        if (questions.isNotEmpty() && !answered && !finished) {
            secondsLeft = 15
            while (secondsLeft > 0) {
                delay(1_000L)
                secondsLeft--
            }
            if (!answered && !finished) {
                answered = true
                message = "Czas minął! 0 punktów."
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("QuizMaster", style = MaterialTheme.typography.headlineMedium)
        if (questions.isEmpty()) {
            Text("Wybierz kategorię:", style = MaterialTheme.typography.titleMedium)
            listOf("Kotlin", "Informatyka").forEach { option ->
                ChoiceRadio(option, category == option) { category = option }
            }
            Text("Wybierz poziom trudności:", style = MaterialTheme.typography.titleMedium)
            listOf("Łatwy", "Średni", "Trudny").forEach { option ->
                ChoiceRadio("$option (${pointsForDifficulty(option)} pkt / pytanie)", difficulty == option) {
                    difficulty = option
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    questions = createQuestions()
                        .filter { it.category == category && it.difficulty == difficulty }
                        .shuffled().take(5)
                    questionIndex = 0
                    selectedAnswer = -1
                    correctAnswers = 0
                    answered = false
                    finished = false
                    secondsLeft = 15
                    message = start()
                }
            ) { Text("Rozpocznij quiz") }
        } else if (!finished) {
            val question = questions[questionIndex]
            Text("Pytanie ${questionIndex + 1} z ${questions.size}", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(
                progress = { (questionIndex + 1).toFloat() / questions.size },
                modifier = Modifier.fillMaxWidth()
            )
            Text("Czas: $secondsLeft s", style = MaterialTheme.typography.titleMedium)
            LinearProgressIndicator(
                progress = { secondsLeft / 15f },
                modifier = Modifier.fillMaxWidth()
            )
            Text(question.text, style = MaterialTheme.typography.titleLarge)
            question.answers.forEachIndexed { index, option ->
                Row {
                    RadioButton(
                        selected = selectedAnswer == index,
                        enabled = !answered,
                        onClick = { if (!answered) selectedAnswer = index }
                    )
                    Text(option, modifier = Modifier.padding(top = 12.dp))
                }
            }
            if (!answered) {
                Button(
                    onClick = {
                        val correct = isCorrect(selectedAnswer, question.correctIndex)
                        if (correct) correctAnswers++
                        message = getFeedback(correct) +
                                if (correct) " +${pointsForDifficulty(difficulty)} pkt" else " 0 pkt"
                        answered = true
                    },
                    enabled = selectedAnswer >= 0,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Sprawdź odpowiedź") }
            } else {
                Text(message)
                Text("Poprawna odpowiedź: ${question.answers[question.correctIndex]}")
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (questionIndex < questions.lastIndex) {
                            questionIndex++
                            selectedAnswer = -1
                            answered = false
                            message = start()
                        } else {
                            finished = true
                        }
                    }
                ) {
                    Text(if (questionIndex == questions.lastIndex) "Zobacz wynik" else "Następne pytanie")
                }
            }
            HorizontalDivider()
            Text("Aktualne punkty: ${calculateScore(correct = correctAnswers, points = pointsForDifficulty(difficulty))}")
        } else {
            val score = calculateScore(correct = correctAnswers, points = pointsForDifficulty(difficulty))
            val percentage = calculatePercentage(correct = correctAnswers, total = questions.size)
            Text("Koniec quizu!", style = MaterialTheme.typography.headlineSmall)
            Text("Kategoria: $category • poziom: $difficulty")
            Text("Poprawne odpowiedzi: $correctAnswers / ${questions.size}")
            Text("Zdobyte punkty: $score")
            Text("Skuteczność: $percentage%")
            Text(when {
                percentage == 100 -> "Doskonale!"
                percentage >= 60 -> "Dobry wynik!"
                else -> "Spróbuj ponownie!"
            })
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                questions = emptyList()
                questionIndex = 0
                selectedAnswer = -1
                correctAnswers = 0
                answered = false
                finished = false
                secondsLeft = 15
                message = start()
            }) { Text("Rozpocznij ponownie") }
        }
    }
}

@Composable
private fun ChoiceRadio(label: String, checked: Boolean, onClick: () -> Unit) {
    Row {
        RadioButton(selected = checked, onClick = onClick)
        Text(label, modifier = Modifier.padding(top = 12.dp))
    }
}

// ====================== FOODORDER ======================

fun startOrder(): String = "Skonfiguruj zamówienie."
fun getFoodPrice(food: String): Int = when (food) {
    "Burger" -> 24
    "Pizza" -> 32
    "Sałatka" -> 19
    else -> 0
}
fun calculateExtras(quantity: Int, extraCheese: Boolean): Int =
    if (extraCheese) quantity * 4 else 0
fun calculateOrder(food: String, quantity: Int, extraCheese: Boolean, delivery: Boolean): Int {
    val foodCost = getFoodPrice(food) * quantity
    val extras = calculateExtras(quantity = quantity, extraCheese = extraCheese)
    val deliveryCost = if (delivery) 8 else 0
    return foodCost + extras + deliveryCost
}
fun isValidQuantity(quantity: Int): Boolean = quantity in 1..10

// Zwraca kwotę rabatu w złotych, a nie kwotę po rabacie.
fun calculateDiscount(total: Int, discount: Int): Int {
    require(discount in 0..30) { "Rabat musi wynosić od 0% do 30%." }
    return total * discount / 100
}

@Composable
fun FoodOrderScreen() {
    var food by remember { mutableStateOf("Burger") }
    var quantityText by remember { mutableStateOf("1") }
    var extraCheese by remember { mutableStateOf(false) }
    var delivery by remember { mutableStateOf(false) }
    var discountText by remember { mutableStateOf("0") }
    var result by remember { mutableStateOf(startOrder()) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("FoodOrder", style = MaterialTheme.typography.headlineMedium)
        Text("Wybierz danie:")
        listOf("Burger", "Pizza", "Sałatka").forEach { item ->
            ChoiceRadio("$item — ${getFoodPrice(item)} zł", food == item) { food = item }
        }
        OutlinedTextField(
            value = quantityText,
            onValueChange = { quantityText = it },
            label = { Text("Liczba porcji (1–10)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Checkbox(checked = extraCheese, onCheckedChange = { extraCheese = it })
            Text("Dodatkowy ser (+4 zł/porcja)", modifier = Modifier.padding(top = 12.dp))
        }
        Row {
            Checkbox(checked = delivery, onCheckedChange = { delivery = it })
            Text("Dostawa (+8 zł)", modifier = Modifier.padding(top = 12.dp))
        }
        OutlinedTextField(
            value = discountText,
            onValueChange = { discountText = it },
            label = { Text("Rabat procentowy (0–30%)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val quantity = quantityText.toIntOrNull()
                val discount = discountText.toIntOrNull()
                result = when {
                    quantity == null || !isValidQuantity(quantity) -> "Liczba porcji musi wynosić 1–10."
                    discount == null || discount !in 0..30 -> "Rabat musi być liczbą całkowitą od 0 do 30%."
                    else -> {
                        val total = calculateOrder(food, quantity, extraCheese, delivery)
                        val discountAmount = calculateDiscount(total, discount)
                        "Zamówienie:\n$food × $quantity\n" +
                                "Dodatkowy ser: ${if (extraCheese) "TAK" else "NIE"}\n" +
                                "Dostawa: ${if (delivery) "TAK" else "NIE"}\n" +
                                "Cena przed rabatem: $total zł\n" +
                                "Rabat ($discount%): -$discountAmount zł\n" +
                                "Do zapłaty: ${total - discountAmount} zł"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Oblicz zamówienie") }
        OutlinedButton(
            onClick = {
                food = "Burger"
                quantityText = "1"
                extraCheese = false
                delivery = false
                discountText = "0"
                result = startOrder()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Nowe zamówienie") }
        HorizontalDivider()
        Text(result)
    }
}

// ====================== TRIPBUDGET  ======================

data class TripResult(val fuelLiters: Double, val totalCost: Double,
                      val costPerPerson: Double, val routeCategory: String)

fun routeCategory(distanceKm: Double): String = when {
    distanceKm <= 100.0 -> "Krótka"
    distanceKm <= 500.0 -> "Średnia"
    else -> "Długa"
}

fun calculateTrip(distanceKm: Double, consumptionPer100Km: Double,
                  fuelPricePerLiter: Double, people: Int): TripResult {
    require(distanceKm.isFinite() && distanceKm > 0)
    require(consumptionPer100Km.isFinite() && consumptionPer100Km > 0)
    require(fuelPricePerLiter.isFinite() && fuelPricePerLiter > 0)
    require(people in 1..8)
    val liters = distanceKm * consumptionPer100Km / 100.0
    val total = liters * fuelPricePerLiter
    require(liters.isFinite() && total.isFinite())
    return TripResult(liters, total, total / people, routeCategory(distanceKm))
}

private fun parsePositiveNumber(value: String): Double? =
    value.trim().replace(',', '.').toDoubleOrNull()?.takeIf { it.isFinite() && it > 0 }

private fun money(value: Double): String = String.format(Locale.forLanguageTag("pl-PL"), "%.2f", value)

@Composable
fun TripBudgetScreen() {
    var distance by remember { mutableStateOf("") }
    var consumption by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var people by remember { mutableStateOf("1") }
    var result by remember { mutableStateOf("Wpisz dane i oblicz koszt podróży.") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("TripBudget", style = MaterialTheme.typography.headlineMedium)
        Text("Kalkulator kosztów podróży samochodem")
        OutlinedTextField(
            value = distance, onValueChange = { distance = it },
            label = { Text("Dystans (km)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = consumption, onValueChange = { consumption = it },
            label = { Text("Średnie spalanie (l/100 km)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price, onValueChange = { price = it },
            label = { Text("Cena paliwa (zł/l)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = people, onValueChange = { people = it },
            label = { Text("Liczba osób (1–8)") }, singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val d = parsePositiveNumber(distance)
                val c = parsePositiveNumber(consumption)
                val p = parsePositiveNumber(price)
                val n = people.trim().toIntOrNull()
                result = if (d == null || c == null || p == null || n == null || n !in 1..8) {
                    "Błędne dane. Dystans, spalanie i cena muszą być dodatnie, a liczba osób musi wynosić 1–8."
                } else {
                    try {
                        val trip = calculateTrip(d, c, p, n)
                        "PODSUMOWANIE PODRÓŻY\n" +
                                "Dystans: ${money(d)} km\n" +
                                "Kategoria trasy: ${trip.routeCategory}\n" +
                                "Potrzebne paliwo: ${money(trip.fuelLiters)} l\n" +
                                "Koszt całkowity: ${money(trip.totalCost)} zł\n" +
                                "Liczba osób: $n\n" +
                                "Koszt na osobę: ${money(trip.costPerPerson)} zł"
                    } catch (_: IllegalArgumentException) {
                        "Wartości są zbyt duże lub niepoprawne."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Oblicz koszt podróży") }
        OutlinedButton(
            onClick = {
                distance = ""
                consumption = ""
                price = ""
                people = "1"
                result = "Wpisz dane i oblicz koszt podróży."
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Wyczyść dane") }
        HorizontalDivider()
        Text(result)
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MaterialTheme { MainMenu() }
}