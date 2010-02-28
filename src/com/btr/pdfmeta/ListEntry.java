package com.btr.pdfmeta;

/*****************************************************************************
 * Small helper class to store files in the JList. 
 *
 * @author Bernd Rosstauscher (pdfmeta@rosstauscher.de) Copyright 2010
 ****************************************************************************/

class ListEntry {
	String name;
	String filePath;
	
	/*************************************************************************
	 * Constructor
	 * @param name
	 * @param filePath
	 ************************************************************************/
	
	public ListEntry(String name, String filePath) {
		this.name = name;
		this.filePath = filePath;
	}
	
	/*************************************************************************
	 * toString
	 * @see java.lang.Object#toString()
	 ************************************************************************/
	@Override
	public String toString() {
		return name;
	}
	
	/*************************************************************************
	 * @return Returns the name.
	 ************************************************************************/
	
	public String getName() {
		return name;
	}

	/*************************************************************************
	 * @return Returns the filePath.
	 ************************************************************************/
	
	public String getFilePath() {
		return filePath;
	}

}