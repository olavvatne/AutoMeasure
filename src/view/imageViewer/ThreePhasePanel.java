package view.imageViewer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JPanel;

import utilities.measurement.Measurement;
import model.ImageDataModel;
import model.MarkerValue;
import model.Offset;
import model.Status;



/**
 * The view layer containing all measurement lines, and adjusters. Its basically an overlay for the image.
 * 
 *  @author Olav
 *
 */
public class ThreePhasePanel extends JPanel implements MouseListener, MouseMotionListener {
	
	public static final int NO_SELECTION = -1;
	
	public static final int OW_HIGH_OFFSET = 7;
	public static final int OW_LOW_OFFSET = 6;
	public static final int OG_HIGH_OFFSET = 4;
	public static final int OG_LOW_OFFSET = 5;
	
	public static final int NR_OF_MARKERS =4;
	public static final int NR_OF_VALUEPOINTS =2;
	public static final int PADDING = 50;
	
	private static Color LIGHT_BLUE = new Color(38, 121, 255);
	private static Color LIGHT_RED = new Color(225, 38, 38);
	private final DecimalFormat df =  new DecimalFormat("#0.00");
	
	private ImageDataModel model;
	private int  windowWidth;
	private int imageWidth;
	private List<Double> markerValue;
	private List<Double> offset;
	private boolean[] isValueLineSelected = new boolean[2];
	private int selected = NO_SELECTION;
	private Measurement calc;
	
	private boolean isOffsetDirty = false;
	
	/**
	 * The constructor of ThreePhasePanel needs the imageModel, containing the marker values and value values.
	 * Also the model is directly updated if changes is done in threephasepanel. ImgWidth is the typical length of the image.
	 * @param data - image model
	 * @param imgWidth - CHECK IF NEEDED
	 */
	public ThreePhasePanel(ImageDataModel data) {
		
		//To avoid markers getting placed whenever, the windowWidth is set to the screen width initially.
		//setData will override the value anyways. TODO: See if a repaint before setting the windowWidth will work in setData
		this.windowWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(); //TODO:REMOVE?
		
		
		setData(data);
		
	}
	
	/**
	 * If previous or next has been clicked or an image is opened from the imageTable, the setData method has to be run either way.
	 * This method is crucial for initializing a threePhasePanel. It creates a new calculator,
	 * reset dirty flags, and distribute the markers evenly if no markers is found in the model.
	 * @param model
	 */
	public void setData(ImageDataModel model) {
		
		markerValue = MarkerValue.getMarkerValues(model.getId());
		offset = Offset.getOffset(model.getId());
		calc = new Measurement();
		this.isOffsetDirty = false;
		
		if(model != null) {
			this.model = model;
			setValueLinesSelected(true);

			if(model.getMarkers() == null) {
				double[] markers = new double[4];
				markers[Measurement.OG_HIGH] = this.windowWidth/5;
				markers[Measurement.OG_LOW] = this.windowWidth/4;
				markers[Measurement.OW_LOW] = this.windowWidth/3;
				markers[Measurement.OW_HIGH] = this.windowWidth/2;
				this.model.setMarkers(markers);
			}
			setWindowWidth((int)this.getWidth());
			calc.setCalculator(offset, markerValue, this.model.getMarkers(), this.model.getValues());
			
		}
		this.repaint();
	}
	
	/**
	 * When the Jframe is being resized this method has to be called to update the windowWidth.
	 * This variable is important for calculating position of markers in the frame.
	 * @param w - new panel width
	 */
	public void setWindowWidth(int w) {
		this.windowWidth = w;
		calc.setGuiValues(this.imageWidth, w);
	}

	public void setImageWidth(int i) {
		this.imageWidth = i;
		calc.setGuiValues(i, this.windowWidth);
	}
	
	private void setValueLinesSelected(boolean value) {
		for(int i = 0; i<this.isValueLineSelected.length; i++) {
			this.isValueLineSelected[i] = true;
		}
	}
	
	private boolean isValueLineSelected(int i) {
		if (this.isValueLineSelected.length >i) {
			return this.isValueLineSelected[i];
		}
		else {
			return false;
		}
	}
	
	private void setValueLineSelected(int i, boolean value) {
		if (this.isValueLineSelected.length > i) {
			this.isValueLineSelected[i] = value;
		}
	}
	
	/**
	 * Overridden paintComponent, which is called whenever the panel should be repainted.
	 * It basically calls paintOverlay which is responsible for drawing all lines in the overlay.
	 */
	protected void paintComponent(Graphics gOrig) {
        super.paintComponent(gOrig);
        Graphics2D g = (Graphics2D) gOrig.create();
        try {
            paintOverlay(g);
        } finally {
            g.dispose();
        }
    }
	
	/**
	 * PaintOverlay paints all values, lines and text in the overlay.
	 * It often calls methods in the Measurement calculator which return the pixel position where a line should be placed.
	 * @param g - a graphics component used to draw lines and text etc
	 */
	private void paintOverlay(Graphics2D g) {
		g.setStroke (new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f));
    	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)0.8f));
    	g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	//OW
    	boolean notOffsetMarker = false;
    	paintLineSlider(g, "Start O/W", Measurement.OW_LOW, (int)calc.getLinePos(Measurement.OW_LOW , notOffsetMarker) , notOffsetMarker, LIGHT_BLUE);
    	paintLineSlider(g, "End O/W", Measurement.OW_HIGH, (int)calc.getLinePos(Measurement.OW_HIGH , notOffsetMarker) , notOffsetMarker, LIGHT_BLUE);
    	paintLineSlider(g, "test O/W", OW_LOW_OFFSET, (int)calc.getLinePos(Measurement.OW_LOW , !notOffsetMarker) , !notOffsetMarker, LIGHT_BLUE);
    	paintLineSlider(g, "test O/W", OW_HIGH_OFFSET, (int)calc.getLinePos(Measurement.OW_HIGH , !notOffsetMarker) , !notOffsetMarker, LIGHT_BLUE);
    	paintLine(g, "O/W", Measurement.OW_VALUE, calc.getThreePhaseValue(Measurement.OW_VALUE), LIGHT_BLUE);
    	
    	//OG
    	paintLineSlider(g, "Start O/G", Measurement.OG_LOW, (int)calc.getLinePos(Measurement.OG_LOW , notOffsetMarker) , notOffsetMarker, LIGHT_RED);
    	paintLineSlider(g, "End O/G", Measurement.OG_HIGH, (int)calc.getLinePos(Measurement.OG_HIGH , notOffsetMarker) , notOffsetMarker, LIGHT_RED);
    	paintLineSlider(g, "test O/W", OG_LOW_OFFSET, (int)calc.getLinePos(Measurement.OG_LOW , !notOffsetMarker) , !notOffsetMarker, LIGHT_RED);
    	paintLineSlider(g, "test O/W", OG_HIGH_OFFSET, (int)calc.getLinePos(Measurement.OG_HIGH , !notOffsetMarker) , !notOffsetMarker , LIGHT_RED);
    	paintLine(g, "O/G", Measurement.OG_VALUE, calc.getThreePhaseValue(Measurement.OG_VALUE), LIGHT_RED);
    	
    	paintStatusRectangle(g);
	}
	
	private void paintStatusRectangle(Graphics2D g) {
		Status status = this.model.getStatus();    	
    	g.setColor(status.color());
    	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)0.3f));
    	g.fillRect(10, 10, 30, 30);
	}
	/**
	 * Called from paintComponent. A convenience method for painting a single line.
	 * In each panel there is 4 lines, so a method is a good idea.
	 * Each line has a text on top of a vertical line going across the screen. A small oval is drawn to indicate that
	 * the line can be dragged.
	 * @param g
	 * @param header What to write over the line
	 * @param type If its selected line is drawn in a darker color
	 * @param pos What pixel column to draw line 
	 * @param offsetMarker If this line actually is a offsetMarker. Shorter line basically.
	 * @param c What color to draw line and text
	 */
	private void paintLineSlider(Graphics2D g, String header, int type, int pos, boolean offsetMarker, Color c) {
		g.setFont(new Font("Arial", Font.PLAIN, 15));
		g.setPaint(c);
		if (type == selected) {
			g.setPaint(c.darker());
		}
		
		int padding = PADDING + ((offsetMarker) ? 300 : 0);
		g.drawLine(pos, padding, pos, this.getHeight()-padding);
    	g.drawString(header, pos-20, padding -30);
    	g.fillOval(pos-5, (this.getHeight()-60)/2, 10, 10);
	}
	
	/**
	 * Very similar to the paintLineSlider, but with no oval to indicate that its draggable.
	 * @param g
	 * @param header What to write over the line
	 * @param type What line it is. Used to get exact pixel position.
	 * @param value A value that will be drawn. 
	 * @param c
	 */
	private void paintLine(Graphics2D g, String header, int type,double value, Color c) {
		if(isValueLineSelected(type)) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
    		g.drawString(header, (int)calc.getValueLinePos(type)-5, 75);
    		g.drawString(df.format(value), (int)calc.getValueLinePos(type)-10, 90);
    		g.drawLine((int)calc.getValueLinePos(type), 100, (int)calc.getValueLinePos(type), this.getHeight()-100);
    	}
	}
	public void mouseClicked(MouseEvent e) {
		
		
	}
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * If mouse is pressed anywhere in the panel this method is called.
	 * It checks the lines currently in the panel, and sets the line actually clicked on to be the selected one.
	 * If the mouse click is not over a marker line, it checks if this was a right or left click. If it was the value line
	 * of either OW or OG is set using setValueLineSelected.
	 */
	public void mousePressed(MouseEvent e) {
		//hvis endring er enabled eller noe.
		for(int i = 0; i<NR_OF_MARKERS; i ++) {
			if(calc.getLinePos(i, true)-10 < e.getX() && e.getX()<calc.getLinePos(i, true) + 10) {
				System.out.println(i + NR_OF_MARKERS + " SELECTED");
				this.selected = i + NR_OF_MARKERS;
			}
		}

		for(int i = 0; i<NR_OF_MARKERS; i ++) {
			if(calc.getLinePos(i, false)-10 < e.getX() && e.getX()<calc.getLinePos(i, false) + 10) {
				this.selected = i;
			}
		}
		
		if (selected == NO_SELECTION) {
			if(e.getButton() == MouseEvent.BUTTON3){
				if (e.getClickCount() > 1) {
					setValueLineSelected(Measurement.OW_VALUE, false);
					selected = NO_SELECTION;
				}
				else {
					calc.setValueLinePos(Measurement.OW_VALUE, (double)e.getX());
					setValueLineSelected(Measurement.OW_VALUE, true);
					selected = NO_SELECTION;
				}
			}
			else if(e.getButton() == MouseEvent.BUTTON1){
				if(e.getClickCount() >1) {
					setValueLineSelected(Measurement.OG_VALUE, false);
					selected = NO_SELECTION;
				}
				else {
					calc.setValueLinePos(Measurement.OG_VALUE, (double)e.getX());
					setValueLineSelected(Measurement.OG_VALUE, true);
					selected = NO_SELECTION;
				}

			}
		}
		this.repaint();
	}
	
	/**
	 * Marker lines are dragged. When user release mouse button this method is called. It checks that a marker has been selected
	 * by mousePressed. If this is the case, the new position of the marker line is recorded using the Measurements setLinePos
	 */
	public void mouseReleased(MouseEvent e) {
		if(selected != NO_SELECTION) {
			if(selected < NR_OF_MARKERS) {
				calc.setLinePos(selected, e.getX(), false);
			}
			else {
				this.offset.set(selected-NR_OF_MARKERS, calc.getNewPercentage(selected, e.getX()));
				//ImageDataModel.offset[selected-NR_OF_MARKERS] = getNewPercentage(selected, e.getX());
				isOffsetDirty = true;
			}
			selected = NO_SELECTION;
			
		}
		this.repaint();
	}
	
	/**
	 * When user is dragging his/her mouse across the screen this method is called. 
	 * Because marker lines is draggable , it has to be redrawn alot,
	 * therefore the markerValue is recorded during the drag from point A to point B
	 */
	public void mouseDragged(MouseEvent e) {
		if (selected != NO_SELECTION) {
			if (selected < NR_OF_MARKERS) {
				calc.setLinePos(selected, e.getX(), false);
			}
			else {
				this.offset.set(selected-NR_OF_MARKERS, calc.getNewPercentage(selected, e.getX()));
				//ImageDataModel.offset[selected-NR_OF_MARKERS] = getNewPercentage(selected, e.getX());
			}
			
			this.repaint();
		}
	}
	
	
	public void mouseMoved(MouseEvent e) {
	}
	
	/**
	 * Before closing a threephasePanel this method should be called. It checks to see if any of the flags has become dirty while the 
	 * panel has been shown. If it has become dirty the offset is changed, and in all cased will the status be set to MANUAL_EDIT
	 * to indicate that this is a changed value from the initial analyzing phase.
	 */
	public void close() {
		if(isOffsetDirty) {
			Offset.changeOffset(this.model.getId(), this.offset, true);
		}
		if(isOffsetDirty || calc.isDirty()) {
			this.model.setStatus(Status.MANUAL_EDIT);
		}
	}
}

