
import java.awt.*;
import javax.swing.*;
/* 难度选择类*/
public class GameSelect {
    //判断是否点击到难度
    boolean hard(){
        if(GameUtil.MOUSE_X>100&&GameUtil.MOUSE_X<400){
            if(GameUtil.MOUSE_Y>50&&GameUtil.MOUSE_Y<150){
                GameUtil.level=1;
                GameUtil.state=0;
                return true;
            }
            if(GameUtil.MOUSE_Y>200&&GameUtil.MOUSE_Y<300){
                GameUtil.level=2;
                GameUtil.state=0;
                return true;
            }
            if(GameUtil.MOUSE_Y>350&&GameUtil.MOUSE_Y<450){
                GameUtil.level=3;
                GameUtil.state=0;
                return true;
            }
            // 添加自定义模式按钮检测
            if(GameUtil.MOUSE_Y>500&&GameUtil.MOUSE_Y<600){
                GameUtil.level=4; // 设置为自定义级别
                showCustomDialog(); // 显示自定义对话框
                return true;
            }
        }
        return false;
    }

    void paintSelf(Graphics g){
        // 绘制简单难度按钮
        g.setColor(Color.black);
        g.drawRect(100,50,300,100);
        GameUtil.drawWord(g,"简单",220,100,30,Color.black);

        // 绘制普通难度按钮
        g.drawRect(100,200,300,100);
        GameUtil.drawWord(g,"普通",220,250,30,Color.black);

        // 绘制困难难度按钮
        g.drawRect(100,350,300,100);
        GameUtil.drawWord(g,"困难",220,400,30,Color.black);
        
        // 绘制自定义难度按钮
        g.drawRect(100,500,300,100);
        GameUtil.drawWord(g,"自定义",220,550,30,Color.black);
    }
    
    // 显示自定义对话框
    void showCustomDialog() {
        boolean validInput = false;
        
        while (!validInput) {
            // 宽度输入
            JPanel widthPanel = new JPanel();
            widthPanel.add(new JLabel("地图宽度 (9-30):"));
            JTextField widthField = new JTextField(10);
            widthField.setText("16");
            widthPanel.add(widthField);
            
            // 高度输入
            JPanel heightPanel = new JPanel();
            heightPanel.add(new JLabel("地图高度 (9-24):"));
            JTextField heightField = new JTextField(10);
            heightField.setText("16");
            heightPanel.add(heightField);
            
            // 雷数输入
            JPanel minePanel = new JPanel();
            minePanel.add(new JLabel("雷的数量 (10-999):"));
            JTextField mineField = new JTextField(10);
            mineField.setText("40");
            minePanel.add(mineField);
            
            // 组合面板
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            
            panel.add(Box.createVerticalStrut(10));
            panel.add(widthPanel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(heightPanel);
            panel.add(Box.createVerticalStrut(10));
            panel.add(minePanel);
            panel.add(Box.createVerticalStrut(10));
            
            panel.setPreferredSize(new Dimension(400, 200));
            
            int result = JOptionPane.showConfirmDialog(null, panel, "自定义难度设置", 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());
                    int mines = Integer.parseInt(mineField.getText());
                    
                    // 更严格的边界检查
                    width = Math.max(9, Math.min(width, 30));
                    height = Math.max(9, Math.min(height, 24));
                    
                    // 计算格子总数
                    int totalCells = width * height;
                    
                    // 限制雷数不超过格子总数的1/3（降低比例以提高稳定性）
                    int maxMines = totalCells/3;
                    
                    // 确保最小有10个雷，最大不超过安全上限
                    mines = Math.max(10, Math.min(mines, maxMines));
                    
                    // 如果用户输入的雷数过多，显示提示信息
                    if (Integer.parseInt(mineField.getText()) > maxMines) {
                        JOptionPane.showMessageDialog(null, 
                            "您输入的雷数量过多！已自动调整为最大安全数量：" + maxMines,
                            "雷数已调整", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                    // 设置自定义参数
                    GameUtil.MAP_W = width;
                    GameUtil.MAP_H = height;
                    GameUtil.RAY_MAX = mines;
                    
                    // 确认参数有效，设置成功
                    validInput = true;
                    
                    // 确认后进入游戏状态
                    GameUtil.state = 0;
                    
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "请输入有效的数字！", "输入错误", JOptionPane.ERROR_MESSAGE);
                    // 不设置validInput为true，循环会继续，重新显示对话框
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, 
                        "发生错误：" + e.getMessage() + "\n请重新输入合理的数值。", 
                        "系统错误", JOptionPane.ERROR_MESSAGE);
                    // 不设置validInput为true，循环会继续，重新显示对话框
                }
            } else {
                // 用户取消，返回难度选择界面
                GameUtil.state = 3;
                return; // 退出方法
            }
        }
    }

    void hard(int level){
        // 根据选择的难度设置游戏参数
        switch (level){
            case 1:
                // 简单难度
                GameUtil.RAY_MAX = 10;
                GameUtil.MAP_W = 9;
                GameUtil.MAP_H = 9;
                break;
            case 2:
                // 普通难度
                GameUtil.RAY_MAX = 40;
                GameUtil.MAP_W = 16;
                GameUtil.MAP_H = 16;
                break;
            case 3:
                // 困难难度
                GameUtil.RAY_MAX = 99;
                GameUtil.MAP_W = 30;
                GameUtil.MAP_H = 16;
                break;
            case 4:
                // 自定义难度 - 参数已在showCustomDialog方法中设置
                break;
        }
    }
}