package example;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 *   Client login frame
 */
public class ChatWithSocket1 extends JFrame {
    private JTextField nametext;
    private JPasswordField passwordtext;
    public ChatWithSocket1(){
        this.init();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void init() {
        this.setTitle("经纬度大佬的聊天室");
        this.setSize(330, 230);
        int y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        this.setLocation((x - this.getWidth()) / 2, (y - this.getHeight()) / 2);
        this.setResizable(false);   //不允许自己修改尺寸....

        /*
         * 登录界面的图标方式
         * */
        Icon icon = new ImageIcon("f://java//src//icon.png");
        JLabel label = new JLabel(icon);    //带图标的标签构造方法
        this.add(label, BorderLayout.NORTH);    //组件->面板->框架

        JPanel mainPanel = new JPanel();
        Border border = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED); //borderfactory是一个产生创建边框的的工厂类
        mainPanel.setBorder(BorderFactory.createTitledBorder(border, "输入登录信息", TitledBorder.CENTER, TitledBorder.TOP)); //标题登录信息,位于中间，上方
        this.add(mainPanel, BorderLayout.CENTER);   //将mainPanel 添加到中间
        mainPanel.setLayout(null);  //自定义布局
        JLabel namelabel = new JLabel("请输入名称");
        namelabel.setBounds(30, 30, 80, 22);
        mainPanel.add(namelabel);

        nametext = new JTextField();    //名称输入域
        nametext.setBounds(115, 30, 165, 22);
        mainPanel.add(nametext);

        JLabel passwordlabel = new JLabel("请输入密码");
        passwordlabel.setBounds(30, 60, 80, 22);
        mainPanel.add(passwordlabel);
        passwordtext = new JPasswordField();    //密码输入域
        passwordtext.setBounds(115, 60, 165, 22);
        mainPanel.add(passwordtext);

        //接下来位置排放...布局问题
        //重置，提交按钮布局
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); //流动布局的右对齐
        this.add(bPanel, BorderLayout.SOUTH);
        JButton reset = new JButton("重置");
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nametext.setText("");
                passwordtext.setText("");
            }
        });
        bPanel.add(reset);

        /*实现提交按钮
         * */
        JButton submit = new JButton("提交");
        submit.addActionListener(new LoginAction(this));
        bPanel.add(submit);
    }
    class LoginAction implements ActionListener{
        private JFrame self;
        public LoginAction(JFrame frame){
            this.self = frame;
        }
        public void actionPerformed(ActionEvent e){
            try{
                Socket socket = new Socket("127.0.0.1",8888);
                new ChatWithSocket2(socket,nametext.getText());
                self.dispose(); //调用该方法关闭登录框
            }catch (UnknownHostException e1){
                e1.printStackTrace();
                JOptionPane.showConfirmDialog(self, "找不到指定服务器!~","连接失败",JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE) ;
            }catch(IOException e1)  {
                e1.printStackTrace() ;
                JOptionPane.showConfirmDialog(self, "连接服务器出错，请重试！","连接失败",JOptionPane.OK_OPTION,JOptionPane.ERROR_MESSAGE) ;
            }
        }

    }


    public static void main(String []args){
        new ChatWithSocket1();
    }
}


