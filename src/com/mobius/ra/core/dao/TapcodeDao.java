package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.Tapcode;

/**
 * @author John Dong
 * @date May 11, 2016
 * @version v 1.0
 */
public class TapcodeDao extends CommonDao {
	private static Logger logger = Logger.getLogger("RA-MSC-IRSF");
	
	public TapcodeDao(Report report) {
		super.report = report;
	}
	
	public List<Tapcode> getAllTapcodes(String alias, Report report) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<Tapcode> tapcodeList = new CopyOnWriteArrayList<Tapcode>();
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(report.getSqls().get("getTapcode"));
		logger.info(sqlSb.toString());
		try {
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				Tapcode tapcode = new Tapcode();
				tapcode.setImsiPrefix(rs.getString("imsi_prefix"));
				tapcode.setTapCode(rs.getString("tap_code"));
				tapcodeList.add(tapcode);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return tapcodeList;
	}
}
	