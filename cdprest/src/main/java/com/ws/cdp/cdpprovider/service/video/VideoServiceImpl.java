package com.ws.cdp.cdpprovider.service.video;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.ws.cdp.cdpapi.message.ReqestMessage;
import com.ws.cdp.cdpapi.message.RespMessage;
import com.ws.cdp.cdpapi.service.VideoService;

@Service()
@Path("video")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces({ ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8 })
public class VideoServiceImpl implements VideoService{
	private static Logger logger = Logger.getLogger(VideoServiceImpl.class);
	ExecutorService threadPool = null;

    public VideoServiceImpl() {
    	threadPool = Executors.newFixedThreadPool(5);
    	
	}
    
	@POST
	@Path("encode")
	public RespMessage processVideo(ReqestMessage req) { 
		String clientIP = RpcContext.getContext().getRemoteHost();
		logger.info("==============" + clientIP +"==============");
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
