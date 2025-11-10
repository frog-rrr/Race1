package tw.edu.pu.csim.tcyang.race

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel // 引入 ViewModel 相關函式

@Composable
fun GameScreen(
    message: String,
    // 透過參數或使用 viewModel() 取得 GameViewModel 實例
    gameViewModel: GameViewModel = viewModel()
) {
    // 獲取當前螢幕密度，用於尺寸轉換 (雖然這裡主要用像素，但這是好的習慣)
    val density = LocalDensity.current

    // 取得 ViewModel 中的狀態，當這些狀態改變時，Canvas 會自動重繪
    val circleX = gameViewModel.circleX
    val circleY = gameViewModel.circleY
    val gameRunning = gameViewModel.gameRunning
    //載入圖片
    val imageBitmaps = listOf(
        ImageBitmap.imageResource(R.drawable.horse0),
        ImageBitmap.imageResource(R.drawable.horse1),
        ImageBitmap.imageResource(R.drawable.horse2),
        ImageBitmap.imageResource(R.drawable.horse3)
    )



    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Yellow)
        // 使用 onSizeChanged 設定遊戲尺寸，並在尺寸確定後啟動遊戲
        .onSizeChanged { size ->
            // 將 Int 尺寸轉換為 Float 像素值
            gameViewModel.setGameSize(size.width.toFloat(), size.height.toFloat())
            if (!gameRunning) {
                gameViewModel.startGame()
            }
        }
    ){
        Text(text = message)
    }

    Canvas (modifier = Modifier
        .fillMaxSize()
        // 呼叫 ViewModel 實例的方法
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                // 修正：使用 gameViewModel 實例來移動圓圈
                gameViewModel.MoveCircle(dragAmount.x, dragAmount.y)
            }

        }

    )

    // 💥 修正：將 drawImage 移到 Canvas 的繪圖區塊內部
    {
        // 使用 ViewModel 中的 circleX 和 circleY 狀態來繪製
        drawCircle(
            color = Color.Red,
            radius = 100f,
            center = Offset(circleX, circleY) // 位置隨著狀態更新而改變
        )

        drawImage(
            image = imageBitmap,
            dstOffset = IntOffset(0, 100), // 靜態位置
            dstSize = IntSize(200, 200)
        )
    }
}