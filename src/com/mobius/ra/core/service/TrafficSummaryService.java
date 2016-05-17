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

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;
import com.mobius.ra.core.dao.TrafficSummaryDao;
import com.mobius.ra.core.pojo.CallsNoDup;
import com.mobius.ra.core.pojo.RaBilling;
import com.mobius.ra.core.pojo.RaTrafficSummary;
import com.mobius.ra.core.pojo.Report;

/**
 * @author Daniel.liu
 * @date Nov 11, 2013
 * @version v 1.0
 */
public class TrafficSummaryService implements ServiceInterface{
	private static String countryPrefix = Constants.coreCfg.getCountryPrefix();
	private static Logger logger = Logger.getLogger("RA-Billing");
	private static String prefix[] = Constants.coreCfg.getNumberingPlan();

	/**
	 * check if omsisdn is on-net according with numbering plan.
	 * @param msisdn
	 * @return
	 */
	private boolean checkIfOnNet(String omsisdn){
		//System.out.println("checkIfOnNet/omsisdn: "+omsisdn);
		for(int i=0; i<prefix.length; i++){
			//System.out.println("numbering_plan: "+prefix[i]);
			if(!Tools.checkNullorSpace(omsisdn)){
				if(omsisdn.length()<=3)
					return true;
				
				if(omsisdn.length()==6&&omsisdn.startsWith(countryPrefix))
					return true;
				
				if(omsisdn.substring(countryPrefix.length()).startsWith(prefix[i]))
					return true;
			}
		}
		return false;
	}
	
	/**
	 * Param: s_msisdn, o_msisdn.
	 * 
	 * get further call type for each record according to numbering plan.
	 * @return
	 */
	private int getFurtherCallType(String smsisdn, String omsisdn, String callType, long duration){
		int furtherCallType = 0;
		switch(new Integer(callType).intValue()){
		//101,102,103,201,202,203,111,112,113,211,212,213
		case 1:
			if(omsisdn.length()>3 && !omsisdn.startsWith(countryPrefix)){
				furtherCallType = 103;
			}else if(this.checkIfOnNet(omsisdn)){
				furtherCallType = 101;
			}else
				furtherCallType = 102;	
			break;
		case 2:
			if(omsisdn.length()<3 || omsisdn.length()>3 && !omsisdn.startsWith(countryPrefix)){
				furtherCallType = 203;
			}else if(this.checkIfOnNet(omsisdn)){
				furtherCallType = 201;
			}else
				furtherCallType = 202;		
			break;
		case 31:
			if(omsisdn.length()>3 && !omsisdn.startsWith(countryPrefix)){
				furtherCallType = 113;
			}else if(this.checkIfOnNet(omsisdn)){
				furtherCallType = 111;
			}else
				furtherCallType = 112;		
			break;
		case 30:
			if(omsisdn.length()<3 || omsisdn.length()>3 && !omsisdn.startsWith(countryPrefix)){
				furtherCallType = 213;
			}else if(this.checkIfOnNet(omsisdn)){
				furtherCallType = 211;
			}else
				furtherCallType = 212;		
			break;
		default:
			break;
		}
			
		return furtherCallType;
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
		TrafficSummaryDao dao = new TrafficSummaryDao(report);
		Calendar base = Calendar.getInstance();
		Calendar c = Calendar.getInstance();
		base.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		c.add(Calendar.DAY_OF_MONTH, report.getStartDateBeforeCurrent());
		String startDate = null;
		int calDays = report.getExecuteDaysDefault();
		//String detailTypes[] = report.getSqls().get(Constants.DETAIL_TYPE).split(",");
		//logger.debug("detailTypes: " + detailTypes[0] + "," + detailTypes[1]);
		
		int []feed_type = report.getFeedType();
		int []furtherCallTypes = {101,102,103,201,202,203,111,112,113,211,212,213};//report.getFurtherCallType();
		

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

			// step0.1: check if the day's moc data and billing data has been
			// generated, if no,
			// return.
			Calendar c2 = c;
			SimpleDateFormat formatter2 = new SimpleDateFormat(Constants.DATE_FORMAT_YEAR_MONTH_DAY2);
			// formatter2.setTimeZone(TimeZone.getTimeZone(GkConstant.GMT));
			String yes2 = formatter2.format(c2.getTime()) + Constants.START_HOUR;
			logger.info("yes2: " + yes2);
			if (!dao.checkIfDataProcessedByPP(yes2, operator, true, report)) {
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

			// step2: load Billing data
			// step2.1: load MSC data of one day
			map = dao.getBilling(yes, operator, true);
			List<RaBilling> billingList = (List<RaBilling>) map.get("list");
			HashMap<String, RaBilling> billingHashMap = (HashMap<String, RaBilling>) map.get("hashMap");
			
			if (mscList.size() == 0 || billingList.size() == 0) {
				logger.info("msc or billing data is null.");
				c.add(Calendar.DAY_OF_MONTH, -1);
				continue;
			}

			// step3: accumulate all counts(record/duration/amount) of type.
			logger.debug("start accumulate all counts(record/duration/amount) of type ...");
			
			// step3.1: get all kinds of count for MSC.
			long startTime = System.currentTimeMillis();
			logger.info("start accumulate for MSC..." + new Date());
			
			Map<Integer, Long> callCountMap = new HashMap<Integer, Long>();
			Map<Integer, Long> durationCountMap = new HashMap<Integer, Long>();
			Map<Integer, Long> amountCountMap = new HashMap<Integer, Long>();
			
			for(CallsNoDup call: mscList){
				int futherCallType = this.getFurtherCallType(call.getCallingNum(), call.getCalledNum(), call.getCallType(), call.getDuration());
				for(int i=0; i<furtherCallTypes.length; i++){
					if(furtherCallTypes[i]==futherCallType){
						//accumulate call count
						long callCount = callCountMap.get(futherCallType)==null?0:callCountMap.get(futherCallType).longValue();
						callCountMap.put(futherCallType, callCount+1);
						
						//accumulate duration
						long durationCount = durationCountMap.get(futherCallType)==null?0:durationCountMap.get(futherCallType).longValue();
						durationCountMap.put(futherCallType, durationCount+call.getDuration());
						
						//accumulate amount
						//long amountCount = amountCountMap.get(futherCallType)==null?0:amountCountMap.get(futherCallType).longValue();
						//amountCountMap.put(futherCallType, ++amountCount+0);
						
						continue;
					}
				}
					
			}
			
			// step 3.2: put into summary pojo list.
			List<RaTrafficSummary> sumList = new ArrayList<RaTrafficSummary>();
			for(int i=0; i<furtherCallTypes.length; i++){
				RaTrafficSummary sum = new RaTrafficSummary();
				sum.setCallType(furtherCallTypes[i]+"");
				
				//maybe there is no records for some call_type defined in furtherCallTypes of config.xml, we need to consider this.
				if(callCountMap.containsKey(furtherCallTypes[i]))
					sum.setRecordCount(callCountMap.get(furtherCallTypes[i]));
				
				if(durationCountMap.containsKey(furtherCallTypes[i]))
					sum.setDurationCount(durationCountMap.get(furtherCallTypes[i]));
				
				if(amountCountMap.containsKey(furtherCallTypes[i]))
					sum.setAmountCount(amountCountMap.get(furtherCallTypes[i]));
				
				sum.setTrafficDate(yes);
				sum.setReportType(301);
				sum.setFeedType(101);
				sumList.add(sum);
			}
			
			long endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step4.1: get all kinds of count for IN.
			startTime = System.currentTimeMillis();
			logger.info("start accumulate for IN..." + new Date());
			
			callCountMap.clear();
			durationCountMap.clear();
			amountCountMap.clear();
			
			for(RaBilling billing: billingList){
				int futherCallType = this.getFurtherCallType(billing.getCallingNum(), billing.getCalledNum(), billing.getCallType(), billing.getDuration());
				for(int i=0; i<furtherCallTypes.length; i++){
					if(furtherCallTypes[i]==futherCallType){
						//accumulate call count
						long callCount = callCountMap.get(futherCallType)==null?0:callCountMap.get(futherCallType).longValue();
						callCountMap.put(futherCallType, callCount+1);
						
						//accumulate duration
						long durationCount = durationCountMap.get(futherCallType)==null?0:durationCountMap.get(futherCallType).longValue();
						durationCountMap.put(futherCallType, durationCount+billing.getDuration());
						
						//accumulate amount
						long amountCount = amountCountMap.get(futherCallType)==null?0:amountCountMap.get(futherCallType).longValue();
						amountCountMap.put(futherCallType, amountCount+billing.getCallAmount());
					}
				}
					
			}
			
			// step 4.2: put into summary pojo list.
			for(int i=0; i<furtherCallTypes.length; i++){
				RaTrafficSummary sum = new RaTrafficSummary();
				sum.setCallType(furtherCallTypes[i]+"");
				
				if(callCountMap.containsKey(furtherCallTypes[i]))
					sum.setRecordCount(callCountMap.get(furtherCallTypes[i]));
				
				if(durationCountMap.containsKey(furtherCallTypes[i]))
					sum.setDurationCount(durationCountMap.get(furtherCallTypes[i]));
				
				if(amountCountMap.containsKey(furtherCallTypes[i]))
					sum.setAmountCount(amountCountMap.get(furtherCallTypes[i]));
							
				sum.setTrafficDate(yes);
				sum.setReportType(301);
				sum.setFeedType(301);
				sumList.add(sum);
			}
			
			endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step6: insert count values.
			logger.debug("batch insert into ra_traffic_suymmary..."+new Date());
			startTime = System.currentTimeMillis();
			
			dao.insertRaTrafficSummary(sumList, operator);
			
			endTime = System.currentTimeMillis();
			logger.info("Cost Time: " + (endTime - startTime) + " ms");

			// step7: update status for exporter and wait for exporting.
			dao.updateStatus4Exporter(yes, operator);

			// step8: day-1, reverse order.
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
	public boolean summaryRedo(String args[], String operator, Report report)
			throws SQLException, ParseException {
		boolean flag = false;
		return flag;
	}
}
