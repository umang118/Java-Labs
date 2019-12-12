/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */
package raycast.entity.geometry;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.Entity;
import raycast.entity.proprty.Sprite;

public class PolyShape implements Entity {

	private int pointCount;
	private double[][] points;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;
	private RectangleBounds recBounds;

	Sprite sprite;

	public PolyShape() {
		// TODO Auto-generated constructor stub

		sprite = new Sprite() {
			@Override
			public void draw(GraphicsContext gc) {
				gc.setLineWidth(getWidth());
				if (gc.getStroke() != null) {
					gc.setStroke(getStroke());
					gc.strokePolygon(points[0], points[1], pointCount);
				}
				if (gc.getFill() == null) {
					gc.setFill(getFill());
					gc.fillPolygon(points[0], points[1], pointCount);
				}

			}

		};

	}

	public PolyShape randomize(double centerX, double centerY, double size, int minPoints, int maxPoints) {

		return this;

	}

	public PolyShape setPoints(double... nums) {

		minX = maxX = nums[0];
		minY = maxY = nums[1];

		// initial points and points count
		pointCount = nums.length / 2;
		points = new double[2][pointCount];

		for (int i = 0, j = 0; i < nums.length; i += 2, j++) {

			updateMinMax(nums[i], nums[i + 1]);

			points[0][j] = nums[i];
			points[1][j] = nums[i + 1];
		}

		recBounds = new RectangleBounds(minX, minY, maxX - minX, maxY - minY);

		return this;

	}

	public void updateMinMax(double x, double y) {

		if (x < minX) {
			minX = x;
		} else if (x > maxX) {
			maxX = x;
		}

		if (y < minY) {
			minY = y;
		} else if (y > maxY) {
			maxY = y;
		}

	}

	public int pointCount() {
		return pointCount;
	}

	public double[][] points() {
		return points;
	}

	public double pX(int index) {

		return points[0][index];
	}

	public double pY(int index) {

		return points[1][index];
	}

	@Override
	public boolean isDrawable() {
		return true;
	}

	@Override
	public Sprite getDrawable() {

		return sprite;

	}

	public void drawCorner(GraphicsContext gc) {
		gc.setFill(Color.RED);

		for (int i = 0; i < pointCount; i++) {
			gc.fillText(Integer.toString(i), points[0][i] - 5, points[1][i] - 5);
			gc.fillOval(points[0][i] - 5, points[1][i] - 5, 10, 10);
		}

	}

	public RectangleBounds getBounds() {

		return recBounds;

	}

}
