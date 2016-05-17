package com.mobius.ra.core.thread;

import java.sql.SQLException;
import java.text.ParseException;

import com.enterprisedt.util.debug.Logger;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.service.InboundRoamingSummaryService;
import com.mobius.ra.core.service.IndRoamingSmySvc4Maldives;
import com.mobius.ra.core.service.InterconnectSvc;
import com.mobius.ra.core.service.IrsfService;
import com.mobius.ra.core.service.MscIrsfService;
import com.mobius.ra.core.service.ServiceInterface;
import com.mobius.ra.core.service.TrafficSummaryService;
import com.mobius.ra.core.service.XMscSummaryService;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class GenerateReportRealtimeThread extends Thread {
	private static Logger logger = Logger.getLogger("RA-Billing");
	private String args[] = null;
	private String operator = null;
	private Report report = null;
	private ServiceInterface service;

	public GenerateReportRealtimeThread(String args[], String operator, Report report) {
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
		else if ("401".equals(report.getType())) {
			if (operator.equals("472_2")) {
				service = new IndRoamingSmySvc4Maldives();
			} else {
				service = new InboundRoamingSummaryService();
			}
			service = new IndRoamingSmySvc4Maldives();
		} else if ("304".equals(report.getType()))
			service = new InterconnectSvc();
		else if ("500".equals(report.getType()))
			service = new IrsfService();
		else if ("600".equals(report.getType()))
			service = new MscIrsfService();
		try {
			if (service.summary(args, operator, report))
				logger.info(report.getName() + "'s Realtime processing is finished");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
