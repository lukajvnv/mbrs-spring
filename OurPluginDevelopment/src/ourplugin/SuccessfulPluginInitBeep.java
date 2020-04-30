package ourplugin;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

public class SuccessfulPluginInitBeep {
	Timer timer;
    Toolkit toolkit;
    
	public SuccessfulPluginInitBeep(int seconds) {
		toolkit = Toolkit.getDefaultToolkit();
	    timer = new Timer();
	    timer.schedule(new SuccessfulPluginInitTask(), seconds*1000);    
    }
	
	class SuccessfulPluginInitTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
	        toolkit = Toolkit.getDefaultToolkit();
            toolkit.beep();
            timer.cancel();
		}
		
	}

	
}
