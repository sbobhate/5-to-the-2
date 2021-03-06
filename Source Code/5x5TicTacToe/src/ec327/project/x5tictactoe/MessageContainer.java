package ec327.project.x5tictactoe;

/*
 * @author Fangliang Ye (Ted)
 * EC327, Spring 2014
 * Boston University, Boston, MA
 */

import java.io.Serializable;

public class MessageContainer implements Serializable {

	private static final long serialVersionUID = 1L;

	public final transient static int MESSAGE_NEW_GAME = 1;
	public final transient static int MESSAGE_SYMBOL_X = 2;
	public final transient static int MESSAGE_SYMBOL_O = 3;
	public final transient static int MESSAGE_ACK = 4;
	public final transient static int MESSAGE_WIN = 5;
	public final transient static int MESSAGE_GAME_OVER = 6;
	public final transient static int MESSAGE_EXIT = 7;
	public final transient static int START_OVER = 8;

	private int message;
	private int coords;
	private String name;

	public MessageContainer() {
		this.setMessage(1);
		this.setCoords(0);
		this.setName("player1");
	}
	
	public MessageContainer(int message, int coords) {
		this.message = message;
		this.coords = coords;
	}
	
	public MessageContainer(int message, int coords, String name) {
		this.message = message;
		this.coords = coords;
		this.setName(name);
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public int getCoords() {
		return coords;
	}

	public void setCoords(int coords) {
		this.coords = coords;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
