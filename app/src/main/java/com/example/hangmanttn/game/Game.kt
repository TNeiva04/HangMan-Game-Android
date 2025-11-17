package com.example.hangmanttn.game

import com.example.hangmanttn.R

enum class GameStatus {
    NOT_STARTED, STARTED, FINISHED_WON, FINISHED_LOST
}

class Game(private val mysteryWord: String) {
    private var currentGallowsState = 0
    private var currentGallowsDrawableId = R.drawable.hangman0
    private var guessWord = mysteryWord.replace(Regex("[A-Z]"), "_")
    private var usedLetters = ""

    private fun getGallowsStateDrawable(): Int {
        return when (currentGallowsState) {
            0 -> R.drawable.hangman0
            1 -> R.drawable.hangman1
            2 -> R.drawable.hangman2
            3 -> R.drawable.hangman3
            4 -> R.drawable.hangman4
            5 -> R.drawable.hangman5
            6 -> R.drawable.hangman6
            7 -> R.drawable.hangman7
            8 -> R.drawable.hangman8
            9 -> R.drawable.hangman9
            else -> -1
        }
    }

    fun checkLetter(inputLetter: String) {
        usedLetters += "$inputLetter, "
        if (mysteryWord.contains(inputLetter)) {
            guessWord = buildString {
                for (i in mysteryWord.indices) {
                    append(if (mysteryWord[i].toString() == inputLetter) inputLetter else guessWord[i])
                }
            }
        } else {
            currentGallowsState++
            currentGallowsDrawableId = getGallowsStateDrawable()
        }
    }

    fun getGallowsDrawableId() = currentGallowsDrawableId

    fun getGuessWord() = guessWord

    fun getUsedLetters(): String {
        return usedLetters
    }

    fun validateInput(input: String): Boolean {
        return input.length == 1 && !usedLetters.contains(input) && input[0].isLetter()
    }

    private fun isGameWon(): Boolean {
        return guessWord == mysteryWord
    }

    private fun isGameLost(): Boolean {
        return currentGallowsDrawableId == R.drawable.hangman9
    }

    fun isGameFinished(): Boolean {
        return isGameWon() || isGameLost()
    }

    fun getGameStatus(): GameStatus {
        return when {
            isGameWon() -> GameStatus.FINISHED_WON
            isGameLost() -> GameStatus.FINISHED_LOST
            else -> GameStatus.STARTED
        }
    }
}