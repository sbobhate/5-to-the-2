package com.tedye.udptest;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	 private static String LOG_TAG="WifiBroadcastActivity";
	 private EditText IPAddress;
	 private String address;
	 public static final int DEFAULT_PORT = 43708;
	 Button startButton;
	 Button stopButton;
	 private receiveThread th = new receiveThread();
	   
	   
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_main);
	       
	     IPAddress = (EditText) this.findViewById(R.id.address );
	     startButton = (Button) this.findViewById(R.id.start);
	     stopButton = (Button) this.findViewById(R.id.stop);
	     startButton.setEnabled(true);
	     stopButton.setEnabled(false);
	       
	     address = getLocalIPAddress();
	  if( address != null ){
	   IPAddress.setText( address );
	  }
	  else {
	   IPAddress.setText("Can not get IP address");
	   
	   return;
	  }

	       
	  startButton.setOnClickListener( listener );
	  stopButton.setOnClickListener( listener );
	    }
	   
	    
	    private View.OnClickListener listener = new View.OnClickListener() {
	  
	  @Override
	  public void onClick(View v) {
	   if( v == startButton ){ 
		   
		   th.start();
	    startButton.setEnabled( false );
	    stopButton.setEnabled(true);
	   }else if( v == stopButton ){
		   th.kill();
	    startButton.setEnabled(true);
	    stopButton.setEnabled(false);
	   }
	  }
	 };
	       
	    private String getLocalIPAddress(){
	     try {
	      for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();  
	       en.hasMoreElements();) { 
	       NetworkInterface intf = en.nextElement(); 
	       for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) { 
	        InetAddress inetAddress = enumIpAddr.nextElement(); 
	        if (!inetAddress.isLoopbackAddress()) {
	         return inetAddress.getHostAddress().toString();
	        }
	       }
	      }
	     } catch (SocketException ex){
	      Log.e(LOG_TAG, ex.toString()); 
	     } 
	     return null;
	    }
	   
        private class receiveThread extends Thread{  
        	private boolean isRunning = true;
            @Override  
               public void run() {  
                   while(true){  
                	   if(isRunning){
                		   try {  
                			   Thread.sleep(1000);  
                		   } catch (InterruptedException e) {  
                			   e.printStackTrace();  
                		   }  
                		   try {  
                			   receiveIP();  
                		   } catch (Exception e) {  
                			   e.printStackTrace();  
                		   }  
                	   }  
                   }
            }  
            public void kill(){
            	isRunning = false;
            }
              
           private void receiveIP() throws Exception{  
                DatagramSocket dgSocket=new DatagramSocket(DEFAULT_PORT);  
                 byte[] by=new byte[1024];  
                 DatagramPacket packet=new DatagramPacket(by,by.length);  
                 dgSocket.receive(packet);  
                   
                 String str=new String(packet.getData(),0,packet.getLength());  
                   
                 Log.i("Debug","Data size:"+str.length());  
                 Log.i("debug","Date received:"+str); 

                 dgSocket.close();  
                 Log.i("debug","recevied message is ok.");  
            }  
	}
        
}