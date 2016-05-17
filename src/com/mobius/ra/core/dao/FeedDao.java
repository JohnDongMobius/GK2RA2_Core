package com.mobius.ra.core.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.pojo.FeedInfo;
import com.mobius.ra.core.pojo.FileReport;
import com.mobius.ra.core.pojo.RAFeed;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class FeedDao {
	public static void main(String args[]){
		FeedDao dao = new FeedDao();
		FeedInfo fi = new FeedInfo();
		fi.setSql("select distinct a.file_name, b.file_size_bytes from ra_billing a, call_count b where a.call_time >= ? and a.call_time < ? and a.billing_type=2 and a.call_type=12 and a.file_name=b.file_name");
		fi.setType(302);
		
		try {
			List<RAFeed> list = dao.getRAFeedList(fi, "core_472_2", "2014-03-19", 1);
			System.out.println(list.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private final Logger logger = Logger.getLogger("RA-Billing");

	public int checkFeedInserted(String alias, FeedInfo feedInfo, String trafficDate, int redoFlg) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from ra_feed_summary where traffic_date = ? and feed_type = ? and redo_times = ?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			baseDao.setInt(2, feedInfo.getType());
			baseDao.setInt(3, redoFlg);

			ResultSet rs = baseDao.executeQuery();
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
		return count;
	}

	private int countArg(String s) {
		String[] array = s.split("\\?");
		if (array != null) {
			return (array.length - 1);
		} else {
			return 0;
		}
	}

	public void deleteRaFeedByDate(String trafficDate, String alias, int type) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("DELETE FROM ra_feed WHERE traffic_date =? and type=?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			baseDao.setInt(2, type);
			baseDao.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	public void deleteRaFeedSummaryByDate(String trafficDate, String alias, int type) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append("DELETE FROM ra_feed_summary WHERE traffic_date =? and feed_type=?");
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			baseDao.setInt(2, type);
			baseDao.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
	}

	public Map<String,String> getDownloadedFileMap(FeedInfo feedInfo, String alias, String trafficDate) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		FileReport fileReport = feedInfo.getFileReport();
		Map<String,String> raFeedMap = new HashMap<String,String>();
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(trafficDate));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(),
				Tools.getHhmmss(Tools.getCalStrAfterOneDay(trafficDate)));
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(fileReport.getReferSql());
			baseDao.prepareStatement(sqlSb.toString());
			if (countArg(fileReport.getReferSql()) > 0) {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
			}
			ResultSet rs = baseDao.executeQuery();

			while (rs.next()) {
				RAFeed raFeed = new RAFeed();
				raFeed.setFileName(rs.getString("file_name"));
				raFeed.setFileSizeBytes(rs.getInt("file_size_bytes"));
				raFeed.setType(feedInfo.getType());
				raFeed.setUnprocessedReason(rs.getString("decoded_result"));
				
				raFeedMap.put(raFeed.getFileName(), raFeed.getFileSizeBytes()+","+raFeed.getUnprocessedReason());
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return raFeedMap;
	}

	public int getFeedCount(String trafficDate, int feedType, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		int count = 0;
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" select count(*) as count from ra_feed where traffic_date = ? and type = ? and respective_type = 1"); //respective_type=1 for vs_report_summary. 2,3 for file-report. 
			baseDao.prepareStatement(sqlSb.toString());
			baseDao.setString(1, trafficDate);
			baseDao.setInt(2, feedType);
			ResultSet rs = baseDao.executeQuery();
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
		return count;
	}

	public ArrayList<RAFeed> getRAFeedList(FeedInfo feedInfo, String alias, String trafficDate) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		ArrayList<RAFeed> raFeedList = new ArrayList<RAFeed>();
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(trafficDate));
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(),
				Tools.getHhmmss(Tools.getCalStrAfterOneDay(trafficDate)));
		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(feedInfo.getSql());
			baseDao.prepareStatement(sqlSb.toString());
			if (countArg(feedInfo.getSql()) > 2) {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
				baseDao.setString(3, startDate);
				baseDao.setString(4, endDate);
			} else {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
			}
			ResultSet rs = baseDao.executeQuery();

			while (rs.next()) {
				RAFeed raFeed = new RAFeed();
				raFeed.setFileName(rs.getString("file_name"));
				raFeed.setFileSizeBytes(rs.getLong("file_size_bytes"));
				raFeed.setType(feedInfo.getType());
				raFeed.setRespectiveType(1);//for vs_report_summary, ra_feed inserted with repective_tpe=1.
				raFeedList.add(raFeed);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return raFeedList;
	}
	
	public ArrayList<RAFeed> getRAFeedList(FeedInfo feedInfo, String alias, String trafficDate, int deviationDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		ArrayList<RAFeed> raFeedList = new ArrayList<RAFeed>();
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(trafficDate), -deviationDay);
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(),
				Tools.getHhmmss(Tools.getCalStrAfterOneDay(trafficDate)), deviationDay);

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(feedInfo.getSql());
			baseDao.prepareStatement(sqlSb.toString());
			if (countArg(feedInfo.getSql()) > 2) {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
				baseDao.setString(3, startDate);
				baseDao.setString(4, endDate);
			} else {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
			}
			ResultSet rs = baseDao.executeQuery();

			while (rs.next()) {
				RAFeed raFeed = new RAFeed();
				raFeed.setFileName(rs.getString("file_name"));
				raFeed.setFileSizeBytes(rs.getLong("file_size_bytes"));
				raFeed.setType(feedInfo.getType());
				raFeed.setRespectiveType(1);//for vs_report_summary, ra_feed inserted with repective_tpe=1.
				raFeedList.add(raFeed);
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return raFeedList;
	}
	
	public Map<String, String> getRAFeedMap(FeedInfo feedInfo, String alias, String trafficDate, int deviationDay) throws SQLException {
		BaseDao baseDao = new BaseDao(alias);
		Map<String,String> map = new HashMap<String,String>();
		String startDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), Tools.getHhmmss(trafficDate), -deviationDay);
		String endDate = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(),
				Tools.getHhmmss(Tools.getCalStrAfterOneDay(trafficDate)), deviationDay);

		try {
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(feedInfo.getSql());
			baseDao.prepareStatement(sqlSb.toString());
			if (countArg(feedInfo.getSql()) > 2) {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
				baseDao.setString(3, startDate);
				baseDao.setString(4, endDate);
			} else {
				baseDao.setString(1, startDate);
				baseDao.setString(2, endDate);
			}
			ResultSet rs = baseDao.executeQuery();

			while (rs.next()) {
				map.put(rs.getString("file_name"), rs.getLong("file_size_bytes")+"");
			}
			rs.close();
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baseDao.close();
		}
		return map;
	}


	public void insertRAFeed(List<RAFeed> raFeedList, String trafficDate, String alias) throws SQLException {
		BaseDao baseDao = new BaseDao(alias, false);
		try {
			baseDao.releaseStmt();
			StringBuilder sqlSb = new StringBuilder();
			sqlSb.append(" insert into ra_feed (traffic_date, file_name, type, file_size_bytes, insert_time, respective_type, unprocessed_reason)");
			sqlSb.append(" values (?,?,?,?,?,?,?) ");

			baseDao.prepareStatement(sqlSb.toString());

			for (RAFeed raFeed : raFeedList) {
				baseDao.setString(1, trafficDate);
				baseDao.setString(2, raFeed.getFileName());
				baseDao.setInt(3, raFeed.getType());
				baseDao.setLong(4, raFeed.getFileSizeBytes());
				baseDao.setString(5, Tools.getCalFullStr(Calendar.getInstance()));
				baseDao.setInt(6, raFeed.getRespectiveType());
				baseDao.setString(7, raFeed.getUnprocessedReason());
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
		logger.info("Inserted into ra_feed: " + raFeedList.size());
	}
		
		public void insertRAFeedSummary(int uploadedFileCount, int processedFileCount, int unprocessedFileCount, int type, String trafficDate, String alias, int redoTimes)
				throws SQLException {
			BaseDao baseDao = new BaseDao(alias, false);
			try {
				baseDao.releaseStmt();
				StringBuilder sqlSb = new StringBuilder();
				sqlSb.append(" insert into ra_feed_summary (traffic_date, feed_type, file_count, uploaded_file_count, processed_file_count, unprocessed_file_count, redo_times)");
				sqlSb.append(" values (?,?,?,?,?,?,?) ");

				baseDao.prepareStatement(sqlSb.toString());
				baseDao.setString(1, trafficDate);
				baseDao.setInt(2, type);
				baseDao.setInt(3, processedFileCount);//file_count=processed_list, it's temporarily used for several days. in future, it will be discarded.
				baseDao.setInt(4, uploadedFileCount);
				baseDao.setInt(5, processedFileCount);
				baseDao.setInt(6, unprocessedFileCount);
				baseDao.setInt(7, redoTimes);
				baseDao.executeUpdate();
				baseDao.conCommit();
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace();
			} finally {
				baseDao.close();
			}
		}
	}
	