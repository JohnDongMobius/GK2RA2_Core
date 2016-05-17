package com.mobius.ra.core.common;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;



/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class DbManager {

	private static DbManager instance = null;

	/**
	 * init
	 */
	public static void init(String dbPath){
		if(instance == null){
			instance = new DbManager();
			try{
				PropertyConfigurator.configure(dbPath);
			}catch (ProxoolException e){
				e.printStackTrace();
				
			}
		}
	}
	
	public static void reInit(String dbPath){
		if(instance == null){
			instance = new DbManager();
			try{
				PropertyConfigurator.configure(dbPath);
			}catch (ProxoolException e){
				e.printStackTrace();
			}
		}else{
			instance = null;
			instance = new DbManager();
			try{
				PropertyConfigurator.configure(dbPath);
			}catch (ProxoolException e){
				e.printStackTrace();
			}
		}
	}

	private DbManager(){

	}

	/**
	 * getInstance
	 * @return
	 */
	public static DbManager getInstance(String dbPath){
		if(instance == null){
			init(dbPath);
		}
		return instance;
	}

}
