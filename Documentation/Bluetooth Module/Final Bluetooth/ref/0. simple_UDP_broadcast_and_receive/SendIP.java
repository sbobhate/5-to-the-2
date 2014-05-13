import java.io.IOException;  
import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;  
import java.net.SocketException;  
import java.net.UnknownHostException;  
  
  
public class SendIP {  
  
    public static void main(String args[]) {  
        new SendIP().lanchApp();  
     }  
      
    private void lanchApp(){  
        SendThread th=new SendThread();  
        th.start();  
    }  
      
    // send every 1 sec.
    private class SendThread extends Thread{  
        @Override  
        public void run() {  
            while(true){  
                try {  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                try {  
                    BroadcastIP();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
          
        private void BroadcastIP()throws Exception{  
             DatagramSocket dgSocket=new DatagramSocket();
              byte b[]="Hello, this is my message".getBytes();  
              DatagramPacket dgPacket=new DatagramPacket(b,b.length,InetAddress.getByName("255.255.255.255"),8989);  // send to "255.255.255.255" = broadcasting 
              dgSocket.send(dgPacket);  
              dgSocket.close();  
              System.out.println("send message is ok.");  
        }  
    }  
      
}