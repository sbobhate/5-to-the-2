package ec327.project.x5tictactoe;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;

public class wifiConnection{
	private String gameRmsg = "";
	private String gameSmsg = "#########################";
	private String receivedMsg = "";
	DatagramSocket dgSocket;
	private boolean host;
	private boolean connected;
	private InetAddress targetIP;
	private int targetPort;
    private int state; // state 1: looking for host, state 2: hosting, state 3: match found,state 0: no player found
    
    wifiConnection(){
    	try {
			dgSocket = new DatagramSocket(8989);
		} catch (SocketException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	connected = false;
    	setup();
    	while(state == 1 || state == 2);
    	if (state != 3){
    		Log.i("Debug", " NO player");
    	} else {
    		Log.i("Debug", " Game Start state " + state);
    		connected = true;
    		gaming th3 = new gaming();
    		th3.start();
    		Log.i("Debug", " Send/Receiving Thread start ");
    	}
    }
    
    	
    private void setup(){
			state = 1;
	    	host  = false;
	    	targetPort = 12000;
	    	search_host th1 = new search_host();
	    	th1.start();
	    	while (state == 1){
	    		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	};
	    	targetPort = 8989;
	    	hosting th2 = new hosting();
	    	try {
				dgSocket = new DatagramSocket(12000);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	th2.start();
	    	while (state == 2){
	    		try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	};
	}

	public void stop(){
		dgSocket.close();
	}
	
	private class gaming extends Thread{
    	@Override
    	public void run(){
    		while (state == 3){
    			try {
	            	sendData(gameSmsg);
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }
    			try {  
    				gameRmsg = receiveData(200);  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }
    			try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	
	}
	
	
	
	
	
    private class hosting extends Thread{
    	int counter = 5 * 60; // host for 1 min
    	@Override
    	public void run(){
	   		while (state == 2){
	   			Log.i("Debug","host started");
				try {  
					setTargetIP(InetAddress.getByName("255.255.255.255"));
	            	sendData("TTT INVITATION 2"); // send to "255.255.255.255" = broadcasting  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }
	
				try {  
	            	receivedMsg = receiveData(200);  
	            } catch (Exception e) {  
	                e.printStackTrace();  
	            }
				if (receivedMsg.equals("INVITATION ACCEPTED 1")){
					state = 3;
					host = true;
					break;
				}
				if (counter == 0){
					break;
				}
				counter--;
			}
            if(!receivedMsg.equals("INVITATION ACCEPTED 1")){
                state = 0;                 //no player
            }
    	}
    	
    }
    
    
    private class search_host extends Thread{
    	int counter = 5 * 10;  // search host for 10 sec.
    	@Override
    	public void run(){
    		while(state == 1){ 
            	Log.i("Debug","search host started");
                try {  
                	receivedMsg = receiveData(200);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }
                if(receivedMsg.equals("TTT INVITATION 2")){
                	state = 3;
                	host = false;// find a host, go to state 3, game starts.
                    try {
                    	sendData("INVITATION ACCEPTED 1");  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }
                	break;} 
                if (counter == 0){break;}
                counter--;
            }
            if(!receivedMsg.equals("TTT INVITATION 2")){
                state = 2;                 //become a host
            }
    	}
        	  	
    }
	
    public void setTargetIP(InetAddress IP){
    	targetIP = IP;
    }
    
	public void sendData(String sendingMsg)throws Exception{  
        byte b[]=sendingMsg.getBytes();  
        DatagramPacket dgPacket=new DatagramPacket(b,b.length,targetIP,targetPort);  
        dgSocket.send(dgPacket);    
        Log.i("Debug","send message is ok.");  
   }
	
    public String receiveData(int timeout) throws Exception{ 
        dgSocket.setSoTimeout(timeout);
        byte[] by=new byte[128];  
        DatagramPacket packet=new DatagramPacket(by,by.length);  
        dgSocket.receive(packet);
        String Msg = new String(packet.getData(),0,packet.getLength());  
        Log.i("Debug","Data size:" + Msg.length());  
        Log.i("Debug","Date received:" + Msg); 
        Log.i("Debug","recevied message is ok."); 
        return Msg;
    } 
    
    public boolean isHost(){
    	return host;
    }
    public boolean isStarted(){
    	return connected;
    }
    
    public void SetSendingMsg(String msg){
    	gameSmsg = msg;
    }
    public String GetReceivedMsg(){
    	return gameRmsg;
    }
	
}