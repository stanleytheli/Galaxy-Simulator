package simulator;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class mouseWheelInput implements MouseWheelListener {

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int timesScrolled = e.getUnitsToScroll();
		
		if (timesScrolled > 0) {
			for (int i = 0;i<timesScrolled;i++) {
				Game.multiplyZoom((float)0.96);
				if (Game.zoomMultiplier > 0.07 && Game.zoomMultiplier < 5 && !(Game.zoomMultiplier == (float)0.07 || Game.zoomMultiplier == 5)) {
					Game.Xchange *= 0.96;
					Game.Ychange *= 0.96;
				}
			}

		} else if (timesScrolled < 0) {
			for (int i = 0;i<timesScrolled*-1;i++) {
				Game.multiplyZoom((float)1.04);
				if (Game.zoomMultiplier > 0.07 && Game.zoomMultiplier < 5 && !(Game.zoomMultiplier == (float)0.07 || Game.zoomMultiplier == 5)) {
					Game.Xchange *= 1.04;
					Game.Ychange *= 1.04;
				}
			}
		}
		
		/* zooming out code
		Game.multiplyZoom((float)0.9);
		if (Game.zoomMultiplier > 0.21 && Game.zoomMultiplier < 4.99) {
			Game.Xchange *= 0.9;
			Game.Ychange *= 0.9;
		}
		*/// zooming in = 1.1

		
	}

}
