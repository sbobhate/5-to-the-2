package ec327.project.x5tictactoe;

/*
 * @author Fangliang Ye (Ted)
 * EC327, Spring 2014
 * Boston University, Boston, MA
 */

import android.app.Application;

public class GlobalVariables extends Application {

	private ConnectionService mConnectionService = null;
	private int symbol;
	
	@Override
	public void onCreate() {

	}
	
	public ConnectionService getConnectionService() {
		return mConnectionService;
	}

	public void setConnectionService(ConnectionService mConnectionService) {
		this.mConnectionService = mConnectionService;
	}
	
	public void setSymbol(int symbol) {
		this.symbol = symbol;
	}
	
	public int getSymbol() {
		return symbol;
	}

}
