package com.moritzbergemann.myapplication.mapactions;

import com.moritzbergemann.myapplication.model.MapException;

/**
 * Interface representing action to take on the map screen depending on what has been selected by
 *  the user. State pattern implementation.
 */
public interface SelectableMapAction {
    /**
     * Performs this action for a given map element
     * @param row row of map element to perform action on
     * @param col column of map element to perform action on
     * @throws MapException
     */
    public void performAction(int row, int col) throws MapException;
}
