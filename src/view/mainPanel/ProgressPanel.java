package view.mainPanel;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import automeasurer.Measurer;

/**
 * A Jpanel containing a progress bar. Can be used as an global 
 * progress bar for an application. The panel listen to events, and
 * update the progress bar when relevant events happen.
 * 
 * A way of offer progress feedback to the user when doing 
 * complex operations like image analysis, and converting thousands of
 * pictures into data models.
 * 
 * @author Olav
 *
 */
public class ProgressPanel extends JPanel implements PropertyChangeListener {
	
	private JProgressBar progress;
	public ProgressPanel() {
		progress = new JProgressBar();
		this.setLayout(new BorderLayout());
		this.add(progress, BorderLayout.CENTER);
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(Measurer.PROGRESS_UPDATE)) {
			System.out.println((Integer)evt.getNewValue());
			this.progress.setValue((Integer)evt.getNewValue());
		}
		else if(evt.getPropertyName().equals(Measurer.SETMAX)) {
			System.out.println((Integer)evt.getNewValue());
			this.progress.setMaximum((Integer)evt.getNewValue());
		}
		else if(evt.getPropertyName().equals(Measurer.FINISHED)) {
			System.out.println("Finished with operation");
			this.progress.setValue(0);
		}
	}

}
