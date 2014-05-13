package ec327.project.x5tictactoe;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class BluetoothClientConnection extends Activity {
	
	List<String> allDevices = new ArrayList<String>();
	BluetoothAdapter mBA;
	BluetoothSocket client;
	List<BluetoothDevice> pairedDevices;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;
	private static final UUID MY_UUID = UUID.fromString("2830e100-0cb6-11e3-8ffd-0800200c9a66");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_bluetooth_client_conn);
				
		TextView info =(TextView) findViewById(R.id.runAs);
		info.setText("Click the Bluetooth connection to join");
		mBA = BluetoothAdapter.getDefaultAdapter();
		
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(!mBA.isEnabled()){
			Intent enableBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBlue,100);
		}
	}
	
	public void selectedPaired(View v){
		ProgressBar progress = (ProgressBar) findViewById(R.id.searching);
		progress.setVisibility(View.VISIBLE);
		pairedDevices = new ArrayList<BluetoothDevice>(); 
		pairedDevices.addAll(mBA.getBondedDevices());
		allDevices = new ArrayList<String>();
		for(BluetoothDevice device : pairedDevices){
			allDevices.add(device.getName());
		}
		ListView devicesList = (ListView)findViewById(R.id.devicesList);
		ArrayAdapter<String> list = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,allDevices);
		devicesList.setAdapter(list);
		devicesList.setOnItemClickListener(setupClient);
		progress.setVisibility(View.GONE);
	}
	
	private final OnItemClickListener setupClient = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			try {
				mBA.cancelDiscovery();
				client = pairedDevices.get(position).createRfcommSocketToServiceRecord(MY_UUID);
				client.connect();
				out = (ObjectOutputStream) client.getOutputStream();
				in = (ObjectInputStream) client.getInputStream();
				Intent i = new Intent(BluetoothClientConnection.this,Game.class);
				i.putExtra("mode", 5);
				startActivity(i);
			} catch (IOException e) {e.printStackTrace();}
		}};
	
	public void selectedSearch(View v){
		ProgressBar progress = (ProgressBar) findViewById(R.id.searching);
		progress.setVisibility(View.VISIBLE);
		pairedDevices = new ArrayList<BluetoothDevice>();
		mBA.startDiscovery();
		allDevices = new ArrayList<String>();
		ListView devicesList = (ListView)findViewById(R.id.devicesList);
		devicesList.setAdapter(null);
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if(BluetoothDevice.ACTION_FOUND.equals(action)){
	        	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	        	pairedDevices.add(device);
	        	allDevices.add(device.getName());
	        	ListView devicesList = (ListView)findViewById(R.id.devicesList);
	    		ArrayAdapter<String> list = new ArrayAdapter<String>(BluetoothClientConnection.this,android.R.layout.simple_list_item_1,allDevices);
	    		devicesList.setAdapter(list);
	    		devicesList.setOnItemClickListener(setupClient);
	    		ProgressBar progress = (ProgressBar) findViewById(R.id.searching);
	    		progress.setVisibility(View.GONE);
	        }
	    }
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
		case 100: 
			if(resultCode!=Activity.RESULT_OK){
				Toast.makeText(this, "Blutooth not on", Toast.LENGTH_SHORT).show();
				finish();
			}
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
	}
}
