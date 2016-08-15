package com.ws.cdp.cdpprovider.service.video;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.ws.cdp.cdpapi.message.ReqestMessage;
import com.ws.cdp.cdpapi.message.RespMessage;
import com.ws.cdp.cdpapi.service.VideoCallbackListener;
import com.ws.cdp.cdpapi.service.VideoCallbackService;

public class VideoCallbackServiceImpl implements VideoCallbackService {
	private static Logger logger = Logger
			.getLogger(VideoCallbackServiceImpl.class);

	ExecutorService threadPool = null;

	public VideoCallbackServiceImpl() {
		logger.info("-------- VideoCallbackServiceImpl ----------");
		threadPool = Executors.newFixedThreadPool(5);
	}

	public void shutdown() {
		try {
			logger.info("waiting for threadpool shutdown...");
			if (null != threadPool)
				threadPool.shutdown();
			// this.finalize();
			logger.info("threadpool shutdown ok!");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		try {
			logger.info("waiting for threadpool shutdown...");
			if (null != threadPool)
				threadPool.shutdown();
			this.finalize();
			logger.info("threadpool shutdown ok!");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out
				.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	public void proceeVideo(ReqestMessage req, VideoCallbackListener listener) {
		RespMessage res = new RespMessage();
		// 解析参数
		String para_url = req.getSrcurl();
		String para_cmds = req.getVideocmds();
		// StringBuffer uuidStr = new StringBuffer();
		res.setAll(200, "Encode Invoked");
		if (null != listener) {
			listener.changed(res);
		}
		logger.info(para_url + " " + para_cmds);

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
		if (null != threadPool) {
			threadPool.execute(new VideoTask(videoContext));
		}

	}

}
