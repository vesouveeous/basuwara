package com.dicoding.basuwara.data.model

data class QuizModel(
    val question: String,
    val options: Map<Char, String>,
    val correctAnswer: Char,
)