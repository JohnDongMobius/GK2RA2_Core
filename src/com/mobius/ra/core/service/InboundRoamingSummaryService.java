package com.mobius.ra.core.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.InBoundRoamingSummaryDao;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.InboundRoamingSummary;
import com.mobius.ra.core.pojo.RaDetail;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date Jul 18, 2014
 * @version v 1.0
 */
public class InboundRoamingSummaryService implements ServiceInterface {
	private static String countryPrefix = Constants.coreCfg.getCountryPrefix();
	private static Logger logger = Logger.getLogger("RA-Billing");
	private static String prefix[] = Constants.coreCfg.getNumberingPlan();

	/**
	 * insert ra_detail from MSC.
	 * 
	 * @param msc
	 * @param detailType
	 * @param reportType
	 * @return
	 */
	protected RaDetail insertRaDetail4Msc(CallsNoDup msc, String detailType, String reportType) {
		// List<RaDetail> detailList = new ArrayList<RaDetail>();
		String localTimezoneStr = Constants.coreCfg.getLocalTimezone();

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

		// detailList.add(detail);

		// System.out.println(moc.getCallTime()+"/"+Tools.getLocalCallTime("GMT",moc.getCallTime()));
		// try {
		// dao.insertRaDetailTable(detail, operator);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }

		return detail;
	}

	/**
	 * @20141023
	 * @author Daniel.liu get tap_code and imsi_prefix with imsi and range from
	 *         tapcode list.
	 * @param operator
	 * @param report
	 * @param simsi
	 * @return
	 */
	private String getImsiPrefixTapCode(Map<String, String> tapCodeMap, long simsi) {
		String imsiPrefixTapCode = null;
		if (tapCodeMap != null) {
			for (String imsiRange : tapCodeMap.keySet()) {
				long startImsi = new Long(imsiRange.split("-")[0]).longValue();
				long endImsi = new Long(imsiRange.split("-")[1]).longValue();
				if (simsi >= startImsi && simsi <= endImsi) {
					imsiPrefixTapCode = tapCodeMap.get(imsiRange);
					break;
				}
				continue;
			}
		}

		if (imsiPrefixTapCode == null) {
			logger.info("tapCode is not found in tap_code list, plz add this. imsi_prefix=" + (simsi + "").substring(0, 5) + ", imsi=" + simsi);
			imsiPrefixTapCode = (simsi + "").substring(0, 5) + ":Unkown";
		}

		return imsiPrefixTapCode;

	}

	/**
	 * for realtime operation.
	 * 
	 * @param args
	 * @param operator
	 * @param report
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Override
	public boolean summary(String args[], String operator, Report report) throws SQLException, ParseException {
		boolean flag = false;
		InBoundRoamingSummaryDao dao = new InBoundRoamingSummaryDao(report);
		Calendar base = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		String startDate = null;
		int calDays = report.getExecuteDaysDefault();

		if (args != null & args.length > 1) {
			startDate = args[0];
			base = Tools.getCalByStrHms(startDate);
			c = Tools.getCalByStrHms(startDate);
			calDays = new Integer(args[1]);
			logger.info("args[] startDate,calDays,base:" + startDate + "," + calDays + "," + base.getTime());
		}

		base.add(Calendar.DAY_OF_MONTH, -calDays);

		while (c.compareTo(base) > 0) {

			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY);
			String yes = formatter.format(c.getTime());
			HashMap<String, String> result = new HashMap<String, String>();
			result.put("traffic_date", yes);

			long startTime4Day = System.currentTimeMillis();
			// System.out.println(yes+" Executing start: " + new Date());
			logger.info(yes + " Executing start: " + new Date());

			// step0.1: check if the day's moc data
			// generated, if no,
			// return.
			Calendar c2 = c;
			SimpleDateFormat formatter2 = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY2);
			// formatter2.setTimeZone(TimeZone.getTimeZone(GkConstant.GMT));
			String yes2 = formatter2.format(c2.getTime()) + Constants.START_HOUR;
			logger.info("yes2: " + yes2);
			if (!dao.checkIfMSCDataProcessedByPP(yes2, operator, true, report)) {
				// System.out.println("The Moc Data of " +
				// formatter2.format(c.getTime()) +
				// " hasn't been generated...");
				logger.info("The Data of " + formatter2.format(c.getTime()) + " hasn't been generated by pp...");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			// step0.2: check if the day's summary has been generated, if yes,
			// return.
			if (dao.checkIfExist(yes, operator, report.getType())) {
				// System.out.println("The report of "+yes+" has existed...");
				logger.info("The report of " + yes + " has existed...\n");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			// step1: load MSC data
			// step1.1: load MSC data of one day
			Map<String, Object> map = dao.getMsc(yes, operator, true);
			List<CallsNoDup> mscList = (List<CallsNoDup>) map.get("list");
			HashMap<String, CallsNoDup> mscHashMap = (HashMap<String, CallsNoDup>) map.get("hashMap");

			// step2: check MSC data
			if (mscList.size() == 0) {
				logger.info("msc is null.");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			// step3: accumulate all counts(record/duration/amount) of type.
			logger.debug("start accumulate all counts(record/duration/amount) of type ...");

			// step3.1: get all kinds of count for MSC.
			long startTime = System.currentTimeMillis();
			logger.info("start accumulate for MSC..." + new Date());

			Map<String, String> tapCodeMap = dao.getTapcodeMap(operator);
			Map<String, Long> callCountMap4In = new HashMap<String, Long>();
			Map<String, Long> callCountVoiceMap4In = new HashMap<String, Long>();
			Map<String, Long> callCountSmsMap4In = new HashMap<String, Long>();
			Map<String, Long> durationCountMap4In = new HashMap<String, Long>();
			Map<String, Long> amountCountMap4In = new HashMap<String, Long>();
			Map<String, Long> callCountMap4Out = new HashMap<String, Long>();
			Map<String, Long> callCountVoiceMap4Out = new HashMap<String, Long>();
			Map<String, Long> callCountSmsMap4Out = new HashMap<String, Long>();
			Map<String, Long> durationCountMap4Out = new HashMap<String, Long>();
			Map<String, Long> amountCountMap4Out = new HashMap<String, Long>();
			long minCallId4In = 0;
			long maxCallId4In = 0;
			long minCallId4Out = 0;
			long maxCallId4Out = 0;
			List<RaDetail> detailList = new ArrayList<RaDetail>();

			for (CallsNoDup call : mscList) {
				// @20141022, remove 5/6 length checking according to request of
				// customer, just imsi range check.
				// String imsiPrefix6 =
				// call.getsImsi()==0?"0":(call.getsImsi()+"").substring(0,6);

				long simsi = call.getsImsi();
				// check which imsi_range and get "imsi_prefix:tap_code" as map
				// key.
				String imsiPrefixTapCode = this.getImsiPrefixTapCode(tapCodeMap, simsi);

				if ("2".equals(call.getCallType()) || "30".equals(call.getCallType())) {// MTC,
																						// incoming
					minCallId4In = call.getCallId();// give initial value
													// instead of 0.
					if (minCallId4In > call.getCallId())
						minCallId4In = call.getCallId();
					if (maxCallId4In < call.getCallId())
						maxCallId4In = call.getCallId();

					if ("0".equals(imsiPrefixTapCode)) {
						// MOC and MTC should has s_imsi value, so exception is
						// very few.
						logger.info("imsi_prefix is 0, s_imsi=" + call.getsImsi() + ",s_msisdn=" + call.getCallingNum() + ",o_msisdn="
								+ call.getCalledNum() + ",call_type=" + call.getCallType() + ",call_time=" + call.getCallTime());
					} else {
						// accumulate call count
						long callCount = callCountMap4In.get(imsiPrefixTapCode) == null ? 0 : callCountMap4In.get(imsiPrefixTapCode).longValue();
						callCountMap4In.put(imsiPrefixTapCode, callCount + 1);

						if ("2".equals(call.getCallType())) {// for voice
							long callCountVoice = callCountVoiceMap4In.get(imsiPrefixTapCode) == null ? 0 : callCountVoiceMap4In.get(
									imsiPrefixTapCode).longValue();
							callCountVoiceMap4In.put(imsiPrefixTapCode, callCountVoice + 1);
						} else {// for sms
							long callCountSms = callCountSmsMap4In.get(imsiPrefixTapCode) == null ? 0 : callCountSmsMap4In.get(imsiPrefixTapCode)
									.longValue();
							callCountSmsMap4In.put(imsiPrefixTapCode, callCountSms + 1);
						}

						// accumulate duration
						long durationCount = durationCountMap4In.get(imsiPrefixTapCode) == null ? 0 : durationCountMap4In.get(imsiPrefixTapCode)
								.longValue();
						durationCountMap4In.put(imsiPrefixTapCode, durationCount + call.getDuration());

						// accumulate amount
						// long amountCount =
						// amountCountMap.get(futherCallType)==null?0:amountCountMap.get(futherCallType).longValue();
						// amountCountMap.put(futherCallType, ++amountCount+0);

						// put inbound roaming incoming into ra_detail.
						// @20141023, add tap code for ra_detail, only for 401
						// report.
						RaDetail rd = this.insertRaDetail4Msc(call, "13", "401");
						rd.setTapCode(imsiPrefixTapCode.split(":")[1]);
						detailList.add(rd);
					}
					continue;

				} else if ("1".equals(call.getCallType()) || "31".equals(call.getCallType())) {// MOC,
																								// outgoing
					minCallId4Out = call.getCallId();// give initial value
														// instead of 0.
					if (minCallId4Out > call.getCallId())
						minCallId4Out = call.getCallId();
					if (maxCallId4Out < call.getCallId())
						maxCallId4Out = call.getCallId();

					if ("0".equals(imsiPrefixTapCode)) {
						// MOC and MTC should has s_imsi value, so exception is
						// very few.
						logger.info("imsi_prefix is 0, s_imsi=" + call.getsImsi() + ",s_msisdn=" + call.getCallingNum() + ",o_msisdn="
								+ call.getCalledNum() + ",call_type=" + call.getCallType() + ",call_time=" + call.getCallTime());

					} else {
						// accumulate call count
						long callCount = callCountMap4Out.get(imsiPrefixTapCode) == null ? 0 : callCountMap4Out.get(imsiPrefixTapCode).longValue();
						callCountMap4Out.put(imsiPrefixTapCode, callCount + 1);

						if ("1".equals(call.getCallType())) {// for voice
							long callCountVoice = callCountVoiceMap4Out.get(imsiPrefixTapCode) == null ? 0 : callCountVoiceMap4Out.get(
									imsiPrefixTapCode).longValue();
							callCountVoiceMap4Out.put(imsiPrefixTapCode, callCountVoice + 1);
						} else {// for sms
							long callCountSms = callCountSmsMap4Out.get(imsiPrefixTapCode) == null ? 0 : callCountSmsMap4Out.get(imsiPrefixTapCode)
									.longValue();
							callCountSmsMap4Out.put(imsiPrefixTapCode, callCountSms + 1);
						}

						// accumulate duration
						long durationCount = durationCountMap4Out.get(imsiPrefixTapCode) == null ? 0 : durationCountMap4Out.get(imsiPrefixTapCode)
								.longValue();
						durationCountMap4Out.put(imsiPrefixTapCode, durationCount + call.getDuration());

						// accumulate amount
						// long amountCount =
						// amountCountMap.get(futherCallType)==null?0:amountCountMap.get(futherCallType).longValue();
						// amountCountMap.put(futherCallType, ++amountCount+0);

						// put inbound roaming incoming into ra_detail.
						// @20141023, add tap code for ra_detail, only for 401
						// report.
						RaDetail rd = this.insertRaDetail4Msc(call, "14", "401");
						rd.setTapCode(imsiPrefixTapCode.split(":")[1]);
						detailList.add(rd);
					}
					continue;

				}

			}

			// step 3.2: put into summary pojo list.
			List<InboundRoamingSummary> sumList = new ArrayList<InboundRoamingSummary>();

			// step 3.2.1: MTC, incoming for tapcode.
			Set<String> keys4In = callCountMap4In.keySet();// imsi_prefix and
															// tap_code is key.
			for (String imsiPrefixTapCode : keys4In) {
				InboundRoamingSummary sum = new InboundRoamingSummary();
				sum.setTrafficDirection("Incoming");

				if (callCountMap4In.containsKey(imsiPrefixTapCode))
					sum.setCallCount(callCountMap4In.get(imsiPrefixTapCode));

				if (callCountVoiceMap4In.containsKey(imsiPrefixTapCode))
					sum.setCallCountVoice(callCountVoiceMap4In.get(imsiPrefixTapCode));

				if (callCountSmsMap4In.containsKey(imsiPrefixTapCode))
					sum.setCallCountSms(callCountSmsMap4In.get(imsiPrefixTapCode));

				if (durationCountMap4In.containsKey(imsiPrefixTapCode))
					sum.setDurationCount(durationCountMap4In.get(imsiPrefixTapCode));

				if (amountCountMap4In.containsKey(imsiPrefixTapCode))
					sum.setAmountCount(amountCountMap4In.get(imsiPrefixTapCode));

				// compile with yyyy-mm-dd and yyyy-mm-dd hh two models.
				if (yes.length() <= 10)
					sum.setTrafficDate(yes);
				else {
					sum.setTrafficDate(yes.substring(0, 10));
					sum.setHour(yes.substring(10, 2));
				}

				// get others.
				sum.setImsiPrefix(imsiPrefixTapCode.split(":")[0]);
				sum.setTapcode(imsiPrefixTapCode.split(":")[1]);
				sum.setMinCallId(minCallId4In);
				sum.setMaxCallId(maxCallId4In);
				sum.setReportType(401);
				sumList.add(sum);
			}

			// step 3.2.2: MOC, outgoing for tapcode.
			Set<String> keys4Out = callCountMap4Out.keySet();// imsi_prefix is
																// key.
			for (String imsiPrefixTapCode : keys4Out) {
				InboundRoamingSummary sum = new InboundRoamingSummary();
				sum.setTrafficDirection("Outgoing");

				if (callCountMap4Out.containsKey(imsiPrefixTapCode))
					sum.setCallCount(callCountMap4Out.get(imsiPrefixTapCode));

				if (callCountVoiceMap4Out.containsKey(imsiPrefixTapCode))
					sum.setCallCountVoice(callCountVoiceMap4Out.get(imsiPrefixTapCode));

				if (callCountSmsMap4Out.containsKey(imsiPrefixTapCode))
					sum.setCallCountSms(callCountSmsMap4Out.get(imsiPrefixTapCode));

				if (durationCountMap4Out.containsKey(imsiPrefixTapCode))
					sum.setDurationCount(durationCountMap4Out.get(imsiPrefixTapCode));

				if (amountCountMap4Out.containsKey(imsiPrefixTapCode))
					sum.setAmountCount(amountCountMap4Out.get(imsiPrefixTapCode));

				// compile with yyyy-mm-dd and yyyy-mm-dd hh two models.
				if (yes.length() <= 10)
					sum.setTrafficDate(yes);
				else {
					sum.setTrafficDate(yes.substring(0, 10));
					sum.setHour(yes.substring(10, 2));
				}

				// get others.
				sum.setImsiPrefix(imsiPrefixTapCode.split(":")[0]);
				sum.setTapcode(imsiPrefixTapCode.split(":")[1]);
				sum.setMinCallId(minCallId4Out);
				sum.setMaxCallId(maxCallId4Out);
				sum.setReportType(401);
				sumList.add(sum);
			}

			long endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step4: insert count values.
			logger.debug("batch insert into ra_traffic_suymmary..." + new Date());
			startTime = System.currentTimeMillis();

			dao.insertInRoamingSummary(sumList, operator);

			endTime = System.currentTimeMillis();

			logger.info("summary list inserted size of this day is " + sumList.size());
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step5: insert ra_detail
			dao.batchInsertRaDetailTable(detailList, operator);

			// step6: update status for exporter and wait for exporting.
			// no need this now, as current RA export files are generated when
			// downloading, but only one time generation.
			// dao.updateStatus4Exporter(yes, operator);

			// step7: day-1, reverse order.
			c.add(Calendar.DAY_OF_MONTH, -1);

			long endTime4Day = System.currentTimeMillis();
			logger.info(yes + " Cost Time: " + (endTime4Day - startTime4Day) + " ms\n");
		}

		flag = true;
		return flag;
	}

	/**
	 * for redo operation.
	 * 
	 * @param args
	 * @param operator
	 * @param report
	 * @throws SQLException
	 * @throws ParseException
	 */
	@Override
	public boolean summaryRedo(String args[], String operator, Report report) throws SQLException, ParseException {
		boolean flag = false;
		return flag;
	}
}
