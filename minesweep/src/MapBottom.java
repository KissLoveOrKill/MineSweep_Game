

import java.awt.*;
/* 底层地图* 绘制游戏相关组件*/
public class MapBottom {

    // 初始化底层地图，生成雷和数字
    BottomRay bottomRay = new BottomRay();
    BottomNum bottomNum = new BottomNum();
    {
        bottomRay.newRay(); // 生成雷的位置
        bottomNum.newNum(); // 根据雷的位置生成数字
    }

    // 重置游戏，清空底层地图并重新生成雷和数字
    void reGame(){
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                GameUtil.DATA_BOTTOM[i][j]=0; // 清空底层数据
            }
        }
        bottomRay.newRay(); // 重新生成雷
        bottomNum.newNum(); // 重新生成数字
    }

    // 绘制底层地图
    void paintSelf(Graphics g){
        g.setColor(Color.black);
        // 绘制竖线
        for (int i = 0; i <= GameUtil.MAP_W; i++) {
            g.drawLine(GameUtil.OFFSET + i * GameUtil.SQUARE_LENGTH,
                    3*GameUtil.OFFSET,
                    GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH,
                    3*GameUtil.OFFSET+GameUtil.MAP_H*GameUtil.SQUARE_LENGTH);
        }
        // 绘制横线
        for (int i = 0; i <=GameUtil.MAP_H; i++){
            g.drawLine(GameUtil.OFFSET,
                    3*GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH,
                    GameUtil.OFFSET+GameUtil.MAP_W*GameUtil.SQUARE_LENGTH,
                    3*GameUtil.OFFSET+i*GameUtil.SQUARE_LENGTH);
        }
        // 绘制雷和数字
        for (int i = 1; i <= GameUtil.MAP_W ; i++) {
            for (int j = 1; j <= GameUtil.MAP_H; j++) {
                // 绘制雷
                if (GameUtil.DATA_BOTTOM[i][j] == -1) {
                    g.drawImage(GameUtil.lei,
                            GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 + (j - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }
                // 绘制数字
                if (GameUtil.DATA_BOTTOM[i][j] >=0) {
                    g.drawImage(GameUtil.images[GameUtil.DATA_BOTTOM[i][j]],
                            GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 15,
                            GameUtil.OFFSET * 3 + (j - 1) * GameUtil.SQUARE_LENGTH + 5,
                            null);
                }

            }
        }

        // 绘制剩余雷数和倒计时
        GameUtil.drawWord(g,""+(GameUtil.RAY_MAX-GameUtil.FLAG_NUM),
                GameUtil.OFFSET,
                2*GameUtil.OFFSET,50,Color.black); // 剩余雷数
        GameUtil.drawWord(g,""+(GameUtil.END_TIME-GameUtil.START_TIME)/1000,
                GameUtil.OFFSET + GameUtil.SQUARE_LENGTH*(GameUtil.MAP_W-1),
                2*GameUtil.OFFSET,50,Color.black); // 倒计时

        // 根据游戏状态绘制表情或结果
        switch (GameUtil.state){
            case 0: // 游戏进行中
                GameUtil.END_TIME=System.currentTimeMillis();
                g.drawImage(GameUtil.face,
                        GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W/2),
                        GameUtil.OFFSET,
                        null);
                break;
            case 1: // 游戏胜利
                g.drawImage(GameUtil.win,
                        GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W/2),
                        GameUtil.OFFSET,
                        null);
                break;
            case 2: // 游戏失败
                g.drawImage(GameUtil.over,
                        GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W/2),
                        GameUtil.OFFSET,
                        null);
                break;

                default:
        }
    }
}
