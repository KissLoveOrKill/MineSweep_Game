
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.util.Objects;


/* 工具类* 存放静态参数 * 工具方法*/
public class GameUtil {
    //地雷个数
    static int RAY_MAX = 100;
    //地图的宽
    static int MAP_W = 36;
    //地图的高
    static int MAP_H = 17;
    //雷区偏移量
    static int OFFSET = 45;
    //格子边长
    static int SQUARE_LENGTH = 50;

    //插旗数量
    static int FLAG_NUM = 0;

    //鼠标相关
    //坐标
    static int MOUSE_X;
    static int MOUSE_Y;
    //状态
    static boolean LEFT = false;
    static boolean RIGHT = false;

    //游戏状态 0 表示游戏中 1 胜利 2 失败 3 难度选择
    static int state = 3;
    //游戏难度
    static int level;

    //倒计时
    static long START_TIME;
    static long END_TIME;

    //底层元素  -1 雷 0 空 1-8 表示对应数字
    static int[][] DATA_BOTTOM = new int[MAP_W+2][MAP_H+2];
    //顶层元素  -1 无覆盖 0 覆盖 1 插旗 2 差错旗
    static int[][] DATA_TOP = new int[MAP_W+2][MAP_H+2];


    // 使用类加载器加载资源
    private static Image loadImage(String path) {
        try {
            return new ImageIcon(GameUtil.class.getResource("/" + path)).getImage();
        } catch (Exception e) {
            System.err.println("无法加载图片: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // 初始化图片
    static Image flag = loadImage("img/flag.png");
    static Image top = loadImage("img/floor.png");
    static Image noflag = loadImage("img/noflag.png");
    static Image lei = loadImage("img/bomb.png");
    static Image face = loadImage("img/face.png");
    static Image over = loadImage("img/fail.png");
    static Image win = loadImage("img/win.jpg");

    static Image[] images = new Image[9];
    static {
        for (int i = 1; i <= 8; i++) {
            images[i] = loadImage("img/nums/" + i + ".png");
        }
    }

    static void drawWord(Graphics g,String str,int x,int y,int size,Color color){
        g.setColor(color);
        g.setFont(new Font("仿宋",Font.BOLD,size));
        g.drawString(str,x,y);

    }

}
