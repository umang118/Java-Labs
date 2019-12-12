
/*
 * @author: Umangkumar Patel
 * 
 * 
 * */

package raycast.animator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.geometry.PolyShape;

public class MultiRayAnimator extends AbstractAnimator {

	private double[] intersectPoint;

	public MultiRayAnimator() {
		// TODO Auto-generated constructor stub
		intersectPoint = new double[4];

	}

	public void drawLine(GraphicsContext gc, Color color, double sx, double sy, double ex, double ey) {
		gc.setLineWidth(1);
		gc.setStroke(color);
		gc.setFill(Color.MAGENTA);
		gc.strokeLine(sx, sy, ex, ey);
		if (map.getDrawIntersectPoint()) {

			gc.fillOval(ex - 5, ey - 5, 10, 10);
		}
	}

	@Override
	void handle(long now, GraphicsContext gc) {
		// TODO Auto-generated method stub
		clearAndFill(gc, Color.BLACK);
		for (PolyShape s : map.shapes()) {
			s.getDrawable().draw(gc);
		}
		drawRays(gc, mouse.x(), mouse.y(), Color.YELLOW);

	}

	private void drawRays(GraphicsContext gc, double startX, double startY, Color color) {
		// TODO Auto-generated method stub
		double endX;
		double endY;
		double rayIncrementer = 360d / map.getRayCount();

		endX = Math.cos(rayIncrementer);
		endY = Math.sin(rayIncrementer);

		for (double rayAngel = 0d; rayAngel < 360; rayAngel += rayIncrementer) {

			endX = Math.cos(Math.toRadians(rayAngel));
			endY = Math.sin(Math.toRadians(rayAngel));

			for (PolyShape s : map.shapes()) {

				for (int i = 0, j = s.pointCount() - 1; i < s.pointCount(); i++, j = i - 1) {

					if (getIntersection(startX, startY, startX + endX, startY + endY, s.pX(i), s.pY(i), s.pX(j),
							s.pY(j))) {
						if (intersectPoint[2] > intersectResult[2]) {
							System.arraycopy(intersectResult, 0, intersectPoint, 0, intersectPoint.length);
						}

					}

				}

			}
			drawLine(gc, color, startX, startY, intersectPoint[0], intersectPoint[1]);
			intersectPoint[2] = Double.MAX_VALUE;
		}

	}

	@Override
	public String toString() {
		return "MultiRayAnimator";
	}

}
