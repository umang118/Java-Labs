/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */
package raycast.entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import raycast.entity.proprty.Drawable;
import raycast.entity.proprty.Sprite;
import utility.Point;

public class FpsCounter implements Entity {

	public static final double ONE_SECOND = 1000000000L;
	public static final double HALF_SECOND = ONE_SECOND / 2F;

	private Font fpsFont;
	private String fpsDisplay;
	private int frameCount;
	private double lastTime;
	private Point pos;
	private Sprite sprite;

	public FpsCounter(double x, double y) {
		// TODO Auto-generated constructor stub
		setPos(x, y);
		setFont(Font.font(Font.getDefault().getFamily(), FontWeight.BLACK, 24));
		sprite = new Sprite() {

			@Override
			public void draw(GraphicsContext gc) {

				Font temp = gc.getFont();
				gc.setFont(fpsFont);
				gc.setFill(getFill());
				gc.fillText(fpsDisplay, x, y);
				gc.setStroke(getStroke());
				gc.setLineWidth(getWidth());
				gc.strokeText(fpsDisplay, x, y);
				gc.setFont(temp);

			}

		};
	}

	public void calculateFPS(long now) {

		if (now - lastTime > HALF_SECOND) {
			fpsDisplay = String.valueOf(frameCount * 2);
			frameCount = 0;
			lastTime = now;

		}
		frameCount++;

	}

	public FpsCounter setFont(Font font) {
		this.fpsFont = font;
		return this;

	}

	public FpsCounter setPos(double x, double y) {

		pos = new Point(x, y);
		return this;

	}

	@Override
	public Drawable<?> getDrawable() {
		// TODO Auto-generated method stub
		return sprite;
	}

	@Override
	public boolean isDrawable() {
		// TODO Auto-generated method stub
		return true;
	}

}
