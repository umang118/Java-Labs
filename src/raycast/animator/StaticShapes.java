/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */
package raycast.animator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.geometry.PolyShape;

public class StaticShapes extends AbstractAnimator {

	private static final Color BACKGROUND = Color.CORAL;

	@Override
	protected void handle(long now, GraphicsContext gc) {
		// TODO Auto-generated method stub

		clearAndFill(gc, BACKGROUND);

		for (PolyShape i : map.shapes()) {
			i.getDrawable().draw(gc);
		}

	}

	@Override
	public String toString() {
		return "Static Shapes";
	}

}
