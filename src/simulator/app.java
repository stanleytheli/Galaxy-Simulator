package simulator;

import javax.swing.SwingUtilities;

public class app {
	public static void openForm(final int formType) {
    	SwingUtilities.invokeLater(new Runnable() {
    		@Override
    		public void run() {
    	    	new MainFrame(formType);
    		}
    	});

	}
}
