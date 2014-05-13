import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class WifiListener {
 private static DatagramSocket udpSocket;
 private static byte[] data = new byte[256];
 private static DatagramPacket udpPacket = new DatagramPacket( data, 256 );
 
 public static void main(String[] args) {
  try {
   udpSocket = new DatagramSocket( 43708 );
  } catch (SocketException e) {
   System.out.println( e.toString());
  }
  
  while( true ){
   try {    
    udpSocket.receive(udpPacket);
   } catch (Exception e) {
    System.out.println( e.toString());
   }
   
   if( udpPacket.getLength() != 0 ){
    String codeString = new String( data, 0, udpPacket.getLength() );
    System.out.println( codeString );
   }
  }
 }
}