package com.jaskirat.event.location;

/**
 * This interface is used when the user location has been fetched by the api.
 */

import android.location.Location;

public interface LocationResult {
	
	/**
	 * This method is called when the user's location has been fetched by the Location api.
	 * 
	 * @param location - the user's location object
	 */
	
	public abstract void gotLocation(Location location);
}
