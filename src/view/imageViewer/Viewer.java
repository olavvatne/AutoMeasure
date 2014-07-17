package view.imageViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;







import model.ImageDataModel;

/**
 * Viewer is the panel that contain the control overlay for the images, threePhasePanel and the imageDataModel that will by manipulated by threephasepanel.
 * 
 * */
public class Viewer extends JPanel {
	protected ImageDataModel data;
	protected ThreePhasePanel threePhasePanel;
	private JLabel imageLabel;
	private JLayeredPane layeredPane;
	private BufferedImage img;
	private String path;
	
	
	public Viewer(ImageDataModel data) {
		this.data = data;
		this.imageLabel = new JLabel();
		this.path = data.getFilePath();
		
		
		
		
		
		this.threePhasePanel = new ThreePhasePanel(this.data, 2464);
		readIconInBackground(new File(path));
		this.threePhasePanel.setOpaque(false);
		this.addMouseListener(threePhasePanel);
		this.addMouseMotionListener(threePhasePanel);
		setLayout();
	}
	
	public void setModel(ImageDataModel data) {
		
		this.path = data.getFilePath();
		this.img = null;
		
		this.threePhasePanel.setData(data);
		readIconInBackground(new File(path));
		this.data = data;
		
	}
	private static BufferedImage readImg(File file) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	
	
	private void readIconInBackground(final File file) {
		SwingWorker<ImageIcon, Void> imageTask = new SwingWorker<ImageIcon, Void>() {

			@Override
			protected ImageIcon doInBackground() throws Exception {
				if (img == null) {
					img = readImg(file);
					threePhasePanel.setImageWidth(img.getWidth());
					System.out.println(img.getWidth());
				}
				return getScaledImage(img);
			}
			
			public void done() {
				ImageIcon icon = null;
				try {
					icon = get();
				} catch (Exception e) {
					// TODO: handle exception
				}
				//imgIcon = icon;
				if(icon != null) {
					imageLabel.setIcon(icon);
					imageLabel.repaint();
					
				}
				
			}
		};
		imageTask.execute();
	}
	
	private ImageIcon getScaledImage(BufferedImage img) {
		int width = this.getSize().width;
		int height = this.getSize().height;
		
		try {
			BufferedImage scaledImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
			Graphics2D gScaledImage = scaledImage.createGraphics();
			gScaledImage.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED );
	
			Dimension dim = new Dimension(width, height);
			
			
			gScaledImage.drawImage( img,(int) ((width-dim.getWidth())/2),
					(int)((height-dim.getHeight())/2), (int)dim.getWidth(), (int)dim.getHeight(), null ); 
			gScaledImage.dispose();
			return new ImageIcon(scaledImage);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	private void setLayout() {
		this.setLayout(new BorderLayout());
		layeredPane = new JLayeredPane();

		layeredPane.add(imageLabel, new Integer(0));
		layeredPane.add(threePhasePanel, new Integer(1));
		this.add(layeredPane, BorderLayout.CENTER);
		
		
		layeredPane.addComponentListener(new ComponentListener() {
			
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void componentResized(ComponentEvent e) {
				threePhasePanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
				imageLabel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
				
				threePhasePanel.setWindowWidth(e.getComponent().getWidth());
                System.out.println("componentResized");
                readIconInBackground(new File(path));
			}
			
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}


}
