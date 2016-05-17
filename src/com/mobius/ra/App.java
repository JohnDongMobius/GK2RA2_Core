package com.mobius.ra;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.DbManager;
import com.mobius.ra.core.common.ParseCfgXML;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.thread.GenerateReportRealtimeThread;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class App {
	private static Logger logger = Logger.getLogger("RA");

	public static void main(String[] args) throws SQLException, InterruptedException {
		if (Tools.isProductEnv()) {
			DbManager.init(Constants.PATH_DATABASE);
		} else {
			logger.info("------------------TEST ENV------------------");
			Constants.PATH_DATABASE = Constants.PATH_DATABASE_TEST;
			Constants.PATH_CONFIGXML = Constants.PATH_CONFIGXML_TEST;
			DbManager.init(Constants.PATH_DATABASE_TEST);
		}

		Constants.coreCfg = ParseCfgXML.parse();
		String operator = Constants.coreCfg.getOperatorCode();
		List<Report> reports = Constants.coreCfg.getReports();

		// if define date executed in start.cmd, executing one time, no loop.
		if (args != null && args.length > 0) {
			generateReports(args, operator, reports);
		} else {
			while (true) {
				logger.info("");
				logger.info("*****summary action starts loop ..." + new Date());

				// do summary action.
				generateReports(args, operator, reports);
				// if execute one loop, sleep for a while.
				try {
					logger.info("*****summary action one loop end, sleep for a while...\n");

					Thread.sleep(Constants.coreCfg.getSleepDurationAfterOneLoop());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * inner operation for generate reports.
	 * 
	 * @param args
	 * @param operator
	 * @param reports
	 * @throws InterruptedException
	 */
	private static void generateReports(String args[], String operator, List<Report> reports) throws InterruptedException {
		// do summary action parallelly.
		if (Constants.GENERATE_ORDER_PARALLELLY.equals(Constants.coreCfg.getGenerateOrder())) {
			for (Report report : reports) {
				if (report.getActive() != Constants.ACTIVE_DEFAULT)
					continue;

				String reportName = report.getName();
				logger.info(reportName + " report is starting....******");

				GenerateReportRealtimeThread reportThread = new GenerateReportRealtimeThread(args, operator, report);
				reportThread.start();

				logger.info(reportName + " report is running....******\n");
			}
		} else {// serially
			GenerateReportRealtimeThread reportThread[] = new GenerateReportRealtimeThread[reports.size()];
			for (int i = 0; i < reports.size(); i++) {
				if (reports.get(i).getActive() != Constants.ACTIVE_DEFAULT)
					continue;

				String reportName = reports.get(i).getName();
				logger.info(reportName + " report is starting....******");
				reportThread[i] = new GenerateReportRealtimeThread(args, operator, reports.get(i));
				if (i > 0 && reportThread[i - 1] != null && reportThread[i - 1].isAlive()) {
					Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
				} else {
					reportThread[i].start();
				}

				// Keep generating report serially, one by one.
				while (true) {
					if (reportThread[i].isAlive())
						Thread.sleep(Constants.coreCfg.getSleepDurationOfSubthread());
					else
						break;
				}

				logger.info(reportName + " report is complete....******\n");
			}
		}
	}
}