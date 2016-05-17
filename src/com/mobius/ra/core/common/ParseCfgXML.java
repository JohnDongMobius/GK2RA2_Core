package com.mobius.ra.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.mobius.ra.core.pojo.FeedInfo;
import com.mobius.ra.core.pojo.FileReport;
import com.mobius.ra.core.pojo.RaCoreCfg;
import com.mobius.ra.core.pojo.Report;
import com.mobius.ra.core.pojo.TransitionStatus;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class ParseCfgXML {
	private static boolean checkNotNvlSpace(String str) {
		if (str != null && !str.equalsIgnoreCase(""))
			return true;
		else
			return false;
	}

	public static void main(String args[]) {
		// System.out.println(checkNotNvlSpace("")?new
		// Integer(""):Constants.ACTIVE_DEFAULT);
		// test();
		parse();
	}

	// parse the RA core configuration file
	public static RaCoreCfg parse() {
		SAXReader reader = new SAXReader();
		RaCoreCfg configObj = new RaCoreCfg();
		try {
			Document doc = reader.read(Constants.PATH_CONFIGXML);
			Node root = doc.selectSingleNode("/ra-core");

			// Parse common
			configObj.setIsProductMode(root.selectSingleNode("common/is-product-mode").getText());

			if (root.selectSingleNode("common/msisdn-prefix") != null)
				configObj.setMsisdnPrefix(root.selectSingleNode("common/msisdn-prefix").getText());
			System.out.println("common/msisdn-prefix: " + configObj.getMsisdnPrefix());

			// if(root.selectSingleNode("common/country-prefix")!=null)
			// configObj.setCountryPrefix(root.selectSingleNode("common/country-prefix").getText());
			// System.out.println("common/country-prefix: " +
			// configObj.getCountryPrefix());
			//
			// if(root.selectSingleNode("common/numbering-plan")!=null){
			// String numberingPlanStr =
			// root.selectSingleNode("common/numbering-plan").getText();
			// if(checkNotNvlSpace(numberingPlanStr)){
			// String []numberingPlan = numberingPlanStr.split(",");
			// configObj.setNumberingPlan(numberingPlan);
			// }
			// System.out.println("common/numbering-plan: " + numberingPlanStr);
			//
			// }
			//
			configObj.setLocalTimezone(root.selectSingleNode("common/local-timezone").getText());
			configObj.setOperatorCode(root.selectSingleNode("common/operator-code").getText());

			String sleep1 = root.selectSingleNode("common/sleep-duration-after-one-loop").getText();
			configObj.setSleepDurationAfterOneLoop(checkNotNvlSpace(sleep1) ? new Integer(sleep1) : Constants.SLEEP_DURATION_AFTER_ONE_LOOP_DEFAULT);

			String sleep2 = root.selectSingleNode("common/sleep-duration-of-subthread").getText();
			configObj.setSleepDurationOfSubthread(checkNotNvlSpace(sleep2) ? new Integer(sleep2) : Constants.SLEEP_DURATION_OF_SUBTHREAD_DEFAULT);

			String sleep3 = root.selectSingleNode("common/sleep-duration-of-paid").getText();
			configObj.setSleepDurationOfPaid(checkNotNvlSpace(sleep3) ? new Integer(sleep3) : Constants.SLEEP_DURATION_OF_PAID_DEFAULT);

			String ct_deviation = root.selectSingleNode("common/deviation/call-time").getText();
			configObj.setCtDeviation(checkNotNvlSpace(ct_deviation) ? new Integer(ct_deviation) : Constants.DEVIATION_DEFAULT);
			System.out.println("common/deviation/call-time: " + configObj.getCtDeviation());

			String dr_deviation = root.selectSingleNode("common/deviation/duration").getText();
			configObj.setDrDeviation(checkNotNvlSpace(dr_deviation) ? new Integer(dr_deviation) : Constants.DEVIATION_DEFAULT);
			System.out.println("common/deviation/duration: " + configObj.getDrDeviation());

			String go = root.selectSingleNode("common/generate-order").getText();
			configObj.setGenerateOrder(checkNotNvlSpace(go) ? go : Constants.GENERATE_ORDER_DEFAULT);

			Element transitionStatus = (Element) root.selectSingleNode("common/transition-status");
			if ("1".equalsIgnoreCase(transitionStatus.attributeValue("active"))) {
				String updatingFileName = transitionStatus.selectSingleNode("updating-filename").getText();
				TransitionStatus ts = new TransitionStatus();
				ts.setUpdatingFilename(checkNotNvlSpace(updatingFileName) ? updatingFileName : "2");
				configObj.setTransitionStatus(ts);
				System.out.println("common/transition-status/updating-filename = " + configObj.getTransitionStatus().getUpdatingFilename());
			}
			String noDataComparison = root.selectSingleNode("common/one-side-nodata-comparison").getText();
			configObj.setNoDataComparison(noDataComparison);
			System.out.println("common/one-side-nodata-comparison: " + configObj.getNoDataComparison());

			// Parse feed-list
			List feedList = root.selectNodes("feed-list/feed");
			List<FeedInfo> feedInfoList = new ArrayList<FeedInfo>();
			for (Object feedObj : feedList) {
				FeedInfo feedInfo = new FeedInfo();
				Element feed = (Element) feedObj;
				feedInfo.setType(Integer.parseInt(feed.attributeValue("type")));
				feedInfo.setActiveFlg(Integer.parseInt(feed.attributeValue("active")));
				feedInfo.setDatabase(feed.attributeValue("database"));
				feedInfo.setSql(feed.selectSingleNode("sql").getText());
				feedInfoList.add(feedInfo);
				System.out.println("feed_type = " + feedInfo.getType());
				System.out.println("active = " + feedInfo.getActiveFlg());
				System.out.println("database = " + feedInfo.getDatabase());
				System.out.println("sql = " + feedInfo.getSql());
				
				//Parse file-report if exist.
				FileReport fileReport = new FileReport();
				if(feed.selectSingleNode("file-report")!=null){
					Element fileReportElm = (Element)feed.selectSingleNode("file-report");
					fileReport.setActiveFlg(Integer.parseInt(fileReportElm.attributeValue("active")));
					if (fileReportElm.selectSingleNode("hostname") != null&&!"".equalsIgnoreCase(fileReportElm.selectSingleNode("hostname").getText())) {
						fileReport.setHostname(fileReportElm.selectSingleNode("hostname").getText());
					}
					if (fileReportElm.selectSingleNode("username") != null&&!"".equalsIgnoreCase(fileReportElm.selectSingleNode("username").getText())) {
						fileReport.setUsername(fileReportElm.selectSingleNode("username").getText());
					}
					if (fileReportElm.selectSingleNode("password") != null&&!"".equalsIgnoreCase(fileReportElm.selectSingleNode("password").getText())) {
						fileReport.setPassword(fileReportElm.selectSingleNode("password").getText());
					}
					if (fileReportElm.selectSingleNode("uploaded-txt-path") != null) {
						fileReport.setUploadedTxtPath(fileReportElm.selectSingleNode("uploaded-txt-path").getText());
					}
					if (fileReportElm.selectSingleNode("refer-sql") != null) {
						fileReport.setReferSql(fileReportElm.selectSingleNode("refer-sql").getText());
					}
					if (fileReportElm.selectSingleNode("compare-deviation-day") != null) {
						String deviationDay = fileReportElm.selectSingleNode("compare-deviation-day").getText();
						if(deviationDay!=null&&!"".equals(deviationDay)) {
							fileReport.setCompareDeviationDay(new Integer(deviationDay).intValue());
						} else {
							fileReport.setCompareDeviationDay(1);//default
						}
					} else {
						fileReport.setCompareDeviationDay(1);//default
					}
//					String deviationDay = fileReportElm.selectSingleNode("compare-deviation-day").getText();
//					if(deviationDay!=null&&!"".equals(deviationDay))
//						fileReport.setCompareDeviationDay(new Integer(deviationDay).intValue());
//					else
//						fileReport.setCompareDeviationDay(1);//default
					if (fileReportElm.selectSingleNode("match-pattern") != null) {
						fileReport.setMatchPattern(fileReportElm.selectSingleNode("match-pattern").getText());
					}
					feedInfo.setFileReport(fileReport);
					
					System.out.println("file-report.active = " + fileReport.getActiveFlg());
					System.out.println("file-report.hostname = " + fileReport.getHostname());
					System.out.println("file-report.username = " + fileReport.getUsername());
					System.out.println("file-report.password = " + fileReport.getPassword());
					System.out.println("file-report.uploaded-txt-path = " + fileReport.getUploadedTxtPath());
					System.out.println("file-report.refer-sql = " + fileReport.getReferSql());
					System.out.println("file-report.compare-deviation-day = " + fileReport.getCompareDeviationDay());
					System.out.println("file-report.match-pattern = " + fileReport.getMatchPattern());
				}
			}
			configObj.setFeedInfoList(feedInfoList);
			
			// Parse report-list
			List reports = root.selectNodes("report-list/report");
			List<Report> reportsObj = new ArrayList<Report>();
			for (Object o : reports) {
				Report reportObj = new Report();

				Element report = (Element) o;
				String name = report.attributeValue("name");
				String type = report.attributeValue("type");
				String mscFeedType = report.attributeValue("msc_feed_type");
				String billingFeedType = report.attributeValue("billing_feed_type");
				String isRecon = report.attributeValue("isRecon");
				reportObj.setName(name);
				reportObj.setType(type);
				if (mscFeedType != null && !mscFeedType.equals("")) {
					reportObj.setMscFeedType(Integer.parseInt(mscFeedType));
				}
				if (billingFeedType != null && !billingFeedType.equals("")) {
					reportObj.setBillingFeedType(Integer.parseInt(billingFeedType));
				}
				System.out.println("report-name/type/mscFeedType/billingFeedType = " + name + "/" + type + "/" + mscFeedType + "/" + billingFeedType);
				if (isRecon != null && !isRecon.equals("")) {
					reportObj.setIsRecon(isRecon);
				}
				System.out.println("report-name/type/mscFeedType/billingFeedType/isRecon = " + name + "/" + type + "/" + mscFeedType + "/" + billingFeedType
						+ "/" + isRecon);
				String active = report.selectSingleNode("active").getText();
				reportObj.setActive(checkNotNvlSpace(active) ? new Integer(active) : Constants.ACTIVE_DEFAULT);
				System.out.println("active = " + active);

				String threadNum = report.selectSingleNode("thread-num").getText();
				reportObj.setThreadNum(checkNotNvlSpace(threadNum) ? new Integer(threadNum) : Constants.THREAD_NUM_DEFAULT);
				System.out.println("thread_num = " + threadNum);

				String redoSwitch = report.selectSingleNode("redo-switch").getText();
				reportObj.setRedoSwitch(checkNotNvlSpace(redoSwitch) ? new Integer(redoSwitch) : Constants.REDO_SWITCH_DEFAULT);
				System.out.println("redo_switch = " + redoSwitch);

				if (report.selectSingleNode("short-active") != null) {
					String shortActive = report.selectSingleNode("short-active").getText();
					reportObj.setShortActive(Integer.parseInt(shortActive));
					System.out.println("short-active = " + shortActive);
				}
				if (report.selectSingleNode("msc-filename-update") != null) {
					String mscFileNameUpdate = report.selectSingleNode("msc-filename-update").getText();
					reportObj.setMscFilenameUpdate(Integer.parseInt(mscFileNameUpdate));
				}

				String date1 = report.selectSingleNode("start-date-before-current").getText();
				reportObj.setStartDateBeforeCurrent(checkNotNvlSpace(date1) ? new Integer(date1) : Constants.START_DATE_BEFORE_CURRENT_DEFAULT);

				String date2 = report.selectSingleNode("start-date-before-redo").getText();
				reportObj.setStartDateBeforeRedo(checkNotNvlSpace(date2) ? new Integer(date2) : Constants.START_DATE_BEFORE_CURRENT_REDO_DEFAULT);

				String date3 = report.selectSingleNode("execute-days-default").getText();
				reportObj.setExecuteDaysDefault(checkNotNvlSpace(date3) ? new Integer(date3) : Constants.EXECUTE_DAYS_DEFAULT);

				if(report.selectSingleNode("subscriber-type")!=null){
					String subcriberType = report.selectSingleNode("subscriber-type").getText();
					reportObj.setSubscriberType(checkNotNvlSpace(subcriberType) ? new Integer(subcriberType)
						: Constants.SUBSCRIBER_TYPE_DEFAULT);
				}
				
				if (report.selectSingleNode("exchange-msisdn") != null) {
					String exchangeMsisdn = report.selectSingleNode("exchange-msisdn").getText();
					reportObj.setExchangeMsisdn(checkNotNvlSpace(exchangeMsisdn) ? new Integer(exchangeMsisdn) : Constants.EXCHANGE_MSISDN_DEFAULT);
				}
				if (report.selectSingleNode("feed-types") != null) {
					String types = report.selectSingleNode("feed-types").getText();
					String typeStr[] = types.split(",");
					int[] feedType = new int[typeStr.length];
					if (checkNotNvlSpace(types)) {
						for (int i = 0; i < typeStr.length; i++) {
							feedType[i] = new Integer(typeStr[i].trim()).intValue();
						}
					}

					reportObj.setFeedType(feedType);
				}

				if (report.selectSingleNode("call-types") != null) {
					String types = report.selectSingleNode("call-types").getText();
					String typeStr[] = types.split(",");
					int[] furtherCallType = new int[typeStr.length];
					if (checkNotNvlSpace(types)) {
						for (int i = 0; i < typeStr.length; i++) {
							furtherCallType[i] = new Integer(typeStr[i].trim()).intValue();
						}
					}
					for (int f : furtherCallType)
						System.out.print(furtherCallType + ",");

					reportObj.setFurtherCallType(furtherCallType);
				}
				List beans = report.selectNodes("beans/bean");
				Map<String, String> beansMap = new HashMap<String, String>();
				if (beans != null && beans.size() > 0) {
					for (Object o2 : beans) {
						Element bean = (Element) o2;
						String id = bean.attributeValue("id");
						String classFullname = bean.attributeValue("class");
						String superFullname = bean.attributeValue("super");

						if (Constants.GET_MATCHING_CLASS.equals(id)) {
							beansMap.put(Constants.GET_MATCHING_CLASS, classFullname);
						}
					}
				}

				Element e = ((Element) report.selectSingleNode("sqls"));
				
				if(e.attributeValue("msc-pp-flg")!=null)
					reportObj.setMscPpFlg(e.attributeValue("msc-pp-flg"));
				System.out.println("msc-pp-flg = " + reportObj.getMscPpFlg());
				
				if(e.attributeValue("msc-pp-redo-flg")!=null)
					reportObj.setMscPpRedoFlg(e.attributeValue("msc-pp-redo-flg"));
				System.out.println("msc-pp-redo-flg = " + reportObj.getMscPpRedoFlg());
				
				if(e.attributeValue("billing-pp-flg")!=null)
					reportObj.setBillingPpFlg(e.attributeValue("billing-pp-flg"));
				System.out.println("billing-pp-flg = " + reportObj.getBillingPpFlg());
				
				if(e.attributeValue("billing-pp-redo-flg")!=null)
					reportObj.setBillingPpRedoFlg(e.attributeValue("billing-pp-redo-flg"));
				System.out.println("billing-pp-redo-flg = " + reportObj.getBillingPpRedoFlg());
				
				List sqls = report.selectNodes("sqls/sql");
				Map<String, String> sqlsMap = new HashMap<String, String>();
				for (Object o2 : sqls) {
					Element sql = (Element) o2;
					if (sql.selectSingleNode("method") != null) {
						String methodName = sql.selectSingleNode("method").getText();
						System.out.println("methodName = " + methodName);

						String sqlScript = sql.selectSingleNode("value").getText();
						System.out.println("sqlScript = " + sqlScript);

						sqlsMap.put(methodName, sqlScript);
					} else if (sql.selectSingleNode("detail-type") != null) {
						String detailType = sql.selectSingleNode("detail-type").getText();
						System.out.println("detailType = " + detailType);
						sqlsMap.put(Constants.DETAIL_TYPE, detailType);
					}
				}

				reportObj.setBeans(beansMap);
				reportObj.setSqls(sqlsMap);
				reportsObj.add(reportObj);
				System.out.println("");
			}

			configObj.setReports(reportsObj);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return configObj;
	}

	public static void test() {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read("config/config.xml");
			Node root = doc.selectSingleNode("/ra-core");

			// Parse common
			System.out.println(root.selectSingleNode("common/is-product-mode").getText());

			// Parse reports
			List reports = root.selectNodes("report-list/report");
			for (Object o : reports) {
				Element report = (Element) o;
				String name = report.attributeValue("name");
				System.out.println("report-name = " + name);

				String active = report.selectSingleNode("active").getText();
				System.out.println("active = " + active);

				String thread_num = report.selectSingleNode("thread-num").getText();
				System.out.println("thread_num = " + thread_num);

				String redo_switch = report.selectSingleNode("redo-switch").getText();
				System.out.println("redo_switch = " + redo_switch);

				List sqls = report.selectNodes("sqls/sql");
				for (Object o2 : sqls) {
					Element sql = (Element) o2;
					if (sql.selectSingleNode("method") != null) {
						String methodName = sql.selectSingleNode("method").getText();
						System.out.println("methodName = " + methodName);

						String sqlScript = sql.selectSingleNode("value").getText();
						System.out.println("sqlScript = " + sqlScript);
					} else if (sql.selectSingleNode("detail-type") != null) {
						String detailType = sql.selectSingleNode("detail-type").getText();
						System.out.println("detailType = " + detailType);
					}
				}

				System.out.println("");
			}

			List values = root.selectNodes("report-list/report/sqls/sql/value");
			for (Object o : values) {
				Element e = (Element) o;
				String sql_value = e.getText();
				// System.out.println("sql_value =" + sql_value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
