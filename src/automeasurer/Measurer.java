package automeasurer;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;









import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.mainPanel.MainPanel;





public class Measurer {
	public static final String SAVE = "save";
	public static final String OPEN = "open";
	public static final String ANALYZE = "analyze";
	public static final String ANALYZE_IMAGE = "analyzeImage";
	public static final String SETMAX = "setmax";
	public static final String FINISHED = "finished";
	public static final String PROGRESS_UPDATE = "progress";
	public static final String CALIBRATE = "calibrate";
	public static final String NEXT = "next";
	public static final String PREVIOUS ="previous";
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() 
			{
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedLookAndFeelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Measurer.run();
			}
		});

	}
	
	public static void run() {
		JFrame frame = new JFrame();
		MainPanel panel = new MainPanel();
		
		Dimension sz = Toolkit.getDefaultToolkit().getScreenSize();
		//frame.setMinimumSize(new Dimension((int)(sz.getWidth()/2), (int)(sz.getHeight()/2)));
		//frame.setPreferredSize(sz);// må vekk for at minimering skal faktisk minimere, maximize forårsaker layout krøll med initflytting
		frame.setLocation(sz.width/10 , sz.height/10);
		frame.setTitle("innlesning");
		ImageIcon c = new ImageIcon(Measurer.class.getResource("/measure.png"));
		frame.setIconImage(c.getImage());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //flytter på seg bug
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon());
		
	}
	
}
