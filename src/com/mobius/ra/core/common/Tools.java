package com.mobius.ra.core.common;

import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class Tools {
	private static Logger logger = Logger.getLogger("RA");

	public static String addDays(String date, int days) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.DAY_OF_MONTH, days);
		return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
	}

	public static boolean checkCallTime(String mocCallTime, String inCallTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mocDate = null;
		Date inDate = null;
		try {
			mocDate = sdf.parse(mocCallTime);
			inDate = sdf.parse(inCallTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		java.util.Calendar mocCal = java.util.Calendar.getInstance();
		java.util.Calendar inCal = java.util.Calendar.getInstance();
		if (mocDate != null) {
			mocCal.setTime(mocDate);
		}
		if (inDate != null) {
			inCal.setTime(inDate);
		}
		// mocCal.add(java.util.Calendar.SECOND, 3600*5);

		for (int i = 0; i < Constants.coreCfg.getCtDeviation(); i++) {
			if (inCal.compareTo(mocCal) == 0)
				return true;
			else if (inCal.compareTo(mocCal) > 0) {
				mocCal.add(java.util.Calendar.SECOND, 1);
				if (inCal.compareTo(mocCal) == 0)
					return true;
			} else if (inCal.compareTo(mocCal) < 0) {
				mocCal.add(java.util.Calendar.SECOND, -1);
				if (inCal.compareTo(mocCal) == 0)
					return true;
			}
		}

		return false;
	}

	public static boolean checkCallTime(String mocCallTime, String inCallTime, int diviation) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date mocDate = null;
		Date inDate = null;
		try {
			mocDate = sdf.parse(mocCallTime);
			inDate = sdf.parse(inCallTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		java.util.Calendar mocCal = java.util.Calendar.getInstance();
		java.util.Calendar inCal = java.util.Calendar.getInstance();
		if (mocDate != null) {
			mocCal.setTime(mocDate);
		}
		if (inDate != null) {
			inCal.setTime(inDate);
		}
		// mocCal.add(java.util.Calendar.SECOND, 3600*5);

		for (int i = 0; i < diviation; i++) {
			if (inCal.compareTo(mocCal) == 0)
				return true;
			else if (inCal.compareTo(mocCal) > 0) {
				mocCal.add(java.util.Calendar.SECOND, 1);
				if (inCal.compareTo(mocCal) == 0)
					return true;
			} else if (inCal.compareTo(mocCal) < 0) {
				mocCal.add(java.util.Calendar.SECOND, -1);
				if (inCal.compareTo(mocCal) == 0)
					return true;
			}
		}

		return false;
	}

	// public static String getPropertyValue(String propertyName) {
	// Properties prop = new Properties();
	// String fileName = "config/config.properties";
	// InputStream is;
	// try {
	// is = new FileInputStream(fileName);
	// prop.load(is);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return prop.getProperty(propertyName);
	// }

	public static boolean checkDuration(long mocDuraLong, long inDuraLong) {
		for (int i = 0; i < Constants.coreCfg.getCtDeviation(); i++) {
			if (mocDuraLong == inDuraLong)
				return true;
			else if (mocDuraLong < inDuraLong) {
				mocDuraLong++;
				if (mocDuraLong == inDuraLong)
					return true;
			} else if (mocDuraLong > inDuraLong) {
				inDuraLong++;
				if (mocDuraLong == inDuraLong)
					return true;
			}
		}
		return false;
	}

	// check if the given string is null or contain space
	public static boolean checkNullorSpace(String str) {
		if (str == null || str.equals(""))
			return true;
		return false;
	}

	public static boolean checkOmsisdn(String mscMsisdn, String tapMsisdn) {
		boolean result = false;
		// if (!tapMsisdn.startsWith(GkConstant.MSISDN_PREFIX))
		// tapMsisdn = GkConstant.MSISDN_PREFIX + tapMsisdn;
		// if (!tapMsisdn.startsWith(getPropertyValue("MSISDN_PREFIX")))
		// tapMsisdn = getPropertyValue("MSISDN_PREFIX") + tapMsisdn;

		// if (tapMsisdn.equals(mscMsisdn))
		// result = true;

		if (Tools.normalizeOmsisdn(mscMsisdn).equals(Tools.normalizeOmsisdn(tapMsisdn)))
			result = true;

		return result;
	}

	public static double formatDouble(double num) {
		DecimalFormat df = new DecimalFormat("######0.00");
		return Double.parseDouble(df.format(num));
	}

	public static Calendar getCalAfterOneHour(Calendar date) {
		date.add(java.util.Calendar.HOUR_OF_DAY, 1);
		return date;
	}

	public static Calendar getCalByStr(String str) {
		Date date = getDateByStr(str);
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		Cal.setTime(date);

		return Cal;
	}

	public static Calendar getCalByStrHms(String str) {
		String datetime = getHhmmss(str);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(datetime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.util.Calendar Cal = java.util.Calendar.getInstance();
		if (date != null) {
			Cal.setTime(date);
		}
		return Cal;
	}

	public static String getCalFullStr(Calendar cal) {
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String getCalStr(Calendar cal) {
		Date date = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		return sdf.format(date);
	}

	public static String getCalStrAfterOneDay(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		java.util.Calendar c = java.util.Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.add(java.util.Calendar.DAY_OF_MONTH, 1);

		return sdf.format(c.getTime());
	}

	// get the full name of the class
	public static String getClassFullname(Map<String, String> beans, String key) {
		String value = null;
		if (beans != null && beans.size() > 0) {
			value = beans.get(Constants.GET_MATCHING_CLASS);
		}
		return value;
	}

	public static Date getDateByStr(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static String getHhmmss(String str) {
		return str + " 00:00:00";
	}

	public static String getLocalCallTime(String timezone, String calltimePa, String localTimezoneStr) {
		String switchTimeZone = timezone;
		String calltime = calltimePa.subSequence(0, 19).toString();
		TimeZone swTimeZone = TimeZone.getTimeZone(switchTimeZone);
		String outPutStr = "0";
		if (!"".equals(calltime) && calltime.length() == 19) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(swTimeZone);
			Date parseDate = null;
			try {
				parseDate = format.parse(calltime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatOut.setTimeZone(TimeZone.getTimeZone(localTimezoneStr));
			outPutStr = formatOut.format(parseDate);

		}
		return outPutStr;
	}

	public static Long getLongValue(Long num) {
		if (num != null)
			return new Long(num);
		else
			return new Long(0);
	}

	public static Long getLongValue(String num) {
		if (num != null && !"".equalsIgnoreCase(num))
			return new Long(num);
		else
			return new Long(0);
	}

	public static boolean isProductEnv() {
		if (new File("/System").isDirectory()) {
			return false;
		} else {
			return true;
		}
	}

	public static void main(String[] args) {
		// System.out.println(checkCallTime("2011-10-08 12:09:20",
		// "2011-10-08 17:09:16"));
		// System.out.println("2011-12-09".replaceAll("-", ""));
		// String s = "8801750018047*0";
		// s = s.replaceAll("#", "");
		// System.out.println(s.contains("*0"));
		// int len = s.length();
		// System.out.println(s.substring(0, len-2));
		// System.out.println(getCalByStrHms("2011-12-20").getTime());
		// System.out.println(Tools.getLocalCallTime("GMT","2012-01-30 10:21:11")+"/"+Tools.normalizeCallTime("Indian/Maldives",
		// "2012-01-30 10:21:11"));
		// System.out.println(Tools.checkOMsisdn("9601234", "1234"));
		// System.out.println(Tools.normalizeOmsisdn("960401"));
		System.out.println(TimeZone.getTimeZone("Asia/Riyadh"));
		System.out.println(TimeZone.getTimeZone("Africa/Mogadishu"));
		System.out.println(TimeZone.getTimeZone("Africa/Algiers"));
		System.out.println(TimeZone.getTimeZone("Asia/Jerusalem"));

		// List list = new ArrayList();
		// for(int i=0;i<10;i++)
		// list.add(i+"");
		// List sub1 = list.subList(1, 3);
		// for(int i=0;i<sub1.size();i++)
		// System.out.println(sub1.get(i));

		String[] s1 = { "a", "b", "a", "c" };
		List<String> list = new ArrayList<String>();
		Set<String> billingFileSet = new HashSet<String>();
		for (String s : s1) {
			if (!list.contains(s)) {
				list.add(s);
			}
			billingFileSet.add(s);
		}

		StringBuilder sqlSb = new StringBuilder();
		sqlSb.append(" insert into ra_detail (call_type, s_msisdn, o_msisdn, duration, call_time,sw_id,s_imsi,s_imei,s_ci,s_lac,trunk_in,trunk_out,term_cause,term_reason,ss_code,charge_indicator,detail_type,report_type,file_name,insert_time,volume_download,volume_upload,charge_identifier,sgsn_address,ggsn_address,pdp_address,short_num,group_num");
		sqlSb.append(") values (");
		sqlSb.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

		// @20141023, add this for inbound roaming report.
		if (true)
			sqlSb.replace(sqlSb.indexOf(") values ("), sqlSb.indexOf(") values (") + 10, ",tap_code) values (?,");
		System.out.println(sqlSb);

		if (true)
			sqlSb.replace(sqlSb.indexOf(") values ("), sqlSb.indexOf(") values (") + 10, ",test2) values (?,");
		System.out.println(sqlSb);

		// for (String s : list)
		// System.out.println(s);
		// for (String s : billingFileSet)
		// System.out.println(s);

		// System.out.println(Tools.getCalFullStr(Calendar.getInstance()));
		// System.out.println("123456".substring(0, 5));
	}

	public static String normalizeCallTime(String timezone, String calltimePa) {
		String switchTimeZone = timezone;
		String calltime = calltimePa.subSequence(0, 19).toString();
		TimeZone swTimeZone = TimeZone.getTimeZone(switchTimeZone);
		String outPutStr = "0";
		if (!"".equals(calltime) && calltime.length() == 19) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(swTimeZone);
			Date parseDate = null;
			try {
				parseDate = format.parse(calltime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatOut.setTimeZone(TimeZone.getTimeZone("GMT"));
			outPutStr = formatOut.format(parseDate);

		}
		return outPutStr;
	}

	public static String normalizeCallTime(String timezone, String calltimePa, int deviationDay) {
		String switchTimeZone = timezone;
		String calltime = calltimePa.subSequence(0, 19).toString();
		TimeZone swTimeZone = TimeZone.getTimeZone(switchTimeZone);
		String outPutStr = "0";
		if (!"".equals(calltime) && calltime.length() == 19) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(swTimeZone);
			Date parseDate = null;
			Date newDate = null;
			try {
				parseDate = format.parse(calltime);
				newDate = new Date(parseDate.getTime() + (1000 * 60 * 60 * 24) * deviationDay);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			SimpleDateFormat formatOut = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatOut.setTimeZone(TimeZone.getTimeZone("GMT"));
			outPutStr = formatOut.format(newDate);

		}
		return outPutStr;
	}

	public static String normalizeOmsisdn(String omsisdn) {
		String newOmsisdn = omsisdn;
		// if(omsisdn.startsWith("960960")){
		// newOmsisdn=omsisdn.substring(3);
		// }
		// if(omsisdn.startsWith("9600")){
		// newOmsisdn=omsisdn.substring(4);
		// }
		// if(omsisdn.startsWith("960")&&omsisdn.length()!=10){
		// newOmsisdn=omsisdn.substring(3);
		// }
		return newOmsisdn;
	}

	// Change prefix from 972 to 970
	public static String changePrefix(String num) {
		if (num.length() < 3) {
			return num;
		}
		if (num.substring(0, 3).equals("972")) {
			return "970" + num.substring(3, num.length());
		} else {
			return num;
		}
	}

}