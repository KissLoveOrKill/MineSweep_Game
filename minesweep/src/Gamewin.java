
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Gamewin extends JFrame {
    // 游戏窗口的宽度和高度计算
    int wigth = 2 * GameUtil.OFFSET + GameUtil.MAP_W * GameUtil.SQUARE_LENGTH;
    int height = 4 * GameUtil.OFFSET + GameUtil.MAP_H * GameUtil.SQUARE_LENGTH;

    // 双缓冲机制的离屏图片
    Image offScreenImage = null;

    // 标志窗口是否需要清除
    boolean ifClear = false;

    // 游戏的底层地图、顶层地图和难度选择界面
    MapBottom mapBottom = new MapBottom();
    MapTop mapTop = new MapTop();
    GameSelect gameSelect = new GameSelect();

    // 游戏是否开始的标志
    boolean begin = false;

    void launch() {
        // 初始化游戏开始时间
        GameUtil.START_TIME = System.currentTimeMillis();
        this.setVisible(true);

        // 根据游戏状态设置窗口大小
        if (GameUtil.state == 3) {
            this.setSize(500, 650); // 难度选择界面
        } else {
            this.setSize(wigth, height); // 游戏界面
        }

        this.setLocationRelativeTo(null); // 窗口居中
        this.setTitle("扫雷游戏"); // 设置窗口标题
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); // 设置关闭操作

        // 添加窗口大小调整监听器
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                ifClear = true; // 标记需要清除窗口
            }
        });

        // 添加鼠标事件监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                switch (GameUtil.state) {
                    case 0: // 游戏进行中
                        if (e.getButton() == 1) { // 左键点击
                            GameUtil.MOUSE_X = e.getX();
                            GameUtil.MOUSE_Y = e.getY();
                            GameUtil.LEFT = true;
                        }
                        if (e.getButton() == 3) { // 右键点击
                            GameUtil.MOUSE_X = e.getX();
                            GameUtil.MOUSE_Y = e.getY();
                            GameUtil.RIGHT = true;
                        }
                    case 1: // 游戏胜利
                    case 2: // 游戏失败
                        if (e.getButton() == 1) { // 左键点击重新开始
                            if (e.getX() > GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W / 2)
                                    && e.getX() < GameUtil.OFFSET + GameUtil.SQUARE_LENGTH * (GameUtil.MAP_W / 2) + GameUtil.SQUARE_LENGTH
                                    && e.getY() > GameUtil.OFFSET
                                    && e.getY() < GameUtil.OFFSET + GameUtil.SQUARE_LENGTH) {
                                mapBottom.reGame(); // 重置底层地图
                                mapTop.reGame(); // 重置顶层地图
                                GameUtil.FLAG_NUM = 0; // 重置旗子数量
                                GameUtil.START_TIME = System.currentTimeMillis(); // 重置开始时间
                                GameUtil.state = 0; // 设置游戏状态为进行中
                            }
                        }
                        if (e.getButton() == 2) { // 中键点击返回难度选择
                            GameUtil.state = 3;
                            begin = true;
                        }
                        break;
                    case 3: // 难度选择界面
                        if (e.getButton() == 1) { // 左键点击选择难度
                            GameUtil.MOUSE_X = e.getX();
                            GameUtil.MOUSE_Y = e.getY();
                            begin = gameSelect.hard(); // 根据选择的难度开始游戏
                        }
                        break;
                    default:
                        // 其他状态
                }
            }
        });

        // 游戏主循环
        while (true) {
            repaint(); // 重绘窗口
            begin(); // 检查是否需要重新开始游戏
            try {
                Thread.sleep(40); // 控制帧率
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void begin() {
        if (begin) {
            begin = false; // 重置开始标志
            gameSelect.hard(GameUtil.level); // 设置游戏难度
            dispose(); // 关闭当前窗口
            Gamewin gameWin = new Gamewin(); // 创建新窗口
            GameUtil.START_TIME = System.currentTimeMillis(); // 重置开始时间
            GameUtil.FLAG_NUM = 0; // 重置旗子数量
            mapBottom.reGame(); // 重置底层地图
            mapTop.reGame(); // 重置顶层地图
            gameWin.launch(); // 启动新窗口
        }
    }

    @Override
    public void paint(Graphics g) {
        if (ifClear) {
            g.clearRect(0, 0, this.getWidth(), this.getHeight()); // 清除窗口内容
            ifClear = false; // 重置清除标志
        }
        if (GameUtil.state == 3) { // 难度选择界面
            gameSelect.paintSelf(g); // 绘制难度选择界面
        } else {
            offScreenImage = this.createImage(wigth, height); // 创建离屏图片
            Graphics gImage = offScreenImage.getGraphics();

            // 设置背景颜色
            if (GameUtil.state == 1) { // 游戏胜利
                gImage.setColor(Color.orange);
            } else if (GameUtil.state == 0) { // 游戏进行中
                gImage.setColor(Color.lightGray);
            } else if (GameUtil.state == 2) { // 游戏失败
                gImage.setColor(Color.red);
            }

            gImage.fillRect(0, 0, wigth, height); // 绘制背景
            mapBottom.paintSelf(gImage); // 绘制底层地图
            mapTop.paintSelf(gImage); // 绘制顶层地图
            g.drawImage(offScreenImage, 0, 0, null); // 将离屏图片绘制到窗口
        }
    }

    public static void main(String[] args) {
        Gamewin gameWin = new Gamewin();
        gameWin.launch(); // 启动游戏窗口
    }
}
