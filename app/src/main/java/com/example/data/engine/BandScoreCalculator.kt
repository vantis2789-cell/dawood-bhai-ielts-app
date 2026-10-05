package com.example.data.engine

import com.example.data.model.IeltsType
import kotlin.math.floor

object BandScoreCalculator {

    fun calculateListeningBand(rawScore: Int): Double {
        val score = rawScore.coerceIn(0, 40)
        return when (score) {
            in 39..40 -> 9.0
            in 37..38 -> 8.5
            in 35..36 -> 8.0
            in 32..34 -> 7.5
            in 30..31 -> 7.0
            in 26..29 -> 6.5
            in 23..25 -> 6.0
            in 18..22 -> 5.5
            in 16..17 -> 5.0
            in 13..15 -> 4.5
            in 10..12 -> 4.0
            in 6..9 -> 3.5
            in 4..5 -> 3.0
            else -> 2.5
        }
    }

    fun calculateReadingBand(rawScore: Int, ieltsType: IeltsType = IeltsType.ACADEMIC): Double {
        val score = rawScore.coerceIn(0, 40)
        return if (ieltsType == IeltsType.ACADEMIC) {
            when (score) {
                in 39..40 -> 9.0
                in 37..38 -> 8.5
                in 35..36 -> 8.0
                in 33..34 -> 7.5
                in 30..32 -> 7.0
                in 27..29 -> 6.5
                in 23..26 -> 6.0
                in 19..22 -> 5.5
                in 15..18 -> 5.0
                in 13..14 -> 4.5
                in 10..12 -> 4.0
                in 8..9 -> 3.5
                in 6..7 -> 3.0
                else -> 2.5
            }
        } else {
            // General Training Reading
            when (score) {
                40 -> 9.0
                39 -> 8.5
                in 37..38 -> 8.0
                36 -> 7.5
                in 34..35 -> 7.0
                in 32..33 -> 6.5
                in 30..31 -> 6.0
                in 27..29 -> 5.5
                in 23..26 -> 5.0
                in 19..22 -> 4.5
                in 15..18 -> 4.0
                in 12..14 -> 3.5
                in 8..11 -> 3.0
                else -> 2.5
            }
        }
    }

    fun calculateRubricBand(
        c1: Double,
        c2: Double,
        c3: Double,
        c4: Double
    ): Double {
        val avg = (c1 + c2 + c3 + c4) / 4.0
        return roundToIeltsBand(avg)
    }

    fun calculateOverallBand(
        listening: Double,
        reading: Double,
        writing: Double,
        speaking: Double
    ): Double {
        val avg = (listening + reading + writing + speaking) / 4.0
        return roundToIeltsBand(avg)
    }

    /**
     * Official IELTS Rounding Algorithm:
     * e.g. 6.25 -> 6.5
     * e.g. 6.75 -> 7.0
     * e.g. 6.125 -> 6.0
     * e.g. 6.375 -> 6.5
     */
    fun roundToIeltsBand(rawAverage: Double): Double {
        val floorVal = floor(rawAverage)
        val remainder = rawAverage - floorVal

        val roundedRemainder = when {
            remainder < 0.25 -> 0.0
            remainder < 0.75 -> 0.5
            else -> 1.0
        }
        return (floorVal + roundedRemainder).coerceIn(1.0, 9.0)
    }
}
