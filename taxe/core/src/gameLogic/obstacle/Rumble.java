package gameLogic.obstacle;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import fvs.taxe.controller.Context;

// adapted from https://carelesslabs.wordpress.com/2014/05/08/simple-screen-shake/
public class Rumble {
	// does all of the maths for the shaking, returns the coordinates corresponding to the amount of movement in that "tick"
	  public float time;
	  Random random;
	  float x, y;
	  float current_time;
	  float power;
	  float current_power;
	  
	  public Rumble(){
	    time = 0;
	    current_time = 0;
	    power = 0;
	    current_power = 0;
	  }
	  
	  // Call this function with the force of the shake 
	  // and how long it should last      
	  public void rumble(Context context, float power, float time) {
	    random = new Random();
	    this.power = power;
	    this.time = time;
	    this.current_time = 0;
	  }
	        
	  public Vector2 tick(float delta){
	    
	    if(current_time <= time) {
	      current_power = power * ((time - current_time) / time);
	      // generate random new x and y values taking into account
	      // how much force was passed in
	      x = (random.nextFloat() - 0.5f) * 2 * current_power;
	      y = (random.nextFloat() - 0.5f) * 2 * current_power;
	      
	      
	      current_time += delta;
	      return new Vector2 (-x, -y);
	    } else {
	    	time = 0;
	    	return new Vector2 (0f, 0f);
	    }
	  }      
	}

