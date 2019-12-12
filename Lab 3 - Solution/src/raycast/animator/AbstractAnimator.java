package raycast.animator;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import raycast.CanvasMap;
import raycast.entity.FpsCounter;
import raycast.entity.geometry.PolyShape;
import utility.Point;

/**
 * this class must extend {@link AnimationTimer}. job of this class is to hold
 * common functionality among animators.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public abstract class AbstractAnimator extends AnimationTimer {

	/**
	 * create a private fps counter object to keep track of fps
	 */
	private FpsCounter fps;

	/**
	 * create a protected class variable of type {@link CanvasMap} and name it map.
	 */
	protected CanvasMap map;

	/**
	 * create a protected class variable of type {@link Point} and name it mouse.
	 */
	protected Point mouse;

	/**
	 * this array has in order x, y, scalar of intersect point with ray and scalar
	 * of intersect with line segment.
	 */
	protected double[] intersectResult;

	/**
	 * create a protected constructor and initialize the
	 * {@link AbstractAnimator#mouse} variable
	 */
	public AbstractAnimator() {
		mouse = new Point();
		intersectResult = new double[4];
		fps = new FpsCounter(10, 25);
		fps.getDrawable().setFill(Color.BLACK).setStroke(Color.WHITE).setWidth(1);
	}

	/**
	 * create a setter called setCanvas to inject (set) the {@link CanvasMap}
	 * 
	 * @param map - {@link CanvasMap} object
	 */
	public void setCanvas(CanvasMap c) {
		this.map = c;
	}

	/**
	 * create a method called mouseDragged that is called every time the position of
	 * mouse changes.
	 * 
	 * @param e - {@link MouseEvent} object that hold the details of the mouse. use
	 *          {@link MouseEvent#getX} and {@link MouseEvent#getY}
	 */
	public void mouseDragged(MouseEvent e) {
		mouse.x(e.getX());
		mouse.y(e.getY());
	}

	/**
	 * create a method called mouseMoved that is called every time the position of
	 * mouse changes.
	 * 
	 * @param e - {@link MouseEvent} object that hold the details of the mouse. use
	 *          {@link MouseEvent#getX} and {@link MouseEvent#getY}
	 */
	public void mouseMoved(MouseEvent e) {
		mouse.x(e.getX());
		mouse.y(e.getY());
	}

	public void clearAndFill(GraphicsContext gc, Color background) {
		gc.setFill(background);
		gc.clearRect(0, 0, map.w(), map.h());
		gc.fillRect(0, 0, map.w(), map.h());
	}

	/**
	 * <p>
	 * create a method called handle that is inherited from
	 * {@link AnimationTimer#handle(long)}. this method is called by JavaFX
	 * application, it should not be called directly.
	 * </p>
	 * <p>
	 * inside of this method call the abstract handle method
	 * {@link AbstractAnimator#handle(GraphicsContext, long)}.
	 * {@link GraphicsContext} can be retrieved from {@link CanvasMap#gc()}
	 * </p>
	 * 
	 * @param now - current time in nanoseconds, represents the time that this
	 *            function is called.
	 */
	@Override
	public void handle(long now) {
		GraphicsContext gc = map.gc();
		if (map.getDrawFPS())
			fps.calculateFPS(now);

		handle(now, gc);
		if (map.getDrawLightSource()) {
			gc.setFill(Color.MAGENTA);
			gc.fillOval(mouse.x() - 5, mouse.y() - 5, 10, 10);
		}
		if (map.getDrawShapeJoints() || map.getDrawBounds())
			for (PolyShape s : map.shapes()) {
				if (map.getDrawBounds())
					s.getBounds().draw(gc);
				if (map.getDrawShapeJoints())
					s.drawCorners(gc);
			}
		if (map.getDrawFPS())
			fps.getDrawable().draw(gc);
	}

	/**
	 * create a protected abstract method called handle, this method to be
	 * overridden by subclasses.
	 * 
	 * @param gc  - {@link GraphicsContext} object.
	 * @param now - current time in nanoseconds, represents the time that this
	 *            function is called.
	 */
	abstract void handle(long now, GraphicsContext gc);

	public boolean getIntersection(double rsx, double rsy, double rex, double rey, double ssx, double ssy, double sex,
			double sey) {
		// given 2 line segments as vectors their intersect will q + tr or p + us where
		// q and p are the starting point in from of (x, y),
		// r and s are the distance of end point to start point in form of ( x2-x1,
		// y2-y1),
		// t and u are scaler values belonging to real numbers, such as 0.5, 1, -1.
		// by finding t and u the intersect can be found.
		// t and u can be found by equaling q + tr = p + us
		// this function can be refactored as below, look at the link in documentation
		// for more details.
		// x is cross product
		// t = (q - p) x s / (r x s)
		// u = (q - p) x r / (r x s)
		// (q - p) x s = ((qx-px)sy-sx(qy-py))
		// (q - p) x r = ((qx-px)ry-rx(qy-py))
		// (r x s) = (rxsy-sxry)

		double qpx = rsx - ssx;
		double qpy = rsy - ssy;

		double rx = rex - rsx;
		double ry = rey - rsy;
		double sx = sex - ssx;
		double sy = sey - ssy;

		double qps = qpx * sy - sx * qpy;
		double qpr = qpx * ry - rx * qpy;

		double rs = rx * sy - sx * ry;

		double rayScaler = -qps / rs;
		double segmentScaler = -qpr / rs;

		intersectResult[0] = rsx + rx * rayScaler;
		intersectResult[1] = rsy + ry * rayScaler;
		intersectResult[2] = rayScaler;
		intersectResult[3] = segmentScaler;

		return rs != 0 && rayScaler >= 0 && segmentScaler >= 0 && segmentScaler <= 1;
	}

}
