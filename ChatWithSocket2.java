package example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
* 客户端....
* */
public class ChatWithSocket2 extends JFrame {
    private Socket socket;  //客户端，和服务器通信
    private JTextArea sendArea; //  消息编辑
    private JTextArea contentArea;  //群聊消息显示
    private String name;    //当前用户名称

    public ChatWithSocket2(Socket socket, String name) {
        this.socket = socket;
        this.name = name;   //端口名字
        this.init();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        //开启线程
        ClientThread thread = new ClientThread(socket,contentArea);
        thread.start();
    }

    public void init() {
        this.setTitle("聊天室");
        this.setSize(330, 400);
        int y = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();

        this.setLocation((x - this.getWidth()) / 2, (y - this.getHeight()) / 2);
        this.setResizable(false);   //不允许自己修改尺寸....

        contentArea = new JTextArea();
        contentArea.setLineWrap(true);  //允许换行.控制每行显示长度最大不超过界面长度,只允许竖直滚动
        JScrollPane logPanel = new JScrollPane(contentArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        sendArea = new JTextArea();
        sendArea.setLineWrap(true);
        JScrollPane sendPanel = new JScrollPane(sendArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //分隔面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, logPanel, sendPanel);  //可以垂直调整..
        splitPane.setDividerLocation(250);
        this.add(splitPane, BorderLayout.CENTER);
        //JSplitPane 用于分隔两个（只能两个）Component并且这两个 Component 可以由用户交互式调整大小
        //按钮面板
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.add(bPanel, BorderLayout.SOUTH);

        JLabel namelabel = new JLabel("昵称: " + this.name + " ");
        bPanel.add(namelabel);

        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //你会发现，无法关闭........
            }
        });
        bPanel.add(closeButton);

        JButton sendButton = new JButton("发送");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = sendArea.getText();    //获取内容
                SimpleDateFormat formater = new SimpleDateFormat("HH:mm:ss");   //两位数的时，分，秒
                String time = formater.format(new Date());
                String sendStr = name + " " + time + " 说: " + str;
                //PrintWriter是一种过滤流，也叫处理流。也就是能对字节流和字符流进行处理
                PrintWriter out = null;
                try {
                    //OutputStreamWriter：是Writer的子类，将输出的字符流变为字节流，即：将一个字符流的输出对象变为字节流的输出对象。
                    out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));    //getOutputStream 字节输出流
                    out.println(sendStr);
                    out.flush();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                sendArea.setText("");   //发送完清空,该处发言..
            }
        });
        bPanel.add(sendButton);
    }
}
class ClientThread extends Thread{
    private Socket socket;
    private JTextArea contentArea;
    public ClientThread(Socket socket,JTextArea contentArea){
        this.socket = socket;
        this.contentArea = contentArea; //内容显示区域
    }

    public void run(){
        BufferedReader br = null;
        try{
            //InputStreamReader：是Reader的子类，将输入的字节流变为字符流，即：将一个字节流的输入对象变为字符流的输入对象。
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = null;
            while((str = br.readLine())!=null){
                System.out.println(str);
                contentArea.append(str);
                contentArea.append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //但对于socket流，把某次写入到该流的字符读取完能认为该socket流结束吗？
            //不能，因为这个流还存在，即使流中已经没有数据，但仍然可以继续写出和读入，那么程序自然会继续读流，而流真正的结束就是关闭
            if (br!=null){  //读完自然br = null.
                try{
                    br.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
