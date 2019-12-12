/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */

package raycast.entity.proprty;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class Sprite implements Drawable<Object> {

	private double strokeWidth;
	private Paint fillColor;
	private Paint strokeColor;

	@Override
	public Sprite setFill(Paint color) {
		this.fillColor = color;
		return this;
	}

	@Override
	public Sprite setStroke(Paint color) {
		this.strokeColor = color;
		return this;

	}

	@Override
	public Sprite setWidth(double width) {

		this.strokeWidth = width;
		return this;

	}

	@Override
	public void draw(GraphicsContext gc) {

		gc.setStroke(strokeColor);
		gc.setFill(fillColor);
		gc.setLineWidth(strokeWidth);
	}

	@Override
	public double getWidth() {
		return strokeWidth;
	}

	@Override
	public Paint getFill() {
		return fillColor;
	}

	@Override
	public Paint getStroke() {
		return strokeColor;
	}

}
