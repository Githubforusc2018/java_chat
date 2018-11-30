package example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatWithSocket3 {
    private List<Socket> sockets = new ArrayList<Socket>(); //类集的应用，
    public ChatWithSocket3() throws Exception{
        ServerSocket ss = new ServerSocket(8888);
        System.out.println("服务器已监听8888端口");
        while(true){
            Socket socket = ss.accept();    //阻塞..直到客户端有消息
            sockets.add(socket);            //添加该socket到类集中
            String ip = socket.getInetAddress().getHostAddress();   //得到Host地址
            System.out.println("新用户进入,ip是:" + ip);
            Thread thread = new Thread(new ServerRunner(sockets,socket));
            thread.start(); //开启一个线程
        }
    }
    public static void main(String [] args){
        try{
            new ChatWithSocket3();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

class ServerRunner implements Runnable{
    private List<Socket> sockets;
    private Socket currentSocket;
    public ServerRunner(List<Socket>sockets,Socket currentScoket){
        this.sockets = sockets;
        this.currentSocket = currentScoket;
    }
    public void run(){
        String ip = currentSocket.getInetAddress().getHostAddress();
        BufferedReader br = null;
        try{

            br = new BufferedReader(new InputStreamReader(currentSocket.getInputStream()));
            String str = null;
            while ((str=br.readLine())!=null){
                System.out.println(ip+"说: "+str);
                for (Socket temp :sockets){
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(temp.getOutputStream()));
                    pw.println(str);
                    pw.flush();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

