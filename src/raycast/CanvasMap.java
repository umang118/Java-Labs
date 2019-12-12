/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */
package raycast;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import raycast.animator.AbstractAnimator;
import raycast.animator.TextAnimator;
import raycast.entity.geometry.PolyShape;

/**
 * this class represents the drawing area. it is backed by {@link Canvas} class.
 * this class itself does not handle any of the drawing. this task is
 * accomplished by the {@link AnimationTimer}.
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 13, 2019
 */
public class CanvasMap implements CanvasMapInterface {

	/**
	 * <p>
	 * create a {@link Canvas} object call board. it provides the tools to draw in
	 * JavaFX. this is also a {@link Node} which means can be added to our JavaFX
	 * application. the object needed to draw on a {@link Canvas} is
	 * {@link GraphicsContext} which is retrieved using
	 * {@link Canvas#getGraphicsContext2D()} method.
	 * </p>
	 */
	private Canvas board;

	/**
	 * create a {@link AbstractAnimator} called animator. {@link AnimationTimer}
	 * provides most common functionally needed to draw animations of ray casting.
	 */
	private AbstractAnimator animator;

	/**
	 * <p>
	 * create an {@link IntegerProperty} called rayCount to keep track of ray count
	 * changes.<br>
	 * this variable can be initialized with {@link SimpleIntegerProperty}
	 * </p>
	 * 
	 * <pre>
	 * IntegerProperty i1 = new SimpleIntegerProperty(1);
	 * IntegerProperty i2 = new SimpleIntegerProperty();
	 * i1.bind(i2);
	 * i2.set(100);
	 * System.out.println(i1.get()); // prints 100
	 * </pre>
	 * <p>
	 * create a getter that returns {@link IntegerProperty} and a method that
	 * returns {@link IntegerProperty#get()}
	 * </p>
	 * 
	 * @return
	 */
	private IntegerProperty RayCount;

	/**
	 * <p>
	 * create a set of {@link BooleanProperty}s to track some drawing options.<br>
	 * create: drawLightSource, drawIntersectPoint, drawShapeJoints, drawSectors,
	 * drawBounds, drawFPS<br>
	 * these variables can be initialized with {@link SimpleBooleanProperty}
	 * </p>
	 * 
	 * <pre>
	 * BooleanProperty b1 = new SimpleBooleanProperty(false);
	 * BooleanProperty b2 = new SimpleBooleanProperty();
	 * b1.bind(b2);
	 * b2.set(true);
	 * System.out.println(b1.get()); // prints true
	 * </pre>
	 * <p>
	 * create a getter that returns {@link BooleanProperty} and a method that
	 * returns {@link BooleanProperty#get()} for each BooleanProperty.
	 * </p>
	 */

	private List<PolyShape> shapes;

	private BooleanProperty drawLightSource;
	private BooleanProperty drawIntersectPoint;
	private BooleanProperty drawShapeJoints;
	private BooleanProperty drawSectors;
	private BooleanProperty drawBounds;
	private BooleanProperty drawFPS;

	/**
	 * create a constructor and initialize all class variables.
	 */

	public CanvasMap() {

		board = new Canvas();
		RayCount = new SimpleIntegerProperty();
		drawLightSource = new SimpleBooleanProperty();
		drawIntersectPoint = new SimpleBooleanProperty();
		drawShapeJoints = new SimpleBooleanProperty();
		drawSectors = new SimpleBooleanProperty();
		drawBounds = new SimpleBooleanProperty();
		drawFPS = new SimpleBooleanProperty();
		animator = new TextAnimator();
		shapes = new ArrayList<>(20);

	}

	/**
	 * create the property class variables functions here
	 */

	@Override
	public IntegerProperty rayCountProperty() {
		return RayCount;

	}

	@Override
	public int getRayCount() {
		return RayCount.get();
	}

	@Override
	public BooleanProperty drawLightSourceProperty() {
		return drawLightSource;
	}

	@Override
	public boolean getDrawLightSource() {
		return drawLightSource.get();
	}

	@Override
	public BooleanProperty drawIntersectPointProperty() {
		return drawIntersectPoint;
	}

	@Override
	public boolean getDrawIntersectPoint() {
		return drawIntersectPoint.get();
	}

	@Override
	public BooleanProperty drawShapeJointsProperty() {
		return drawShapeJoints;
	}

	@Override
	public boolean getDrawShapeJoints() {
		return drawShapeJoints.get();
	}

	@Override
	public BooleanProperty drawSectorsProperty() {
		return drawSectors;
	}

	@Override
	public boolean getDrawSectors() {
		return drawSectors.get();
	}

	@Override
	public BooleanProperty drawBoundsProperty() {
		return drawBounds;
	}

	@Override
	public boolean getDrawBounds() {
		return drawBounds.get();
	}

	@Override
	public BooleanProperty drawFPSProperty() {
		return drawFPS;
	}

	@Override
	public boolean getDrawFPS() {
		return drawFPS.get();
	}

	@Override
	public List<PolyShape> shapes() {
		return shapes;
	}

	/**
	 * create a method called setAnimator. set an {@link AbstractAnimator}. if an
	 * animator exists {@link CanvasMap#stop()} it and call
	 * {@link CanvasMap#removeMouseEvents()}. then set the new animator and call
	 * {@link CanvasMap#start()} and {@link CanvasMap#registerMouseEvents()}.
	 * 
	 * @param newAnimator - new {@link AbstractAnimator} object
	 * @return the current instance of this object
	 */

	@Override
	public CanvasMap setAnimator(AbstractAnimator newAnimator) {

		if (animator != null) {
			stop();

			removeMouseEvents();

			animator = newAnimator;
			start();
			registerMouseEvents();

		}
		return this;

	}

	/**
	 * <p>
	 * create a method called registerMouseEvents. register the mouse events for
	 * when the mouse is {@link MouseEvent#MOUSE_MOVED} or
	 * {@link MouseEvent#MOUSE_DRAGGED}.<br>
	 * call {@link CanvasMap#addEventHandler} twice and pass to it
	 * {@link MouseEvent#MOUSE_DRAGGED}, {@link animator#mouseDragged} and
	 * {@link MouseEvent#MOUSE_MOVED}, {@link animator#mouseMoved}.
	 * </p>
	 * <p>
	 * a method can be passed directly as an argument if the method signature
	 * matches the functional interface. in this example you will pass the animator
	 * method using object::method syntax.
	 * </p>
	 */

	private void registerMouseEvents() {
		// TODO Auto-generated method stub

		board.addEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);
		board.addEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);

	}

	/**
	 * <p>
	 * create a method called removeMouseEvents. remove the mouse events for when
	 * the mouse is {@link MouseEvent#MOUSE_MOVED} or
	 * {@link MouseEvent#MOUSE_DRAGGED}.<br>
	 * call {@link CanvasMap#removeEventHandler} twice and pass to it
	 * {@link MouseEvent#MOUSE_DRAGGED}, {@link animator#mouseDragged} and
	 * {@link MouseEvent#MOUSE_MOVED}, {@link animator#mouseMoved}.
	 * </p>
	 * <p>
	 * a method can be passed directly as an argument if the method signature
	 * matches the functional interface. in this example you will pass the animator
	 * method using object::method syntax.
	 * </p>
	 */

	private void removeMouseEvents() {
		// TODO Auto-generated method stub

		board.addEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
		board.addEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);

	}

	/**
	 * <p>
	 * register the given {@link EventType} and {@link EventHandler}
	 * </p>
	 * 
	 * @param event   - an event such as {@link MouseEvent#MOUSE_MOVED}.
	 * @param handler - a lambda to be used when registered event is triggered.
	 */
	public <E extends Event> void addEventHandler(EventType<E> event, EventHandler<E> handler) {
		board.addEventHandler(event, handler);
	}

	/**
	 * <p>
	 * remove the given {@link EventType} registered with {@link EventHandler}
	 * </p>
	 * 
	 * @param event   - an event such as {@link MouseEvent#MOUSE_MOVED}.
	 * @param handler - a lambda to be used when registered event is triggered.
	 */
	public <E extends Event> void removeEventHandler(EventType<E> event, EventHandler<E> handler) {
		board.removeEventHandler(event, handler);
	}

	/**
	 * create a method called start. start the animator.
	 * {@link AnimationTimer#start()}
	 */

	@Override
	public void start() {
		// TODO Auto-generated method stub
		animator.start();

	}

	/**
	 * create a method called stop. stop the animator. {@link AnimationTimer#stop()}
	 */

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		animator.stop();
	}

	/**
	 * create a method called getCanvas. get the JavaFX {@link Canvas} node
	 * 
	 * @return {@link Canvas} node
	 */

	@Override
	public Canvas getCanvas() {

		return board;
	}

	/**
	 * create a method called gc. get the {@link GraphicsContext} of {@link Canvas}
	 * that allows direct drawing.
	 * 
	 * @return {@link GraphicsContext} of {@link Canvas}
	 */
	@Override
	public GraphicsContext gc() {

		return board.getGraphicsContext2D();

	}

	/**
	 * create a method called h. get the height of the map,
	 * {@link Canvas#getHeight()}
	 * 
	 * @return height of canvas
	 */
	@Override
	public double h() {
		// TODO Auto-generated method stub

		return board.getHeight();
	}

	/**
	 * create a method called w. get the width of the map, {@link Canvas#getWidth()}
	 * 
	 * @return width of canvas
	 */

	@Override
	public double w() {

		return board.getWidth();
	}

	@Override
	public void addSampleShapes() {
		// TODO Auto-generated method stub

		PolyShape p1 = new PolyShape().setPoints(100, 120, 200, 150, 225, 200, 325, 300);
		p1.getDrawable().setFill(Color.VIOLET);
		p1.getDrawable().setStroke(Color.BLACK);
		p1.getDrawable().setWidth(2);

		shapes.add(p1);

		PolyShape p2 = new PolyShape().setPoints(350, 320, 550, 490, 450, 500, 100, 250);
		p1.getDrawable().setFill(Color.BROWN);

		p1.getDrawable().setStroke(Color.RED);
		p1.getDrawable().setWidth(2);
		shapes.add(p2);

		PolyShape p3 = new PolyShape().setPoints(283, 30, 533, 32, 619, 180, 340, 209);
		p1.getDrawable().setFill(Color.GREEN);
		p1.getDrawable().setWidth(2);
		p1.getDrawable().setStroke(Color.BLUE);

		shapes.add(p3);

	}

}
