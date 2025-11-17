package tw.edu.pu.csim.tcyang.race

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // 引入 ViewModel 相關函式

@Composable
fun GameScreen(
    message: String,
    // 透過參數或使用 viewModel() 取得 GameViewModel 實例
    gameViewModel: GameViewModel = viewModel()
) {
    // 取得 ViewModel 中的狀態
    val horses = gameViewModel.horses
    val winnerMessage = gameViewModel.winnerMessage

    // 圓形狀態
    val circleX = gameViewModel.circleX
    val circleY = gameViewModel.circleY

    // 讀取 updateTick 確保 Composable 觀察到狀態變化，從而觸發重繪
    val updateTick = gameViewModel.updateTick


    // 載入圖片 (假設 R.drawable.horse0 到 R.drawable.horse3 存在)
    val imageBitmaps = listOf(
        ImageBitmap.imageResource(R.drawable.horse0),
        ImageBitmap.imageResource(R.drawable.horse1),
        ImageBitmap.imageResource(R.drawable.horse2),
        ImageBitmap.imageResource(R.drawable.horse3)
    )

    Box(modifier = Modifier
        .fillMaxSize()
        // 背景換回黃色
        .background(Color.Yellow)
        // 使用 onSizeChanged 設定遊戲尺寸，並在尺寸確定後啟動遊戲
        .onSizeChanged { size ->
            // 將 Int 尺寸轉換為 Float 像素值
            gameViewModel.setGameSize(size.width.toFloat(), size.height.toFloat())
            if (!gameViewModel.gameRunning) {
                gameViewModel.startGame()
            }
        }
    ) {
        // --- 標題與勝者訊息區 ---
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 標題
            Text(
                text = message,
                color = Color.Black, // 標題顏色改為黑色，在黃色背景上更清楚
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            // 勝者訊息 (顯示在標題下方)
            winnerMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // --- 遊戲繪圖區 ---
        Canvas(modifier = Modifier.fillMaxSize()) {

            // 圓形繪製邏輯
            drawCircle(
                color = Color.Red,
                radius = 100f,
                center = Offset(circleX, circleY) // 位置隨著狀態更新而改變
            )


            // 繪製馬匹
            horses.forEach { horse ->
                // 檢查索引是否有效
                val imageIndex = horse.number.coerceIn(0, imageBitmaps.size - 1)

                drawImage(
                    image = imageBitmaps[imageIndex],
                    dstOffset = IntOffset(horse.horseX, horse.horseY),
                    dstSize = IntSize(200, 200) // 假設馬匹圖片大小為 200x200
                )
            }
        }
    }
}