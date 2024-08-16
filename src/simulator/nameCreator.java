package simulator;

public class nameCreator {
	/*
	NAME FORMAT KEY
	
	s = start combination - combinations of consenants that work at the start of names
	b = end combination - combinations of consentants that work at the end of vowels or vowel combinations
	a = vowel - letters that bridge between other components
	c = hard consenant - consenants that stop the sound of vowels and replace them with its own sound
	r = soft consenant - consenants that change the sound of the vowel before it
	d = double consenant - two consenants that work together
	i = vowel combination - two vowels that work together
	
	
	*/
	
	
	private static String[][] nameFormats = {{"c","a","b"},{"c","a","c"},{"c","a","c","a","c"},{"s","a","r","a","c"},{"s","a","b"},{"r","a","c","a","r","c"},{"s","a","b","a","b","a","r"},{"c","a","d","a"},{"c","a","d","a","r"},{"s","a","s","a","r"},{"c","a","b","a","b","a","r","a"},{"c","i","c"},{"c","a","d","i","c"},{"c","i","b"},{"c","a","d","i"},{"a","c","a","r","a","c","a"},{"s","i","c"},{"a","s","a","r","a","c"}};
	private static String[] scombinations = {"sh","ch","th","st","chr","wh","br","wr","str","tr","ph"};
	private static String[] ecombinations = {"gh","ck","tch","ng","hn","ph","st","ch","th","lf","ngth","tr"};
	private static String[] vowels = {"a","e","i","o","u"};
	private static String[] hconsenants = {"b","c","d","f","g","j","k","m","p","q","s","t","v","x","z"};
	private static String[] sconsenants = {"h","l","n","r","w","y"};
	private static String[] dconsenants = {"tt","rr","ll","ss"};
	private static String[] vcombinations = {"ee","ea","oo","ow","ai","oa","ia","iu"};
	private static String output = "";
	private static String[] formatused;
	public static String gamer = "";
	private static String[] countryNameFormats = {"Imperium of [NAME]","Kingdom of [NAME]","[NAME] Dynasty","[NAME] Kingdom","United States of [NAME]","[NAME] Empire","[NAME] Federation","Federation of [NAME]","[NAME] Republic","Republic of [NAME]","[NAME] Alliance","United Systems of [NAME]","[NAME] Confederacy","Confederacy of [NAME]","[NAME] Collective","[NAME] Enclave", "[NAME] League", "[NAME] Legion", "[NAME] Pact", "Legion of [NAME]", "[NAME] Council", "Council of [NAME]"};

	private static String[] nouns = {"Wrath","Blood", "Glory", "Unity", "Tyranny", "Might", "Will", "Chaos", "Divinity", "Justice"};
	private static String[] properNoun = {"Emperor", "God", "King", "God-Emperor", "Leader", "Tyrant"};
	private static String[] verbs = {"Reap", "Harvest", "Fight", "Burn", "Restore", "Destroy", "Rebuild", "Save", "Enforce", "Unite"};
	private static String[] adjectives = {"Infinite", "Blinding", "Mighty", "New", "True", "Divine", "Supreme", "Golden", "Unending"};
	private static String[] countryMottoFormats = {"VERB our ADJ NOUN!","VERB our NOUN!","For the PROPER!","NOUN for the ADJ PROPER!","For NOUN and NOUN!","There is no PROPER but the PROPER!","NOUN of the Galaxy", "VERB in the Name of the PROPER!", "For NOUN, for ADJ NOUN!", "ADJ NOUN Awaits us!", "The Galaxy will VERB.", "We will VERB the Galaxy.", "We Represent the ADJ NOUN.", "We will VERB your NOUN.", "We are the ADJ era.", "We are the ADJ ADJ NOUN!", "We shall never bow to NOUN.", "NOUN? NOUN? Who cares?", "Our PROPER is ADJ", "My Throne is of ADJ NOUN!", "The PROPER's Rule is of ADJ NOUN!", "The PROPER will VERB his NOUN.", "We do not fear NOUN.", "We are the Legion of NOUN.","VERB Under the PROPER's NOUN!","VERB Under the PROPER!"};

	public static String createMotto() {
		String output = countryMottoFormats[math.randomint(0, countryMottoFormats.length-1)];
		while (output.contains("VERB") || output.contains("ADJ") || output.contains("PROPER") || output.contains("NOUN")) {
			output = output.replaceFirst("PROPER", properNoun[math.randomint(0, properNoun.length-1)]);
			output = output.replaceFirst("NOUN", nouns[math.randomint(0, nouns.length-1)]);
			output = output.replaceFirst("ADJ", adjectives[math.randomint(0,adjectives.length-1)]);
			output = output.replaceFirst("VERB", verbs[math.randomint(0, verbs.length-1)]);
		}
		return output;
	}


	public static String createName() {
		//selects format
		formatused = nameFormats[math.randomint(0,nameFormats.length-1)];
		//decodes format
		for (String character : formatused) {
			if (character == "s") {
				output += scombinations[math.randomint(0,scombinations.length-1)];
			} else if (character == "b") {
				output += ecombinations[math.randomint(0,ecombinations.length-1)];
			} else if (character == "a") {
				output += vowels[math.randomint(0,vowels.length-1)];
			} else if (character == "c") {
				output += hconsenants[math.randomint(0,hconsenants.length-1)];
			} else if (character == "r") {
				output += sconsenants[math.randomint(0,sconsenants.length-1)];
			} else if (character == "d") {
				output += dconsenants[math.randomint(0,dconsenants.length-1)];
			} else if (character == "i") {
				output += vcombinations[math.randomint(0,vcombinations.length-1)];
			}
		}
	
		output = output.substring(0,1).toUpperCase() + output.substring(1);
		gamer = output;
		output = "";
		if (math.randomint(1, 3) == 1) {
			formatused = nameFormats[math.randomint(0,nameFormats.length-1)];
			//decodes format
			for (String character : formatused) {
				if (character == "s") {
					output += scombinations[math.randomint(0,scombinations.length-1)];
				} else if (character == "b") {
					output += ecombinations[math.randomint(0,ecombinations.length-1)];
				} else if (character == "a") {
					output += vowels[math.randomint(0,vowels.length-1)];
				} else if (character == "c") {
					output += hconsenants[math.randomint(0,hconsenants.length-1)];
				} else if (character == "r") {
					output += sconsenants[math.randomint(0,sconsenants.length-1)];
				} else if (character == "d") {
					output += dconsenants[math.randomint(0,dconsenants.length-1)];
				} else if (character == "i") {
					output += vcombinations[math.randomint(0,vcombinations.length-1)];
				}
			}
		
			output = output.substring(0,1).toUpperCase() + output.substring(1);
			gamer += " " + output;
			output = "";
		}
		
		return countryNameFormats[math.randomint(0, countryNameFormats.length-1)].replace("[NAME]", gamer);
	}
	public static String createStarSystemName() {
		//selects format
		formatused = nameFormats[math.randomint(0,nameFormats.length-1)];
		//decodes format
		for (String character : formatused) {
			if (character == "s") {
				output += scombinations[math.randomint(0,scombinations.length-1)];
			} else if (character == "b") {
				output += ecombinations[math.randomint(0,ecombinations.length-1)];
			} else if (character == "a") {
				output += vowels[math.randomint(0,vowels.length-1)];
			} else if (character == "c") {
				output += hconsenants[math.randomint(0,hconsenants.length-1)];
			} else if (character == "r") {
				output += sconsenants[math.randomint(0,sconsenants.length-1)];
			} else if (character == "d") {
				output += dconsenants[math.randomint(0,dconsenants.length-1)];
			} else if (character == "i") {
				output += vcombinations[math.randomint(0,vcombinations.length-1)];
			}
		}
	
		output = output.substring(0,1).toUpperCase() + output.substring(1);
		gamer = output;
		output = "";
		if (math.randomint(1, 3) == 1) {
			formatused = nameFormats[math.randomint(0,nameFormats.length-1)];
			//decodes format
			for (String character : formatused) {
				if (character == "s") {
					output += scombinations[math.randomint(0,scombinations.length-1)];
				} else if (character == "b") {
					output += ecombinations[math.randomint(0,ecombinations.length-1)];
				} else if (character == "a") {
					output += vowels[math.randomint(0,vowels.length-1)];
				} else if (character == "c") {
					output += hconsenants[math.randomint(0,hconsenants.length-1)];
				} else if (character == "r") {
					output += sconsenants[math.randomint(0,sconsenants.length-1)];
				} else if (character == "d") {
					output += dconsenants[math.randomint(0,dconsenants.length-1)];
				} else if (character == "i") {
					output += vcombinations[math.randomint(0,vcombinations.length-1)];
				}
			}
		
			output = output.substring(0,1).toUpperCase() + output.substring(1);
			gamer += " " + output;
			output = "";
		}
		
		return gamer;

	}
	
	public static String[] appendTo(String[] appendTo, String appendThis) {
		String[] output = new String[appendTo.length+1];
		int g = 0;
		for (String i : appendTo) {
			output[g] = i;
			g++;
		}
		output[g] = appendThis;
		return output;
	}
	public static boolean inArray(String[] array, String lookFor) {
		for (String i : array) {
			if (i.equals(lookFor)) {
				return true;
			}
		}
		return false;
	}
	public static String[] purge(String[] purgeFrom, String purgeThis) {
		String[] output = {};
		for (String i : purgeFrom) {
			if (i.equals(purgeThis)) {
				continue;
			} else {
				output = appendTo(output,i);
			}
		}
		return output;
	}
	public static String[] splitVowels(String splitThis) {
		String[] output = {};
		String[] input = splitThis.split("");
		String[] splitInput = {};
		String addto = "";
		int b = 0;
		for (String i : input) {
			if (i.equals("a") || i.equals("e") || i.equals("i") || i.equals("o") || i.equals("u") || i.equals("y") || i.equals(" ")) {
				splitInput = appendTo(splitInput,addto);
				splitInput = appendTo(splitInput,i);
				addto = "";
			} else {
				addto = addto + i;
			}
			b++;
		}
		splitInput = appendTo(splitInput,addto);
		splitInput = purge(splitInput,"");
		for (String i : splitInput) {
			if (inArray(vowels,i)) {
				output = appendTo(output,"a");
			} else if (inArray(hconsenants,i)) {
				output = appendTo(output,"c");
			} else if (inArray(sconsenants,i)) {
				output = appendTo(output,"r");
			} else if (inArray(dconsenants,i)) {
				output = appendTo(output,"d");
			} else if (inArray(ecombinations,i)) {
				output = appendTo(output,"b");
			} else if (inArray(scombinations,i)) {
				output = appendTo(output,"s");
			} else {
				output = appendTo(output,i);
			}
		}
		return output;
	}

}
