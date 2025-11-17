package com.example.hangmanttn.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangmanttn.R
import java.util.Locale

@Composable
private fun GallowsImage(resId: Int, modifier: Modifier = Modifier, tint: Color = Color.Black) {
    Image(
        painter = painterResource(resId),
        contentDescription = null,
        colorFilter = ColorFilter.tint(tint),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun GuessWordText(word: String, modifier: Modifier = Modifier) {
    Text(
        text = word.uppercase(Locale.getDefault()),
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Bold,
        letterSpacing = 8.sp,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun UsedLettersText(usedLetters: String, modifier: Modifier) {
    Column {
        Text(
            text = stringResource(id = R.string.used_letters),
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Light,
            color = colorScheme.primary,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = usedLetters.uppercase(Locale.getDefault()),
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Light,
            color = colorScheme.primary,
            modifier = modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LetterInputField(
    textFieldValue: String,
    buttonText: String,
    buttonEnabled: Boolean,
    isError: Boolean,
    onButtonClick: () -> Unit,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            singleLine = true,
            shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
            label = { Text(text = stringResource(id = R.string.enter_letter)) },
            isError = isError,
            supportingText = {
                if (isError) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Invalid input")
                        Text(
                            "Char limit is 1 (${textFieldValue.length})"
                        )
                    }

                }
            }
        )

        Button(
            onClick = onButtonClick,
            enabled = buttonEnabled,
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 8.dp,
                bottomStart = 0.dp,
                bottomEnd = 8.dp
            ),
            modifier = Modifier
                .defaultMinSize(minHeight = TextFieldDefaults.MinHeight)
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
fun GameLayout(
    mysteryWord: String,
    modifier: Modifier = Modifier,
    onEnd: (GameStatus) -> Unit
) {
    var inputLetter by remember { mutableStateOf("") }
    val game = remember { Game(mysteryWord) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        GallowsImage(resId = R.drawable.hangman0, tint = colorScheme.primary)
        GuessWordText(
            word = game.getGuessWord(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        LetterInputField(
            textFieldValue = inputLetter,
            buttonText = stringResource(id = R.string.check),
            onButtonClick = {
                game.checkLetter(inputLetter)
                if (game.isGameFinished()) {
                    onEnd(game.getGameStatus())
                }
                inputLetter = ""
            },
            onValueChange = {
                inputLetter = it.uppercase()
            },
            isError = inputLetter.isNotEmpty() && !game.validateInput(inputLetter),
            buttonEnabled = inputLetter.isNotEmpty() && game.validateInput(inputLetter)
        )
        UsedLettersText(
            usedLetters = game.getUsedLetters(),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(
    showBackground = true, showSystemUi = true, device = "id:pixel_8"
)
@Composable
fun GameLayoutPreview() {
    GameLayout(mysteryWord = "HANGMAN", onEnd = {})
}