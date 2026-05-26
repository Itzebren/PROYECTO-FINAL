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

import com.android.mobile.games.app.games.fruitninja.data.RetrofitGameService
import kotlinx.coroutines.launch

@Composable
fun FruitNinjaMenuScreen(
    onStartGameClick: (FruitNinjaDifficulty, String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf(FruitNinjaDifficulty.SAVE_SEMESTER) }
    var username by remember { mutableStateOf("") }
    var showHelpModal by remember { mutableStateOf(false) }
    var showRankingModal by remember { mutableStateOf(false) }
    var rankingData by remember { mutableStateOf<List<Pair<String, Int>>>(emptyList()) }
    var isLoadingRanking by remember { mutableStateOf(false) }

    val gameService = remember { RetrofitGameService() }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. FONDO DE PORTADA
        Image(
            painter = painterResource(id = R.drawable.portada_slash),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. BOTONES SUPERIORES (AYUDA Y RANKING)
        Row(
            modifier = Modifier.align(Alignment.TopEnd).padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Ranking
            IconButton(
                onClick = {
                    showRankingModal = true
                    isLoadingRanking = true
                    coroutineScope.launch {
                        rankingData = gameService.getRanking()
                        isLoadingRanking = false
                    }
                },
                modifier = Modifier.size(56.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFFFD700),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("🏆", fontSize = 24.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Botón Ayuda
            IconButton(
                onClick = { showHelpModal = true },
                modifier = Modifier.size(56.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFD100FF),
                    border = BorderStroke(2.dp, Color.White)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("?", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. PANEL DE CONTROL
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.Black.copy(alpha = 0.6f)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo de Nombre de Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("NOMBRE DEL PROGRAMADOR", color = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(0.85f),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 16.sp
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFD100FF),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        cursorColor = Color(0xFFD100FF)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Modos
                ModeSelectorRow(
                    selected = selectedDifficulty,
                    onSelected = { selectedDifficulty = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón Principal
                Button(
                    onClick = { 
                        if (username.isNotBlank()) {
                            onStartGameClick(selectedDifficulty, username) 
                        }
                    },
                    enabled = username.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .heightIn(min = 56.dp, max = 80.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD100FF),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "[ ¡A COMPILAR! ]",
                        fontSize = 18.sp, // Reducido de 22.sp para mejor ajuste
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        softWrap = false, // Evita saltos de línea si es posible
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTÓN CERRAR LAB
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth(0.7f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "CERRAR LABORATORIO",
                        fontSize = 11.sp, // Ajuste menor
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                }
            }
        }

        // Modal de Ranking
        if (showRankingModal) {
            AlertDialog(
                onDismissRequest = { showRankingModal = false },
                containerColor = Color(0xFF1A1A2E),
                title = { Text("TOP PROGRAMADORES", color = Color(0xFF00FFF0), fontFamily = FontFamily.Monospace) },
                text = {
                    Column(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)) {
                        if (isLoadingRanking) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Color(0xFF00FFF0))
                        } else if (rankingData.isEmpty()) {
                            Text("No hay registros aún.", color = Color.White)
                        } else {
                            rankingData.forEachIndexed { index, pair ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("${index + 1}. ${pair.first}", color = Color.White, fontFamily = FontFamily.Monospace)
                                    Text("${pair.second} pts", color = Color(0xFF00FFF0), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showRankingModal = false }) {
                        Text("CERRAR", color = Color(0xFF00FFF0))
                    }
                }
            )
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