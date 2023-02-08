package com.example.cup2.util

import com.example.cup2.data.SampleData
import kotlin.math.abs

fun getRandomNumberOfAnswers(quantity: Int, size: Int): List<Int> {
    val list = mutableListOf<Int>()
    var remainder = quantity
    repeat(size - 1) {
        val answers = (0..remainder).random()
        list.add(answers)
        remainder -= answers
    }
    list.add(remainder)
    return list.shuffled()
}

fun isCorrectQuizOption(text: String): Boolean = text == SampleData.quizOptions.first()

fun formatNumberOfAnswers(quantity: Int): String = if (quantity == 1) "answer" else "answers"

fun isCorrectElementMatching(words: List<String>, iconNames: List<String>, connections: List<Pair<Int, Int>>): Boolean {
    val elements = SampleData.matchedElements
    for (pair in connections) {
        if (elements.map { it.first }.indexOf(words[pair.first]) != elements.map { it.second.name }.indexOf(iconNames[pair.second])) {
            return false
        }
    }
    return true
}

fun getRandomSum(size: Int): List<Int> {
    if (size >= 3) {
        val list = List(size - 1) { (0..10).random() }
        return (list + abs(list[0] - list[1])).shuffled()
    } else throw Exception("Too small size")
}