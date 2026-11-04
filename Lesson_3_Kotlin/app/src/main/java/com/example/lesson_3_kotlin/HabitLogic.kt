package com.example.lesson_3_kotlin

data class Habit(
    val id: Int,
    val name: String,
    val points: Int,
    val isDefault: Boolean = false,
    val completed: Boolean = false
)

val defaultHabits = listOf(
    Habit(1, "Czytanie książki", 10, isDefault = true),
    Habit(2, "Ćwiczenia fizyczne", 20, isDefault = true),
    Habit(3, "Nauka programowania", 15, isDefault = true),
    Habit(4, "Spacer", 5, isDefault = true)
)

fun validateHabit(name: String, pointsText: String, habits: List<Habit>): String? {
    val trimmedName = name.trim()
    val points = pointsText.toIntOrNull()
    return when {
        trimmedName.isEmpty() -> "Wpisz nazwę nawyku."
        habits.any { it.name.equals(trimmedName, ignoreCase = true) } ->
            "Nawyk o tej nazwie już istnieje."
        points == null || points !in 1..100 -> "Punkty muszą być liczbą całkowitą od 1 do 100."
        else -> null
    }
}

fun totalHabitPoints(habits: List<Habit>): Int =
    habits.filter { it.completed }.sumOf { it.points }

fun applyBonus(points: Int, bonus: (Int) -> Int): Int = bonus(points)

fun getAchievement(points: Int): String = when {
    points >= 100 -> "Mistrz nawyków"
    points >= 50 -> "Bardzo dobry dzień"
    points >= 20 -> "Dobry początek"
    else -> "Każdy krok ma znaczenie"
}

fun welcome(name: String = "Gość"): String = "Witaj, ${name.trim().ifBlank { "Gość" }}!"
fun String.isShortName(): Boolean = trim().length < 3
