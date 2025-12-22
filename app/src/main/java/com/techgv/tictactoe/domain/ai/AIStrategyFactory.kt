package com.techgv.tictactoe.domain.ai

import com.techgv.tictactoe.data.model.AIDifficulty

/**
 * Factory to create AI strategies based on difficulty level.
 */
object AIStrategyFactory {
    fun create(difficulty: AIDifficulty): AIStrategy {
        return when (difficulty) {
            AIDifficulty.EASY -> EasyAIStrategy()
            AIDifficulty.MEDIUM -> MediumAIStrategy()
            AIDifficulty.HARD -> HardAIStrategy()
        }
    }
}
