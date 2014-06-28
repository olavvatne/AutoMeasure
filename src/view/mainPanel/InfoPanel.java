package view.mainPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Info panel that can be used to display summaried information about a particular
 * data model.
 * @author Olav
 *
 */
public class InfoPanel extends JPanel {
	
	public InfoPanel() {
		this.setLayout(new BorderLayout());
		this.setBorder(new EmptyBorder(10, 10, 10 ,10));
		JLabel label = new JLabel("test");
		this.add(label, BorderLayout.CENTER);
	}
}
