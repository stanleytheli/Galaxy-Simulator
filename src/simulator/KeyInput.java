package simulator;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput extends KeyAdapter {
	
	private boolean[] KeyDown = new boolean[4];
	
	private float Yspeed;
	private float Xspeed;
	private double playerSpeed = 1;
	
	private Game game;
	public KeyInput(Game game) {
		this.game = game;
		KeyDown[0] = false;
		KeyDown[1] = false;
		KeyDown[2] = false;
		KeyDown[3] = false;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			KeyDown[0] = true;
		}
		if (key == KeyEvent.VK_A) {
			KeyDown[1] = true;
		}
		if (key == KeyEvent.VK_S) {
			KeyDown[2] = true;
		}
		if (key == KeyEvent.VK_D) {
			KeyDown[3] = true;
		}
		if (key == KeyEvent.VK_Q) {
			Game.multiplyZoom((float)0.9);
			if (Game.zoomMultiplier > 0.07 && Game.zoomMultiplier < 5 && !(Game.zoomMultiplier == (float)0.07 || Game.zoomMultiplier == 5)) {
				Game.Xchange *= 0.9;
				Game.Ychange *= 0.9;
			}
		}
		if (key == KeyEvent.VK_E) {
			Game.multiplyZoom((float)1.1);
			if (Game.zoomMultiplier > 0.07 && Game.zoomMultiplier < 5 && !(Game.zoomMultiplier == (float)0.07 || Game.zoomMultiplier == 5)) {
				Game.Xchange *= 1.1;
				Game.Ychange *= 1.1;
			}

		}
		if (key == KeyEvent.VK_K) {
			Game.paused = true;
			Game.frozen = true;
			app.openForm(2);
		}
		if (key == KeyEvent.VK_SHIFT) {
			Game.drawGUI = !Game.drawGUI;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_W) {
			KeyDown[0] = false;
		}
		if (key == KeyEvent.VK_A) {
			KeyDown[1] = false;
		}
		if (key == KeyEvent.VK_S) {
			KeyDown[2] = false;
		}
		if (key == KeyEvent.VK_D) {
			KeyDown[3] = false;
		}
		
	}
	
	public void tick() {
		playerSpeed = Math.sqrt((double)(0.5 / Game.zoomMultiplier));
		playerSpeed = math.min(math.max(playerSpeed, 0.5), 1.5);
		if (KeyDown[0]) {
			Yspeed += playerSpeed;
		}
		if (KeyDown[1]) {
			Xspeed += playerSpeed;
		}
		if (KeyDown[2]) {
			Yspeed -= playerSpeed;
		}
		if (KeyDown[3]) {
			Xspeed -= playerSpeed;
		}
		
		Yspeed *= 0.85;
		Xspeed *= 0.85;
		
		Game.setVelX(Xspeed);
		Game.setVelY(Yspeed);
	}
}
