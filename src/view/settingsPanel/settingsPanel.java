package view.settingsPanel;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class settingsPanel extends JPanel {
	JTabbedPane tabbedPane;
	private static int maxW = 0;
    private static int maxH = 0;
    
	public settingsPanel(JPanel parentPanel) {
		 super(new GridBagLayout());
		initComponents(parentPanel);
	}
	
	private void initComponents(JPanel parentPanel) {
		tabbedPane = new JTabbedPane();
		
		//TODO:Better way to get height.
		final Dimension originalTabsDim = new Dimension(400, 400);
		tabbedPane.addChangeListener(new ChangeListener() {
			//Is this necessary?
            @Override
            public void stateChanged(ChangeEvent e) {

                Component p =   ((JTabbedPane) e.getSource()).getSelectedComponent();
                Dimension panelDim = p.getPreferredSize();

                /*Dimension nd = new Dimension(
                        originalTabsDim.width - ( maxW - panelDim.width),
                        originalTabsDim.height - ( maxH - panelDim.height) );
                 */
                Dimension nd = originalTabsDim;
                tabbedPane.setPreferredSize(nd);

                //p.get
            }

        });

		
		JComponent analyzer = makeTextPanel("ANALYZER OPTIONS");
        tabbedPane.addTab("Analyzer", null, analyzer,
                "Analyzer options");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        JComponent writer = makeTextPanel("EXCEL PANEL");
        tabbedPane.addTab("Writer", null, writer,
                "Writer options");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent gui = makeTextPanel("GUI PANEL");
        tabbedPane.addTab("Gui options", null, gui,
                "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        this.add(tabbedPane, c);
        c.gridwidth = 1;
        c.gridy = 1;
        c.anchor  = GridBagConstraints.LINE_END;
        this.add(saveButton,c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        this.add(cancelButton,c);
	}
	
	protected void setSize(Component p) {
		  tabbedPane.setPreferredSize(this.getMinimumSize());
	      p.setPreferredSize(this.getMinimumSize());
	        
	}
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel();
        panel.setSize(new Dimension(400,400));
        panel.setMinimumSize(new Dimension(400,400));
        System.out.println(panel.getSize());
        JLabel filler = new JLabel(text);
        panel.add(filler);
        return panel;
    }
}
