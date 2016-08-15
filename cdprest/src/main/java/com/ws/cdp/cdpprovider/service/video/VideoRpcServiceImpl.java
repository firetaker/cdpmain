package com.ws.cdp.cdpprovider.service.video;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ws.cdp.cdpapi.service.VideoRpcService;


public class VideoRpcServiceImpl implements VideoRpcService{
	private static Logger logger = Logger.getLogger(VideoRpcServiceImpl.class);

	ExecutorService threadPool = null;
	public VideoRpcServiceImpl(){
		threadPool = Executors.newFixedThreadPool(5);
	}
	public RespMessage processVideo(ReqestMessage req) {
		RespMessage res = new RespMessage();
		// 解析参数
		String url = req.getSrcurl();
		String cmds = req.getVideocmds();
		logger.info(url + " " + cmds);
		
		VideoContext videoContext = new VideoContext();
		//TODO 组织参数
		if (null != threadPool) {
			threadPool.execute(new VideoTask(videoContext));
		}
		res.setAll(200, "Encode Invoked");
		return res;
	}

}
