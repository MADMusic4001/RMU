package com.madinnovations.rmu.view.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

/**
 * Utility methods for retrieving resource items.
 */
public interface ResourceUtils {

	/**
	 * Gets a resource string for a given id
	 *
	 * @param id  the id of the resource string
	 * @return  the String for the given id or null if not found.
	 */
	public abstract String getString(@NonNull Object id);

	/**
	 * Returns a localized formatted string from the application's package's
	 * default string table, substituting the format arguments as defined in
	 * {@link java.util.Formatter} and {@link java.lang.String#format}.
	 *
	 * @param resId Resource id for the format string
	 * @param formatArgs The format arguments that will be used for
	 *                   substitution.
	 * @return The string data associated with the resource, formatted and
	 *         stripped of styled text information.
	 */
	public abstract String getString(@NonNull Object resId, Object... formatArgs);

	/**
	 * Gets the bitmap with the given id.
	 *
	 * @param id  the id of the Bitmap to retrieve
	 * @return  the Bitmap for the given id or null if not found.
	 */
	public abstract Bitmap getBitmap(@NonNull Object id);
}
