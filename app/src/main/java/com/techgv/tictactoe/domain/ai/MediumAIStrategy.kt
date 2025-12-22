package com.techgv.tictactoe.domain.ai

import com.techgv.tictactoe.data.model.Player
import kotlin.random.Random

/**
 * Medium AI: Uses Minimax but makes suboptimal moves ~30% of the time.
 * Provides a balanced challenge.
 */
class MediumAIStrategy(
    private val hardStrategy: HardAIStrategy = HardAIStrategy(),
    private val easyStrategy: EasyAIStrategy = EasyAIStrategy(),
    private val mistakeProbability: Float = 0.3f
) : AIStrategy {

    override fun findBestMove(board: List<Player>, aiPlayer: Player): Int {
        return if (Random.nextFloat() < mistakeProbability) {
            // Make a random move (~30% of the time)
            easyStrategy.findBestMove(board, aiPlayer)
        } else {
            // Make the optimal move (~70% of the time)
            hardStrategy.findBestMove(board, aiPlayer)
        }
    }
}
