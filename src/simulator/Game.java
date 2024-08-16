package simulator;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;

public class Game extends Canvas implements Runnable {
	
	private static final long serialVersionUID = -2268661975898887974L; // no clue what this is but I apparently need it so

	public static float timeSpeed = (float) 0.5; // Time multiplier, 0.5 = normal time don't ask questions
	public static int galaxySize = 500; // How large the galaxy is, in star systems.
	public static int factions = 20; // How many factions will be spawned
	public static boolean squares = false;
	public static int gameMode = 0; //0 = normal, 1 = anarchy, 2 = deathmatch
	
	public static int displayCount = 10;
	
	public static boolean drawGUI = true;
	
	public static String[] customFactionNames = {};
	
	public static int minDistance = 50;
	public static int minGDistance = 36;
	
	public static int starCount = 200; //background stars

	public static float timescale = (float) (1 / timeSpeed);
	public static int initialFactions = factions; //just to keep track
	public static int fps; 
	public static float zoomMultiplier = (float) 1; //zoom multiplier 
	private static Font font1 = new Font("Arial", Font.PLAIN, 32);
	private static Font font2 = new Font("Arial", Font.PLAIN, 20);
	private static Font font3 = new Font("Arial", Font.PLAIN, 26);
	public static float year = 0;
	
	public static float velX = 0; //X velocity of the galaxy
	public static float velY = 0;
	
	public static float Xchange = 0; //how much the user has moved horizontally
	public static float Ychange = 0;
	public static boolean paused = true; //whether or not the game will tick
	public static boolean frozen = false; //whether or not the game will render
	public static boolean loading = false; //display the loading screen
		
	public static boolean clickedOnFactionBtn = false; //these are to make sure the user can't click while render() is still running and mess up everything
	public static boolean clickedOnStarSystem = false;
	public static boolean clickedOnLeaderboard = false;
	public static boolean clickedOnCollapse = false;
	public static boolean clickedPlantBtn = false;
	
	public static boolean showingStarSystemInfo = false;
	public static int systemClickedOn = -1;
	public static boolean showingFactionInfo = false;
	public static int factionClickedOn = -1;
	
	public static int tickTimer;
	public static int renderTimer;
	
	public static int buttonTouching = 0;

	private static boolean running = false; 
	public static final int WIDTH = 1000, HEIGHT = 1000;
	
	private Thread thread;
	private KeyInput keyinput;
	private mouseInput mouseinput;
	private mouseWheelInput mousewheel;
	private mouseMotionInput mousemotion;

	public static int[][] factionColors = new int[0][3]; //RGB colors
	public static String[] factionNames = customFactionNames.clone();
	public static int[] factionTech = new int[factions];
	public static int[] factionStarSystems = {};
	public static int[] mostStarsHeld = new int[factions];
	public static int[] factionBattlesWon = new int[factions];
	public static int[] factionBattlesLost = new int[factions];
	public static String[] factionMottos = {};
	
	public static int[][] factionOpinions = new int[factions][factions];
	public static int[] alliances = {};
	public static int[] untrustworthy = {};

	public static int[] starSystemID = {}; // the ID is used to identify the star system
	public static int[] starSystemSize = {}; // the size of the star system (for cosmetic purposes only)
	public static float[] starSystemX = {}; // the X position of the star system
	public static float[] starSystemY = {}; // the Y position
	public static int[] starSystemFaction = {}; // which faction it is, 0 = no faction, 1-6 = factions
	public static boolean[] capitolStarSystem = {}; // whether the star system is the a capitol of a faction
	public static float[] starSystemAge = {}; // the age in frames of the star system
	public static String[] starSystemName = {}; 
	public static int[][] starSystemNeighbors = {}; //the ID of each star system within 75 units of this one
	public static int[] starSystemTroops = {}; 
	public static int[] starSystemNeighborCount = new int[galaxySize];
			
	private static float[] origX = starSystemX.clone(); 
	private static float[] origY = starSystemY.clone();
	private static int[] origS = starSystemSize.clone();
	
	private static int[] battlesWon = {}; 
	private static int[] battlesLost = {};

	private float[] starX = {}; // stars, just for cosmetics
	private float[] starY = {};
	private int[] starSize = {};
	
	private BufferStrategy bs;
	private Graphics g;
	private Graphics2D g2d;
	
	public static int galaxyShape = 0; //0 = normal; 1 = donut; 2 = wide donut; 3 = spiral
	
	public static void main(String args[]) {
		new Game();
	}

	public Game() { //initializing the game
		this.setMinimumSize(new Dimension(1000,1000));
		if (galaxyShape == 1) {
			double radius = Math.sqrt(galaxySize) * 25;
			getStarSystemCircle(radius,10);
		}
		if (galaxyShape == 2) {
			double radius = Math.sqrt(galaxySize) * 25;
			getStarSystemWideCircle(radius,10);
		}
		if (galaxyShape == 3) {
			double radius = Math.sqrt(galaxySize) * 19;
			getStarSystemSpiral(radius,5,0);
		}
		keyinput = new KeyInput(this);
		mouseinput = new mouseInput();
		mousewheel = new mouseWheelInput();
		mousemotion = new mouseMotionInput();
		this.addKeyListener(keyinput); //key presses
		this.addMouseListener(mouseinput); //mouse clicks
		this.addMouseWheelListener(mousewheel); //mouse scroll wheel
		this.addMouseMotionListener(mousemotion); //mouse 

		int point = 0;
		
		float spawnX = 0; //where a star is created, X and Y
		float spawnY = 0;

		if (starSystemID.length == 0) {
			spawnX = 500;
			spawnY = 500;
		} else {
			point = math.randomint(0, starSystemID.length - 1); //chooses random star system
			spawnX = starSystemX[point];
			spawnY = starSystemY[point];

			spawnX += math.randomint(-75, 75); //normal loading mode, chooses to change position in a random direction
			spawnY += math.randomint(-75, 75); //50 in both directions is the max because distance from star at 0,0 to 50,50 is 70 and the range for neighbor-ship is 75 and no star can have 0 neighbors because that would be LAME and waste resources
		}
		
		//creating factions
		for (int i = 0; i < factions; i++) {
			generateNewColor(); //creates a new color
			factionNames = math.append(factionNames, nameCreator.createName()); //creates a new name
			factionStarSystems = math.append(factionStarSystems, 1);
			mostStarsHeld[i] = 1;
			factionMottos = math.append(factionMottos, nameCreator.createMotto());
		}
		//creating star systems
		for (int i = starSystemID.length; i < galaxySize; i++) {
			starSystemID = math.append(starSystemID, i); //star system ID used to identify star systems. i = star system ID, in this loop
			starSystemSize = math.append(starSystemSize, math.randomint(12, 18)); //a star system's cosmetic size
			starSystemX = math.append(starSystemX, spawnX); //a star system's X position
			starSystemY = math.append(starSystemY, spawnY);
			starSystemName = math.append(starSystemName, nameCreator.createStarSystemName()); //creates name for star system
			
			starSystemFaction = math.append(starSystemFaction, 0);
			
			capitolStarSystem = math.append(capitolStarSystem, false);
			starSystemAge = math.append(starSystemAge, 0);
			battlesWon = math.append(battlesWon, 0);
			battlesLost = math.append(battlesLost, 0);
			starSystemTroops = math.append(starSystemTroops, 0);
			
			int nearestStar = 0;
			
			do {
				point = math.randomint(0, starSystemID.length - 1); //chooses random star system
			
				spawnX = starSystemX[point];
				spawnY = starSystemY[point];
	
				spawnX += math.randomint(-75, 75); //normal loading mode, chooses to change position in a random direction
				spawnY += math.randomint(-75, 75); //50 in both directions is the max because distance from star at 0,0 to 50,50 is 70 and the range for neighbor-ship is 75 and no star can have 0 neighbors because that would be LAME and waste resources
				
				nearestStar = findNearestStar(spawnX, spawnY);
				
			} while (math.distance(spawnX, spawnY, starSystemX[nearestStar], starSystemY[nearestStar]) < minDistance || math.distance(spawnX, spawnY, starSystemX[nearestStar], starSystemY[nearestStar]) > 74); //checks if too close to another star system

		}
		//each star stores its neighbor-list in this array
		for (int star : starSystemID) {
			starSystemNeighbors = math.append(starSystemNeighbors, getNeighbors(star));
		}
		
		//puts the camera at the center of the galaxy, center being the average position of all star systems
		adjustScreenCenter();
		
		int faction = 1;
		int b = math.randomint(0, starSystemID.length - 1); // plants the seeds of life into the galaxy -- selects x random stars then makes them capitols and gives them a faction. x being the faction count
		for (int i = 0; i < factions; i++) {
			while (capitolStarSystem[b] == true) {
				b = math.randomint(0, starSystemID.length - 1);
			}
			capitolStarSystem[b] = true; //you are a capitol now
			starSystemSize[b] = 32;
			starSystemFaction[b] = faction;
			starSystemTroops[b] = 1000;
			faction++;
		}
		for (int i = 0; i < starCount; i++) { // spawns 150 cosmetic stars in the background
			starX = math.append(starX, math.randomint(0, 1000));
			starY = math.append(starY, math.randomint(0, 1000));
			starSize = math.append(starSize, math.randomint(2, 6));
		}
			
		updateTech(); //badly named, this method counts the amount of capitols each faction holds
		randomCollapse();
		updateLeaderboard();
		
	}

	public static void initialize() { //same purpose as the constructor Game(). slightly different just so that java doesn't die when this is called.
		loading = true;
		
		minDistance = 50;
		minGDistance = 36;
				
		systemClickedOn = -1;
		factionClickedOn = -1;
				
		year = 0;
		showingStarSystemInfo = false;
		factionColors = new int[0][3];
		factionNames = customFactionNames.clone();
		factionTech = new int[factions];
		factionStarSystems = new int[0];
		mostStarsHeld = new int[factions];
		factionBattlesWon = new int[factions];
		factionBattlesLost = new int[factions];
		factionMottos = new String[0];
		zoomMultiplier = 1;

		starSystemID = new int[0];// the ID is used to identify the star system
		starSystemSize = new int[0];
		starSystemFaction = new int[0];
		starSystemX = new float[0];
		starSystemY = new float[0];
		starSystemAge = new float[0];
		starSystemName = new String[0];
		capitolStarSystem = new boolean[0];
		starSystemTroops = new int[0];
		starSystemNeighborCount = new int[galaxySize];

		starSystemNeighbors = new int[0][];

		battlesWon = new int[0];
		battlesLost = new int[0];

		if (galaxyShape == 1) {
			double radius = Math.sqrt(galaxySize) * 25;
			getStarSystemCircle(radius,10);
		}
		if (galaxyShape == 2) {
			double radius = Math.sqrt(galaxySize) * 25;
			getStarSystemWideCircle(radius,10);
		}
		if (galaxyShape == 3) {
			double radius = Math.sqrt(galaxySize) * 18;
			getStarSystemSpiral(radius,4,0);
		}

		int point = 0;
		
		float spawnX = 0; //where a star is created, X and Y
		float spawnY = 0;

		if (starSystemID.length == 0) {
			spawnX = 500;
			spawnY = 500;
		} else {
			point = math.randomint(0, starSystemID.length - 1); //chooses random star system
			spawnX = starSystemX[point];
			spawnY = starSystemY[point];

			spawnX += math.randomint(-75, 75); //normal loading mode, chooses to change position in a random direction
			spawnY += math.randomint(-75, 75); //50 in both directions is the max because distance from star at 0,0 to 50,50 is 70 and the range for neighbor-ship is 75 and no star can have 0 neighbors because that would be LAME and waste resources
		}
		for (int i = 0; i < factions; i++) {
			generateNewColor();
			factionNames = math.append(factionNames, nameCreator.createName());
			factionStarSystems = math.append(factionStarSystems, 1);
			mostStarsHeld[i] = 1;
			factionMottos = math.append(factionMottos, nameCreator.createMotto());
		}
		if (gameMode == 0 || gameMode == 2) {
			for (int i = starSystemID.length; i < galaxySize; i++) {
				starSystemID = math.append(starSystemID, i);
				starSystemSize = math.append(starSystemSize, math.randomint(12, 18));
				starSystemX = math.append(starSystemX, spawnX);
				starSystemY = math.append(starSystemY, spawnY);
				starSystemName = math.append(starSystemName, nameCreator.createStarSystemName());
				starSystemFaction = math.append(starSystemFaction, 0);
				
				capitolStarSystem = math.append(capitolStarSystem, false);
				starSystemAge = math.append(starSystemAge, 0);
				battlesWon = math.append(battlesWon, 0);
				battlesLost = math.append(battlesLost, 0);
				starSystemTroops = math.append(starSystemTroops, 0);
				
				int nearestStar = 0;
				
				do {
					point = math.randomint(math.max(0,i-1500), starSystemID.length - 1);
					spawnX = starSystemX[point];
					spawnY = starSystemY[point];
		
					spawnX += math.randomint(-75, 75);
					spawnY += math.randomint(-75, 75);
					
					nearestStar = findNearestStar(spawnX, spawnY);
					
				} while (math.distance(spawnX, spawnY, starSystemX[nearestStar],starSystemY[nearestStar]) < minDistance || math.distance(spawnX, spawnY, starSystemX[nearestStar],starSystemY[nearestStar]) > 74);
	
			}
		} else if (gameMode == 1) {
			for (int i = starSystemID.length; i < galaxySize; i++) {
				starSystemID = math.append(starSystemID, i);
				starSystemSize = math.append(starSystemSize, math.randomint(12, 18));
				starSystemX = math.append(starSystemX, spawnX);
				starSystemY = math.append(starSystemY, spawnY);
				starSystemName = math.append(starSystemName, nameCreator.createStarSystemName());
				starSystemFaction = math.append(starSystemFaction, math.randomint(1, factions));
				
				capitolStarSystem = math.append(capitolStarSystem, false);
				starSystemAge = math.append(starSystemAge, 0);
				battlesWon = math.append(battlesWon, 0);
				battlesLost = math.append(battlesLost, 0);
				starSystemTroops = math.append(starSystemTroops, 0);
				
				int nearestStar = 0;
				
				do {
					point = math.randomint(math.max(0,i-1500), starSystemID.length - 1);
					spawnX = starSystemX[point];
					spawnY = starSystemY[point];
		
					spawnX += math.randomint(-75, 75);
					spawnY += math.randomint(-75, 75);
					
					nearestStar = findNearestStar(spawnX, spawnY);
					
				} while (math.distance(spawnX, spawnY, starSystemX[nearestStar],starSystemY[nearestStar]) < minDistance || math.distance(spawnX, spawnY, starSystemX[nearestStar],starSystemY[nearestStar]) > 74);
	
			}
		}
		for (int star : starSystemID) {
			starSystemNeighbors = math.append(starSystemNeighbors, getNeighbors(star));
		}
		adjustScreenCenter();
		
		if (factions > galaxySize) {
			factions = galaxySize;
		}

		int faction = 1;
		int b = math.randomint(0, starSystemID.length - 1); // plants the seeds of life into the galaxy
		for (int i = 0; i < factions; i++) {
			while (capitolStarSystem[b] == true) {
				b = math.randomint(0, starSystemID.length - 1);
			}
			capitolStarSystem[b] = true;
			starSystemSize[b] = 32;
			starSystemFaction[b] = faction;
			starSystemTroops[b] = 1000;
			faction++;
		}
		
		updateTech();
		if (gameMode != 2) {
			randomCollapse();
		}
		updateLeaderboard();
		
		loading = false;
	}

	public synchronized void start() { //starts game
		thread = new Thread(this);
		thread.start();
		running = true;
	}

	public synchronized void stop() { //stops game
		try {
			thread.join();
			running = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() { // FPS counter + main loop
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				if (!frozen) {
					tick();
					keyinput.tick();
				}
				delta--;
			}
			if (running)
				if (!frozen) {
					render();
				} else if (loading) {
					loadingScreenRender();
				}
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				fps = frames; //fps counter
				/*
				if (frames < 10) {
					stop();
				}
				*/
				frames = 0;
			}
		}
		stop();

	}

	private void tick() { //tick method
		if (!paused) {
			updateTech();
			timescale = (float) (1 / timeSpeed);
			year += 2 * timeSpeed;
			if (gameMode != 2) {
				randomCollapse(); //updates faction star system count, collapses factions randomly
			}

			for (int star : starSystemID) {
				
				if (starSystemFaction[star] > 0) { 
					starSystemAge[star] += timeSpeed; 
					int target = starSystemNeighbors[star][math.randomint(0, starSystemNeighbors[star].length - 1)]; // chooses one random neighbor out of the neighbor list
					if (starSystemFaction[target] != starSystemFaction[star]) {
						int conquerDifficulty = 0;
						if (starSystemFaction[target] == 0) {
							if (math.randomint(1, (int) (20 * timescale)) == 1) { // if the neighbor does not belong to a faction
								starSystemFaction[target] = starSystemFaction[star];
								starSystemAge[target] = 0;
							}
						} else { //capitol = 250, developed = 105, not = 35, no faction = 20. lower number = easier to conquer
							if (capitolStarSystem[target]) {
								conquerDifficulty = 250;
							} else if (starSystemAge[target] > 500) {
								conquerDifficulty = 105;
							} else {
								conquerDifficulty = 35;
							}
							if (factionTech[starSystemFaction[star] - 1] > factionTech[starSystemFaction[target] - 1]) { //10% easier/harder to fight if you/they have bad tech
								conquerDifficulty *= 0.9;
							} else if (factionTech[starSystemFaction[star] - 1] < factionTech[starSystemFaction[target] - 1]) {
								conquerDifficulty *= 1.1;
							}
							if (factionTech[starSystemFaction[star] - 1] > factionTech[starSystemFaction[target] - 1] + 2) { //3 or more tech advantage = way better
								conquerDifficulty *= 0.9;
							} else if (factionTech[starSystemFaction[star] - 1] < factionTech[starSystemFaction[target] - 1] - 2) { //3 or less tech disadvantage = way worse
								conquerDifficulty *= 1.1;
							}
							if (factionTech[starSystemFaction[star] - 1] > factionTech[starSystemFaction[target] - 1] + 9) { //10 or more tech advantage = way way better
								conquerDifficulty *= 0.8;
							} else if (factionTech[starSystemFaction[star] - 1] < factionTech[starSystemFaction[target] - 1] - 9) { //10 or less tech disadvantage = way way worse
								conquerDifficulty *= 1.2;
							}
	
							if (math.randomint(1, (int)(conquerDifficulty * timescale)) == 1) {
								factionBattlesLost[starSystemFaction[target]-1]++;
								factionBattlesWon[starSystemFaction[star]-1]++;
							
								starSystemFaction[target] = starSystemFaction[star];
								starSystemAge[target] = 0;
								
								battlesLost[target]++;
								battlesWon[star]++;
							}
						}
						
					}
				}
			}
			
			for (int i = 0; i < starX.length; i++) { // stars
				if (timeSpeed == 10) {
					starX[i] -= ((starSize[i]) / 2)*(timeSpeed/4);
				} else {
					starX[i] -= ((starSize[i]) / 2)*(timeSpeed/2);
	
				}
				if (starX[i] < 0) {
					starX[i] = 1000;
					starY[i] = math.randomint(0, 1000);
				}
			}
		}
		tickTimer++;
		if (tickTimer > 20) {
			tickTimer = 0;
			updateLeaderboard();
		}

	}
	
	private void render() { //render method
		
		bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g2d = (Graphics2D) g;
				
		if (mouseinput.mouseDown) { //move camera by dragging
			moveX2(mouseinput.getMouseLocation()[0] - mouseinput.startX); //moves camera
			moveY2(mouseinput.getMouseLocation()[1] - mouseinput.startY);
			mouseinput.startX = mouseinput.getMouseLocation()[0]; //updates mouse x and mouse y
			mouseinput.startY = mouseinput.getMouseLocation()[1];
		}

		moveX2(velX); //WASD movement
		moveY2(velY);
		
		origX = starSystemX.clone(); 
		origY = starSystemY.clone();
		origS = starSystemSize.clone(); //math for zooming in or out
		
		multiplyX(zoomMultiplier);
		multiplyY(zoomMultiplier);
		multiplySize(zoomMultiplier);

		adjustScreenCenter();


		g.setColor(Color.black); // background
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < starX.length; i++) { // stars
			g.setColor(Color.white);
			g2d.setComposite(makeTransparent((float) 1));
			g.fillOval((int) starX[i], (int) starY[i], starSize[i], starSize[i]);
		}
		
		// b = 0; used to be here
		for (int star : starSystemID) { //draw the wormholes between stars that allow them to interact
			if (starSystemX[star] > 1200 || starSystemX[star] < -200 || starSystemY[star] > 1200 || starSystemY[star] < -200) { //if the star is too far offscreen
				continue;
			}

			if (starSystemFaction[star] == 0) {
				g.setColor(Color.gray);
			} else { //below is the code for setting your color to the faction color of the star
				g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1], factionColors[starSystemFaction[star] - 1][2]));
			}
			for (int neighbor : starSystemNeighbors[star]) {
				g2d.setComposite(makeTransparent((float) 0.15));
				g2d.setStroke(new BasicStroke((int) 5 * zoomMultiplier));
				g.drawLine((int) (starSystemX[star] + starSystemSize[star] * 0.5),
						(int) (starSystemY[star] + starSystemSize[star] * 0.5),
						(int) (starSystemX[neighbor] + starSystemSize[neighbor] * 0.5),
						(int) (starSystemY[neighbor] + starSystemSize[neighbor] * 0.5));

			}

		}
		for (int star : starSystemID) { //star systems
			if (starSystemX[star] > 1200 || starSystemX[star] < -200 || starSystemY[star] > 1200|| starSystemY[star] < -200) {
				continue;
			}
			g2d.setComposite(makeTransparent(1));
			if (systemClickedOn == starSystemID[star]) { //when you are on the info page of a star system/faction, it will highlight that system/faction's systems
				g2d.setComposite(makeTransparent((float) 0.25));
				if (starSystemFaction[star] == 0) {
					g.setColor(Color.gray);
				} else {
					g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1],factionColors[starSystemFaction[star] - 1][2]));
				}
				g.fillOval((int) (starSystemX[star] - (starSystemSize[star]*1.5)),(int) (starSystemY[star] - (starSystemSize[star]*1.5)),(int) (starSystemSize[star] * 4),(int)(starSystemSize[star] * 4));
			}
			if (factionClickedOn == starSystemFaction[star]) {
				g2d.setComposite(makeTransparent((float) 0.25));
				g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1],factionColors[starSystemFaction[star] - 1][2]));
				g.fillOval((int) (starSystemX[star] - (starSystemSize[star]*1.5)),(int) (starSystemY[star] - (starSystemSize[star]*1.5)),(int) (starSystemSize[star] * 4),(int)(starSystemSize[star] * 4));

			}
			//code for highlighting every star
			/*
			if (starSystemFaction[star] > 0) {
				g2d.setComposite(makeTransparent((float) 0.25));
				g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1],factionColors[starSystemFaction[star] - 1][2]));
				g.fillOval((int) (starSystemX[star] - (starSystemSize[star]*1.5)),(int) (starSystemY[star] - (starSystemSize[star]*1.5)),(int) (starSystemSize[star] * 4),(int)(starSystemSize[star] * 4));
			}
			*/
			if (zoomMultiplier > 0.15) {
				if (squares) { //draws the actual star
					if (starSystemAge[star] > 500 && capitolStarSystem[star] == false) { // developed systems get a white border
						g2d.setComposite(makeTransparent((float) 0.2));
						g.setColor(Color.white);
						g.fillRect((int) (starSystemX[star] - (5 * zoomMultiplier)),(int) (starSystemY[star] - (5 * zoomMultiplier)),(int) (starSystemSize[star] + (10 * zoomMultiplier)),(int) (starSystemSize[star] + (10 * zoomMultiplier)));
					}
					if (starSystemFaction[star] == 0) {
						g.setColor(Color.gray);
					} else {
						g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1], factionColors[starSystemFaction[star] - 1][2]));
					}
					if (capitolStarSystem[star]) { // draws a border
						g2d.setComposite(makeTransparent((float) 0.25));
						g.fillRect((int) (starSystemX[star] - (5 * zoomMultiplier)),
								(int) (starSystemY[star] - (5 * zoomMultiplier)),
								(int) (starSystemSize[star] + (10 * zoomMultiplier)),
								(int) (starSystemSize[star] + (10 * zoomMultiplier)));
					} else {
						g2d.setComposite(makeTransparent((float) 0.25));
						g.fillRect((int) (starSystemX[star] - (3 * zoomMultiplier)),
								(int) (starSystemY[star] - (3 * zoomMultiplier)),
								(int) (starSystemSize[star] + (6 * zoomMultiplier)),
								(int) (starSystemSize[star] + (6 * zoomMultiplier)));
					}
					g2d.setComposite(makeTransparent((float) 1));
					g.fillRect((int) starSystemX[star], (int) starSystemY[star], starSystemSize[star], starSystemSize[star]); // draws star system
				} else {
					if (starSystemAge[star] > 500 && capitolStarSystem[star] == false) { // developed systems get a white border
						g2d.setComposite(makeTransparent((float) 0.2));
						g.setColor(Color.white);
						g.fillOval((int) (starSystemX[star] - (5 * zoomMultiplier)),(int) (starSystemY[star] - (5 * zoomMultiplier)),(int) (starSystemSize[star] + (10 * zoomMultiplier)),(int) (starSystemSize[star] + (10 * zoomMultiplier)));
					}
					if (starSystemFaction[star] == 0) {
						g.setColor(Color.gray);
					} else {
						g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1], factionColors[starSystemFaction[star] - 1][2]));
					}
					if (capitolStarSystem[star]) { // draws a border
						g2d.setComposite(makeTransparent((float) 0.25));
						g.fillOval((int) (starSystemX[star] - (5 * zoomMultiplier)),
								(int) (starSystemY[star] - (5 * zoomMultiplier)),
								(int) (starSystemSize[star] + (10 * zoomMultiplier)),
								(int) (starSystemSize[star] + (10 * zoomMultiplier)));
					} else {
						g2d.setComposite(makeTransparent((float) 0.25));
						g.fillOval((int) (starSystemX[star] - (3 * zoomMultiplier)),
								(int) (starSystemY[star] - (3 * zoomMultiplier)),
								(int) (starSystemSize[star] + (6 * zoomMultiplier)),
								(int) (starSystemSize[star] + (6 * zoomMultiplier)));
					}
					g2d.setComposite(makeTransparent((float) 1));
					g.fillOval((int) starSystemX[star], (int) starSystemY[star], starSystemSize[star], starSystemSize[star]); // draws star system
					g2d.setComposite(makeTransparent((float) 0.25));
	
				}
			} else { //if player zoomed out too much, only draws capitol star systems
				if (capitolStarSystem[star]) { //only draws capitols
					if (starSystemFaction[star] == 0) {
						g.setColor(Color.gray);
					} else {
						g.setColor(new Color(factionColors[starSystemFaction[star] - 1][0],factionColors[starSystemFaction[star] - 1][1], factionColors[starSystemFaction[star] - 1][2]));
					}
					g2d.setComposite(makeTransparent((float) 0.25));
					g.fillRect((int) (starSystemX[star] - (5 * zoomMultiplier)),(int) (starSystemY[star] - (5 * zoomMultiplier)),(int) (starSystemSize[star] + (10 * zoomMultiplier)),(int) (starSystemSize[star] + (10 * zoomMultiplier)));
					g2d.setComposite(makeTransparent((float) 1));
					g.fillRect((int) starSystemX[star], (int) starSystemY[star], starSystemSize[star], starSystemSize[star]); // draws star system
				}
			}
			
		}

		if (drawGUI) {
			//hover effect for leaderboard
			if (buttonTouching < 16 && buttonTouching > 5 /*if the user is hovering over a leaderboard faction*/ && Game.factions > Game.buttonTouching-6 /*if the user is hovering over a faction that exists */ && Game.factionStarSystems[math.findNotLargest(Game.factionStarSystems, Game.buttonTouching-6)] > 0 /*if the hovered-over-faction is still alive*/) {
				g2d.setComposite(makeTransparent((float)0.4));
				g.setColor(Color.GRAY);
				g.fillRect(5, ((buttonTouching-5) * 25)+57, 420, 25);
			}
		
			g2d.setComposite(makeTransparent((float) 1)); 
			g.setColor(Color.white);
			g.setFont(font1);
			g.drawString("Year: " + (int) year, 10, 42);
			g.setFont(font2);
	
			int texty = 77;
			
			g.drawString("Top Factions: ", 10, texty);
			
			int focusedFaction = 0;
	
			//findNotLargest can find the 2nd largest/3rd largest/4th largest etc.
			texty += 25;
			if (factions > 10) {
				for (int i = 0; i < displayCount; i++) {
					focusedFaction = math.findNotLargest(factionStarSystems, i);
					if (factionStarSystems[focusedFaction] > 0) {
						g.setColor(new Color(factionColors[focusedFaction][0],
								factionColors[focusedFaction][1],
								factionColors[focusedFaction][2]));
						g.drawString((i + 1) + ". " + factionNames[focusedFaction] + ": "
								+ factionStarSystems[focusedFaction] + " ("
								+ factionTech[focusedFaction] + ")", 10, texty);
						texty += 25;
					}
				}
			} else {
				for (int i = 0; i < factions; i++) {
					focusedFaction = math.findNotLargest(factionStarSystems, i);
					g.setColor(new Color(factionColors[focusedFaction][0],
							factionColors[focusedFaction][1],
							factionColors[focusedFaction][2]));
					if (factionStarSystems[focusedFaction] > 0) {
						g.drawString((i + 1) + ". " + factionNames[focusedFaction] + ": "
								+ factionStarSystems[focusedFaction] + " ("
								+ factionTech[focusedFaction] + ")", 10, texty);
					}
					texty += 25;
				}
	
			}
	
			g.setColor(Color.white); //pause, time, new galaxy, and shape buttons
			g.fillRect(934, 0, 50, 50);
			g.fillRect(934, 60, 50, 50);
			g.fillRect(934, 120, 50, 50);
			g.fillRect(934, 240, 50, 50);
			if (buttonTouching == 1) {
				g.setFont(font3);
				g.drawString("Play/pause", 804, 38);
			} else if (buttonTouching == 2) {
				g.setFont(font3);
				g.drawString("Simulation Speed", 724, 94);
			} else if (buttonTouching == 3) {
				g.setFont(font3);
				g.drawString("New Galaxy", 792, 154);
			} else if (buttonTouching == 17) {
				g.setFont(font3);
				g.drawString("System Shapes", 750, 274);
			}
			g.setColor(Color.black);
			if (squares) {
				g.fillRect(944, 250, 30, 30);
			} else {
				g.fillOval(939, 245, 40, 40);
			}
			if (!paused) {
				g.fillRect(944, 10, 10, 30);
				g.fillRect(964, 10, 10, 30);
			} else {
				g.fillPolygon(new int[] { 944, 944, 974 }, new int[] { 10, 40, 25 }, 3);
			}
			if (timeSpeed - 0.5 > -0.05 && timeSpeed - 0.5 < 0.05) {
				g.setFont(font1);
				g.drawString("1x", 942, 95);
			}
			if (timeSpeed == 1) {
				g.setFont(font1);
				g.drawString("2x", 942, 95);
			}
			if (timeSpeed == 2) {
				g.setFont(font1);
				g.drawString("4x", 942, 95);
			}
			if (timeSpeed == 5) {
				g.setFont(font3);
				g.drawString("10x", 939, 95);
			}
			if (timeSpeed == 10) {
				g.setFont(font3);
				g.drawString("20x", 939, 95);
			}
			if (timeSpeed == (float) 0.35) {
				g.setFont(new Font("Arial", Font.PLAIN, 23));
				g.drawString("0.7x", 938, 95);
			}
			if (timeSpeed == (float) 0.25) {
				g.setFont(new Font("Arial", Font.PLAIN, 23));
				g.drawString("0.5x", 938, 95);
			}
			if (timeSpeed == (float) 0.1) {
				g.setFont(new Font("Arial", Font.PLAIN, 23));
				g.drawString("0.2x", 938, 95);
			}
			g.fillRect(954, 128, 10, 35);
			g.fillRect(942, 140, 35, 10);
			
			// b = 0; used to be here
			for (int star : starSystemID) {
				starSystemX[star] = starSystemX[star] + starSystemSize[star] / 2;
				starSystemY[star] = starSystemY[star] + starSystemSize[star] / 2;
			}
			renderTimer++;
			if (renderTimer > 15) { //only checks what you clicked every 15 frames, works fast enough that user doesn't notice unless their fps sucks, improves performance i think
				renderTimer = 0;
				if (clickedOnStarSystem) {
					clickedOnStarSystem = false;
					if (math.distanceToNearestPoint(mouseinput.mouseX, mouseinput.mouseY, starSystemX, starSystemY) < 15 * zoomMultiplier) {
						systemClickedOn = math.findNearestPoint(mouseinput.mouseX, mouseinput.mouseY, starSystemX, starSystemY);
						showingStarSystemInfo = true;
						showingFactionInfo = false;
						factionClickedOn = -1;
					}
				}
				if (clickedOnFactionBtn) {
					clickedOnFactionBtn = false;
					
					showingFactionInfo = true;
					factionClickedOn = starSystemFaction[systemClickedOn];
					systemClickedOn = -1;
					showingStarSystemInfo = false;
				}
				if (clickedOnCollapse) {
					clickedOnCollapse = false;
					collapseFaction(factionClickedOn);
				}
				if (clickedPlantBtn) {
					clickedPlantBtn = false;
					
					System.out.println(systemClickedOn);
					
					//create new faction
					generateNewColor();
					factionNames = math.append(factionNames, nameCreator.createName());
					factionTech = math.append(factionTech, 0);
					factionStarSystems = math.append(factionStarSystems, 0);
					factions++;
					factionBattlesWon = math.append(factionBattlesWon, 0);
					factionBattlesLost = math.append(factionBattlesLost, 0);
					mostStarsHeld = math.append(mostStarsHeld, 0);
					factionMottos = math.append(factionMottos, nameCreator.createMotto());
					
					//change star system's faction to that one
					starSystemFaction[systemClickedOn] = factions;
					origX[systemClickedOn] -= (32 - origS[systemClickedOn])/2;
					origY[systemClickedOn] -= (32 - origS[systemClickedOn])/2;
					origS[systemClickedOn] = 32;
					capitolStarSystem[systemClickedOn] = true;
					
					//make star system a capitol
					showingFactionInfo = true;
					factionClickedOn = starSystemFaction[systemClickedOn];
					systemClickedOn = -1;
					showingStarSystemInfo = false;
				}
			}
	
			if (showingStarSystemInfo && systemClickedOn != -1) { //star system info
				try {
					g.setColor(Color.white);
					g.fillRect(650, 600, 350, 400);
					g.setColor(Color.black);
					g.setFont(font1);
					g.drawString(starSystemName[systemClickedOn], 660, 635);
					g.setFont(font2);
					
					g.drawString("Faction: ", 660, 667);
					g.drawString("Rule Length: " + (int)(2 * starSystemAge[systemClickedOn]) + " Years", 660, 692);
					g.drawString("Battles Won: " + battlesWon[systemClickedOn], 660, 717);
					g.drawString("Battles Lost: " + battlesLost[systemClickedOn], 660, 742);
					g.drawString("System ID: " + systemClickedOn, 660, 792);
					g2d.setStroke(new BasicStroke(5));
		
					if (starSystemFaction[systemClickedOn] == 0) {
						g.setColor(Color.black);
						g.drawString("Not Populated", 736, 667);
						g.setColor(Color.gray);
						g.drawPolygon(new int[] { 647, 981, 981, 647 }, new int[] { 597, 597, 958, 958 }, 4);
					} else {
						g.setColor(new Color(factionColors[starSystemFaction[systemClickedOn] - 1][0],
								factionColors[starSystemFaction[systemClickedOn] - 1][1],
								factionColors[starSystemFaction[systemClickedOn] - 1][2]));
						g.drawPolygon(new int[] { 647, 981, 981, 647 }, new int[] { 597, 597, 958, 958 }, 4);
						g.drawString(factionNames[(starSystemFaction[systemClickedOn]) - 1], 736, 667);
					}
					
					if (buttonTouching == 4) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 915, 947, 947, 915 }, 4);
					}
					g.setColor(Color.black);
					g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 915, 947, 947, 915 }, 4);
					g.setFont(font3);
					g.drawString("Close", 780, 940);
					
					if (starSystemFaction[systemClickedOn] != 0) {
						if (buttonTouching == 5) {
							g.setColor(Color.LIGHT_GRAY);
							g.fillPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
						}
						g.setColor(Color.black);
						g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
						g.setFont(font3);
						g.drawString("Faction Info", 745, 898);
					} else {
						if (buttonTouching == 5) {
							g.setColor(Color.LIGHT_GRAY);
							g.fillPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
						}
						g.setColor(Color.black);
						g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
						g.setFont(font3);
						g.drawString("Plant Life Here", 730, 898);
	
					}
				} catch (ArrayIndexOutOfBoundsException thisIsNotABug) {
					//thisIsNotABug
				}
			}
			
			if (showingFactionInfo && factionClickedOn != -1 && factionClickedOn != 0) { //faction info
				try {
					g.setColor(Color.white);
					g.fillRect(650, 600, 350, 400);
					g.setColor(Color.black);
					if (factionNames[factionClickedOn-1].length() < 18) {
						g.setFont(font1);
					} else {
						g.setFont(font3);
					}
					g.drawString(factionNames[factionClickedOn-1], 660, 635);
					g.setFont(font2);
					g.drawString("\"" + factionMottos[factionClickedOn-1] + "\"", 660, 667);
					g.drawString("Systems: " + factionStarSystems[factionClickedOn-1], 660, 692);
					g.drawString("Peak Systems: " + mostStarsHeld[factionClickedOn-1], 660, 717);
					g.drawString("Faction Tech: " + factionTech[factionClickedOn-1], 660, 742);
					g.drawString("Battles Won: " + factionBattlesWon[factionClickedOn-1], 660, 767);
					g.drawString("Battles Lost: " + factionBattlesLost[factionClickedOn-1], 660, 792);
					//g.drawString("Faction ID: " + (factionClickedOn - 1), 660, 817);
					g2d.setStroke(new BasicStroke(5));
					
					if (buttonTouching == 4) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 915, 947, 947, 915 }, 4);
					}
					if (buttonTouching == 5) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
					}
					if (buttonTouching == 16) {
						g.setColor(Color.LIGHT_GRAY);
						g.fillPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 831, 863, 863, 831 }, 4);
					}
					
					g.setColor(Color.black);
					g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 915, 947, 947, 915 }, 4);
					g.setFont(font3);
					g.drawString("Close", 780, 940);
					
					g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 873, 905, 905, 873 }, 4);
					g.setFont(font3);
					g.drawString("Collapse", 760, 898);
	
					g.drawPolygon(new int[] { 660, 660, 967, 967 }, new int[] { 831, 863, 863, 831 }, 4);
					g.setFont(font3);
					g.drawString("Edit Faction", 745, 856);
	
					g.setColor(new Color(factionColors[factionClickedOn - 1][0],factionColors[factionClickedOn - 1][1],factionColors[factionClickedOn - 1][2]));
		
					g.drawPolygon(new int[] { 647, 981, 981, 647 }, new int[] { 597, 597, 958, 958 }, 4);
				} catch (ArrayIndexOutOfBoundsException stillNotABug) {
					//stillNotABug
				}
	
			}
			g.setColor(Color.white);
			g.setFont(font2);
			g.drawString("FPS: " + fps, 10, 950);
		}
		starSystemX = origX.clone();
		starSystemY = origY.clone();
		starSystemSize = origS.clone();
		
		g.dispose();
		bs.show();

	}
	private void loadingScreenRender() {
		
		bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		g2d = (Graphics2D) g;
		
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 1000, 1000);
		
		g.setColor(Color.DARK_GRAY);
		g.fillRect(90, 440, 820, 55);
		
		g.setColor(Color.white);
		g.setFont( new Font("Arial", Font.PLAIN, 100));
		g.drawString("Loading...", 300, 400);
		
		g.setFont(new Font("Arial", Font.PLAIN,50));
		
		g.fillRect(100, 450, (int)(((double)starSystemID.length / (double)(galaxySize))*800), 35);
		g.drawString("Generating Stars... " + starSystemID.length + "/" + galaxySize, 500 - ((g.getFontMetrics().stringWidth("Generating Stars... " + starSystemID.length + "/" + galaxySize))/2), 560);
		
		g.dispose();
		bs.show();

	}
	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	private static int findNearestStar(float x, float y) { // finds the closest star and gives its ID
		float lowestDistanceSoFar = 99999;
		int lowestDistID = 0;
		int b = 0;
		for (float position : starSystemID) {
			if (math.distance(x, y, starSystemX[b], starSystemY[b]) < lowestDistanceSoFar) {
				lowestDistanceSoFar = math.distance(x, y, starSystemX[b], starSystemY[b]);
				lowestDistID = starSystemID[b];
			}
			b++;
		}
		return lowestDistID;
	}

	private static int[] getNeighbors(int yourID) { // finds the stars within 75 units of this one, and gives their IDs
		int[] neighbors = {};
		int b = 0;
		for (int position : starSystemID) {
			if (math.distance(starSystemX[yourID], starSystemY[yourID], starSystemX[position],starSystemY[position]) < 75) {
				neighbors = math.append(neighbors, position);
			}
			b++;
		}

		return neighbors;
	}

	private static int[] getNeighbors(int yourID, float range) { // finds the stars within 75 units of this one, and gives their IDs
		int[] neighbors = {};
		int b = 0;
		for (float position : starSystemID) {
			if (math.distance(starSystemX[yourID], starSystemY[yourID], starSystemX[b], starSystemY[b]) < range) {
				neighbors = math.append(neighbors, b);
			}
			b++;
		}

		return neighbors;
	}

	private static float getAvgX() { //gets the avg X of all star systems
		float totalX = 0;
		for (float position : starSystemX) {
			totalX += position;
		}
		return totalX / galaxySize;
	}

	private static float getAvgY() { //gets the avg Y of all star systems
		float totalY = 0;
		for (float position : starSystemY) {
			totalY += position;
		}
		return totalY / galaxySize;
	}

	public static void adjustScreenCenter() { //adjusts the screen center so that the center = the avg star system position. takes into account camera movement.
		float changeX = 500 - getAvgX() + Xchange;
		float changeY = 500 - getAvgY() + Ychange;
		moveX(changeX);
		moveY(changeY);
	}
	
	public static void reverseAdjustScreenCenter() { //i don't know either
		float changeX = 500 - getAvgX() + Xchange;
		float changeY = 500 - getAvgY() + Ychange;
		moveX(changeX * -1);
		moveY(changeY * -1);
	}

	private static int countStarSystems(int faction) { //counts how many star systems are owned by a particular faction
		int output = 0;
		for (int star : starSystemFaction) {
			if (star == faction) {
				output++;
			}
		}
		return output;
	}

	public static void moveX(float move) { //moves all star systems, does not affect camera
		int b = 0;
		for (float position : starSystemID) {
			starSystemX[b] += move;
			b++;
		}
	}

	public static void moveY(float move) {//moves all star systems, does not affect camera
		int b = 0;
		for (float position : starSystemID) {
			starSystemY[b] += move;
			b++;
		}
	}

	public static void moveX2(float move) {//moves all star systems, affects camera
		int b = 0;
		for (float position : starSystemID) {
			starSystemX[b] += move;
			b++;
		}
		Xchange += move;
	}

	public static void moveY2(float move) {//moves all star systems, affects camera
		int b = 0;
		for (float position : starSystemID) {
			starSystemY[b] += move;
			b++;
		}
		Ychange += move;
	}

	public static void multiplyX(float move) { 
		int b = 0;
		for (float position : starSystemID) {
			starSystemX[b] *= move;
			b++;
		}
	}

	public static void multiplyY(float move) {
		int b = 0;
		for (float position : starSystemID) {
			starSystemY[b] *= move;
			b++;
		}
	}

	public static void multiplySize(float move) {
		int b = 0;
		for (float position : starSystemID) {
			starSystemSize[b] *= move;
			b++;
		}
	}

	public static void setVelX(float newvelX) {
		velX = newvelX;
	}

	public static void setVelY(float newvelY) {
		velY = newvelY;
	}

	public static void generateNewColor() { //makes a new RGB color by making 3 random numbers each 0-255
		boolean brightColors = true;
		if (brightColors) {
			int R = 0;
			int G = 0;
			int B = 0;
			int avg;
			int req = 100;
			do {
				R = math.randomint(0, 255);
				G = math.randomint(0, 255);
				B = math.randomint(0,255);
				avg = (R + G + B) / 3;
			} while ((Math.abs(R - avg) + Math.abs(G - avg) + Math.abs(B - avg) / 3) < req); 
			factionColors = math.append(factionColors, new int[] {R,G,B});
		} else {
			factionColors = math.append(factionColors, new int[] {math.randomint(0, 255),math.randomint(0, 255),math.randomint(0, 255)});

		}
	}

	public static void updateTech() {
		int b = 0;
		int tech = 0;
		int capitols = 0;
		for (int star : starSystemID) {
			if (capitolStarSystem[star]) {
				if (math.randomint(1, (int)(500*(Math.pow((double)2, (double)factionTech[starSystemFaction[star] - 1])))) == 1) {
					factionTech[starSystemFaction[star] - 1]++;
				}
			}
		}
		for (int i = 1;i<factions;i++) {
			if (math.randomint(1,(int)(20000*timescale)) == 1) {
				if (factionTech[i-1] > 0) {
					factionTech[i-1]--;
				}
			}
		}
	}

	public static float findNearestCapitolSystem(int yourID) { //does what it says
		float lowestDistanceSoFar = 99999;
		int lowestDistID = 0;
		int b = 0;
		for (float position : starSystemID) {
			if (capitolStarSystem[b] && starSystemFaction[b] == starSystemFaction[yourID]) {
				if (math.distance(starSystemX[yourID], starSystemY[yourID], starSystemX[b],
						starSystemY[b]) < lowestDistanceSoFar) {
					lowestDistanceSoFar = math.distance(starSystemX[yourID], starSystemY[yourID], starSystemX[b],starSystemY[b]);
					lowestDistID = starSystemID[b];
				}
			}
			b++;
		}
		return lowestDistanceSoFar;
	}

	public static void changeZoom(float zoomChange) {
		zoomMultiplier += zoomChange;
		if (zoomMultiplier < 0.2) {
			zoomMultiplier = (float) 0.2;
		}
		if (zoomMultiplier > 5) {
			zoomMultiplier = 5;
		}
	}

	public static void multiplyZoom(float multiplier) {
		zoomMultiplier *= multiplier;
		if (zoomMultiplier < 0.07) {
			zoomMultiplier = (float) 0.07;
		}
		if (zoomMultiplier > 5) {
			zoomMultiplier = 5;
		}

	}

	public static void pauseOrResume() { //toggles pause
		paused = !paused;
	}

	public static void collapseFaction(int factionID) { //collapsefaction method collapses faction, each star system owned by this faction has a 80% chance of joining a new faction 
		int newFactionCount = math.randomint(15,20);
		for (int i = 0;i<newFactionCount;i++) { //creates 15-20 new factions for the rebels
			generateNewColor();
			factionNames = math.append(factionNames, nameCreator.createName());
			factionTech = math.append(factionTech, math.randomint(math.max(0, factionTech[factionID-1]-5), factionTech[factionID-1]-1));
			factions++;
			factionStarSystems = math.append(factionStarSystems, 0);
			factionBattlesWon = math.append(factionBattlesWon, 0);
			factionBattlesLost = math.append(factionBattlesLost, 0);
			mostStarsHeld = math.append(mostStarsHeld, 0);
			factionMottos = math.append(factionMottos, nameCreator.createMotto());			
		}
		factionTech[factionID-1] -= math.randomint(0, 3);
		if (factionTech[factionID-1] < 0) {
			factionTech[factionID-1] = 0;
		}

		for (int star : starSystemID) { //gives the rebels a random amount of star systems
			
			if (starSystemFaction[star] == factionID) {
				if (capitolStarSystem[star]) {
					if (math.randomint(1, 5) != 5) {
						starSystemFaction[star] = factions - math.randomint(1, newFactionCount);
					}
				} else {
					if (math.randomint(1,7) == 1) {
						starSystemFaction[star] = 0;
						starSystemAge[star] = 0;
					} else if (math.randomint(1, 6) != 4) {
						starSystemFaction[star] = factions - math.randomint(1, newFactionCount);
					}

				}
			}
		}
	}
	public static void getStarSystemCircle(double radius, double step) {
		int b = 0;
		for (int i = 0;i<360;i+=step) {
			starSystemID = math.append(starSystemID, b);
			starSystemSize = math.append(starSystemSize, math.randomint(12, 18));
			starSystemX = math.append(starSystemX, (float)math.getCircleX(i, radius));
			starSystemY = math.append(starSystemY, (float)math.getCircleY(i, radius));
			starSystemName = math.append(starSystemName, nameCreator.createStarSystemName());
			if (gameMode == 1) {
				starSystemFaction = math.append(starSystemFaction, math.randomint(1, factions));
			} else {
				starSystemFaction = math.append(starSystemFaction, 0);
			}
			capitolStarSystem = math.append(capitolStarSystem, false);
			starSystemAge = math.append(starSystemAge, 0);
			battlesWon = math.append(battlesWon, 0);
			battlesLost = math.append(battlesLost, 0);
			starSystemTroops = math.append(starSystemTroops, 0);
			b++;
		}
	}
	public static void getStarSystemWideCircle(double radius, double step) {
		int b = 0;
		for (int i = 0;i<360;i+=step) {
			starSystemID = math.append(starSystemID, b);
			starSystemSize = math.append(starSystemSize, math.randomint(12, 18));
			starSystemX = math.append(starSystemX, (float)(math.getCircleX(i, radius)*1.35));
			starSystemY = math.append(starSystemY, (float)(math.getCircleY(i, radius)*0.8));
			starSystemName = math.append(starSystemName, nameCreator.createStarSystemName());
			if (gameMode == 1) {
				starSystemFaction = math.append(starSystemFaction, math.randomint(1, factions));
			} else {
				starSystemFaction = math.append(starSystemFaction, 0);
			}
			capitolStarSystem = math.append(capitolStarSystem, false);
			starSystemAge = math.append(starSystemAge, 0);
			battlesWon = math.append(battlesWon, 0);
			battlesLost = math.append(battlesLost, 0);
			starSystemTroops = math.append(starSystemTroops, 0);
			b++;
		}
	}
	public static void getStarSystemSpiral(double radius, double step, int startAngle) {
		double initialRadius = radius;
		double radiusChange = (radius*2.25)/(360/step);
		radius *= 0.5;
		
		int b = 0;
		for (int i = startAngle ;i<180+startAngle;i+=step) {
			addStar(b,(float)(math.getCircleX(i, radius)),(float)(math.getCircleY(i, radius)));
			b++;
			addStar(b,(float)(math.getCircleX(i+72, radius)),(float)(math.getCircleY(i+72, radius)));
			b++;
			addStar(b,(float)(math.getCircleX(i+144, radius)),(float)(math.getCircleY(i+144, radius)));
			b++;
			addStar(b,(float)(math.getCircleX(i+216, radius)),(float)(math.getCircleY(i+216, radius)));
			b++;
			addStar(b,(float)(math.getCircleX(i+288, radius)),(float)(math.getCircleY(i+288, radius)));
			b++;
			
			radius += radiusChange;
			if (b < 20) {
				radius += radiusChange;
			}
			
			step = math.max(step*0.99, 2);
			
		}
				
		addStar(b,(float)(initialRadius/9),(float)(initialRadius/9));
		b++;

		addStar(b,(float)(initialRadius/9),(float)(initialRadius/-9));
		b++;
		
		addStar(b,(float)(initialRadius/-9),(float)(initialRadius/9));
		b++;
		
		addStar(b,(float)(initialRadius/-9),(float)(initialRadius/-9));
		b++;

	}
	public static void addStar(int id,float x, float y) {
		starSystemID = math.append(starSystemID, id);
		starSystemSize = math.append(starSystemSize, math.randomint(12, 18));
		starSystemX = math.append(starSystemX, x);
		starSystemY = math.append(starSystemY, y);
		starSystemName = math.append(starSystemName, nameCreator.createStarSystemName());
		if (gameMode == 1) {
			starSystemFaction = math.append(starSystemFaction, math.randomint(1, factions));
		} else {
			starSystemFaction = math.append(starSystemFaction, 0);
		}
		capitolStarSystem = math.append(capitolStarSystem, false);
		starSystemAge = math.append(starSystemAge, 0);
		battlesWon = math.append(battlesWon, 0);
		battlesLost = math.append(battlesLost, 0);
		starSystemTroops = math.append(starSystemTroops, 0);
	}
	public static void randomCollapse() {
		for (int i = 0; i < factions; i++) {
			double territoryPercent = (double)factionStarSystems[i] / (double)galaxySize;
			if (territoryPercent > 0.2 && math.randomint(1, (int)((6750)*timescale)) == 1) { //collapses are dependent on faction size
				collapseFaction(i+1);
			} else if (territoryPercent > 0.35 && math.randomint(1, (int)((2250)*timescale)) == 1) {
				collapseFaction(i+1);
			} else if (territoryPercent > 0.8 && math.randomint(1, (int)((750)*timescale)) == 1) {
				collapseFaction(i+1);
			}
		}
	}
	public static void updateLeaderboard() {
		int[] newFactionStarSystems = new int[factions];
		for (int i = 0;i < galaxySize; i++) {
			if (starSystemFaction[i] != 0) {
				newFactionStarSystems[starSystemFaction[i]-1]++;
			}
		}
		for (int i = 0; i < newFactionStarSystems.length;i++) {
			if (newFactionStarSystems[i] > mostStarsHeld[i]) {
				mostStarsHeld[i] = newFactionStarSystems[i];
			}
		}
		factionStarSystems = newFactionStarSystems.clone();
	}
}


