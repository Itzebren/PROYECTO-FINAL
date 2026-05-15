package com.android.mobile.games.app.games.fruitninja.data

class FakeGameService {
    fun getRanking() = listOf(
        "Senior_Dev" to 15000,
        "Junior_IPN" to 8500,
        "Bug_Hunter" to 4200
    )

    fun uploadScore(score: Int) {
        // Simulación de envío a FastAPI
        println("Enviando dictamen de $score puntos al servidor...")
    }
}