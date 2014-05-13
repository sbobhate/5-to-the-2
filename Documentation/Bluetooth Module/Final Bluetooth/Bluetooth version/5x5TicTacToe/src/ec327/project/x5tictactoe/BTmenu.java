package ec327.project.x5tictactoe;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class BTmenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btmenu);
		
		BluetoothAdapter mBA = BluetoothAdapter.getDefaultAdapter();
		if (mBA == null) {
			Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	public void selectedJoin(View v){
		Intent i = new Intent(this,BluetoothClientConnection.class);
		startActivity(i);
	}
	
	public void selectedNew(View v){
		Intent i = new Intent(this,BluetoothServerConnection.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}