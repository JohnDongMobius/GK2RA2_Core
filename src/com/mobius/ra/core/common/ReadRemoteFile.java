package com.mobius.ra.core.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ReadRemoteFile {
	private static Logger logger = Logger.getLogger("Read-Remote-File");

	public static void main(String[] args) {
		String username = "root";
		String password = "mo2bi5us0ws5";
		String hostname = "10.213.2.39";

		String remoteFile = "/cdrshare01/mediation/472_2/tapout_txt"+File.separatorChar+"TAPOUT*20140319.txt";

		List<String> list = new ReadRemoteFile().retrieveFile(hostname,
				username, password, remoteFile);
		for (String s : list) {
			System.out.println(s);
		}
		System.out.println(list.size());
		System.out.println(remoteFile);
		
		//test remove list function.
		List<String> bl = new ArrayList<String>();
		for(int i=0;i<10;i++)
			bl.add(i+"a");
		
		List<String> sl = new ArrayList<String>();
		for(int i=3;i<6;i++)
			sl.add(i+"a");
		
		bl.removeAll(sl);
		for(String s : bl)
			System.out.println(s);
		
	}

	public List<String> retrieveFile(String hostname, String username,
			String password, String remoteFile) {
		JSch jsch = new JSch();
		List<String> uploadedFileList = new ArrayList<String>();

		try {
			Session session = jsch.getSession(username, hostname, 22);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			logger.info("Establishing Connection...");

			session.connect();
			logger.info("Connection established.");
			logger.info("Crating SFTP Channel.");

			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			logger.info("SFTP Channel created.");

			InputStream is = null;
			is = sftpChannel.get(remoteFile);

			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = br.readLine()) != null) {
				// logger.info(line);
				//RAFeed raFeed = new RAFeed();
				//raFeed.setFileName(line);
				uploadedFileList.add(line);
			}
			br.close();
			sftpChannel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			logger.info(e);
		}
		
		if(uploadedFileList.size()>=1)
			uploadedFileList.remove(uploadedFileList.size() - 1);
		
		return uploadedFileList;
	}
}
