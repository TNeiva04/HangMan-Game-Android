package com.example.hangmanttn

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangmanttn.game.GameLayout
import com.example.hangmanttn.game.GameStatus
import com.example.hangmanttn.ui.theme.HangmanTTNTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }

    @Composable
    fun GameStartButton(
        text: String,
        enabled: Boolean,
        textColor: Color,
        modifier: Modifier,
        onClick: () -> Unit
    ) {
        TextButton(onClick = onClick, enabled = enabled) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = if (enabled) textColor else textColor.copy(alpha = 0.5f),
                fontFamily = FontFamily.Cursive,
                fontSize = 36.sp,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
    }

    @Preview(name = "GameStartButtonPreview")
    @Composable
    fun GameStartButtonPreview() {
        HangmanTTNTheme {
            GameStartButton("Let's start!", true, textColor = Color.Red, modifier = Modifier) {
            }
        }
    }

    @Composable
    fun MainScreen(modifier: Modifier = Modifier) {
        val wordsArray = stringArrayResource(id = R.array.words)
        var gameStatus: GameStatus by remember { mutableStateOf(GameStatus.NOT_STARTED) }
        var mysteryWord = ""
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Log.d("MainScreen", "MainScreenComposable (${gameStatus}): $mysteryWord")
                GameStartButton(
                    text = stringResource(R.string.start_text),
                    enabled = true,
                    textColor = colorScheme.primary,
                    modifier = modifier
                ) {
                    if (gameStatus == GameStatus.NOT_STARTED) {
                        mysteryWord = getMysteryWord(wordsArray)
                        gameStatus = GameStatus.STARTED
                    }
                }
                if (gameStatus != GameStatus.NOT_STARTED) {
                    when (gameStatus) {
                        GameStatus.STARTED -> {
                            GameLayout(
                                mysteryWord = mysteryWord,
                                onEnd = { gameStatus = it }
                            )
                        }
                        GameStatus.FINISHED_WON -> {
                            Text(
                                text = stringResource(R.string.won_text, mysteryWord),
                                textAlign = TextAlign.Center,
                                color = colorScheme.primary,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                            LaunchedEffect(Unit) {
                                delay(2000) // 2 s delay
                                gameStatus = GameStatus.NOT_STARTED
                            }
                        }

                        GameStatus.FINISHED_LOST -> {
                            Text(
                                text = stringResource(R.string.lost_text, mysteryWord),
                                textAlign = TextAlign.Center,
                                color = colorScheme.primary,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(16.dp)
                            )
                            LaunchedEffect(Unit) {
                                delay(2000) // 2 s delay
                                gameStatus = GameStatus.NOT_STARTED
                            }

                        }

                        else -> {}
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    fun MainScreenPreview() {
        HangmanTTNTheme {
            MainScreen()
        }
    }

    private fun getMysteryWord(words: Array<String>): String {
        return words.random().uppercase()
    }

}