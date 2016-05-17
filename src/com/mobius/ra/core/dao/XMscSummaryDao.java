package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.RaDetail;
import com.mobius.ra.core.pojo.RaFileName;
import com.mobius.ra.core.pojo.RaMatched;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class XMscSummaryDao extends CommonDao {
	private final Logger logger = Logger.getLogger("RA-Billing");

	public XMscSummaryDao(Report report) {
		super.report = report;
	}

	/**
	 * insert reconsiliating details via batch. CommonDao has this method, so no
	 * need this one.
	 * 
	 * @param detailList
	 * @param operator
	 * @throws SQLException
	 */
	// public void batchInsertRaDetailTable2(List<RaDetail> detailList, String
	// operator) throws SQLException {
	// BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator, false);
	// try {
	// baseDao.releaseStmt();
	// StringBuilder sqlSb = new StringBuilder();
	// sqlSb.append(" insert into ra_detail (call_type, s_msisdn, o_msisdn, duration, call_time,sw_id,s_imsi,s_imei,s_ci,s_lac,trunk_in,trunk_out,term_cause,term_reason,ss_code,charge_indicator,detail_type,report_type,file_name,insert_time,volume_download,volume_upload,charge_identifier,sgsn_address,ggsn_address,pdp_address,short_num,group_num)");
	// sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");
	//
	// baseDao.prepareStatement(sqlSb.toString());
	//
	// for (RaDetail detail : detailList) {
	// baseDao.setString(1, detail.getCallType());
	// baseDao.setLong(2, Tools.getLongValue(detail.getCallingNum()));
	// baseDao.setLong(3, Tools.getLongValue(detail.getCalledNum()));
	// baseDao.setLong(4, detail.getDuration());
	// baseDao.setString(5, detail.getCallTime());
	// baseDao.setLong(6, Tools.getLongValue(detail.getSwId()));
	// baseDao.setLong(7, Tools.getLongValue(detail.getsImsi()));
	// baseDao.setLong(8, Tools.getLongValue(detail.getsImei()));
	// baseDao.setLong(9, Tools.getLongValue(detail.getsCi()));
	//
	// baseDao.setLong(10, Tools.getLongValue(detail.getsLac()));
	// baseDao.setLong(11, Tools.getLongValue(detail.getTrunkIn()));
	// baseDao.setLong(12, Tools.getLongValue(detail.getTrunkOut()));
	// baseDao.setLong(13, Tools.getLongValue(detail.getTermCause()));
	// baseDao.setLong(14, Tools.getLongValue(detail.getTermReason()));
	// baseDao.setLong(15, Tools.getLongValue(detail.getSsCode()));
	// baseDao.setLong(16, Tools.getLongValue(detail.getChargeIndicator()));
	// baseDao.setLong(17, detail.getDetailType());
	// baseDao.setLong(18, detail.getReportType());
	// baseDao.setString(19, detail.getFileName());
	// baseDao.setString(20, detail.getInsertTime());
	// baseDao.setLong(21, Tools.getLongValue(detail.getVolumeDownload()));
	// baseDao.setLong(22, Tools.getLongValue(detail.getVolumeUpload()));
	// baseDao.setLong(23, Tools.getLongValue(detail.getChargeIdentifier()));
	// baseDao.setString(24, detail.getSgsnAddress());
	// baseDao.setString(25, detail.getGgsnAddress());
	// baseDao.setString(26, detail.getPdpAddress());
	// baseDao.setInt(27, detail.getShortNum());
	// baseDao.setLong(28, detail.getGroupNum());
	// baseDao.addBatch();
	// }
	// baseDao.exeBatchUpdate();
	// baseDao.conCommit();
	// } catch (Exception e) {
	// e.printStackTrace();
	// e.printStackTrace();
	// } finally {
	// baseDao.close();
	// }
	// }

	/**
	 * insertRaFileNameTable via batch
	 * 
	 * @param detail
	 * @param operator
	 * @throws SQLException
	 */
	public void batchInsertRaFileNameTable(List<RaFileName> fileNameList, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_feed (traffic_date,file_name,report_type,respective_type,insert_time)");
			sqlSb.append(" values (?,?,?,?,?)  ");

			baseDao.prepareStatement(sqlSb.toString());

			for (RaFileName fileName : fileNameList) {
				baseDao.setString(1, fileName.getCallTime().substring(0, 11));
				baseDao.setString(2, fileName.getFileName());
				baseDao.setInt(3, fileName.getReportType());
				baseDao.setInt(4, fileName.getRespectiveType());
				baseDao.setString(5, fileName.getInsertTime());
				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}

	}

	public void batchInsertRaMatchedTable(List<RaMatched> raMatcheds, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_matched (msc_call_type, msc_s_msisdn, msc_o_msisdn, msc_duration, msc_call_time,msc_s_imsi,billing_call_type,billing_s_msisdn,billing_o_msisdn,billing_duration,billing_call_time,billing_s_imsi,flag)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?)  ");
			baseDao.prepareStatement(sqlSb.toString());
			for (RaMatched raMatched : raMatcheds) {
				baseDao.setString(1, raMatched.getMsc_call_type());
				baseDao.setString(2, raMatched.getMsc_s_msisdn());
				baseDao.setString(3, raMatched.getMsc_o_msisdn());
				baseDao.setLong(4, raMatched.getMsc_duration());
				baseDao.setString(5, raMatched.getMsc_call_time());
				baseDao.setLong(6, raMatched.getMsc_s_imsi());
				baseDao.setString(7, raMatched.getBilling_call_type());
				baseDao.setString(8, raMatched.getBilling_s_msisdn());
				baseDao.setString(9, raMatched.getBilling_o_msisdn());
				baseDao.setLong(10, raMatched.getBilling_duration());
				baseDao.setString(11, raMatched.getBilling_call_time());
				baseDao.setLong(12, raMatched.getBilling_s_imsi());
				baseDao.setInt(13, raMatched.getFlg());
				baseDao.addBatch();
			}
			baseDao.exeBatchUpdate();
			baseDao.conCommit();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	/**
	 * check if report has been generated.
	 * 
	 * @param date
	 * @param operator
	 * @param billingType
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfExist(String date, String operator, String billingType) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		int count = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("select count(*) as count from ra_summary where traffic_date=? and report_type=?");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date);
			baseDao.setString(2, billingType);

			ResultSet rs = baseDao.executeQuery();
			// long endTime = System.currentTimeMillis();
			// LogToolkit.info("SQL : " + sqlSb.toString());
			// LogToolkit.info("Cost Time: " + (endTime - startTime) + " ms");
			while (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return count > 0 ? true : false;
	}

	/**
	 * checkIfPrepaidListIsUpading
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public boolean checkIfPrepaidListIsUpading(String date, String operator) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		int count = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("select count(*) as count from ra_process_status where status_parameter='paid_list_update_status' and status_value=0");

			baseDao.prepareStatement(sqlSb.toString());
			// baseDao.setString(1, date);

			ResultSet rs = baseDao.executeQuery();
			// long endTime = System.currentTimeMillis();
			// LogToolkit.info("SQL : " + sqlSb.toString());
			// LogToolkit.info("Cost Time: " + (endTime - startTime) + " ms");
			while (rs.next()) {
				count = rs.getInt("count");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return count > 0 ? true : false;
	}

	/**
	 * delete ra_detail of one day.
	 * 
	 * @param
	 * @throws SQLException
	 */
	public void deleteRaDetailByDate(String date, String operator, String reportType) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();

			sqlSb.append("DELETE FROM ra_detail WHERE call_time >=? and call_time <? and report_type=?");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getHhmmss(date));
			baseDao.setString(2, Tools.getCalStrAfterOneDay(date));
			baseDao.setString(3, reportType);

			baseDao.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	/**
	 * delete ra_file_name of one day.
	 * 
	 * @param
	 * @throws SQLException
	 */
	public void deleteRaFileNameByDate(String date, String operator, String reportType) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("DELETE FROM ra_feed WHERE traffic_date =? and report_type=?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date);
			baseDao.setString(2, reportType);
			baseDao.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	// public String getFileName(RaDetail raDetail, String alias) throws
	// SQLException {
	// BaseDao baseDao = new BaseDao(alias);
	// String fileName = null;
	// try {
	// StringBuilder sqlSb = new StringBuilder();
	// sqlSb.append("select file_name from short_calls_with_file_name where s_msisdn=? and call_time=?");
	// baseDao.prepareStatement(sqlSb.toString());
	// baseDao.setString(1, raDetail.getCallingNum());
	// baseDao.setString(2, raDetail.getCallTime());
	// ResultSet rs = baseDao.executeQuery();
	// while (rs.next()) {
	// fileName = rs.getString("file_name");
	// }
	// if (fileName == null) {
	// sqlSb = new StringBuilder();
	// sqlSb.append("select file_name from short_calls_with_file_name where o_msisdn=? and call_time=?");
	// baseDao.prepareStatement(sqlSb.toString());
	// baseDao.setString(1, raDetail.getCalledNum());
	// baseDao.setString(2, raDetail.getCallTime());
	// rs = baseDao.executeQuery();
	// while (rs.next()) {
	// fileName = rs.getString("file_name");
	// }
	// }
	//
	// rs.close();
	// rs = null;
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// baseDao.close();
	// }
	// return fileName;
	// }

	public HashMap<String, String> getFileNameHashMap(String date, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		HashMap<String, String> fileNameHashMap = new HashMap<String, String>();
		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append("select file_name, call_time, s_msisdn from short_calls_with_file_name where call_time >=? and call_time <=?");
		baseDao.prepareStatement(sqlSb.toString());
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(date));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));
		baseDao.setString(1, startDate);
		baseDao.setString(2, endDate);
		ResultSet rs = baseDao.executeQuery();
		while (rs.next()) {
			fileNameHashMap.put(rs.getString("call_time").substring(0, 19) + rs.getString("s_msisdn"), rs.getString("file_name"));
		}
		System.out.println("Size of fileNameHashMap: " + fileNameHashMap.size());
		return fileNameHashMap;
	}

	/**
	 * getRedoTimes
	 * 
	 * @param date
	 * @param operator
	 * @param billingType
	 * @return
	 * @throws SQLException
	 */
	public int getRedoTimes(String date, String operator, String billingType) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		int count = -1;// -1 means no report for this date.

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("select * from ra_summary where traffic_date=? and report_type=?");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, date);
			baseDao.setString(2, billingType);

			ResultSet rs = baseDao.executeQuery();
			// long endTime = System.currentTimeMillis();
			// LogToolkit.info("SQL : " + sqlSb.toString());
			// LogToolkit.info("Cost Time: " + (endTime - startTime) + " ms");
			while (rs.next()) {
				count = rs.getInt("redo_times");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return count;
	}

	/**
	 * getSubsriberNumWithShortNum
	 * 
	 * @param shortNum
	 * @param groupNum
	 * @return
	 * @throws SQLException
	 */
	public long getSubsriberNumWithShortNum(int shortNum, long groupId, String operator) throws SQLException {
		// long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_CORE + operator);
		long subscriberNum = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("select subscriber_num from short_msisdn where short_num=? and group_id=?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setInt(1, shortNum);
			baseDao.setLong(2, groupId);
			ResultSet rs = baseDao.executeQuery();

			// long endTime = System.currentTimeMillis();
			// LogToolkit.info("SQL : " + sqlSb.toString());
			// LogToolkit.info("Cost Time: " + (endTime - startTime) + " ms");
			while (rs.next()) {
				subscriberNum = rs.getLong("subscriber_num");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return subscriberNum;
	}

	/**
	 * getXOnlyCallCount
	 * 
	 * @param date
	 * @param operator
	 * @return
	 * @throws SQLException
	 */
	public long[] getXOnlyCallCount(String date, String operator, String detailType) throws SQLException {
		long startTime = System.currentTimeMillis();
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		long callCount = 0;
		long durationCount = 0;

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as call_count_only, sum(duration) as duration_only from ra_detail ");
			sqlSb.append(" where call_time >= ? ");
			sqlSb.append(" and call_time < ? ");
			sqlSb.append(" and detail_type = ?");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, Tools.getHhmmss(date));
			baseDao.setString(2, Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));
			baseDao.setString(3, detailType);
			this.logger.info(Tools.getHhmmss(date) + ", " + Tools.getHhmmss(Tools.getCalStrAfterOneDay(date)));

			ResultSet rs = baseDao.executeQuery();

			long endTime = System.currentTimeMillis();
			this.logger.info("SQL : " + sqlSb.toString());
			this.logger.info("Cost Time: " + (endTime - startTime) + " ms");

			while (rs.next()) {
				callCount = rs.getLong("call_count_only");
				durationCount = rs.getLong("duration_only");
			}

			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		long arr[] = { callCount, durationCount };
		return arr;
	}

	/**
	 * insetCountTable
	 * 
	 * @param HashMap
	 *            <String, String>, operator
	 * @throws SQLException
	 */
	public void insertCountTable(HashMap<String, String> hm, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("insert into ra_summary(                                                                  ");
			sqlSb.append("traffic_date,moc_call_count,moc_duration,billing_call_count,billing_duration,                                 ");
			sqlSb.append("moc_call_count_without_billing,moc_duration_without_billing,billing_call_count_without_moc,billing_duration_without_moc, insert_date, report_type, msc_file_count, billing_file_count)");
			sqlSb.append("values(DATE_FORMAT(?,'%Y-%m-%d'),?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,?,?)                                                                           ");
			baseDao.prepareStatement(sqlSb.toString());

			baseDao.setString(1, hm.get("traffic_date"));
			baseDao.setString(2, hm.get("moc_call_count"));
			baseDao.setString(3, hm.get("moc_duration"));
			baseDao.setString(4, Tools.getLongValue(hm.get("billing_call_count")).toString());
			baseDao.setString(5, Tools.getLongValue(hm.get("billing_duration")).toString());
			baseDao.setString(6, Tools.getLongValue(hm.get("moc_call_count_without_billing")).toString());
			baseDao.setString(7, Tools.getLongValue(hm.get("moc_duration_without_billing")).toString());
			baseDao.setString(8, Tools.getLongValue(hm.get("billing_call_count_without_moc")).toString());
			baseDao.setString(9, Tools.getLongValue(hm.get("billing_duration_without_moc")).toString());
			baseDao.setString(10, hm.get("report_type"));
			baseDao.setString(11, Tools.getLongValue(hm.get("msc_file_count")).toString());
			baseDao.setString(12, Tools.getLongValue(hm.get("billing_file_count")).toString());

			baseDao.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	/**
	 * insertRaDetailTable
	 * 
	 * @param detail
	 * @param operator
	 * @throws SQLException
	 */
	public void insertRaDetailTable(RaDetail detail, String operator) throws SQLException {
		BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_detail (call_type, s_msisdn, o_msisdn, duration, call_time,sw_id,s_imsi,s_imei,s_ci,s_lac,trunk_in,trunk_out,term_cause,term_reason,ss_code,charge_indicator,detail_type,report_type,volume_download,volume_upload,charge_identifier,sgsn_address,ggsn_address,pdp_address,file_name)");
			sqlSb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, detail.getCallType());
			baseDao.setLong(2, Tools.getLongValue(detail.getCallingNum()));
			baseDao.setLong(3, Tools.getLongValue(detail.getCalledNum()));
			baseDao.setLong(4, detail.getDuration());
			baseDao.setString(5, detail.getCallTime());
			baseDao.setLong(6, Tools.getLongValue(detail.getSwId()));
			baseDao.setLong(7, Tools.getLongValue(detail.getsImsi()));
			baseDao.setLong(8, Tools.getLongValue(detail.getsImei()));
			baseDao.setLong(9, Tools.getLongValue(detail.getsCi()));
			baseDao.setLong(10, Tools.getLongValue(detail.getsLac()));
			baseDao.setLong(11, Tools.getLongValue(detail.getTrunkIn()));
			baseDao.setLong(12, Tools.getLongValue(detail.getTrunkOut()));
			baseDao.setLong(13, Tools.getLongValue(detail.getTermCause()));
			baseDao.setLong(14, Tools.getLongValue(detail.getTermReason()));
			baseDao.setLong(15, Tools.getLongValue(detail.getSsCode()));
			baseDao.setLong(16, Tools.getLongValue(detail.getChargeIndicator()));
			baseDao.setLong(17, detail.getDetailType());
			baseDao.setLong(18, detail.getReportType());
			baseDao.setLong(19, Tools.getLongValue(detail.getVolumeDownload()));
			baseDao.setLong(20, Tools.getLongValue(detail.getVolumeUpload()));
			baseDao.setLong(21, Tools.getLongValue(detail.getChargeIdentifier()));
			baseDao.setString(22, detail.getSgsnAddress());
			baseDao.setString(23, detail.getGgsnAddress());
			baseDao.setString(24, detail.getPdpAddress());
			baseDao.setString(25, detail.getFileName());

			baseDao.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	/**
	 * updateCountTable
	 * 
	 * @param hm
	 * @param operator
	 * @throws SQLException
	 */
	public void updateCountTable(HashMap<String, String> hm, String operator) throws SQLException {
		{

			BaseDao baseDao = new BaseDao(Constants.DB_NAME_FRAUD + operator);
			try {
				baseDao.releaseStmt();
				StringBuilder sqlSb = new StringBuilder();
				sqlSb.append("update ra_summary set moc_call_count=?, moc_duration=?, billing_call_count=?, billing_duration=?,                     ");
				sqlSb.append("			 moc_call_count_without_billing=?, moc_duration_without_billing=?, billing_call_count_without_moc=?, billing_duration_without_moc=?, ");
				sqlSb.append("			 insert_date=CURRENT_TIMESTAMP, redo_times=?, msc_file_count=?, billing_file_count=?                                      ");
				sqlSb.append("where traffic_date=? and report_type=?                                                                                             ");

				baseDao.prepareStatement(sqlSb.toString());

				baseDao.setString(1, hm.get("moc_call_count"));
				baseDao.setString(2, hm.get("moc_duration"));
				baseDao.setString(3, hm.get("in_call_count"));
				baseDao.setString(4, hm.get("in_duration"));
				baseDao.setString(5, hm.get("moc_call_count_without_in"));
				baseDao.setString(6, hm.get("moc_duration_without_in"));
				baseDao.setString(7, hm.get("in_call_count_without_moc"));
				baseDao.setString(8, hm.get("in_duration_without_moc"));
				baseDao.setString(9, hm.get("transition_status"));
				baseDao.setString(10, hm.get("msc_file_count"));
				baseDao.setString(11, hm.get("billing_file_count"));
				baseDao.setString(12, hm.get("traffic_date"));
				baseDao.setString(13, hm.get("report_type"));

				baseDao.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				baseDao.close();
			}
		}
	}

	public List<String> getShorNums(String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		List<String> shortNums = new ArrayList<String>();
		try {
			StringBuilder sqlSb = new StringBuilder();
			String sqlString = " select * from short_msisdn ";
			sqlSb.append(sqlString);
			baseDao.prepareStatement(sqlSb.toString());
			ResultSet rs = baseDao.executeQuery();
			while (rs.next()) {
				shortNums.add(rs.getString("short_num"));
				shortNums.add(rs.getString("subscriber_num"));
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return shortNums;
	}
}