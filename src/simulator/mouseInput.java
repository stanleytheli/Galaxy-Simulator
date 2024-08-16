package simulator;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;
import java.awt.MouseInfo;
import java.util.Arrays;
import java.util.List;


public class mouseInput implements MouseListener {
	public static boolean mouseDown = false;
	
	public static int startX, startY, nowX, nowY, changeInX, changeInY, mouseX, mouseY;	
	
	//leaderboard: 400,327
	//first item is at y level 77
	//last item would be y level 302
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		mouseX = e.getX();
		mouseY = e.getY();
		//System.out.println(mouseX + ", " + mouseY);
		if (!Game.frozen && Game.drawGUI) {
			if (mouseX > 930 && mouseY < 50) {
				Game.paused = !Game.paused;
			} else if (mouseX > 930 && mouseY < 110) {
				if (Game.timeSpeed == (float)0.35) {
					Game.timeSpeed = (float)0.5;
					return;
				}
				if (Game.timeSpeed == (float)0.5) {
					Game.timeSpeed = (float)1;
					return;
				}
				if (Game.timeSpeed == (float)1) {
					Game.timeSpeed = (float)2;
					return;
				}
				if (Game.timeSpeed == (float)2) {
					Game.timeSpeed = (float)5;
					return;
				}
				if (Game.timeSpeed == (float)5) {
					Game.timeSpeed = (float)10;
					return;
				}
				if (Game.timeSpeed == (float)10) {
					Game.timeSpeed = (float)0.1;
					return;
				}
				if (Game.timeSpeed == (float)0.1) {
					Game.timeSpeed = (float)0.25;
					return;
				}
				if (Game.timeSpeed == (float)0.25) {
					Game.timeSpeed = (float)0.35;
					return;
				}
			} else if (mouseX > 930 && mouseY < 170) {
				Game.paused = true;
				Game.frozen = true;
				app.openForm(1);
			} else if (Game.showingStarSystemInfo && mouseX > 660 && mouseY < 947 && mouseY > 915 && mouseX < 967) {
				//g.drawPolygon(new int[] {660,660,967,967}, new int[] {915,947,947,915}, 4);
				Game.showingStarSystemInfo = false;
				Game.systemClickedOn = -1;
			} else if (Game.showingStarSystemInfo && Game.starSystemFaction[Game.systemClickedOn] != 0 && mouseX > 660 && mouseX < 967 && mouseY > 873 && mouseY < 905) {
				//g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
				Game.clickedOnFactionBtn = true;
			} else if (Game.showingStarSystemInfo && Game.starSystemFaction[Game.systemClickedOn] == 0 && mouseX > 660 && mouseX < 967 && mouseY > 873 && mouseY < 905) {				
				//Generate new faction
				System.out.println("hi");
				Game.clickedPlantBtn = true;
			} else if (Game.showingFactionInfo && mouseX > 660 && mouseX < 967 && mouseY > 873 && mouseY < 905) {
				Game.clickedOnCollapse = true;
			} else if (Game.showingFactionInfo && mouseX > 660 && mouseY < 947 && mouseY > 915 && mouseX < 967) {
				//g.drawPolygon(new int[] {660,660,967,967}, new int[] {915,947,947,915}, 4);
				Game.showingFactionInfo = false;
				Game.factionClickedOn = -1;
			} else if (Game.showingFactionInfo && Game.buttonTouching == 16) {
				Game.paused = true;
				Game.frozen = true;
				app.openForm(2);
			} else if (mouseX > 930 && mouseY < 290 && mouseY > 240) {
				Game.squares = !Game.squares;
			} else if (Game.buttonTouching < 16 && Game.buttonTouching > 5) {
				if (Game.factions > Game.buttonTouching-6 && Game.factionStarSystems[math.findNotLargest(Game.factionStarSystems, Game.buttonTouching-6)] > 0) {
					Game.clickedOnLeaderboard = false;
					Game.showingFactionInfo = true;
					Game.factionClickedOn = math.findNotLargest(Game.factionStarSystems, Game.buttonTouching-6) + 1;
					Game.systemClickedOn = -1;
					Game.showingStarSystemInfo = false;
				}
			} else {
				Game.clickedOnStarSystem = true;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		startX = getMouseLocation()[0];
		startY = getMouseLocation()[1];
		mouseDown = true;
	}
	
	public static int[] getMouseLocation() {
		String[] input = {MouseInfo.getPointerInfo().getLocation().toString().split(",")[0],MouseInfo.getPointerInfo().getLocation().toString().split(",")[1]};
		String[] input2 = {"",""};
		
		char[] numbers = {'0','1','2','3','4','5','6','7','8','9','-'};
		
		for (int i = 0;i<input[0].length();i++) {
			if (contains(numbers,input[0].charAt(i))) {
				input2[0] += input[0].charAt(i);
			}
		}
		for (int i = 0;i<input[1].length();i++) {
			if (contains(numbers,input[1].charAt(i))) {
				input2[1] += input[1].charAt(i);
			}
		}
		int[] output = new int[2];
		output[0] = Integer.parseInt(input2[0]);
		output[1] = Integer.parseInt(input2[1]);
		
		return output;
	}
	
	public static boolean contains(char[] array, char contain) {
		for (char check : array) {
			if (check == contain) {
				return true;
			}
		}
		return false;
	}
	
	
}
