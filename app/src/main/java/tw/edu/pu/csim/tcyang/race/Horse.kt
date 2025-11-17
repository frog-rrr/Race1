package tw.edu.pu.csim.tcyang.race

import kotlin.random.Random

// 賽馬類別 (根據使用者要求)
class Horse(n: Int, val id: Int) {
    // n 是馬匹的索引 (0, 1, 2)
    var horseX = 0 // X 座標 (起點)

    // 根據使用者提供的 Y 座標計算邏輯：100 + 220 * n
    var horseY = 100 + 220 * n

    var number = 0 // 圖片索引 (0-3)

    // 馬匹跑動邏輯：更新 X 座標和圖片索引
    fun HorseRun(){
        number ++
        if (number > 3) {
            number = 0
        }
        // 隨機增加 10 到 30 之間的距離 (nextInt(10, 31) 會產生 10, 11, ..., 30)
        horseX += Random.nextInt(10, 31)
    }

    // 重置馬匹回起點
    fun reset() {
        horseX = 0
        number = 0
        // horseY 保持不變，因為它是固定的賽道位置
    }
}