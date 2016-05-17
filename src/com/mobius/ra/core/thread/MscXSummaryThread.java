package com.mobius.ra.core.thread;

import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.service.MatchingAbstract;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class MscXSummaryThread extends Thread {
	private static Logger logger = Logger.getLogger("RA-Billing");
	Map<String, Object> objsMap = null;

	public MscXSummaryThread(Map<String, Object> objsMap) {
		this.objsMap = objsMap;
	}

	@Override
	public void run() {
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);
		Map<String, String> beans = report.getBeans();
		String matchingFullname = Tools.getClassFullname(beans, Constants.GET_MATCHING_CLASS);
		if (Tools.checkNullorSpace(matchingFullname))
			matchingFullname = Constants.MATCHING_CLASS_DEFAULT;

		try {
			MatchingAbstract o = (MatchingAbstract) Class.forName(matchingFullname).newInstance();
			o.matchOrder(this.objsMap);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
