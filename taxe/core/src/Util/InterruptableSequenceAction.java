package Util;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

public class InterruptableSequenceAction extends SequenceAction {
	// a sequenceAction that cna be interrupted with setInterrupt. interrupted indefinitely, requires interrupt changing to set back.
	boolean interrupted = false;
	
	public InterruptableSequenceAction() {
		super();
	}
	
	public void setInterrupt(boolean interrupted){
		this.interrupted = interrupted;
	}
	
	public boolean getInterrupt(){
		return this.interrupted;
	}

	@Override 
	public boolean act (float delta) {
		if (!interrupted) {
			return super.act(delta);
		}
		return false;
	}
}
