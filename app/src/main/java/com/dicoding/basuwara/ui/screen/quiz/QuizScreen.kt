package com.dicoding.basuwara.ui.screen.quiz

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dicoding.basuwara.data.model.QuizModel

@Composable
fun QuizScreen(
    viewModel: QuizScreenViewModel = hiltViewModel(),
    quizType: String,
    onFinishClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val questions = viewModel.getQuizBasedOnType(quizType)
    val correctAnswer = questions.map {
        it.correctAnswer
    }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val answer = remember { mutableStateListOf<Char>() }
    var score by remember { mutableIntStateOf(0) }

    if (answer.isEmpty()) {
        repeat(questions.size) {
            answer.add(' ')
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        QuizQuestion(
            question = questions[currentQuestionIndex],
            selectedAnswer = answer[currentQuestionIndex],
            onAnswerSelected = {
                answer[currentQuestionIndex] = it
            })
        Spacer(modifier = Modifier.height(16.dp))
        QuizNavigation(
            onPreviousClick = {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--
                }
            },
            onNextClick = {
                if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                }
            },
            onFinishClick = {
                var result = 0
                answer.forEachIndexed { index, answer ->
                    if (answer == correctAnswer[index]) result++
                }
                score = result * 10
                onFinishClick(score)
            },
            isLastQuestion = currentQuestionIndex == questions.size - 1,
            isFirstQuestion = currentQuestionIndex == 0
        )
    }
}

@Composable
fun QuizQuestion(
    question: QuizModel,
    selectedAnswer: Char,
    onAnswerSelected: (Char) -> Unit
) {
    Text(
        text = question.question,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold
    )
    RadioGroup(options = question.options, selectedAnswer = selectedAnswer, onAnswerSelected = onAnswerSelected)
}

@Composable
fun QuizNavigation(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onFinishClick: () -> Unit,
    isLastQuestion: Boolean,
    isFirstQuestion: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        if (isFirstQuestion) {
            Spacer(modifier = Modifier.weight(1f))
        } else {
            QuizNavigationButton(
                text = "Previous",
                onClick = onPreviousClick,
                icon = Icons.Default.ArrowBack,
                bgColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f, true)
            )
        }

        if (isLastQuestion){
            QuizNavigationButton(
                text = "Finish",
                onClick = { onFinishClick() },
                icon = Icons.Default.Check,
                bgColor = Color.Green,
                modifier = Modifier.weight(1f, true)
            )
        } else {
            QuizNavigationButton(
                text = "Next",
                onClick = { onNextClick() },
                icon = Icons.Default.ArrowForward,
                bgColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f, true)
            )
        }
    }
}

@Composable
fun QuizNavigationButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current.density
    Row(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick() }
            .clip(MaterialTheme.shapes.medium)
            .background(bgColor)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorResource(id = android.R.color.white),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = colorResource(id = android.R.color.white),
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp / density
        )
    }
}

@Composable
fun RadioGroup(options: Map<Char, String>, selectedAnswer: Char, onAnswerSelected: (Char) -> Unit) {

    options.forEach { option ->
        Row(
            modifier = Modifier.clickable { onAnswerSelected(option.key) }
        ) {
            RadioButton(
                selected = option.key == selectedAnswer,
                onClick = {  }
            )
            Text(
                text = option.value,
                modifier = Modifier.align(Alignment.CenterVertically))
        }
    }
}
