/**
 * @author Umang
 * @student_No: 040918355
 * @email: pate0585@algonquinlive.com
 * */
package raycast.entity;

import raycast.entity.proprty.Drawable;

public interface Entity {

	public Drawable<?> getDrawable();

	public boolean isDrawable();

}
