package tw.edu.pu.csim.tcyang.race

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.window.layout.WindowMetricsCalculator
import tw.edu.pu.csim.tcyang.race.ui.theme.賽馬raceTheme

class MainActivity : ComponentActivity() {
    // 實例化 ViewModel
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //強迫橫式螢幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        // 隱藏狀態列
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        // 確保內容延伸到至邊緣
        WindowCompat.setDecorFitsSystemWindows(
            window, false)

        // 步驟 1: 獲取 WindowMetricsCalculator 實例
        val windowMetricsCalculator =
            WindowMetricsCalculator.getOrCreate()

        // 步驟 2: 計算當前視窗的 WindowMetrics
        val currentWindowMetrics=
            windowMetricsCalculator.computeCurrentWindowMetrics(this)

        // 步驟 3: 從 bounds 獲取像素尺寸
        val bounds = currentWindowMetrics.bounds

        val screenWidthPx = bounds.width().toFloat()
        val screenHeightPx = bounds.height().toFloat()

        // 將螢幕尺寸傳給 ViewModel
        gameViewModel.setGameSize(screenWidthPx, screenHeightPx)

        setContent {
            賽馬raceTheme {
                // 將 gameViewModel 實例傳遞給 GameScreen
                GameScreen(
                    message = "賽馬遊戲(作者：李念恩)",
                    gameViewModel = gameViewModel
                )
            }
        }
    }
}

// 由於遊戲邏輯集中在 GameScreen，Greeting/Preview 保持不變或可移除。
@Composable
fun Greeting(name: String) {
    // 保持原始結構
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    // 保持原始結構
}