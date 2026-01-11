package com.techgv.tictactoe.analytics

import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace

/**
 * Helper class for Firebase Performance Monitoring
 * Provides convenient methods for custom traces
 */
class PerformanceHelper {

    private val performance: FirebasePerformance = FirebasePerformance.getInstance()

    /**
     * Start a custom trace for game duration
     */
    fun startGameTrace(): Trace {
        return performance.newTrace("game_round").apply {
            start()
        }
    }

    /**
     * Stop game trace and add metadata
     */
    fun stopGameTrace(trace: Trace, winner: String, moveCount: Int) {
        trace.putAttribute("winner", winner)
        trace.putMetric("move_count", moveCount.toLong())
        trace.stop()
    }

    /**
     * Trace AI move calculation time
     */
    fun traceAIMove(difficulty: String, block: () -> Int): Int {
        val trace = performance.newTrace("ai_move_calculation").apply {
            putAttribute("difficulty", difficulty)
            start()
        }

        val result = block()

        trace.stop()
        return result
    }

    /**
     * Enable/disable performance monitoring at runtime
     */
    fun setPerformanceCollectionEnabled(enabled: Boolean) {
        performance.isPerformanceCollectionEnabled = enabled
    }
}
