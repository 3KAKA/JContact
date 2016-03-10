// @author Bhavya Mehta
package com.soubw.juitls;

// Gives index bar view touched Y axis value, position of section and preview text value to list view 
public interface JIndexBarFilter {
	void filterList(float sideIndexY, int position, String previewText);
}
