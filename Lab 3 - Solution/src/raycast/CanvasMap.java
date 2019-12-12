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
	 * list of all shapes in this application
	 */
	private List<PolyShape> shapes;

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
	 * this variable can be initialized with {@link SimpleBooleanProperty}
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
	 * create a getter that returns {@link IntegerProperty and a method that returns
	 * {@link IntegerProperty#get()}
	 * </p>
	 */
	private IntegerProperty rayCount;
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
	 * create a getter that returns {@link BooleanProperty and a method that returns
	 * {@link BooleanProperty#get()} for each BooleanProperty.
	 * </p>
	 */
	private BooleanProperty drawLightSource, drawIntersectPoint, drawShapeJoints, drawSectors, drawBounds, drawFPS;

	/**
	 * create a constructor and initialize all class variables.
	 */
	public CanvasMap() {
		shapes = new ArrayList<>(20);
		board = new Canvas();
		rayCount = new SimpleIntegerProperty(360);
		drawFPS = new SimpleBooleanProperty(false);
		drawIntersectPoint = new SimpleBooleanProperty(false);
		drawLightSource = new SimpleBooleanProperty(true);
		drawBounds = new SimpleBooleanProperty(true);
		drawShapeJoints = new SimpleBooleanProperty(false);
		drawSectors = new SimpleBooleanProperty(false);
	}

	/**
	 * create the property class variables functions here
	 */
	@Override
	public BooleanProperty drawFPSProperty() {
		return drawFPS;
	}

	@Override
	public boolean getDrawFPS() {
		return drawFPS.get();
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
	public BooleanProperty drawSectorsProperty() {
		return drawSectors;
	}

	@Override
	public boolean getDrawSectors() {
		return drawSectors.get();
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
	public IntegerProperty rayCountProperty() {
		return rayCount;
	}

	@Override
	public int getRayCount() {
		return rayCount.get();
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
		}
		animator = newAnimator;
		registerMouseEvents();
		start();
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
	public void registerMouseEvents() {
		addEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
		addEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);
//		addEventHandler( MouseEvent.MOUSE_MOVED, e->animator.mouseMoved( e));
//		addEventHandler( MouseEvent.MOUSE_MOVED, (MouseEvent e)->animator.mouseMoved( e));
//		addEventHandler( MouseEvent.MOUSE_MOVED, new EventHandler< MouseEvent>(){
//			public void handle(MouseEvent e) {
//				animator.mouseMoved( e);
//			};
//		});
	}

	/**
	 * <p>
	 * create a method called removeMouseEvents. remove the mouse events for when
	 * the mouse is {@link MouseEvent#MOUSE_MOVED} or
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
	public void removeMouseEvents() {
		removeEventHandler(MouseEvent.MOUSE_DRAGGED, animator::mouseDragged);
		removeEventHandler(MouseEvent.MOUSE_MOVED, animator::mouseMoved);
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
		animator.start();
	}

	/**
	 * create a method called stop. stop the animator. {@link AnimationTimer#stop()}
	 */
	@Override
	public void stop() {
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
	 * create a method called w. get the height of the map,
	 * {@link Canvas#getHeight()}
	 * 
	 * @return height of canvas
	 */
	@Override
	public double h() {
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

	/**
	 * get the list of all shapes.
	 * 
	 * @return list of shapes
	 */
	@Override
	public List<PolyShape> shapes() {

		return shapes;
	}

	/**
	 * load a set of sample {@link PolyShapes}
	 */
	@Override
	public void addSampleShapes() {
		PolyShape ps = new PolyShape().setPoints(0, 0, 1000, 0, 1000, 1000, 0, 1000);
		ps.getDrawable().setFill(Color.BLACK);
		shapes.add(ps);

		PolyShape s = new PolyShape().setPoints(100, 100, 200, 100, 200, 200, 100, 200);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);

		s = new PolyShape().setPoints(100, 100, 200, 100, 200, 200, 100, 200);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().randomize(350, 150, 100, 3, 10);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().setPoints(600, 100, 500, 100, 450, 200, 550, 200);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().randomize(550, 350, 100, 3, 10);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().setPoints(600, 600, 500, 600, 550, 500);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().randomize(350, 550, 100, 3, 10);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().setPoints(100, 600, 200, 600, 250, 500, 150, 500);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);
		s = new PolyShape().randomize(150, 350, 100, 3, 10);
		s.getDrawable().setStroke(Color.BLACK).setWidth(1.5);
		shapes.add(s);

	}
}
