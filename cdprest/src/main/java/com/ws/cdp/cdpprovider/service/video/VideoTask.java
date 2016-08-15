package com.ws.cdp.cdpprovider.service.video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ws.cdp.cdpprovider.service.config.CommonConfig;
import com.ws.cdp.cdpprovider.service.config.CommonConstants;
import com.ws.cdp.cdpprovider.service.util.FileUtil;
import com.ws.cdp.cdpprovider.service.util.LocalFileContext;
import com.ws.cdp.cdpprovider.service.util.Xs3Util;

public class VideoTask implements Runnable {
	private static Logger logger = Logger.getLogger(VideoTask.class);
	
	static String ak = "UF4EVVNNW63IXVKBKFWM";
	static String sk = "dNQ6bcKptZBKBM6dAOooRNCMlMBoxUX9b0WnZWwE";
	static String ep = "10.3.194.227:7480";
	static String bucket = "mybuck";
	static String originObj = "0.mp4";
	static String newObj = "0_1.mp4";
	
	private VideoContext videoContext; 	
	public VideoTask(){		
	}
	
    public VideoTask(VideoContext videoContext){
		this.videoContext = videoContext;
	}
        
    public static void main(String[] args) {
    	String fileSaveTo = CommonConfig.getConf().getSavePath() + "/"
				+ "0.mp4";
		String fielTranTo = CommonConfig.getConf().getTranPath() + "/"
				+ "0_1.mp4";

		// TODO STEP1 组织FFmepg的参数
		List<String> cmds = new ArrayList<String>();
		cmds.add("-y");
		cmds.add("-v");
		cmds.add("error");
		cmds.add("-i");
		cmds.add(fileSaveTo);
		cmds.add("-strict");
		cmds.add("experimental");
		cmds.add("-f");
		cmds.add("mp4");
		cmds.add("-vcodec");
		cmds.add("libx264");
		cmds.add("-s");
		cmds.add("640x480");
		cmds.add("-r");
		cmds.add("24/1");
		cmds.add("-acodec");
		cmds.add("aac");
		cmds.add("-ac");
		cmds.add("1");
		cmds.add("-b:a");
		cmds.add("32768");
		cmds.add("-sn");

		cmds.add(fielTranTo);

		// TODO STEP2 :组织本地文件的上下文参数
		String ak = "UF4EVVNNW63IXVKBKFWM";
		String sk = "dNQ6bcKptZBKBM6dAOooRNCMlMBoxUX9b0WnZWwE";
		String ep = "10.3.194.227:7480";
		String bucket = "mybuck";
		String originObj = "0.mp4";
		String newObj = "0_1.mp4";
		LocalFileContext localFileContext = new LocalFileContext(ak, sk, ep,
				bucket, originObj, newObj, fileSaveTo, fielTranTo);

		VideoContext videoContext = new VideoContext(cmds, localFileContext);
		VideoTask task = new VideoTask(videoContext);
		try {
			task.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void run() {
// DownloadFile();
// TranVideo();
// UploadFile();
	
		DownLoadCallable downable = new DownLoadCallable(videoContext);
		final ListenableFuture<Integer> listenableFuture = videoContext.getExecPool().submit(downable);		
		DownLoadListener downLoadListener = new DownLoadListener(listenableFuture, videoContext);
		listenableFuture.addListener(downLoadListener, videoContext.getExecPool());		
	}
	
	
	public VideoContext getVideoContext() {
		return videoContext;
	}
	
	public void setVideoContext(VideoContext videoContext) {
		this.videoContext = videoContext;
	}

}