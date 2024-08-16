package simulator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class mouseMotionInput implements MouseMotionListener {
	
	private int mouseX;
	private int mouseY;
	
	//leaderboard: 400,302
	//first item is at y level 77
	//last item would be y level 302
	//add 3 to y level in order to make it look nice

	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
		if (mouseX > 930 && mouseY < 50) {
			Game.buttonTouching = 1;
		} else if (mouseX > 930 && mouseY < 110) {
			Game.buttonTouching = 2;
		} else if (mouseX > 930 && mouseY < 170) {
			Game.buttonTouching = 3;
		} else if (Game.showingStarSystemInfo && mouseX > 660 && mouseY < 947 && mouseY > 915 && mouseX < 967) {
			Game.buttonTouching = 4;
		} else if (((Game.showingStarSystemInfo) || (Game.showingFactionInfo)) && mouseX > 660 && mouseX < 967 && mouseY > 873 && mouseY < 905) {
			Game.buttonTouching = 5;
		} else if (Game.showingFactionInfo && mouseX > 660 && mouseY < 947 && mouseY > 915 && mouseX < 967) {
			Game.buttonTouching = 4;
		} else if (Game.showingFactionInfo && mouseX > 660 && mouseX < 967 && mouseY > 831 && mouseY < 863) {
			Game.buttonTouching = 16;
		} else if (mouseX > 930 && mouseY < 290 && mouseY > 240) {
			Game.buttonTouching = 17;
		} else if (mouseX < 425 && mouseY < 327) {
			if (mouseY > 77) {
				Game.buttonTouching = 6 + (int)((mouseY-77)/25);
			} else {
				Game.buttonTouching = 0;
			}
		} else {
			Game.buttonTouching = 0;
		}

	}

}
