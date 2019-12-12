/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */
package raycast.entity.proprty;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public interface Drawable<T> {

	public T setFill(Paint paint);

	public T setStroke(Paint paint);

	public T setWidth(double width);

	public void draw(GraphicsContext gc);

	public Paint getFill();

	public Paint getStroke();

	public double getWidth();

}
