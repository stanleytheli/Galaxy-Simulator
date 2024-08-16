package simulator;
import java.util.Arrays; 

public class math {
		public static int randomint(int min, int max) {
		    int dif = max - min+1;
		    return ((int)(Math.random()*dif))+min;
		}
		public static float distance(float x1, float y1, float x2, float y2) {
			return (float)Math.sqrt(((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2)));
		}
		public static int[] append(int[] array, int item) {
			int[] output = new int[array.length+1];
			int b = 0;
			for (int i = 0;i<array.length;i++) {
				output[b] = array[i];
				b++;
			}
			output[output.length-1] = item;
			return output;
		}
		public static float[] append(float[] array, float item) {
			float[] output = new float[array.length+1];
			int b = 0;
			for (int i = 0;i<array.length;i++) {
				output[b] = array[i];
				b++;
			}
			output[output.length-1] = item;
			return output;
		}
		public static boolean[] append(boolean[] array, boolean item) {
			boolean[] output = new boolean[array.length+1];
			int b = 0;
			for (int i = 0;i<array.length;i++) {
				output[b] = array[i];
				b++;
			}
			output[output.length-1] = item;
			return output;
		}
		public static int[][] append(int[][] array, int[] item) {
			int[][] output = new int[array.length+1][];
			int b = 0;
			for (int i = 0;i<array.length;i++) {
				output[b] = array[i];
				b++;
			}
			output[output.length-1] = item;
			return output;
		}
		public static String[] append(String[] array, String item) {
			String[] output = new String[array.length+1];
			int b = 0;
			for (int i = 0;i<array.length;i++) {
				output[b] = array[i];
				b++;
			}
			output[output.length-1] = item;
			return output;
		}
		public static int[] sortArrayDescending(int[] array) {
			int[] output = new int[array.length];
			int[] sortThis = array.clone();
			for (int i = 0;i<array.length;i++) {
				output[i] = sortThis[findLargestInArray(sortThis)];
				sortThis[findLargestInArray(sortThis)] = -999;
			}
			return output;
		}
		public static int findLargestInArray(int[] array) {
			int largestSoFar = -1000000000;
			int b = 0;
			int output = 0;
			for (int number : array) {
				if (number > largestSoFar) {
					output = b;
					largestSoFar = number;
				}
				b++;
			}
			return output;
		}
		public static int[] sortIndexes(int[] array) {
			int[] output = new int[array.length];
			int[] sortThis = array.clone();
			for (int i = 0;i<array.length;i++) {
				output[i] = findLargestInArray(sortThis);
				sortThis[findLargestInArray(sortThis)] = -999;
			}
			return output;
		}
	    public static int findNotLargest(int[] array,int place) {
	    	int[] findIn = sortIndexes(array);
			return findIn[place];
	    }
		public static int findNearestPoint(float x,float y,float[] xlist, float[] ylist) { //finds the closest star and gives its ID
			float lowestDistanceSoFar = 99999;
			int lowestDistID = 0;
			int b = 0;
			for (float position : xlist) {
				if (distance(x, y, xlist[b], ylist[b]) < lowestDistanceSoFar) {
					lowestDistanceSoFar = math.distance(x, y, xlist[b], ylist[b]);
					lowestDistID = b;
				}
				b++;
			}
			return lowestDistID;
		}
		public static float distanceToNearestPoint(float x,float y,float[] xlist, float[] ylist) { //finds the closest star and gives its ID
			float lowestDistanceSoFar = 99999;
			int b = 0;
			for (float position : xlist) {
				if (distance(x, y, xlist[b], ylist[b]) < lowestDistanceSoFar) {
					lowestDistanceSoFar = math.distance(x, y, xlist[b], ylist[b]);
				}
				b++;
			}
			return lowestDistanceSoFar;
		}
		public static double getCircleX(double angle, double radius) { //input degrees
			angle = (angle/360) * 6.282;
			return (radius * (Math.cos(angle)));
		}
		public static double getCircleY(double angle, double radius) {
			angle = (angle/360) * 6.282;
			return (radius * Math.sin(angle));
		}
		public static float[] getCirclePoint(double angle, double radius) {
			return new float[] {(float)getCircleX(angle, radius),(float)getCircleY(angle,radius)};
		}
		public static int min(int a, int b) {
			if (a > b) {
				return b;
			} else {
				return a;
			}
		}
		public static int max(int a, int b) {
			if (a > b) {
				return a;
			} else {
				return b;
			}
		}
		public static double min(double a, double b) {
			if (a > b) {
				return b;
			} else {
				return a;
			}
		}
		public static double max(double a, double b) {
			if (a > b) {
				return a;
			} else {
				return b;
			}
		}

}
