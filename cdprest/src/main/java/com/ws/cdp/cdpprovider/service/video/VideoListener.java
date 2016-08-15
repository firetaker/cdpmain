package com.ws.cdp.cdpprovider.service.video;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ListenableFuture;
import com.ws.cdp.cdpprovider.service.config.CommonConstants;
import com.ws.cdp.cdpprovider.service.util.FileUtil;

/**
 * 跟踪视频处理调用的结果
 * @author ZhuQiang 20160819
 */
public class VideoListener implements Runnable {

	private static Logger logger = Logger.getLogger(VideoListener.class);
	ListenableFuture<Integer> videoFuture;
	private VideoContext videoContext;
	
	public VideoListener(ListenableFuture<Integer> videoFuture, VideoContext videoContext) {
		this.videoFuture = videoFuture;
		this.videoContext = videoContext;
	}

	public void run() {
		try {
			if(CommonConstants.TASK_SUCCESS == videoFuture.get())
			{
				logger.info("video process successed");
				
				UploadCallable uploadCallable = new UploadCallable(videoContext);
				final ListenableFuture<Integer> listenableFuture = videoContext.getExecPool().submit(uploadCallable);
				
				UploadListener uploadListener = new UploadListener(listenableFuture, videoContext);
				listenableFuture.addListener(uploadListener, videoContext.getExecPool());
			}else if (CommonConstants.TASK_FAILED == videoFuture.get()) {
				logger.info("video process failed");
				videoContext.handleResult(CommonConstants.TASK_FAILED, CommonConstants.TASK_STEP_FFMPEG);
				// 删除本地文件
				FileUtil.deleteFile(videoContext.getLocalFileContext().getFileFromPath());
				FileUtil.deleteFile(videoContext.getLocalFileContext().getFileToPath());
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
			// 删除本地文件
			FileUtil.deleteFile(videoContext.getLocalFileContext().getFileFromPath());
			FileUtil.deleteFile(videoContext.getLocalFileContext().getFileToPath());
			videoContext.handleResult(CommonConstants.TASK_FAILED, CommonConstants.TASK_STEP_FFMPEG);
		}
	}

	public ListenableFuture<Integer> getVideoFuture() {
		return videoFuture;
	}

	public void setVideoFuture(ListenableFuture<Integer> videoFuture) {
		this.videoFuture = videoFuture;
	}
	
	public VideoContext getVideoContext() {
		return videoContext;
	}

	public void setVideoContext(VideoContext videoContext) {
		this.videoContext = videoContext;
	}

}
