package ec327.project.x5tictactoe;

/* 
 * @author Micheal Reavey
 * EC327, Spring 2014
 * Boston University, Boston, MA
 */

public class Sample {

	private SoundPalette s1 = new SoundPalette();
    private int buffsize;
    private int amp;
    private int samplerate;
    private int halfway; 
    private double [] tune;

    protected void speedup(double factor)
    {
    	buffsize = (int)(buffsize*factor);
    }
    
    protected double [] getSampleType(int number)
    { 
    	switch (number) {
    	    case 1: //mute
    	        tune = new double[] {0, 0};
    	        this.amp = 5000;
    	        return tune;
    	    case 2: //bass
    		    tune = new double[] {s1.e3, 1, 0, 2, s1.g3, 1, 0, 2, s1.a3, 1, 0, 1, s1.bb3, 1, 0, 1, 
    	    			s1.b3, 1, 0, 1, s1.f3, 1, 0, 1, s1.d3, 1, 0, 1};
    	    	this.amp = 1000;
    	    	this.halfway = 12;
    	    	return tune;
    	    case 3: //melody
    	    	tune = new double[] {s1.b5, 1, s1.a5, 1, s1.g4, 1, s1.a5, 1, s1.g4, 1, s1.e4, 1, s1.g4, 1, s1.fs4, 
    	    			1, s1.d4, 1, s1.b4, 1, s1.d4, 1, 0, 5 };
    	    	this.amp = 5000;
    	    	this.halfway = 16;
    	    	return tune;
    	    case 4:  //welcome screen
    		    tune = new double[] {s1.e3, 1, 0, 2, s1.g3, 1, 0, 12};
	    	    this.amp = 1000;
        		return tune;
    	    case 5:  //pause
        		tune = new double[] {s1.c5, 1, s1.f4, 1, s1.c5, 1, s1.e4, 2};
        		this.amp = 10000;
        		return tune;
    	    case 6: //quit
        		tune = new double[] {0, 1, s1.b4, 1, 0, 1, s1.e5, 1, 0, 3, s1.e2, 2};
        		this.amp = 10000;
        		return tune;
    	    case 7: //win 
    		    tune = new double[] {s1.b4, 1, s1.a4, 1, s1.g3, 1, s1.a4, 1, 0, 1, s1.e3, 1, 0, 1, s1.g3, 1, 
    		    		0, 1, s1.d4, 1, 0, 2, s1.e4, 2};
    	    	this.amp = 10000;
    	    	return tune;
    	    case 8: //lose
    		    tune = new double[] {s1.b4, 1, s1.a4, 1, s1.g3, 1, s1.e3, 1, 0, 1, s1.d3, 1, 0, 1, s1.g3, 1, 
    		    		0, 1, s1.f3, 1, 0, 2, s1.e3, 2};
    	    	this.amp = 10000;
        		return tune;
    	    case 9: //tie
    		    tune = new double[] {s1.b4, 1, s1.a4, 1, s1.g3, 1, s1.a4, 1, 0, 1, s1.d4, 1, 0, 1, s1.b4, 1, 
    		    		0, 1, s1.e3, 1, 0, 2, s1.b4, 2};
    	    	    this.amp = 10000;
    	    	return tune;
    	    default: //return muted track by default
    	    	tune = new double[] {0, 0};
    	        this.amp = 5000;
    	        return tune;
    	}
    }
    
    //The below methods are accessed from Track and SoundEffect
    protected int getAmplitude() {
    	return amp;
    }
    
    protected void setAmplitude(int amp) {
        this.amp = amp;
    }
    
    protected int getBuffsize() {
    	return buffsize;
    }
    
    protected void setBuffsize(int buff) {
    	this.buffsize = buff;
    }
    
    protected int getSampleRate() {
    	return samplerate;
    }
    
    protected void setSampleRate(int samp) {
    	this.samplerate = samp;
    }
    
    protected int getHalfwayPoint() {
    	return halfway;
    }
}
