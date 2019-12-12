package raycast.animator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import raycast.entity.geometry.PolyShape;

public class StaticShapes extends AbstractAnimator{
	
	private static final Color BACKGROUND = Color.BISQUE;

	@Override
	public void handle( long now, GraphicsContext gc){
		clearAndFill( gc, BACKGROUND);
		for( PolyShape s : map.shapes()){
			s.getDrawable().draw( gc);
		}
	}

	@Override
	public String toString(){
		return "Single Source Mouse Rays";
	}
}
