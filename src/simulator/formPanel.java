package simulator;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;


public class formPanel extends JPanel{
	private JTextField galaxySizeField;
	private JTextField factionsField;
	private JTextField customNamesField;
	private JList anarchyModeList;
	private JButton enterBtn;
	private JButton cancelBtn;
	private FormListener formlistener;
	private JList generatorModeList;

	private JTextField factionName;
	private JTextField factionR;
	private JTextField factionG;
	private JTextField factionB;
	private JTextField factionTech;
	
	Object thisFormPanel = this;
	
	public formPanel(int formType) {
		if (formType == 1) {
			galaxySizeField = new JTextField(15);
			factionsField = new JTextField(15);
			customNamesField = new JTextField(15);
			enterBtn = new JButton("Simulate!");
			cancelBtn = new JButton("Cancel");
			anarchyModeList = new JList();
			generatorModeList = new JList();
						
			DefaultListModel listModel = new DefaultListModel();
			
			listModel.add(0, "Normal                              ");
			listModel.add(1, "Anarchy");
			listModel.add(2, "Deathmatch");
			
			anarchyModeList.setModel(listModel);
			
			DefaultListModel listModel2 = new DefaultListModel();
			
			listModel2.add(0, "Normal                              ");
			listModel2.add(1, "Donut");
			listModel2.add(2, "Wide Donut");
			listModel2.add(3, "Spiral");
			
			generatorModeList.setModel(listModel2);
			
			anarchyModeList.setBorder(BorderFactory.createEtchedBorder());
			generatorModeList.setBorder(BorderFactory.createEtchedBorder());
			
			generatorModeList.setSelectedIndex(Game.galaxyShape);
			
			anarchyModeList.setSelectedIndex(Game.gameMode);
			galaxySizeField.setText(String.valueOf(Game.galaxySize));
			factionsField.setText(String.valueOf(Game.initialFactions));
			
			enterBtn.addActionListener(new ActionListener() {
	
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						int galaxySize = Integer.parseInt(galaxySizeField.getText());
						int factions = Integer.parseInt(factionsField.getText());
						int gameMode = anarchyModeList.getSelectedIndex();						
						int generatorMode = generatorModeList.getSelectedIndex();
						String customNames = customNamesField.getText();
						
						FormEvent event = new FormEvent(thisFormPanel,galaxySize,factions,gameMode,generatorMode,customNames);
						
						formlistener.formEventOccured(event);
					} catch (Exception userInputBad) {
						//don't puke, the only reason the code would ever go here is if the user input a non-number, why would they do that
					}
				}
				
			});
			
			cancelBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					FormEvent event = new FormEvent(thisFormPanel);
					formlistener.formEventOccured(event);

				}
				
			});
			
			layoutComponents1();
			
			Dimension dim = getPreferredSize();
			dim.width = 500;
			setPreferredSize(dim);
			
			Border innerBorder = BorderFactory.createTitledBorder("New Galaxy Settings");
			Border outerBorder = BorderFactory.createEmptyBorder(1,5,5,5);
			setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
		} else if (formType == 2) {
			
			factionName = new JTextField(15);
			factionR = new JTextField(15);
			factionG = new JTextField(15);
			factionB = new JTextField(15);
			enterBtn = new JButton("Save Changes");
			cancelBtn = new JButton("Cancel");
			factionTech = new JTextField(15);
			
			factionName.setText(Game.factionNames[Game.factionClickedOn - 1]);
			factionR.setText(String.valueOf(Game.factionColors[Game.factionClickedOn - 1][0]));
			factionG.setText(String.valueOf(Game.factionColors[Game.factionClickedOn - 1][1]));
			factionB.setText(String.valueOf(Game.factionColors[Game.factionClickedOn - 1][2]));
			factionTech.setText(String.valueOf(Game.factionTech[Game.factionClickedOn - 1]));
			
			enterBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String newFactionName = factionName.getText();
						int newFactionR = Integer.parseInt(factionR.getText());
						int newFactionG = Integer.parseInt(factionG.getText());
						int newFactionB = Integer.parseInt(factionB.getText());
						int newFactionTech = Integer.parseInt(factionTech.getText());
						if (newFactionTech < 0) {
							newFactionTech = 0;
						}
						
						FormEvent event = new FormEvent(thisFormPanel,newFactionName,newFactionR,newFactionG,newFactionB,newFactionTech);
						
						formlistener.formEventOccured(event);

					} catch (Exception userInputBad) {
						//why would you not input stuff correctly though that's like basic manners
					}
					
				}
				
			});
			
			cancelBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					FormEvent event = new FormEvent(thisFormPanel);
					formlistener.formEventOccured(event);
				}
				
			});
			
			layoutComponents2();
			
			Dimension dim1 = getPreferredSize();
			dim1.width = 500;
			setPreferredSize(dim1);
			
			Border innerBorder1 = BorderFactory.createTitledBorder("Faction Settings");
			Border outerBorder1 = BorderFactory.createEmptyBorder(1,5,5,5);
			setBorder(BorderFactory.createCompoundBorder(outerBorder1, innerBorder1));

		}
	}
	public void layoutComponents2() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy = 0;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Faction Name: "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(factionName,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Faction Tech: "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(factionTech,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Faction Color (R): "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(factionR,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Faction Color (G): "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(factionG,gc);

		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Faction Color (B): "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(factionB,gc);
	

		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(enterBtn,gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(cancelBtn,gc);





	}
	public void layoutComponents1() {
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy = 0;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Galaxy Size: "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(galaxySizeField,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("Faction Count: "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(factionsField,gc);

		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0,0,0,15);
		add(new JLabel("Simulation Mode: "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0,0,0,0);
		add(anarchyModeList,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(0,0,0,15);
		add(new JLabel("Galaxy Shape: "),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(0,0,0,0);
		add(generatorModeList,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.insets = new Insets(20,0,0,15);
		add(new JLabel("<html>Custom Faction Names <br>(Separated by comma): </html>"),gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(20,0,0,0);
		add(customNamesField,gc);
		
		///////////////////////////////////////////////ROW///////////////////////////////////////////////
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.insets = new Insets(10,0,0,15);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(enterBtn,gc);
		
		gc.gridx = 1;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.insets = new Insets(10,0,0,0);
		add(cancelBtn,gc);
		

	}
	public void addLabel(Component obj, int x, int y, double weight) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.weighty = weight;
		gc.insets = new Insets(0,0,0,15);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(obj,gc);
	}
	public void addComponent(Component obj,int x, int y) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.weighty = 1;
		gc.insets = new Insets(0,0,0,0);
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		add(obj,gc);
	}
	public void addLabel(Component obj, int x, int y) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.weighty = 1;
		gc.insets = new Insets(0,0,0,15);
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		add(obj,gc);
	}
	public void addCustomLabel(Component obj, int x, int y, double weightx, double weighty, Insets insets) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.weightx = weightx;
		gc.weighty = weighty;
		gc.insets = insets;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
	}
	public void addCustomComponent(Component obj, int x, int y, double weightx, double weighty, Insets insets) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx = x;
		gc.gridy = y;
		gc.weightx = weightx;
		gc.weighty = weighty;
		gc.insets = insets;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
	}
	public void setFormListener(FormListener listener) {
		this.formlistener = listener;
	}

}
