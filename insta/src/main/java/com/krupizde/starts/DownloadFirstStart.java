package com.krupizde.starts;

import com.krupizde.main.App;
import com.krupizde.main.DownloadFromDatabase;
/**
 * Class repserents Starting method, where downloading is started first, and standard iteration afterwards
 * @author Zdeněk Krupička (radamanak@gmail.com)
 *
 */
public class DownloadFirstStart extends Start{

	@Override
	public void start() {
		System.out.println("Starting download-first start");
		try {
			App.instagram();
		} catch (Exception e) {
		}
		final DownloadFromDatabase down = new DownloadFromDatabase();
		down.start();
		try {
			down.join();
		} catch (InterruptedException e) {
		}
		StartsManager.getManager().start("-di");
		
	}

	@Override
	public String startParam() {
		return "-df";
	}

	@Override
	public String description() {
		return "Will start application downloading loaded mediums first and then continue in Discrete start";
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "Download first start";
	}

}
