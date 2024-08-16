package simulator;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Math;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


import java.awt.event.*;

public class MainFrame extends JFrame implements ActionListener{

	private formPanel formpanel;
	public MainFrame(final int formType) {
		super("Galaxy Simulator");
		formpanel = new formPanel(formType);
		add(formpanel,BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		setSize(525,450);
		formpanel.setFormListener(new FormListener() {

			@Override
			public void formEventOccured(FormEvent e) {
				if (!e.getCancel()) {
					if (formType == 1) {
						// TODO Auto-generated method stub
						Game.galaxySize = e.getGalaxySize();
						Game.factions = e.getFactions();
						Game.gameMode = e.getAnarchyMode();
						Game.Xchange = 0;
						Game.Ychange = 0;
						Game.galaxyShape = e.getGeneratorMode();
						if (e.getCustomNames().length() > 0) {
							Game.customFactionNames = e.getCustomNames().split(",");
							for (int i = 0;i<Game.customFactionNames.length;i++) {
								Game.customFactionNames[i] = Game.customFactionNames[i].trim();
							}
						} else {
							Game.customFactionNames = new String[0];
						}
						
						Game.initialFactions = e.getFactions();
						Game.initialize();
						
						Game.frozen = false;

						dispose();
						
					} else if (formType == 2) {
						Game.factionNames[Game.factionClickedOn - 1] = e.getNewFactionName();
						Game.factionColors[Game.factionClickedOn - 1][0] = e.getNewFactionR();
						Game.factionColors[Game.factionClickedOn - 1][1] = e.getNewFactionG();
						Game.factionColors[Game.factionClickedOn - 1][2] = e.getNewFactionB();
						Game.factionTech[Game.factionClickedOn - 1] = e.getNewFactionTech();
						
						Game.frozen = false;
	
						dispose();
					}
				} else {
					Game.frozen = false;
					dispose();
				}
			}
			
		});
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
