package com.android.mobile.games.app.games.codemerge.engine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.mobile.games.app.games.codemerge.data.ICodeMergeGameService
import com.android.mobile.games.app.games.codemerge.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.sqrt
import kotlin.random.Random

class CodeMergeViewModel(
    private val service: ICodeMergeGameService
) : ViewModel() {

    private val _state = MutableStateFlow(CodeMergeGameState())
    val state: StateFlow<CodeMergeGameState> = _state.asStateFlow()

    private val GRAVITY = 0.5f
    private val BOUNCE = 0.2f
    private val FRICTION = 0.98f
    private val BASE_RADIUS = 40f
    private val LOSS_LINE_Y = 150f
    private var gameOverTimer = 0L

    init {
        startGameLoop()
    }

    fun handleIntent(intent: CodeMergeIntent) {
        viewModelScope.launch {
            when (intent) {
                is CodeMergeIntent.StartGame -> {
                    _state.update { CodeMergeGameState() }
                    gameOverTimer = 0L
                }
                is CodeMergeIntent.MoveCurrentElement -> {
                    _state.update { it.copy(currentElementX = intent.x) }
                }
                is CodeMergeIntent.DropElement -> {
                    dropElement()
                }
                is CodeMergeIntent.Tick -> {
                    withContext(Dispatchers.Default) {
                        updatePhysics()
                    }
                }
                is CodeMergeIntent.SubmitScore -> {
                    submitScore(intent.name)
                }
            }
        }
    }

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(16) // ~60 FPS
                handleIntent(CodeMergeIntent.Tick)
            }
        }
    }

    private fun dropElement() {
        if (_state.value.isGameOver) return
        
        val newElement = CodeElement(
            x = _state.value.currentElementX,
            y = 50f,
            level = _state.value.nextLevel
        )
        
        val nextLevel = CodeLevel.entries.filter { it.ordinal <= 2 }.random()
        
        _state.update { 
            it.copy(
                elements = it.elements + newElement,
                nextLevel = nextLevel
            )
        }
    }

    private fun updatePhysics() {
        val currentState = _state.value
        if (currentState.isGameOver) return

        val elements = currentState.elements.toMutableList()
        
        // 1. Apply Gravity and Movement
        for (i in elements.indices) {
            val el = elements[i]
            val newVy = el.vy + GRAVITY
            val newX = el.x + el.vx
            val newY = el.y + newVy
            
            elements[i] = el.copy(
                x = newX,
                y = newY,
                vx = el.vx * FRICTION,
                vy = newVy
            )
        }

        // 2. Wall Collisions
        for (i in elements.indices) {
            val el = elements[i]
            val radius = BASE_RADIUS * el.level.radiusScale
            
            var nx = el.x
            var ny = el.y
            var nvx = el.vx
            var nvy = el.vy

            if (nx - radius < 0) {
                nx = radius
                nvx = -nvx * BOUNCE
            } else if (nx + radius > 1000f) {
                nx = 1000f - radius
                nvx = -nvx * BOUNCE
            }

            if (ny + radius > 1000f) { // Bottom
                ny = 1000f - radius
                nvy = -nvy * BOUNCE
                nvx *= 0.9f // Ground friction
            }

            elements[i] = el.copy(x = nx, y = ny, vx = nvx, vy = nvy)
        }

        // 3. Circle Collisions and Merging
        var scoreAdd = 0
        val toRemove = mutableSetOf<String>()
        val toAdd = mutableListOf<CodeElement>()

        for (i in elements.indices) {
            for (j in i + 1 until elements.size) {
                val e1 = elements[i]
                val e2 = elements[j]
                if (toRemove.contains(e1.id) || toRemove.contains(e2.id)) continue

                val r1 = BASE_RADIUS * e1.level.radiusScale
                val r2 = BASE_RADIUS * e2.level.radiusScale
                val dx = e2.x - e1.x
                val dy = e2.y - e1.y
                val dist = sqrt(dx * dx + dy * dy)
                val minDist = r1 + r2

                if (dist < minDist) {
                    if (e1.level == e2.level && e1.level != CodeLevel.PROJECT_COMPLETE) {
                        // MERGE!
                        toRemove.add(e1.id)
                        toRemove.add(e2.id)
                        val nextLvl = e1.level.next()!!
                        toAdd.add(CodeElement(
                            x = (e1.x + e2.x) / 2f,
                            y = (e1.y + e2.y) / 2f,
                            level = nextLvl
                        ))
                        scoreAdd += nextLvl.score
                    } else {
                        // Resolve collision
                        val overlap = minDist - dist
                        val nx = dx / dist
                        val ny = dy / dist
                        
                        elements[i] = elements[i].copy(
                            x = elements[i].x - nx * overlap / 2f,
                            y = elements[i].y - ny * overlap / 2f,
                            vx = elements[i].vx - nx * BOUNCE,
                            vy = elements[i].vy - ny * BOUNCE
                        )
                        elements[j] = elements[j].copy(
                            x = elements[j].x + nx * overlap / 2f,
                            y = elements[j].y + ny * overlap / 2f,
                            vx = elements[j].vx + nx * BOUNCE,
                            vy = elements[j].vy + ny * BOUNCE
                        )
                    }
                }
            }
        }

        val finalElements = elements.filterNot { toRemove.contains(it.id) } + toAdd

        // 4. Check Loss Line
        var isAboveLine = false
        for (el in finalElements) {
            val radius = BASE_RADIUS * el.level.radiusScale
            // Fix: absoluteValue is a property, not a function
            if (el.y - radius < LOSS_LINE_Y && el.vy.absoluteValue < 0.1f) {
                isAboveLine = true
                break
            }
        }

        var gameOver = false
        if (isAboveLine) {
            if (gameOverTimer == 0L) {
                gameOverTimer = System.currentTimeMillis()
            } else if (System.currentTimeMillis() - gameOverTimer > 1500) {
                gameOver = true
            }
        } else {
            gameOverTimer = 0L
        }

        _state.update { 
            it.copy(
                elements = finalElements,
                currentScore = it.currentScore + scoreAdd,
                isGameOver = gameOver
            )
        }
    }

    private fun submitScore(name: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            service.saveResult(MergeRunResult(playerName = name, score = _state.value.currentScore))
            _state.update { it.copy(isLoading = false) }
        }
    }

    private val Float.absoluteValue: Float get() = if (this < 0) -this else this
}
