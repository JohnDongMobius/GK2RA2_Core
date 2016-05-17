package com.mobius.ra.core.service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.ReadRemoteFile;
import com.mobius.ra.core.dao.FeedDao;
import com.mobius.ra.core.pojo.FeedInfo;
import com.mobius.ra.core.pojo.RAFeed;
import com.mobius.ra.core.pojo.Report;

public class FeedService {
	
	private static Logger logger = Logger.getLogger("RA-Feed-Service");

	public void insertRAFeed(String trafficDate, int redoTimes, Report report) throws SQLException, CloneNotSupportedException {
		FeedDao dao = new FeedDao();
		ReadRemoteFile p = new ReadRemoteFile();
		List<FeedInfo> feedInfoList = Constants.coreCfg.getFeedInfoList();
		String aliasCore;
		String aliasFraud;
		int mscFeedType = report.getMscFeedType();
		int billingFeedType = report.getBillingFeedType();
		
		for (FeedInfo feedInfo : feedInfoList) {
			// alias = Constants.DB_NAME_FRAUD +
			// Constants.coreCfg.getOperatorCode();
			// int count = dao.checkFeedInserted(alias, feedInfo, trafficDate,
			// redoTimes);
			
			if (feedInfo.getActiveFlg() == 0) 
				continue;
			
			if(mscFeedType!=feedInfo.getType()&&billingFeedType!=feedInfo.getType())
				continue;
			
			if (feedInfo.getDatabase() != null) {
				aliasCore = Constants.DB_NAME_MEDIATOR + Constants.coreCfg.getOperatorCode();
			} else {
				aliasCore = Constants.DB_NAME_CORE + Constants.coreCfg.getOperatorCode();
			}
			
			//get file count for vs_summary_report and delete-insert.
			List<RAFeed> callCountFileList = dao.getRAFeedList(feedInfo, aliasCore, trafficDate);
			aliasFraud = Constants.DB_NAME_FRAUD + Constants.coreCfg.getOperatorCode();
			
			logger.info("delete ra_feed ... feed_type: "+feedInfo.getType());
			dao.deleteRaFeedByDate(trafficDate, aliasFraud, feedInfo.getType());
			
				
			//if need file-report
			if(feedInfo.getFileReport()!=null && feedInfo.getFileReport().getActiveFlg()==1){
				//for 472_2 old way
				if (feedInfo.getFileReport().getHostname() != null && feedInfo.getFileReport().getUsername() != null && feedInfo.getFileReport().getPassword() != null) {
					
					logger.info("insert ra_feed for vs_summary_report... feed_type: "+feedInfo.getType() + ", respective_type:1");
					dao.insertRAFeed(callCountFileList, trafficDate, aliasFraud);//insert for processedList for vs_report.
					
					String fileName = feedInfo.getFileReport().getMatchPattern().replace("?", trafficDate.replace("-", ""));			
					String remoteFilePathAndFileName = feedInfo.getFileReport().getUploadedTxtPath() + File.separatorChar + fileName;
					
					//get all uploaded file for one traffic date
					List<String> uploadedList = 
						p.retrieveFile(feedInfo.getFileReport().getHostname(), feedInfo.getFileReport().getUsername(), feedInfo.getFileReport().getPassword(), remoteFilePathAndFileName); 
					logger.debug("uploadedList size before removing processedList: "+uploadedList.size());
					
					//try to get unprocessed file comparing with at least three days processed file from call_count. 
					List<RAFeed> unprocessedList = new ArrayList<RAFeed>();
					List<String> unprocessedStrList = new ArrayList<String>();
					for(String s:uploadedList)
						unprocessedStrList.add(s);
					
					List<String> comparedStrList = new ArrayList<String>();
					List<RAFeed> comparedBufferList = dao.getRAFeedList(feedInfo, aliasCore, trafficDate, 1);
					for(RAFeed raf : comparedBufferList)
						comparedStrList.add(raf.getFileName());
					
					//get unprocessed list.
					unprocessedStrList.removeAll(comparedStrList);
					
					//get processed list.
					List<RAFeed> processedList = new ArrayList<RAFeed>();
					List<String> processedStrList = new ArrayList<String>();
					for(String s:uploadedList)
						processedStrList.add(s);
					processedStrList.removeAll(unprocessedStrList);
					
					logger.info("uploadedList/processList/unprocessList size: "+uploadedList.size()+"/"+processedStrList.size()+"/"+unprocessedStrList.size());
					
					//get file_size and unprocessed reason from table downloaded_file, load all records currently.
					Map<String,String> downloadedFileMap = dao.getDownloadedFileMap(feedInfo, aliasCore, trafficDate);
					logger.info("downloaedFile size: "+downloadedFileMap.size());
					
					for(String fileNameStr : unprocessedStrList){
						
						RAFeed feed = new RAFeed();
						feed.setFileName(fileNameStr);
						feed.setType(feedInfo.getType());
						feed.setRespectiveType(2);//in file-report, unprocessedList's repective_type=2.
						
						if(downloadedFileMap.containsKey(fileNameStr)){
							String str = downloadedFileMap.get(fileNameStr);
							String[] sa= str.split(",");
							if(sa!=null&&sa.length==2){
								if(sa[0]!=null)
									feed.setFileSizeBytes(new Integer(sa[0]).intValue());
								else 
									logger.info("we don't find fileSizeBytes"+fileNameStr);
								
								if(sa[1]!=null)
									feed.setUnprocessedReason(sa[1]);
								else
									logger.info("we don't find unprocessedReason for "+fileNameStr);
							}
						}else{
							logger.info(fileNameStr + " not found in downloaded_files, ");
						}
						unprocessedList.add(feed);
					}
					
					//arrange processedList and insert with respective_type=3.
					Map<String,String> comparedBufferMap = dao.getRAFeedMap(feedInfo, aliasCore, trafficDate, feedInfo.getFileReport().getCompareDeviationDay());
					for(String s:processedStrList){
						RAFeed feed = new RAFeed();
						if(comparedBufferMap.containsKey(s)){
							feed.setType(feedInfo.getType());
							feed.setFileName(s);
							feed.setFileSizeBytes(new Integer(comparedBufferMap.get(s)).intValue());
							feed.setRespectiveType(3);
						}
						processedList.add(feed);
					}
					
					logger.info("insert ra_feed for file-report processed, feed_type:"+feedInfo.getType()+", respective_type:3");
					dao.insertRAFeed(processedList, trafficDate, aliasFraud);//insert for unprocessedList.				
					
					logger.info("insert ra_feed for file-report unprocessed, feed_type:"+feedInfo.getType()+", respective_type:2");
					dao.insertRAFeed(unprocessedList, trafficDate, aliasFraud);//insert for unprocessedList.				
					
					logger.info("delete ra_feed_summary for " +trafficDate+", feed_type:"+feedInfo.getType());
					dao.deleteRaFeedSummaryByDate(trafficDate, aliasFraud, feedInfo.getType());//delete-insert for file-report summary.
					
					logger.info("insert ra_feed_summary for "+ trafficDate+","+uploadedList.size()+"/"+processedList.size()+"/"+unprocessedList.size()+", feed_type:"+feedInfo.getType());
					dao.insertRAFeedSummary(uploadedList.size(), processedList.size(), unprocessedList.size(), feedInfo.getType(), trafficDate, aliasFraud, redoTimes);
				} 
				
				else {//for current 425_6 and 472_2
					//try to get unprocessed file comparing with at least three days processed file from call_count. 
					List<RAFeed> unprocessedList = new ArrayList<RAFeed>();
					List<RAFeed> processedList = new ArrayList<RAFeed>();
					List<RAFeed> processedListVs = new ArrayList<RAFeed>();
					
					//get file_size and unprocessed reason from table downloaded_file, load all records currently.
					Map<String,String> downloadedFileMap = dao.getDownloadedFileMap(feedInfo, aliasCore, trafficDate);
					logger.info("downloaedFile size: "+downloadedFileMap.size());
					
					Iterator<Entry<String, String>> iter = downloadedFileMap.entrySet().iterator();
					while (iter.hasNext()) {
						String fileName = iter.next().getKey();
						String fileSizeUnprocessedReason = downloadedFileMap.get(fileName);
						RAFeed feed = new RAFeed();
						feed.setFileName(fileName);
						feed.setType(feedInfo.getType());
						feed.setRespectiveType(2);//in file-report, unprocessedList's repective_type=2.
						String[] sa= fileSizeUnprocessedReason.split(",");
						if(sa!=null&&sa.length==2){
							if(sa[0]!=null) {
								feed.setFileSizeBytes(new Integer(sa[0]).intValue());
							} else { 
								logger.info("we don't find fileSizeBytes"+fileName);
							}
							
							if(sa[1]!=null) {
								feed.setUnprocessedReason(sa[1]);
								if (sa[1].equals("1")) { //1 indicates file is  decoded with no error.
									feed.setRespectiveType(3);//in file-report, processedList's repective_type=3.
									processedList.add(feed);
									processedListVs.add(feed.clone());
								} else {
									feed.setRespectiveType(2);//in file-report, unprocessedList's repective_type=2.
									unprocessedList.add(feed);
								}
								
							} else {
								logger.info("we don't find unprocessedReason for "+fileName);
							}
						}
					}
					
					//for VS report.
					for(RAFeed feed : processedListVs){
						feed.setRespectiveType(1);
					}
					
					logger.info("insert ra_feed for vs_summary_report... feed_type: "+feedInfo.getType() + ", respective_type:1");
					//dao.insertRAFeed(processedListVs, trafficDate, aliasFraud);//insert for processedList for vs_report.
					dao.insertRAFeed(callCountFileList, trafficDate, aliasFraud);//insert for processedList for vs_report.
					
					logger.info("insert ra_feed for file-report processed, feed_type:"+feedInfo.getType()+", respective_type:3");
					dao.insertRAFeed(processedList, trafficDate, aliasFraud);//insert for processedList.				
					
					logger.info("insert ra_feed for file-report unprocessed, feed_type:"+feedInfo.getType()+", respective_type:2");
					dao.insertRAFeed(unprocessedList, trafficDate, aliasFraud);//insert for unprocessedList.				
					
					logger.info("delete ra_feed_summary for " +trafficDate+", feed_type:"+feedInfo.getType());
					dao.deleteRaFeedSummaryByDate(trafficDate, aliasFraud, feedInfo.getType());//delete-insert for file-report summary.
					
					logger.info("insert ra_feed_summary for "+ trafficDate+","+downloadedFileMap.size()+"/"+processedList.size()+"/"+unprocessedList.size()+", feed_type:"+feedInfo.getType());
					dao.insertRAFeedSummary(downloadedFileMap.size(), processedList.size(), unprocessedList.size(), feedInfo.getType(), trafficDate, aliasFraud, redoTimes);
				}
			}else{ //if no need file_report, still need to insert into ra_feeds for vs_report.
				logger.info("insert ra_feed for vs_summary_report... feed_type: "+feedInfo.getType() + ", respective_type:1");
				dao.insertRAFeed(callCountFileList, trafficDate, aliasFraud);//insert for processedList for vs_report.
				
			}
			
		}
	}
}
