
import java.awt.*;

/* 顶层地图类* 绘制顶层组件* 判断逻辑*/
public class MapTop {

    // 格子位置
    int temp_x; // 当前鼠标所在格子的 x 坐标
    int temp_y; // 当前鼠标所在格子的 y 坐标

    // 重置游戏
    void reGame(){
        // 将顶层地图的所有格子重置为覆盖状态
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                GameUtil.DATA_TOP[i][j]=0;
            }
        }
    }

    // 判断逻辑
    void logic(){

        temp_x=0;
        temp_y=0;
        // 判断鼠标是否在有效区域内
        if(GameUtil.MOUSE_X>GameUtil.OFFSET && GameUtil.MOUSE_Y>3*GameUtil.OFFSET){
            temp_x = (GameUtil.MOUSE_X - GameUtil.OFFSET)/GameUtil.SQUARE_LENGTH+1;
            temp_y = (GameUtil.MOUSE_Y - GameUtil.OFFSET * 3)/GameUtil.SQUARE_LENGTH+1;
        }

        // 如果鼠标位置在地图范围内
        if(temp_x>=1 && temp_x<=GameUtil.MAP_W
                && temp_y>=1 && temp_y<=GameUtil.MAP_H){
            if(GameUtil.LEFT){
                // 如果格子被覆盖，则翻开
                if(GameUtil.DATA_TOP[temp_x][temp_y]==0){
                    GameUtil.DATA_TOP[temp_x][temp_y]=-1;
                }
                spaceOpen(temp_x,temp_y); // 打开空格
                GameUtil.LEFT=false; // 重置左键状态
            }
            if(GameUtil.RIGHT){
                // 如果格子被覆盖，则插旗
                if(GameUtil.DATA_TOP[temp_x][temp_y]==0){
                    GameUtil.DATA_TOP[temp_x][temp_y]=1;
                    GameUtil.FLAG_NUM++;
                }
                // 如果格子已插旗，则取消插旗
                else if(GameUtil.DATA_TOP[temp_x][temp_y]==1){
                    GameUtil.DATA_TOP[temp_x][temp_y]=0;
                    GameUtil.FLAG_NUM--;
                }
                // 如果格子已翻开，则尝试翻开周围数字
                else if(GameUtil.DATA_TOP[temp_x][temp_y]==-1){
                    numOpen(temp_x,temp_y);
                }
                GameUtil.RIGHT=false; // 重置右键状态
            }
        }
        boom(); // 检查是否失败
        victory(); // 检查是否胜利
    }

    // 数字翻开
    void numOpen(int x,int y){
        // 记录周围旗子的数量
        int count=0;
        if(GameUtil.DATA_BOTTOM[x][y]>0){
            for (int i = x-1; i <=x+1 ; i++) {
                for (int j = y-1; j <=y+1 ; j++) {
                    if(GameUtil.DATA_TOP[i][j]==1){
                        count++;
                    }
                }
            }
            // 如果旗子数量等于数字，则翻开周围格子
            if(count==GameUtil.DATA_BOTTOM[x][y]){
                for (int i = x-1; i <=x+1 ; i++) {
                    for (int j = y-1; j <=y+1 ; j++) {
                        if(GameUtil.DATA_TOP[i][j]!=1){
                            GameUtil.DATA_TOP[i][j]=-1;
                        }
                        // 必须在雷区范围内
                        if(i>=1&&j>=1&&i<=GameUtil.MAP_W&&j<=GameUtil.MAP_H){
                            spaceOpen(i,j);
                        }
                    }
                }
            }
        }
    }

    // 失败判定
    boolean boom(){
        // 如果旗子数量等于雷的数量，翻开所有未打开的格子
        if(GameUtil.FLAG_NUM==GameUtil.RAY_MAX){
            for (int i = 1; i <=GameUtil.MAP_W ; i++) {
                for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                    if(GameUtil.DATA_TOP[i][j]==0){
                        GameUtil.DATA_TOP[i][j]=-1;
                    }
                }
            }
        }
        // 检查是否点到雷
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                if(GameUtil.DATA_BOTTOM[i][j]==-1&&GameUtil.DATA_TOP[i][j]==-1){
                   GameUtil.state = 2; // 设置游戏状态为失败
                    seeBoom(); // 显示所有雷
                    return true;
                }
            }
        }
        return false;
    }

    // 失败显示
    void seeBoom(){
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                // 底层是雷，顶层不是旗，显示雷
                if(GameUtil.DATA_BOTTOM[i][j]==-1&&GameUtil.DATA_TOP[i][j]!=1){
                    GameUtil.DATA_TOP[i][j]=-1;
                }
                // 底层不是雷，顶层是旗，显示差错旗
                if(GameUtil.DATA_BOTTOM[i][j]!=-1&&GameUtil.DATA_TOP[i][j]==1){
                    GameUtil.DATA_TOP[i][j]=2;
                }
            }
        }
    }

    // 胜利判断
    boolean victory(){
        // 统计未打开的格子数
        int count=0;
        int Num=0;
        for (int i = 1; i <=GameUtil.MAP_W ; i++) {
            for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                if(GameUtil.DATA_TOP[i][j]==0||GameUtil.DATA_TOP[i][j]==1){
                    count++;
                }
            }
        }
        // 如果未打开的格子数等于雷的数量，则胜利
        if(GameUtil.FLAG_NUM==GameUtil.RAY_MAX||count==GameUtil.RAY_MAX){
            for (int i = 1; i <=GameUtil.MAP_W ; i++) {
                for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                    // 未翻开的格子变成旗
                    if(GameUtil.DATA_TOP[i][j]==1&&GameUtil.DATA_BOTTOM[i][j]!=-1){
                        Num++;
                    }
                }
            }
            // 如果插旗数量等于雷的数量，则胜利
            if(Num==GameUtil.RAY_MAX){
                GameUtil.state=1; // 设置游戏状态为胜利
                for (int i = 1; i <=GameUtil.MAP_W ; i++) {
                    for (int j = 1; j <=GameUtil.MAP_H ; j++) {
                        if(GameUtil.DATA_TOP[i][j]==0){
                            GameUtil.DATA_TOP[i][j]=1;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    // 打开空格
    void spaceOpen(int x,int y){
        if(GameUtil.DATA_BOTTOM[x][y]==0){
            for (int i = x-1; i <=x+1 ; i++) {
                for (int j = y-1; j <=y+1 ; j++) {
                    // 如果格子被覆盖，则递归打开
                    if(GameUtil.DATA_TOP[i][j]!=-1){
                        if(GameUtil.DATA_TOP[i][j]==1){GameUtil.FLAG_NUM--;}
                        GameUtil.DATA_TOP[i][j]=-1;
                        // 必须在雷区范围内
                        if(i>=1&&j>=1&&i<=GameUtil.MAP_W&&j<=GameUtil.MAP_H){
                            spaceOpen(i,j);
                        }
                    }
                }
            }
        }
    }

    // 绘制方法
    void paintSelf(Graphics g){
        logic(); // 执行逻辑判断
        for (int i = 1; i <= GameUtil.MAP_W ; i++) {
            for (int j = 1; j <= GameUtil.MAP_H; j++) {
                // 绘制覆盖状态的格子
                if (GameUtil.DATA_TOP[i][j] == 0) {
                    g.drawImage(GameUtil.top,
                            GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 + (j - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }
                // 绘制插旗状态的格子
                if (GameUtil.DATA_TOP[i][j] == 1) {
                    g.drawImage(GameUtil.flag,
                            GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 + (j - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }
                // 绘制差错旗状态的格子
                if (GameUtil.DATA_TOP[i][j] == 2) {
                    g.drawImage(GameUtil.noflag,
                            GameUtil.OFFSET + (i - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.OFFSET * 3 + (j - 1) * GameUtil.SQUARE_LENGTH + 1,
                            GameUtil.SQUARE_LENGTH - 2,
                            GameUtil.SQUARE_LENGTH - 2,
                            null);
                }


            }
        }
    }
}
