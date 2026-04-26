package com.android.mobile.games.app.games.fruitninja.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaGameState
import com.android.mobile.games.app.games.fruitninja.model.color

@Composable
fun FruitNinjaCanvas(
    gameState: FruitNinjaGameState,
    onCanvasSizeChanged: (Float, Float) -> Unit,
    onSlice: (Offset) -> Unit
) {

    val slashTrail = remember {
        mutableStateListOf<Offset>()
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {

                detectDragGestures(
                    onDragStart = { offset ->

                        slashTrail.clear()
                        slashTrail.add(offset)

                        onSlice(offset)
                    },

                    onDrag = { change, _ ->

                        slashTrail.add(change.position)

                        if (slashTrail.size > 15) {
                            slashTrail.removeAt(0)
                        }

                        onSlice(change.position)
                    },

                    onDragEnd = {
                        slashTrail.clear()
                    }
                )
            }
    ) {

        onCanvasSizeChanged(
            size.width,
            size.height
        )

        /*
         Fondo temporal.
         Después será reemplazado
         por background.png
         */

        drawRect(
            color = Color(0xFF101820)
        )

        /*
         Dibujar frutas / bombas
         (placeholder visual por ahora)
         luego se reemplaza por assets reales
         */

        gameState.items.forEach { item ->

            drawCircle(
                color = item.type.color(),
                radius = item.radius,
                center = item.position
            )
        }

        /*
         Trail del dedo
         efecto tipo slash
         */

        if (slashTrail.size > 1) {

            for (i in 0 until slashTrail.lastIndex) {

                drawLine(
                    color = Color.White,
                    start = slashTrail[i],
                    end = slashTrail[i + 1],
                    strokeWidth = 10f
                )
            }
        }
    }
}