package ec327.project.x5tictactoe;

/*
 * @author Fangliang Ye (Ted)
 * EC327, Spring 2014
 * Boston University, Boston, MA
 */

import ec327.project.x5tictactoe.ConnectionService.State;
import ec327.project.x5tictactoe.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BTmenu extends Activity {

	// Debugging
	private static final String TAG = "BTmenu_Activity";
	private static final boolean D = true;

	private static final int REQUEST_CONNECT = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	public static final int MESSAGE_TOAST = 1;
	public static final int MESSAGE_DEVICE_NAME = 2;
	public static final int MESSAGE_READ = 3;

	public static String TOAST;

	private static BluetoothAdapter mBluetoothAdapter;
	private static BluetoothDevice mBluetoothDevice;

	public static String mConnectedDeviceName;
	public static String DEVICE_NAME;

	AlertDialog alert;
	ProgressDialog progress;

	public byte[] messageContainer2ByteArray(MessageContainer m) throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(m);
		return baos.toByteArray();

	}

	
	public void connect(View view){
		
		if (((GlobalVariables) getApplication()).getConnectionService() != null && mBluetoothAdapter.isEnabled()) {
			if (((GlobalVariables) getApplication()).getConnectionService().getState() != State.connected) {
				Intent startConnection = new Intent(getApplicationContext(), DeviceConnect.class);
				startActivityForResult(startConnection, REQUEST_CONNECT);
			}
		} else {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}


	}
	
	public void StartGame(View view){
		if (((GlobalVariables) getApplication()).getConnectionService() != null && mBluetoothAdapter.isEnabled()) {
			if (((GlobalVariables) getApplication()).getConnectionService().getState() == State.connected) {

				MessageContainer startGameMessage = new MessageContainer(MessageContainer.MESSAGE_NEW_GAME, -1, "player1");

				try {

					((GlobalVariables) getApplication()).getConnectionService().write(messageContainer2ByteArray(startGameMessage));
					((GlobalVariables) getApplication()).setSymbol(MessageContainer.MESSAGE_SYMBOL_X);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {

			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}
	}

	private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (D) {
				if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON) {
					Log.v(TAG, "RECEIVED BLUETOOTH STATE CHANGE: STATE_TURNING_ON");
				}

				if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
					Log.v(TAG, "RECEIVED BLUETOOTH STATE CHANGE: STATE_ON");
				}
			}

			if (mBluetoothAdapter.isEnabled()) {
				((GlobalVariables) getApplication()).setConnectionService(new ConnectionService(mHandler));
				((GlobalVariables) getApplication()).getConnectionService().start();
			}
		}

	};

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "ONSTART");

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		}

		if (((GlobalVariables) getApplication()).getConnectionService() != null) {
			((GlobalVariables) getApplication()).getConnectionService().setHandler(mHandler);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (((GlobalVariables) getApplication()).getConnectionService() != null) {
			((GlobalVariables) getApplication()).getConnectionService().setHandler(mHandler);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_btmenu);

		Typeface face = Typeface.createFromAsset(getAssets(), "tr2n.ttf"); 
		TextView connect = (TextView) findViewById(R.id.connect);
		TextView startbtgame = (TextView) findViewById(R.id.startbtgame);
		connect.setTypeface(face);
		startbtgame.setTypeface(face);
		
		Log.v(TAG, "ONCREATE");
		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast.makeText(this, "Bluetooth is not supported", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

		if (mBluetoothAdapter.isEnabled()) {
			((GlobalVariables) getApplication()).setConnectionService(new ConnectionService(mHandler));
			((GlobalVariables) getApplication()).getConnectionService().start();
		}

		// are you sure dialog
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:

					MessageContainer m = new MessageContainer(MessageContainer.MESSAGE_EXIT, -1);

					try {
						if (((GlobalVariables) getApplication()).getConnectionService().getState() == State.connected) {
							((GlobalVariables) getApplication()).getConnectionService().write(messageContainer2ByteArray(m));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					finish();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					break;
				}
			}
		};

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
		alt_bld.setMessage("Leaving Bluetooth Game?").setCancelable(false).setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener);
		alert = alt_bld.create();
		alert.setTitle("Are you sure?");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (((GlobalVariables) getApplication()).getConnectionService() != null) {
			((GlobalVariables) getApplication()).getConnectionService().stop();
			unregisterReceiver(bluetoothReceiver);
		}
	}

	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case MESSAGE_TOAST:

				String message = (String) msg.obj;

				if (message.contains("Connected"))
					Toast.makeText(getApplicationContext(), "Connected! Please start a new multiplayer game!", Toast.LENGTH_LONG).show();

				if (progress != null) {
					if (progress.isShowing()) {
						progress.dismiss();
					}
				}

				break;

			case MESSAGE_READ:

				try {

					byte[] readBuf = (byte[]) msg.obj;
					// int paramInt = msg.arg1;

					ByteArrayInputStream bais = new ByteArrayInputStream(readBuf);
					ObjectInputStream ois;
					ois = new ObjectInputStream(bais);

					MessageContainer readedMessage = (MessageContainer) ois.readObject();
					Log.i("BT menu", "Message NUM: " + readedMessage.getMessage());

					switch (readedMessage.getMessage()) {

					case MessageContainer.MESSAGE_NEW_GAME:

						Log.i("BT menu", "MESSAGE_NEW_GAME received from: " + readedMessage.getName() + " sending ACK");

						MessageContainer ackNewGame = new MessageContainer(MessageContainer.MESSAGE_ACK, -1, "player2");
						((GlobalVariables) getApplication()).getConnectionService().write(messageContainer2ByteArray(ackNewGame));
						((GlobalVariables) getApplication()).setSymbol(MessageContainer.MESSAGE_SYMBOL_O);

						Intent newActivity = new Intent(getApplicationContext(), Game.class);
						newActivity.putExtra("game_type", 4);
						startActivity(newActivity);

						break;

					case MessageContainer.MESSAGE_ACK:

						Log.i("BT menu", "MESSAGE_ACK received");

						if (readedMessage.getCoords() == -1) {
							Log.i("BT menu", "ACK for starting new Game with: " + readedMessage.getName());

							Intent ackGameActivity = new Intent(getApplicationContext(), Game.class);
							ackGameActivity.putExtra("game_type", 4);
							startActivity(ackGameActivity);
						}

						break;

					case MessageContainer.MESSAGE_EXIT:

						Log.i("BT menu", "Stop connection and accept new ones");
						((GlobalVariables) getApplication()).getConnectionService().stop();
						((GlobalVariables) getApplication()).getConnectionService().start();

						break;

					default:

						break;
					}

				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				break;

			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case REQUEST_CONNECT:

			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "No device selected", Toast.LENGTH_LONG).show();
				break;
			}

			progress = ProgressDialog.show(this, "Please wait", "Setting up connection...", true, true);

			String address = data.getStringExtra(DeviceConnect.EXTRA_DEVICE_ADDRESS);
			mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);
			Log.i(TAG, mBluetoothDevice.getName() + mBluetoothDevice.getAddress());

			((GlobalVariables) getApplication()).getConnectionService().connect(mBluetoothDevice);

			break;

		case REQUEST_ENABLE_BT:

			if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Cannot enable Bluetooth", Toast.LENGTH_LONG).show();
				break;
			}

			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Bluetooth is enabled", Toast.LENGTH_LONG).show();

				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
				break;
			}

			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_connect:

			Intent startConnection = new Intent(getApplicationContext(), DeviceConnect.class);
			startActivityForResult(startConnection, REQUEST_CONNECT);

			break;

		case R.id.menu_discoverable:
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
			break;

		case R.id.menu_disconnect:

			((GlobalVariables) getApplication()).getConnectionService().stop();
			((GlobalVariables) getApplication()).getConnectionService().start();

			break;

		}

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_bt, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		alert.show();
	}

}
