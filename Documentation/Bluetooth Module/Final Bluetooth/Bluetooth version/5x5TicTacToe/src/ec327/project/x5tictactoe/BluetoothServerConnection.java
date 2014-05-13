package ec327.project.x5tictactoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class BluetoothServerConnection extends Activity{

	BluetoothAdapter mBA;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	private static final UUID MY_UUID = UUID.fromString("2830e100-0cb6-11e3-8ffd-0800200c9a66");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_server_conn);
				
		mBA = BluetoothAdapter.getDefaultAdapter();
				
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);
		
		ServerTask st = new ServerTask();
		st.execute(MY_UUID);
	}
	
	private class ServerTask extends AsyncTask<UUID,Void,BluetoothSocket>{

		@Override
		protected BluetoothSocket doInBackground(UUID... uuid) {
			BluetoothServerSocket bss;
			try {
				bss = mBA.listenUsingRfcommWithServiceRecord("BluetoothNikhil", uuid[0]);
				BluetoothSocket connected = bss.accept();
				if(connected!=null)
					bss.close();
				return connected;
			} catch (IOException e) {e.printStackTrace();}
			
			return null;
		}

		@Override
		protected void onPostExecute(BluetoothSocket socket) {
			if(socket == null) return;
			try {
				out = (ObjectOutputStream) socket.getOutputStream();
				in = (ObjectInputStream) socket.getInputStream();
				Intent i = new Intent(BluetoothServerConnection.this,Game.class);
				i.putExtra("mode", 4);
				startActivity(i);
			} catch (IOException e) {e.printStackTrace();}
		}
	}
}
