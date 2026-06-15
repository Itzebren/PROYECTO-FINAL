package com.android.mobile.games.app.games.codemerge.model

import java.util.UUID

data class MergeRunResult(
    val id: String = UUID.randomUUID().toString(),
    val playerName: String,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
)

enum class CodeLevel(val score: Int, val radiusScale: Float) {
    NULO(10, 1.0f),           // Nivel 1: nulo.png
    BUG(20, 1.2f),            // Nivel 2: bug.png
    ERROR(30, 1.44f),         // Nivel 3: error.png
    IPN_CARD(40, 1.73f),      // Nivel 4: ipn_card.png
    CAFE(50, 2.07f),          // Nivel 5: cafe_con_vida.png
    PROJECT_COMPLETE(100, 2.48f); // Nivel 6: proyecto_compilado.png

    fun next(): CodeLevel? = entries.getOrNull(ordinal + 1)
}

data class CodeElement(
    val id: String = UUID.randomUUID().toString(),
    val x: Float,
    val y: Float,
    val vx: Float = 0f,
    val vy: Float = 0f,
    val level: CodeLevel,
    val isStatic: Boolean = false
)
