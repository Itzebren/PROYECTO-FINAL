package com.android.mobile.games.app.games.fruitninja.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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

        // 2. BOTÓN DE AYUDA "?"
        IconButton(
            onClick = { showHelpModal = true },
            modifier = Modifier.align(Alignment.TopEnd).padding(24.dp).size(56.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFD100FF),
                border = BorderStroke(2.dp, Color.White) // Añadimos borde blanco para que resalte
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text("?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 3. PANEL DE CONTROL (Con fondo protector para mejorar contraste)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            color = Color.Black.copy(alpha = 0.4f) // Capa oscura sutil detrás de los botones
        ) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Selector de Modos Mejorado
                ModeSelectorRow(
                    selected = selectedDifficulty,
                    onSelected = { selectedDifficulty = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Botón Principal "[ ¡A COMPILAR! ]"
                Button(
                    onClick = { onStartGameClick(selectedDifficulty) },
                    modifier = Modifier.fillMaxWidth(0.85f).height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD100FF)),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "[ ¡A COMPILAR! ]",
                        fontSize = 22.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTÓN CERRAR LAB (Ahora mucho más visible)
                OutlinedButton(
                    onClick = onBackClick,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        "CERRAR LABORATORIO",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 4. MODAL DE AYUDA (Se mantiene igual)
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FruitNinjaDifficulty.entries.forEach { mode ->
            val isSelected = selected == mode
            FilterChip(
                selected = isSelected,
                onClick = { onSelected(mode) },
                label = {
                    Text(
                        mode.label,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    // Seleccionado: Lila brillante
                    selectedContainerColor = Color(0xFFD100FF),
                    selectedLabelColor = Color.White,
                    // No seleccionado: Fondo oscuro sólido para que no se transparente el fondo
                    containerColor = Color(0xFF2D2D44),
                    labelColor = Color.White.copy(alpha = 0.8f)
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = Color.White.copy(alpha = 0.3f),
                    selectedBorderColor = Color.White,
                    borderWidth = 1.dp
                )
            )
        }
    }
}