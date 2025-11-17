package tw.edu.pu.csim.tcyang.race

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// 原 Horse 類別已移至 Horse.kt，並根據使用者要求修改

class GameViewModel : ViewModel() {

    // 遊戲尺寸狀態
    var screenWidthPx by mutableStateOf(0f)
        private set

    var screenHeightPx by mutableStateOf(0f)
        private set

    // --- 圓形狀態 ---
    var circleX by mutableStateOf(100f)
        private set
    var circleY by mutableStateOf(0f)
        private set
    private val circleRadius = 100f // 圓形半徑

    // 遊戲狀態 (用來控制遊戲循環是否運行)
    var gameRunning by mutableStateOf(false)

    // 勝利訊息狀態 (三秒後會被清除)
    var winnerMessage by mutableStateOf<String?>(null)
        private set

    // 馬匹列表 (現在使用的是 Horse.kt 中的 Horse 類別)
    var horses by mutableStateOf(listOf<Horse>())
        private set

    // 用於強制 Compose 偵測到狀態變化的計數器
    var updateTick by mutableStateOf(0)
        private set

    // 遊戲循環 Job，用於控制遊戲開始和停止
    private var gameLoopJob: Job? = null


    fun setGameSize(w: Float, h: Float) {
        if (screenWidthPx == w && screenHeightPx == h) return
        screenWidthPx = w
        screenHeightPx = h

        // 移除設定馬匹終點線 finishLineX 的邏輯

        // 首次設定尺寸時初始化馬匹和圓形位置
        if (horses.isEmpty()) {
            initializeHorses()
        }

        // 初始化圓形位置：將圓形放置在底部附近
        circleY = h - circleRadius
        circleX = circleRadius
    }

    // 初始化/重置馬匹位置和速度
    private fun initializeHorses() {
        val horseCount = 3

        horses = List(horseCount) { index ->
            // 使用 Horse(n: Int, id: Int) 構造函數。n=index, id=index+1
            Horse(
                n = index, // 馬匹索引 (0, 1, 2)
                id = index + 1 // 馬匹 ID (1, 2, 3)
            )
        }
    }


    fun startGame() {
        // 如果遊戲正在運行或有獲勝訊息，則不重複啟動
        if (gameRunning || winnerMessage != null) return

        gameRunning = true
        // 確保 Job 被取消，防止重複啟動
        gameLoopJob?.cancel()

        gameLoopJob = viewModelScope.launch {
            while (gameRunning) {
                // 每次循環更新馬匹位置
                val winningHorseId = updateHorsePositions()

                // 圓形移動邏輯
                updateCirclePosition()

                if (winningHorseId != null) {
                    // 有馬匹獲勝，宣布勝者並暫停遊戲
                    declareWinner(winningHorseId)
                    break // 跳出 while 循環
                }

                delay(100) // 100 毫秒更新一次
            }
        }
    }

    // 更新圓形位置邏輯
    private fun updateCirclePosition() {
        // 圓形移動邏輯
        circleX += 10f

        // 右邊邊緣設為終點線
        // 當圓形中心點加上半徑達到螢幕邊緣時重置
        if (circleX >= screenWidthPx - circleRadius) {
            circleX = circleRadius // 重置到左邊 (考慮半徑)
        }
    }


    // 更新馬匹位置並檢查勝者
    private fun updateHorsePositions(): Int? {
        var winnerId: Int? = null

        // 直接在現有列表上操作 (因為 Horse 內部屬性是 var)
        horses.forEach { horse ->
            horse.HorseRun()
            // *** 修改獲勝條件：碰到畫面右邊邊緣才算獲勝 (馬匹圖片寬度為 200px) ***
            if (horse.horseX >= screenWidthPx.toInt() - 200 && winnerId == null) {
                // 第一個到達終點的馬獲勝
                winnerId = horse.id
                gameRunning = false // 停止遊戲循環
            }
        }

        // 透過改變 updateTick 狀態來強制 Compose 偵測到畫面更新
        updateTick++

        return winnerId
    }

    // 宣布勝者並延遲重置
    private fun declareWinner(winnerId: Int) {
        winnerMessage = "第${winnerId}馬獲勝"
        viewModelScope.launch {
            delay(3000) // 顯示三秒
            resetGame() // 重置遊戲
        }
    }

    // 重置遊戲
    private fun resetGame() {
        winnerMessage = null // 清除勝利訊息

        // 重置圓形位置
        circleX = circleRadius

        // 重置所有馬匹位置
        horses.forEach { horse ->
            // 使用 Horse 類別中的 reset() 函數
            horse.reset()
        }

        // 透過增加 updateTick 確保重置後的初始位置能被立刻重繪
        updateTick++

        // 啟動下一輪比賽
        startGame()
    }

    // 確保 ViewModel 銷毀時停止 Job
    override fun onCleared() {
        super.onCleared()
        gameLoopJob?.cancel()
    }

    // 移除不相關的圓形功能
    fun MoveCircle(x: Float, y: Float) {
        // 刪除此功能
    }

}