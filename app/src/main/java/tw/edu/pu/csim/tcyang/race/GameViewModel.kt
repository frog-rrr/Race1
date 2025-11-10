package tw.edu.pu.csim.tcyang.race

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    var screenWidthPx by mutableStateOf(0f)
        private set

    var screenHeightPx by mutableStateOf(0f)
        private set

    var circleX by mutableStateOf(100f)
        private set

    var circleY by mutableStateOf(0f)
        private set

    var gameRunning by mutableStateOf(false)

    fun setGameSize(w: Float, h: Float) {
        screenWidthPx = w
        screenHeightPx = h
    }

    fun MoveCircle(x: Float, y: Float) {
        circleX += x
        circleY += y
    }
    val horse = Horse()

    fun startGame() {
        gameRunning = true
        circleX = 100f
        circleY = screenHeightPx - 100f

        viewModelScope.launch {
            while (gameRunning) {
                horse.HorseRun()
                if (horse.horseX >= screenWidthPx - 200){
                    horse.horseX = 0
                }

                delay(100)
                circleX += 10

                if (circleX >= screenWidthPx - 100) {
                    circleX = 100f
                }
            }
        }
    }




}