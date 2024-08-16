package simulator;

import java.awt.event.ActionListener;
import java.util.EventObject;

public class FormEvent extends EventObject {

	private int galaxySize;
	private int Factions;
	private int gameMode;
	private boolean rebellions;
	private int generatorMode;
	private String customNames;
	
	private boolean canceled;
	
	private String newFactionName;
	private int newFactionR;
	private int newFactionG;
	private int newFactionB;
	private int newFactionTech;

	public FormEvent(Object source, int galaxySize, int Factions, int gameMode, int generatorMode, String customNames) {
		super(source);
		this.galaxySize = galaxySize;
		this.Factions = Factions;
		this.gameMode = gameMode;
		this.generatorMode = generatorMode;
		this.customNames = customNames;
		canceled = false;

	}

	public FormEvent(Object source, String newFactionName, int newFactionR, int newFactionG, int newFactionB, int newFactionTech) {
		super(source);
		this.newFactionName = newFactionName;
		this.newFactionR = newFactionR;
		this.newFactionG = newFactionG;
		this.newFactionB = newFactionB;
		this.newFactionTech = newFactionTech;
		canceled = false;

	}
	
	public FormEvent(Object source) {
		super(source);
		canceled = true;
	}
	
	public int getGalaxySize() {
		return galaxySize;
	}

	public int getFactions() {
		return Factions;
	}

	public int getAnarchyMode() {
		return gameMode;
	}

	public int getGeneratorMode() {
		return generatorMode;
	}

	public String getNewFactionName() {
		return newFactionName;
	}

	public int getNewFactionR() {
		return newFactionR;
	}

	public int getNewFactionG() {
		return newFactionG;
	}
	
	public int getNewFactionB() {
		return newFactionB;
	}
	
	public boolean getCancel() {
		return canceled;
	}
	
	public int getNewFactionTech() {
		return newFactionTech;
	}
	
	public String getCustomNames() {
		return customNames;
	}
}
