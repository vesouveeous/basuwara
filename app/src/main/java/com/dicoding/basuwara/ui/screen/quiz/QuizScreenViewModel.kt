package com.dicoding.basuwara.ui.screen.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.basuwara.data.model.QuizModel
import com.dicoding.basuwara.data.repository.AuthRepository
import com.dicoding.basuwara.util.QuizQuestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizScreenViewModel @Inject constructor(
) : ViewModel() {
    fun getQuizBasedOnType(quizType: String): List<QuizModel> {
        return when(quizType) {
            "jawa" -> QuizQuestions.javaneseQuiz
            "sunda" -> emptyList()
            else -> emptyList()
        }
    }
}