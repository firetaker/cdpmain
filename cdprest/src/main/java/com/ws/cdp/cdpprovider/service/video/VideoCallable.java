package com.ws.cdp.cdpprovider.service.video;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.ws.cdp.cdpprovider.service.config.CommonConstants;

/**
 *  调用ffmpeg命令行处理 cmd 中的视频
 * @author 朱强 20160810
 *
 */
public class VideoCallable implements Callable<Integer> {
	private static Logger logger = Logger.getLogger(VideoCallable.class);

	private VideoContext videoContext;
	
	public VideoCallable(VideoContext videoContext) {
		this.videoContext = videoContext;
	}

	/**
	 * 调用ffmpeg处理视频
	 */
	public Integer call() throws Exception {
		try {
			FFmpegHandler ffmpegHdl = new FFmpegHandler();
			if(null == videoContext.getCmds() || videoContext.getCmds().isEmpty())
			{
				return CommonConstants.TASK_FAILED;
			}
			for (String cmd : videoContext.getCmds()) {
				ffmpegHdl.addArgument(cmd);
			}
			try {
				int result = ffmpegHdl.pb_execute();
				return result;
			} catch (Exception e) {
				logger.error(e.getMessage(), e);;
			}
			return CommonConstants.TASK_FAILED;
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
		}
		return CommonConstants.TASK_FAILED;
	}

	public VideoContext getVideoContext() {
		return videoContext;
	}

	public void setVideoContext(VideoContext videoContext) {
		this.videoContext = videoContext;
	}
}
