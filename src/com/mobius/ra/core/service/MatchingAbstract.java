package com.mobius.ra.core.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.XMscSummaryDao;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.RaDetail;
import com.mobius.ra.core.pojo.RaMatched;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public abstract class MatchingAbstract {
	private static Logger logger = Logger.getLogger("RA-Billing");
	/**
	 * used to storage detail instances that inserted into ra_detail table via
	 * batch.
	 */
	protected List<RaDetail> detailList = new ArrayList<RaDetail>();
	HashMap<String, String> fileNameHashMap = null;
	protected List<RaMatched> raMatcheds = new ArrayList<RaMatched>();

	/**
	 * insert into ra_detail table via batch.
	 * 
	 * @param detailList
	 * @param objsMap
	 */
	protected void batchInsetRaDetailTable(Map<String, Object> objsMap) {
		String operator = (String) objsMap.get(Constants.KEY_OPERATOR);
		XMscSummaryDao dao = (XMscSummaryDao) objsMap.get(Constants.KEY_DAO);
		if (detailList.size() > 0) {
			try {
				dao.batchInsertRaDetailTable(this.detailList, operator);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void batchInsetRaMatchedTable(Map<String, Object> objsMap) {
		String operator = (String) objsMap.get(Constants.KEY_OPERATOR);
		XMscSummaryDao dao = (XMscSummaryDao) objsMap.get(Constants.KEY_DAO);
		if (raMatcheds.size() > 0) {
			try {
				dao.batchInsertRaMatchedTable(this.raMatcheds, operator);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * insert into RaDetail Object from billing table.
	 * 
	 * @param billing
	 * @param objsMap
	 */
	protected void insertRaDetail4Billing(RaBilling billing, Map<String, Object> objsMap) {
		String detailType = (String) objsMap.get(Constants.KEY_DETAILTYPE);
		String reportType = (String) objsMap.get(Constants.KEY_REPORTTYPE);
		String localTimezoneStr = Constants.coreCfg.getLocalTimezone();

		RaDetail detail = new RaDetail();
		detail.setCallType(billing.getCallType());
		detail.setCallingNum(billing.getCallingNum());
		detail.setCalledNum(billing.getCalledNum());
		detail.setDuration(billing.getDuration());
		detail.setCallTime(Tools.getLocalCallTime("GMT", billing.getCallTime(), localTimezoneStr));
		detail.setSwId(billing.getSwID());
		detail.setsImsi(billing.getsImsi());
		detail.setsImei(billing.getsImei());
		detail.setsCi(billing.getsCi());
		detail.setsLac(billing.getsLac());
		detail.setTrunkIn(billing.getTrunkIn());
		detail.setTrunkOut(billing.getTrunkOut());
		detail.setTermCause(billing.getTermCause());
		detail.setTermReason(billing.getTermReason());
		detail.setSsCode(billing.getSsCode());
		detail.setChargeIndicator(billing.getChargeIndicator());
		detail.setDetailType(new Integer(detailType));
		detail.setReportType(new Integer(reportType));
		detail.setFileName(billing.getFileName());
		detail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
		detail.setVolumeDownload(billing.getVolumeDownload());
		detail.setVolumeUpload(billing.getVolumeUpload());
		detail.setChargeIdentifier(billing.getChargeIdentifier());

		detailList.add(detail);
		// logger.info(in.getCallTime()+"/"+Tools.getLocalCallTime("GMT",in.getCallTime()));
		// try {
		// dao.insertRaDetailTable(detail, operator);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
	}

	protected void insertRaDetail4Msc(CallsNoDup msc, Map<String, Object> objsMap) {
		String localTimezoneStr = Constants.coreCfg.getLocalTimezone();
		String detailType = (String) objsMap.get(Constants.KEY_DETAILTYPE);
		String reportType = (String) objsMap.get(Constants.KEY_REPORTTYPE);

		RaDetail detail = new RaDetail();
		detail.setCallType(msc.getCallType());
		detail.setCallingNum(msc.getCallingNum());
		detail.setCalledNum(msc.getCalledNum());
		detail.setDuration(msc.getDuration());
		detail.setCallTime(Tools.getLocalCallTime("GMT", msc.getCallTime(), localTimezoneStr));
		detail.setSwId(msc.getSwId());
		detail.setsImsi(msc.getsImsi());
		detail.setsImei(msc.getsImei());
		detail.setsCi(msc.getsCi());
		detail.setsLac(msc.getsLac());
		detail.setTrunkIn(msc.getTrunkIn());
		detail.setTrunkOut(msc.getTrunkOut());
		detail.setTermCause(msc.getTermCause());
		detail.setTermReason(msc.getTermReason());
		detail.setSsCode(msc.getSsCode());
		detail.setChargeIndicator(msc.getChargeIndicator());
		detail.setDetailType(new Integer(detailType));
		detail.setReportType(new Integer(reportType));
		detail.setFileName(msc.getFileName());
		detail.setInsertTime(Tools.getCalFullStr(Calendar.getInstance()));
		detail.setVolumeDownload(msc.getVolumeDownload());
		detail.setVolumeUpload(msc.getVolumeUpload());
		detail.setChargeIdentifier(msc.getChargeIdentifier());
		detail.setShortNum(msc.getShortNum());
		detail.setGroupNum(msc.getGroupNum());

		detailList.add(detail);

		// System.out.println(moc.getCallTime()+"/"+Tools.getLocalCallTime("GMT",moc.getCallTime()));
		// try {
		// dao.insertRaDetailTable(detail, operator);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
	}

	protected void insertRaMatched(CallsNoDup msc, RaBilling billing, int flag) {
		String localTimezoneStr = Constants.coreCfg.getLocalTimezone();
		RaMatched raMatched = new RaMatched();
		raMatched.setBilling_call_time(Tools.getLocalCallTime("GMT", billing.getCallTime(), localTimezoneStr));
		raMatched.setBilling_call_type(billing.getCallType());
		raMatched.setBilling_duration(billing.getDuration());
		raMatched.setBilling_o_msisdn(billing.getCalledNum());
		raMatched.setBilling_s_imsi(billing.getsImsi());
		raMatched.setBilling_s_msisdn(billing.getCallingNum());
		raMatched.setMsc_call_time(Tools.getLocalCallTime("GMT", msc.getCallTime(), localTimezoneStr));
		raMatched.setMsc_call_type(msc.getCallType());
		raMatched.setMsc_duration(msc.getDuration());
		raMatched.setMsc_o_msisdn(msc.getCalledNum());
		raMatched.setMsc_s_imsi(msc.getsImsi());
		raMatched.setMsc_s_msisdn(msc.getCallingNum());
		raMatched.setFlg(flag);
		raMatcheds.add(raMatched);
	}

	/**
	 * get MSC only against billing.
	 * 
	 * @param objsMap
	 * @throws SQLException
	 */
	public abstract void matchOrder(Map<String, Object> objsMap) throws SQLException;

	/**
	 * get billing only against MSC.
	 * 
	 * @param objsMap
	 * @throws SQLException
	 */
	public abstract void matchReverse(Map<String, Object> objsMap) throws SQLException;

	protected void setFileName(Map<String, Object> objsMap) throws SQLException {
		Report report = (Report) objsMap.get(Constants.KEY_REPORT);
		if (report.getMscFilenameUpdate() == Constants.INT_ONE) {
			String fileName = null;
			@SuppressWarnings("unchecked")
			HashMap<String, String> fileNameHashMap = (HashMap<String, String>) objsMap.get(Constants.KEY_FILENAME_HASHMAP);
			int j=0;
			for (RaDetail raDetail : this.detailList) {
				String gmtTime = Tools.normalizeCallTime(Constants.coreCfg.getLocalTimezone(), raDetail.getCallTime());
				j++;
				if(raDetail!=null){
					fileName = fileNameHashMap.get(gmtTime + raDetail.getCallingNum());
					if(j<=10)
						System.out.println("file_name sample"+j+":"+fileName);
				}
				else
					System.out.println("raDetail is null");
				raDetail.setFileName(fileName);
			}
		}
	}

	protected List<CallsNoDup> setSubsriberNum(List<CallsNoDup> mscList, Report report) throws SQLException {
		if (report.getShortActive() == Constants.INT_ONE) {
			logger.info("Start set subsriberNum" + new Date());
			XMscSummaryDao xMscSummaryDao = new XMscSummaryDao(report);
			for (CallsNoDup callsNoDup : mscList) {
				if (callsNoDup.getCallingNum() != null && callsNoDup.getCallingNum().length() == 3 && callsNoDup.getChargeIndicator() != 0) {
					int shortNum = Integer.parseInt(callsNoDup.getCallingNum());
					long subscriberNum = xMscSummaryDao.getSubsriberNumWithShortNum(shortNum, callsNoDup.getChargeIndicator(),
							Constants.coreCfg.getOperatorCode());
					if (subscriberNum != 0) {
						callsNoDup.setShortNum(shortNum);
						callsNoDup.setCallingNum(Constants.coreCfg.getMsisdnPrefix() + Long.toString(subscriberNum));
						callsNoDup.setGroupNum(callsNoDup.getChargeIndicator());
					}
				}

				if (callsNoDup.getCalledNum() != null && callsNoDup.getCalledNum().length() == 3 && callsNoDup.getChargeIndicator() != 0) {
					int shortNum = Integer.parseInt(callsNoDup.getCalledNum());
					long subscriberNum = xMscSummaryDao.getSubsriberNumWithShortNum(shortNum, callsNoDup.getChargeIndicator(),
							Constants.coreCfg.getOperatorCode());
					if (subscriberNum != 0) {
						callsNoDup.setShortNum(shortNum);
						callsNoDup.setCalledNum(Constants.coreCfg.getMsisdnPrefix() + Long.toString(subscriberNum));
						callsNoDup.setGroupNum(callsNoDup.getChargeIndicator());
					}
				}
			}
			logger.info("Finished set subsriberNum" + new Date());
		}
		return mscList;
	}
}
