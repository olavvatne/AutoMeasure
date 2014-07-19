package view.settingsPanel;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.management.RuntimeErrorException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class settingsPanel extends JPanel {
	private JTabbedPane tabbedPane;
	
	private static int maxW = 0;
    private static int maxH = 0;
    
	public settingsPanel(JPanel parentPanel) {
		 super(new GridBagLayout());
		initComponents(parentPanel);
	}
	
	
	
	protected void setSize(Component p) {
		  tabbedPane.setPreferredSize(this.getMinimumSize());
	      p.setPreferredSize(this.getMinimumSize());
	        
	}
	
	private void close() {
		JDialog topFrame = (JDialog) SwingUtilities.getWindowAncestor(this);
		if(topFrame instanceof JDialog) {
			topFrame.dispose();			
		}
		else {
			throw new RuntimeException("SettingsPanel not put inside a JDialog");
		}
	}
	
	private void save() {
		Component[] panels = tabbedPane.getComponents();
		for (int i = 0; i< panels.length; i++) {
			if(panels[i] instanceof BaseSettings) {
				((BaseSettings)panels[i]).save();
			}
		}
		close();
	}
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel();
        panel.setSize(new Dimension(400,400));
        panel.setMinimumSize(new Dimension(400,400));
        JLabel filler = new JLabel(text);
        panel.add(filler);
        return panel;
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
            }
        });

		
		BaseSettings analyzer = new AnalyzerSettings();
        tabbedPane.addTab("Analyzer", null, analyzer,
                "Analyzer options");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        BaseSettings writer = new WriterSettings();
        tabbedPane.addTab("Writer", null, writer,
                "Writer options");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        BaseSettings gui = new GuiSettings();
        tabbedPane.addTab("Gui options", null, gui,
                "Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
        
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
}
