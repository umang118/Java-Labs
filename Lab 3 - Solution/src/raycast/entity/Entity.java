package raycast.entity;

import raycast.entity.property.Drawable;

public interface Entity{

	boolean isDrawable();

	Drawable< ?> getDrawable();
}
