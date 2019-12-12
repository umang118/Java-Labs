package raycast;

import java.util.List;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import raycast.animator.AbstractAnimator;
import raycast.animator.MultiRayAnimator;
import raycast.animator.StaticShapes;
import raycast.animator.TextAnimator;

/**
 * this is the start of JavaFX application. this class must extend
 * {@link Application}.
 * 
 * @see <a href="https://stackoverflow.com/a/565282/764951">Two line segments
 *      intersect</a>
 * @see <a href="https://ncase.me/sight-and-light/">Sight and Light, simple ray
 *      cast tutorial</a>
 * 
 * @author Shahriar (Shawn) Emami
 * @version Jan 7, 2019
 */
public class RayCast extends Application {

	/**
	 * size of the scene
	 */
	private double width = 700, height = 700;
	/**
	 * title of application
	 */
	private String title = "RayCast";
	/**
	 * background color of application
	 */
	private Color background = Color.LIGHTPINK;
	/**
	 * {@link BorderPane} is a layout manager that manages all nodes in 5 areas as
	 * below:
	 * 
	 * <pre>
	 * -----------------------
	 * |        top          |
	 * -----------------------
	 * |    |          |     |
	 * |left|  center  |right|
	 * |    |          |     |
	 * -----------------------
	 * |       bottom        |
	 * -----------------------
	 * </pre>
	 * 
	 * this object is passed to {@link Scene} object in {@link RayCast#start(Stage)}
	 * method.
	 */
	private BorderPane root;
	/**
	 * this object represents the canvas (drawing area)
	 */
	private CanvasMap board;
	/**
	 * this is a ChoiceBox that holds all different animators available
	 */
	private ChoiceBox<AbstractAnimator> animatorsBox;
	/**
	 * <p>
	 * a list of all animators available.<br>
	 * {@link ObservableList} is a wrapper of a normal {@link List} data structure.
	 * the difference being {@link ObservableList} is capable of notifying any
	 * observer that the list has been changed if elements have been added, removed
	 * or the list is changed in any manner. the best way to initialize any
	 * ObservableList is to use the utility class {@link FXCollections}.
	 * </p>
	 * 
	 * <pre>
	 * ObservableList<Integer> nums = FXCollections.observableArrayList();
	 * nums.addListener((Change<? extends Integer> c) -> {
	 * 	while (c.next()) {
	 * 		List<? extends Integer> added = c.getAddedSubList();
	 * 		System.out.println(added);
	 * 		List<? extends Integer> removed = c.getRemoved();
	 * 		System.out.println(removed);
	 * 	}
	 * });
	 * nums.addAll(1, 2, 3, 4, 5, 6, 7, 8, 9);
	 * nums.removeAll(1, 3, 5, 7, 9);
	 * </pre>
	 * <p>
	 * every time values are added or removed they will be printed.<br>
	 * however, in this application we are not using this feature of list.
	 * </p>
	 */
	private ObservableList<AbstractAnimator> animators;

	/**
	 * this method is called at the very beginning of the JavaFX application and can
	 * be used to initialize all components in the application. however,
	 * {@link Scene} and {@link Stage} must not be created in this method. this
	 * method does not run JavaFX thread.
	 */
	@Override
	public void init() throws Exception {
		animators = FXCollections.observableArrayList(new TextAnimator(), new StaticShapes(), new MultiRayAnimator());
		board = new CanvasMap();

		ToolBar statusBar = createStatusBar();
		ToolBar optionsBar = createOptionsBar();

		root = new BorderPane();
		root.setTop(optionsBar);
		root.setCenter(board.getCanvas());
		root.setBottom(statusBar);

		board.getCanvas().widthProperty().bind(root.widthProperty());
		board.getCanvas().heightProperty()
				.bind(root.heightProperty().subtract(statusBar.heightProperty()).subtract(optionsBar.heightProperty()));

		animators.forEach(a -> a.setCanvas(board));
		board.addSampleShapes();
	}

	/**
	 * <p>
	 * this method is called when JavaFX application is started and it is running on
	 * JavaFX thread. this method must at least create {@link Scene} and finish
	 * customizing {@link Stage}. these two objects must be on JavaFX thread when
	 * created.
	 * </p>
	 * <p>
	 * {@link Stage} represents the frame of your application, such as minimize,
	 * maximize and close buttons.<br>
	 * {@link Scene} represents the holder of all your JavaFX {@link Node}s.<br>
	 * {@link Node} is the super class of every javaFX class.
	 * </p>
	 * 
	 * @param primaryStage - primary stage of your application that will be rendered
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// scene holds all JavaFX components that need to be displayed in Stage
		Scene scene = new Scene(root, width, height, background);
		primaryStage.setScene(scene);
		primaryStage.setTitle(title);
		// when escape key is pressed close the application
		primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ESCAPE == event.getCode()) {
				primaryStage.hide();
			}
		});
		// display the JavaFX application
		primaryStage.show();
		// select first index of animatorsBox as start,
		// this will also sets the new animator as the lambda we setup will be triggered
		animatorsBox.getSelectionModel().select(2);
	}

	/**
	 * this method is called at the very end when the application is about to exit.
	 * this method is used to stop or release any resources used during the
	 * application.
	 */
	@Override
	public void stop() throws Exception {
		board.stop();
	}

	/**
	 * create a {@link ToolBar} that represent the menu bar at the top of the
	 * application.
	 * 
	 * @return customized {@link ToolBar}
	 */
	public ToolBar createOptionsBar() {
		Button startButton = createButton("Start", a -> board.start());

		Button stopButton = createButton("Stop", a -> board.stop());

		Pane menuBarFiller1 = new Pane();
		Pane menuBarFiller2 = new Pane();
		HBox.setHgrow(menuBarFiller1, Priority.ALWAYS);
		HBox.setHgrow(menuBarFiller2, Priority.ALWAYS);

		Spinner<Integer> rayCount = new Spinner<>(0, Integer.MAX_VALUE, 360 * 5);
		rayCount.setEditable(true);
		rayCount.setMaxWidth(100);
		board.rayCountProperty().bind(rayCount.valueProperty());

		MenuButton options = new MenuButton("Options", null, createCheckMenuItem("FPS", true, board.drawFPSProperty()),
				createCheckMenuItem("Intersects", false, board.drawIntersectPointProperty()),
				createCheckMenuItem("Lights", false, board.drawLightSourceProperty()),
				createCheckMenuItem("Joints", false, board.drawShapeJointsProperty()),
				createCheckMenuItem("Bounds", false, board.drawBoundsProperty()),
				createCheckMenuItem("Sectors", false, board.drawSectorsProperty()));

		animatorsBox = new ChoiceBox<>(animators);
		animatorsBox.getSelectionModel().selectedItemProperty().addListener((v, o, n) -> board.setAnimator(n));

		return new ToolBar(startButton, stopButton, menuBarFiller1, rayCount, options, menuBarFiller2,
				new Label("Animators "), animatorsBox);
	}

	/**
	 * create a {@link ToolBar} that will represent the status bar of the
	 * application.
	 * 
	 * @return customized {@link ToolBar}
	 */
	public ToolBar createStatusBar() {
		Label mouseCoordLabel = new Label("(0,0)");
		Label dragCoordLabel = new Label("(0,0)");

		board.addEventHandler(MouseEvent.MOUSE_MOVED,
				e -> mouseCoordLabel.setText("(" + e.getX() + "," + e.getY() + ")"));
		board.addEventHandler(MouseEvent.MOUSE_DRAGGED,
				e -> dragCoordLabel.setText("(" + e.getX() + "," + e.getY() + ")"));

		return new ToolBar(new Label("Mouse: "), mouseCoordLabel, new Label("Drag: "), dragCoordLabel);
	}

	/**
	 * <p>
	 * create a {@link CheckMenuItem} with given text, initial state and
	 * {@link BooleanProperty} binding. {@link BooleanProperty} is a special class
	 * that can be binded to another {@link BooleanProperty}. this means every time
	 * bindee changes the other {@link BooleanProperty} changes as well. for
	 * example:
	 * </p>
	 * 
	 * <pre>
	 * BooleanProperty b1 = new SimpleBooleanProperty(false);
	 * BooleanProperty b2 = new SimpleBooleanProperty();
	 * b1.bind(b2);
	 * b2.set(true);
	 * System.out.println(b1.get()); // prints true
	 * IntegerProperty i1 = new SimpleIntegerProperty(1);
	 * IntegerProperty i2 = new SimpleIntegerProperty();
	 * i1.bind(i2);
	 * i2.set(100);
	 * System.out.println(i1.get()); // prints 100
	 * </pre>
	 * 
	 * <p>
	 * if p2 changes p1 changes as well. this relationship is NOT bidirectional.
	 * </p>
	 * 
	 * @param text       - String to be displayed
	 * @param isSelected - initial state, true id selected
	 * @param binding    - {@link BooleanProperty} that will be notified if state of
	 *                   this {@link CheckMenuItem} is changed
	 * @return customized {@link CheckMenuItem}
	 */
	public CheckMenuItem createCheckMenuItem(String text, boolean isSelected, BooleanProperty binding) {
		CheckMenuItem check = new CheckMenuItem(text);
		binding.bind(check.selectedProperty());
		check.setSelected(isSelected);
		return check;
	}

	/**
	 * create a {@link Button} with given text and lambda for when it is clicked
	 * 
	 * @param text   - String to be displayed
	 * @param action - lambda for when the button is clicked, the lambda will take
	 *               one argument of type ActionEvent
	 * @return customized {@link Button}
	 */
	public Button createButton(String text, EventHandler<ActionEvent> action) {
		Button button = new Button(text);
		button.setOnAction(action);
		return button;
	}

	/**
	 * main starting point of the application
	 * 
	 * @param args - arguments provided through command line, if any
	 */
	public static void main(String[] args) {
		// launch( args); method starts the javaFX application.
		// some IDEs are capable of starting JavaFX without this method.
		launch(args);
	}
}
