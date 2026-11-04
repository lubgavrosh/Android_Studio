package com.example.lesson_3_kotlin

data class TextStats(
    val characters: Int,
    val nonWhitespaceCharacters: Int,
    val vowels: Int,
    val digits: Int,
    val sentences: Int,
    val longestWordLength: Int,
    val shortText: Boolean,
    val palindrome: Boolean
)

data class EditorState(
    val text: String = "",
    val history: List<String> = emptyList()
)

fun analyzeText(text: String): TextStats {
    val words = Regex("[\\p{L}\\p{N}]+").findAll(text).map { it.value }.toList()
    val normalized = text.lowercase().filter { it.isLetterOrDigit() }
    return TextStats(
        characters = text.length,
        nonWhitespaceCharacters = text.count { !it.isWhitespace() },
        vowels = text.count { it.lowercaseChar() in "aąeęioóuy" },
        digits = text.count { it.isDigit() },
        sentences = text.split(Regex("[.!?]+"))
            .count { it.isNotBlank() },
        longestWordLength = words.maxOfOrNull { it.length } ?: 0,
        shortText = text.length < 5,
        palindrome = normalized.isNotEmpty() && normalized == normalized.reversed()
    )
}

fun authorSignature(author: String = "Gość"): String =
    "Autor: ${author.trim().ifBlank { "Gość" }}"

fun transformText(text: String, operation: (String) -> String): String = operation(text)

fun removeExtraSpaces(text: String): String = text.trim().replace(Regex("\\s+"), " ")
fun removeDigits(text: String): String = text.filterNot { it.isDigit() }

fun editText(state: EditorState, operation: (String) -> String): EditorState {
    val edited = operation(state.text)
    return if (edited == state.text) state
    else EditorState(edited, state.history + state.text)
}

fun undoTextEdit(state: EditorState): EditorState =
    if (state.history.isEmpty()) state
    else EditorState(state.history.last(), state.history.dropLast(1))
