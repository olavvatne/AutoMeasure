package view.settingsPanel;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

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
		final Dimension originalTabsDim = new Dimension(400, 400);
		System.out.println(originalTabsDim + "ORIGINAL");
		tabbedPane.addChangeListener(new ChangeListener() {

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
                "Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        JComponent writer = makeTextPanel("EXCEL PANEL");
        tabbedPane.addTab("Writer", null, writer,
                "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JComponent gui = makeTextPanel("GUI PANEL");
        tabbedPane.addTab("Gui", null, gui,
                "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        this.add(tabbedPane, c);
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
