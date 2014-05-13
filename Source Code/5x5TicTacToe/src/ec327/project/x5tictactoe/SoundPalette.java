package ec327.project.x5tictactoe;

/*
 * @author Micheal Reavey
 * EC327, Spring 2014
 * Boston University, Boston, MA
 */

public class SoundPalette {
	
	private double semitone = Math.pow(2.0f, 1/12.f);
	private double base_freq = 440.f;
	
	//These are all pitches, referred to by their pitch names for convenience
	//Accessed from Sample class
	protected double g5 = base_freq*(double)Math.pow(semitone, 23);
	protected double e5 = base_freq*(double)Math.pow(semitone, 19);
	protected double d5 = base_freq*(double)Math.pow(semitone, 17);
	protected double c5 = base_freq*(double)Math.pow(semitone, 15);	
	protected double b5 = base_freq*(double)Math.pow(semitone, 14);
	protected double bb5 = base_freq*(double)Math.pow(semitone, 13);
	protected double a5 = base_freq*(double)Math.pow(semitone, 12);
	protected double gs4 = base_freq*(double)Math.pow(semitone, 11);
	protected double g4 = base_freq*(double)Math.pow(semitone, 10);
	protected double fs4 = base_freq*(double)Math.pow(semitone, 9);
	protected double f4 = base_freq*(double)Math.pow(semitone, 8);
	protected double e4 = base_freq*(double)Math.pow(semitone, 7);
	protected double eb4 = base_freq*(double)Math.pow(semitone, 6);
	protected double d4 = base_freq*(double)Math.pow(semitone, 5);
	protected double cs4 = base_freq*(double)Math.pow(semitone, 4);
	protected double c4 = base_freq*(double)Math.pow(semitone, 3);
	protected double b4 = base_freq*(double)Math.pow(semitone, 2);
	protected double a4 = base_freq*(double)Math.pow(semitone, 0);
	protected double g3 = base_freq*(double)Math.pow(semitone, -2);
	protected double fs3 = base_freq*(double)Math.pow(semitone, -3);
	protected double f3 = base_freq*(double)Math.pow(semitone, -4);
	protected double e3 = base_freq*(double)Math.pow(semitone, -5);
	protected double eb3 = base_freq*(double)Math.pow(semitone, -6);
	protected double d3 = base_freq*(double)Math.pow(semitone, -7);
	protected double db3 = base_freq*(double)Math.pow(semitone, -8);
	protected double c3 = base_freq*(double)Math.pow(semitone, -9);
	protected double b3 = base_freq*(double)Math.pow(semitone, -10);
	protected double bb3 = base_freq*(double)Math.pow(semitone, -11);
	protected double a3 = base_freq*(double)Math.pow(semitone, -12);
	protected double e2 = base_freq*(double)Math.pow(semitone, -17);
	protected double e1 = base_freq*(double)Math.pow(semitone, -29);
	protected double e0 = base_freq*(double)Math.pow(semitone, -41);
	
	protected double e4andb4 = base_freq*(double)Math.pow(semitone, 7) * base_freq*(double)Math.pow(semitone, 14);

}
