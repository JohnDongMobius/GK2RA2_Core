package com.mobius.ra.core.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.DbManager;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.CdrDetailDao;
import com.mobius.ra.core.pojo.Operator;
import com.mobius.ra.core.pojo.Report;

public class DeleteCdrDetailSvc {
	private static CdrDetailDao cdrDetailDao = new CdrDetailDao();
	private final static Logger LOG = Logger.getLogger("");
	String timeZoneString;
	Operator operator;
	static String reportDate;
	Report report;

	public static void generate() throws SQLException, IOException, ClassNotFoundException {
		if (Tools.isProductEnv()) {
			DbManager.init(Constants.PATH_DATABASE);
		} else {
			DbManager.init(Constants.PATH_DATABASE_TEST);
		}
		Calendar base = Calendar.getInstance();
		int calDays = 30;
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, -calDays);
		c.add(Calendar.DAY_OF_MONTH, 1);
		while (c.compareTo(base) > 0) {
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			reportDate = formatter.format(c.getTime());
			LOG.info(reportDate);
			cdrDetailDao.deleteCdrDetail(reportDate, "fraud_425_6");
			c.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	public static void main(String[] args) throws SQLException, InterruptedException, IOException,
			ClassNotFoundException {
		generate();
	}
}
