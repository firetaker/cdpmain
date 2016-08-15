package com.ws.cdp.cdpprovider.service.video;

import java.util.List;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.ws.cdp.cdpprovider.service.util.LocalFileContext;

public class VideoContext {
	private static Logger logger = Logger.getLogger(VideoContext.class);
	private List<String> cmds;
	private LocalFileContext localFileContext;
	private ListeningExecutorService execPool;
	
	public VideoContext() {
		try {
			execPool = MoreExecutors.listeningDecorator(Executors
			          .newFixedThreadPool(2));
			logger.info("------------> thread pool start successed ");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public VideoContext(List<String> cmds, LocalFileContext localFileContext) {
		this.cmds = cmds;
		this.localFileContext = localFileContext;
		try {
			execPool = MoreExecutors.listeningDecorator(Executors
			          .newFixedThreadPool(2));
			//execPool = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
			logger.info("------------> thread pool start successed ");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}		
	}
	
	public void cleanup(){
		try {
			execPool.shutdown();
			execPool = null;
			logger.info("------------> thread pool shutdown successed ");
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void PostTaskResult()
	{
		// TODO 发送视频处理的结果	
		logger.info("message sended ok");
	}
	
	public void handleResult(int result, String step) {		
		try {
			PostTaskResult();
			logger.info(step);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} finally{
			cleanup(); // 释放当前线程池资源
		}
	}

	public List<String> getCmds() {
		return cmds;
	}

	public void setCmds(List<String> cmds) {
		this.cmds = cmds;
	}

	public LocalFileContext getLocalFileContext() {
		return localFileContext;
	}

	public void setLocalFileContext(LocalFileContext localFileContext) {
		this.localFileContext = localFileContext;
	}
	
	public ListeningExecutorService getExecPool() {
		return execPool;
	}

	public void setExecPool(ListeningExecutorService execPool) {
		this.execPool = execPool;
	}
}
