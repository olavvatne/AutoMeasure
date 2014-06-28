package view.imageViewer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
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

import model.ImageDataModel;
import analyze.ImageMarkerPoint;



/**
 * The view layer containing all measurement lines, and adjusters
 * @author Olav
 *
 */
public class ThreePhasePanel extends JPanel implements MouseListener, MouseMotionListener {
	
	public static final int NO_SELECTION = -1;
	public static final int OW_HIGH = 3;
	public static final int OW_LOW = 2;
	public static final int OG_HIGH = 0;
	public static final int OG_LOW = 1;
	
	public static final int OW_HIGH_OFFSET = 7;
	public static final int OW_LOW_OFFSET = 6;
	public static final int OG_HIGH_OFFSET = 4;
	public static final int OG_LOW_OFFSET = 5;
	
	public static final int OG_VALUE = 0;
	public static final int OW_VALUE = 1;
	public static final int NR_OF_MARKERS =4;
	public static final int NR_OF_VALUEPOINTS =2;
	public static final int PADDING = 50;
	
	private static Color LIGHT_BLUE = new Color(38, 121, 255);
	private static Color LIGHT_RED = new Color(225, 38, 38);
	private final DecimalFormat df =  new DecimalFormat("#0.00");
	
	private ImageDataModel model;
	private int  windowWidth;
	private int imageWidth;
	private double[] markerValue = new double[]{18, 1, 1, 18}; //finn en måte å generaliser denne og!
	private boolean[] isValueLineSelected = new boolean[2];
	private int selected = NO_SELECTION;
	
	public ThreePhasePanel(ImageDataModel data, int imgWidth) {
		
		this.windowWidth = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		this.imageWidth = imgWidth;

		
		if(data.isMarkersValid()) {
			this.model = data;
			setValueLinesSelected(true);
		}
		else {
			this.model = new ImageDataModel(data.getDate(), data.getFilePath()); //Kopi av data sånn at man kan hente ut denne og erstatte om man ønsker
			double[] markers = new double[4];
			markers[OG_HIGH] = this.windowWidth/5;
			markers[OG_LOW] = this.windowWidth/4;
			markers[OW_LOW] = this.windowWidth/3;
			markers[OW_HIGH] = this.windowWidth/2;
			this.model.setMarkers(markers);
			this.model.setValues(new double[2]);
		}
	}
	
	
	public void setData(ImageDataModel model) {
		this.model = model;
		this.repaint();
	}
	
	public void setWindowWidth(int w) {
		this.windowWidth = w;
	}
	
	private double getOffset(double x1, double x2, double percentage) {
		
		return ((((x2 - x1)*percentage)/this.imageWidth)*this.windowWidth);
	}
	
	//Kanskje litt dårlig måte å gjøre det på? med tanke på offset og slik.
	private double getLinePos(int i, boolean withOffset) {
		if (i<NR_OF_MARKERS) {
			if (withOffset) {
				if (i%2== 0) {
					return ((this.model.getMarkerXPosition(i)/this.imageWidth)*this.windowWidth) 
							+getOffset(model.getMarkerXPosition(i), model.getMarkerXPosition(i+1), ImageDataModel.offset[i]);
				}
				else {
					return ((this.model.getMarkerXPosition(i)/this.imageWidth)*this.windowWidth) -
							getOffset(model.getMarkerXPosition(i-1), model.getMarkerXPosition(i), ImageDataModel.offset[i]);
				}
				
			}
			else {
				return ((this.model.getMarkerXPosition(i)/this.imageWidth)*this.windowWidth);
			}
			
		}
		return -1;
	}
	
	private void setLinePos(int i, double value, boolean withOffset) {
		if (i<NR_OF_MARKERS) {
			if (withOffset) {
				if (i%2== 0) {
					double offset = getOffset(model.getMarkerXPosition(i), model.getMarkerXPosition(i+1), ImageDataModel.offset[i]);
					this.model.setMarkerXPosition(i, ((value / this.windowWidth) * this.imageWidth) - offset);
				}
				else {
					double offset = getOffset(model.getMarkerXPosition(i-1), model.getMarkerXPosition(i), ImageDataModel.offset[i]);
					this.model.setMarkerXPosition(i, ((value / this.windowWidth) * this.imageWidth) + offset);
				}
				
			}
			else {
				this.model.setMarkerXPosition(i, ((value/this.windowWidth)*this.imageWidth));
			}
			
		}
	}
	
	private double getLineValue(int i) {
		if(i<NR_OF_MARKERS) {
			return markerValue[i];
		}
		return -1;
	}
	
	private void setLineValue(int i, double value) {
		if(i<NR_OF_MARKERS) {
			markerValue[i] = value;
		}
	}
	
	private double getValueLinePos(int i) {
		if(i<NR_OF_VALUEPOINTS) {
			return ((this.model.getValuesXPosition(i)/this.imageWidth)*this.windowWidth);
		}
		return -1;
	}
	
	private void setValueLinePos(int i, double value) {
		if(i<NR_OF_VALUEPOINTS) {
			this.model.setValuesXPosition(i, ((value/this.windowWidth)*this.imageWidth));
		}
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
	
	public double getValue(int markerLow, int markerHigh,  int valueType) {
		double lineInPixel = getLinePos(markerHigh, true) -getLinePos(markerLow, true);
		double lineInValue = getLineValue(markerHigh) - getLineValue(markerLow);
		double lineToPos = getValueLinePos(valueType) -getLinePos(markerLow, true);
		return ((lineToPos/lineInPixel)*lineInValue)+ getLineValue(markerLow);
	}
	
	protected void paintComponent(Graphics gOrig) {
        super.paintComponent(gOrig);
        Graphics2D g = (Graphics2D) gOrig.create();
        try {
            paintOverlay(g);
        } finally {
            g.dispose();
        }
    }
	
	private void paintOverlay(Graphics2D g) {
		g.setStroke (new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, null, 0.0f));
    	g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,(float)0.8f));
    	g.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	//OW
    	boolean notOffsetMarker = false;
    	paintLineSlider(g, "Start O/W", OW_LOW, (int)getLinePos(OW_LOW , notOffsetMarker) , notOffsetMarker, LIGHT_BLUE);
    	paintLineSlider(g, "End O/W", OW_HIGH, (int)getLinePos(OW_HIGH , notOffsetMarker) , notOffsetMarker, LIGHT_BLUE);
    	paintLineSlider(g, "test O/W", OW_LOW_OFFSET, (int)getLinePos(OW_LOW , !notOffsetMarker) , !notOffsetMarker, LIGHT_BLUE);
    	paintLineSlider(g, "test O/W", OW_HIGH_OFFSET, (int)getLinePos(OW_HIGH , !notOffsetMarker) , !notOffsetMarker, LIGHT_BLUE);
    	paintLine(g, "O/W", OW_VALUE, getValue(OW_LOW, OW_HIGH, OW_VALUE), LIGHT_BLUE);
    	
    	//OG
    	paintLineSlider(g, "Start O/G", OG_LOW, (int)getLinePos(OG_LOW , notOffsetMarker) , notOffsetMarker, LIGHT_RED);
    	paintLineSlider(g, "End O/G", OG_HIGH, (int)getLinePos(OG_HIGH , notOffsetMarker) , notOffsetMarker, LIGHT_RED);
    	paintLineSlider(g, "test O/W", OG_LOW_OFFSET, (int)getLinePos(OG_LOW , !notOffsetMarker) , !notOffsetMarker, LIGHT_RED);
    	paintLineSlider(g, "test O/W", OG_HIGH_OFFSET, (int)getLinePos(OG_HIGH , !notOffsetMarker) , !notOffsetMarker , LIGHT_RED);
    	paintLine(g, "O/G", OG_VALUE, getValue(OG_LOW, OG_HIGH, OG_VALUE), LIGHT_RED);
    }
	
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
	
	private void paintLine(Graphics2D g, String header, int type,double value, Color c) {
		if(isValueLineSelected(type)) {
			g.setFont(new Font("Arial", Font.BOLD, 20));
    		g.drawString(header, (int)getValueLinePos(type)-5, 75);
    		g.drawString(df.format(value), (int)getValueLinePos(type)-10, 90);
    		g.drawLine((int)getValueLinePos(type), 100, (int)getValueLinePos(type), this.getHeight()-100);
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
	public void mousePressed(MouseEvent e) {
		//hvis endring er enabled eller noe.
		for(int i = 0; i<NR_OF_MARKERS; i ++) {
			if(getLinePos(i, true)-10 < e.getX() && e.getX()<getLinePos(i, true) + 10) {
				System.out.println(i + NR_OF_MARKERS + " SELECTED");
				this.selected = i + NR_OF_MARKERS;
			}
		}

		for(int i = 0; i<NR_OF_MARKERS; i ++) {
			if(getLinePos(i, false)-10 < e.getX() && e.getX()<getLinePos(i, false) + 10) {
				this.selected = i;
			}
		}
		
		if (selected == NO_SELECTION) {
			if(e.getButton() == MouseEvent.BUTTON3){
				if (e.getClickCount() > 1) {
					setValueLineSelected(OW_VALUE, false);
					selected = NO_SELECTION;
				}
				else {
					setValueLinePos(OW_VALUE, e.getX());
					setValueLineSelected(OW_VALUE, true);
					selected = NO_SELECTION;
				}
			}
			else if(e.getButton() == MouseEvent.BUTTON1){
				if(e.getClickCount() >1) {
					setValueLineSelected(OG_VALUE, false);
					selected = NO_SELECTION;
				}
				else {
					setValueLinePos(OG_VALUE, e.getX());
					setValueLineSelected(OG_VALUE, true);
					selected = NO_SELECTION;
				}

			}
		}
		this.repaint();
		
		
	}
	public void mouseReleased(MouseEvent e) {
		if(selected != NO_SELECTION) {
			if(selected < NR_OF_MARKERS) {
				this.setLinePos(selected, e.getX(), false);
			}
			else {
				ImageDataModel.offset[selected-NR_OF_MARKERS] = getNewPercentage(selected, e.getX());
			}
			selected = NO_SELECTION;
			
		}
		this.repaint();
		
		
	}
	
	
	
	public void mouseDragged(MouseEvent e) {
		if (selected != NO_SELECTION) {
			if (selected < NR_OF_MARKERS) {
				this.setLinePos(selected, e.getX(), false);
			}
			else {
				
				ImageDataModel.offset[selected-NR_OF_MARKERS] = getNewPercentage(selected, e.getX());
			}
			
			
			this.repaint();
		}
	}
	public void mouseMoved(MouseEvent e) {
		
		
	}
	
	/**
	 * Returns a new percentage for the offset
	 * 
	 * @param selected -Offset line that is selected by user
	 * @param pos - The new position of the offset
	 * @return
	 */
	public double getNewPercentage(int selected, int pos) {
		double x1;
		double x2;
		if(selected%2 == 0) {
			x1 = getLinePos(selected-NR_OF_MARKERS, false);
			x2 = getLinePos(selected-NR_OF_MARKERS +1, false);
			//legge inn sperringer om pos er mindre enn 0 eller større enn andre siden av menisk
			return Math.abs((pos-x1)/(x1-x2));
		}
		else {
			x1 = getLinePos(selected-NR_OF_MARKERS, false);
			x2 = getLinePos(selected-NR_OF_MARKERS-1, false);
			return Math.abs((pos-x1)/(x1-x2));
			
		}	
	}	
}

