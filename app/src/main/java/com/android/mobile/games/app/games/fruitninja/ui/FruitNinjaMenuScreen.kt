package com.android.mobile.games.app.games.fruitninja.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.mobile.games.app.R
import com.android.mobile.games.app.games.fruitninja.model.FruitNinjaDifficulty

@Composable
fun FruitNinjaMenuScreen(
    onStartGameClick: (FruitNinjaDifficulty) -> Unit,
    onBackClick: () -> Unit
) {
    // Estado para saber qué modo está seleccionado y si el modal de ayuda está abierto
    var selectedDifficulty by remember { mutableStateOf(FruitNinjaDifficulty.SAVE_SEMESTER) }
    var showHelpModal by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. FONDO DE PORTADA
        Image(
            painter = painterResource(id = R.drawable.portada_slash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. BOTÓN DE AYUDA "?" (Esquina superior derecha)
        IconButton(
            onClick = { showHelpModal = true },
            modifier = Modifier.align(Alignment.TopEnd).padding(24.dp).size(48.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFD100FF)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("?", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 3. CONTENIDO PRINCIPAL (Abajo)
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Selector de Modos (Botones lila)
            ModeSelectorRow(
                selected = selectedDifficulty,
                onSelected = { selectedDifficulty = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de Inicio brillante
            Button(
                onClick = { onStartGameClick(selectedDifficulty) },
                modifier = Modifier.fillMaxWidth(0.8f).height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD100FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "[ ¡A COMPILAR! ]",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            TextButton(onClick = onBackClick) {
                Text("Cerrar Lab", color = Color.White.copy(alpha = 0.7f))
            }
        }

        // 4. MODAL DE AYUDA (Se muestra al presionar "?")
        if (showHelpModal) {
            AlertDialog(
                onDismissRequest = { showHelpModal = false },
                containerColor = Color(0xFF1A1A2E),
                title = { Text("MISIÓN: CÓDIGO LIMPIO", color = Color(0xFFD100FF), fontFamily = FontFamily.Monospace) },
                text = {
                    Column {
                        Text("OBJETIVO: Elimina los BUGS y ERRORES antes de la entrega.", color = Color.White)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("• COMBO: Corta 3+ ítems.", color = Color.White.copy(0.7f), fontSize = 12.sp)
                        Text("• IPN CARD: +5 segundos.", color = Color.White.copy(0.7f), fontSize = 12.sp)
                        Text("• CAFÉ TACHADO: ¡Cuidado! Quita vidas.", color = Color.White.copy(0.7f), fontSize = 12.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showHelpModal = false }) {
                        Text("ENTENDIDO", color = Color(0xFFD100FF))
                    }
                }
            )
        }
    }
}

@Composable
private fun ModeSelectorRow(
    selected: FruitNinjaDifficulty,
    onSelected: (FruitNinjaDifficulty) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FruitNinjaDifficulty.entries.forEach { mode ->
            FilterChip(
                selected = selected == mode,
                onClick = { onSelected(mode) },
                label = { Text(mode.label, fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFD100FF),
                    labelColor = Color.White,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}