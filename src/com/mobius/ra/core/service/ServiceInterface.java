package com.mobius.ra.core.service;

import java.sql.SQLException;
import java.text.ParseException;

import com.mobius.ra.core.pojo.Report;

public interface ServiceInterface {
	/**
	 * for realtime operation.
	 * 
	 * @param args
	 * @param operator
	 * @param report
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean summary(String args[], String operator, Report report) throws SQLException, ParseException;
	
	/**
	 * for redo operation.
	 * 
	 * @param args
	 * @param operator
	 * @param report
	 * @throws SQLException
	 * @throws ParseException
	 */
	public boolean summaryRedo(String args[], String operator, Report report) throws SQLException, ParseException;
}
