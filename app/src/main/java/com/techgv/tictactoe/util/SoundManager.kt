package com.techgv.tictactoe.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.techgv.tictactoe.R

/**
 * Manages sound effects for the game using SoundPool.
 * SoundPool is optimized for short, low-latency sound effects.
 */
class SoundManager(context: Context) {

    private val soundPool: SoundPool
    private var clickSoundId: Int = 0
    private var winSoundId: Int = 0
    private var clickSoundLoaded: Boolean = false
    private var winSoundLoaded: Boolean = false

    init {
        // Build SoundPool with audio attributes for game sounds
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(2) // Allow 2 simultaneous sounds
            .setAudioAttributes(audioAttributes)
            .build()

        // Set load complete listener
        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                when (sampleId) {
                    clickSoundId -> clickSoundLoaded = true
                    winSoundId -> winSoundLoaded = true
                }
            }
        }

        // Load sounds
        clickSoundId = soundPool.load(context, R.raw.cell_click, 1)
        winSoundId = soundPool.load(context, R.raw.win_sound, 1)
    }

    /**
     * Plays the cell click sound effect.
     * @param volume Volume level from 0.0f to 1.0f (default: 1.0f)
     */
    fun playClickSound(volume: Float = 1.0f) {
        if (clickSoundLoaded && clickSoundId != 0) {
            soundPool.play(
                clickSoundId,
                volume, // Left volume
                volume, // Right volume
                1, // Priority
                0, // Loop (0 = no loop)
                1.0f, // Playback rate (1.0 = normal)
            )
        }
    }

    /**
     * Plays the win celebration sound effect.
     * @param volume Volume level from 0.0f to 1.0f (default: 1.0f)
     */
    fun playWinSound(volume: Float = 1.0f) {
        if (winSoundLoaded && winSoundId != 0) {
            soundPool.play(
                winSoundId,
                volume, // Left volume
                volume, // Right volume
                1, // Priority
                0, // Loop (0 = no loop)
                1.0f, // Playback rate (1.0 = normal)
            )
        }
    }

    /**
     * Releases the SoundPool resources.
     * Must be called when the sound manager is no longer needed to prevent memory leaks.
     */
    fun release() {
        soundPool.release()
        clickSoundLoaded = false
        winSoundLoaded = false
    }
}
