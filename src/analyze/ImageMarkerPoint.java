package analyze;

public class ImageMarkerPoint {
	
	
	private double x;
	private double y;
	private double area;
	
	public ImageMarkerPoint(double x, double y, double area) {
		this.x = x;
		this.y = y;
		this.area = area;
	}
	
	public double getY() {
		return y;
	}
	public double getArea() {
		return area;
	}
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public String toString() {
		return "ImageMarkerPoint [x=" + x + ", y=" + y + ", area=" + area + "]";
	}
	
}
