package com.mobius.ra.core.thread;

import java.sql.SQLException;
import java.text.ParseException;

import org.apache.log4j.Logger;

import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.service.InboundRoamingSummaryService;
import com.mobius.ra.core.service.InterconnectSvc;
import com.mobius.ra.core.service.ServiceInterface;
import com.mobius.ra.core.service.TrafficSummaryService;
import com.mobius.ra.core.service.XMscSummaryService;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class GenerateReportRedoThread extends Thread {
	private static Logger logger = Logger.getLogger("RA-Billing");
	private String args[] = null;
	private String operator = null;
	private Report report = null;
	private ServiceInterface service;

	public GenerateReportRedoThread(String args[], String operator, Report report) {
		this.args = args;
		this.operator = operator;
		this.report = report;
	}

	@Override
	public void run() {
		if (!"N".equals(report.getIsRecon()))
			service = new XMscSummaryService();
		else if ("301".equals(report.getType()))
			service = new TrafficSummaryService();
		else if ("401".equals(report.getType()))
			service = new InboundRoamingSummaryService();
		else if ("304".equals(report.getType()))
			service = new InterconnectSvc();
		try {
			if (service.summaryRedo(args, operator, report))
				logger.info(report.getName() + "'s Redo processing is finished");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
