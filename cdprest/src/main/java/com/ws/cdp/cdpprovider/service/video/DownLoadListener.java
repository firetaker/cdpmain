package com.ws.cdp.cdpprovider.service.video;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ListenableFuture;
import com.ws.cdp.cdpprovider.service.config.CommonConstants;

/**
 * 跟踪下载任务的结果
 * @author ZhuQiang 20160819
 */
public class DownLoadListener implements Runnable{
	private static Logger logger = Logger
			.getLogger(DownLoadListener.class);
	ListenableFuture<Integer> downloadFuture;
	private VideoContext videoContext;
	
	public DownLoadListener(ListenableFuture<Integer> downloadFuture, VideoContext videoContext)
	{
		this.downloadFuture = downloadFuture;
		this.videoContext = videoContext;
	}
	
	public void run() {
		try {
			
			if(CommonConstants.XS3_SUCCESS == downloadFuture.get())
			{
				logger.info("download successed");
				// 发起对视频的处理
				VideoCallable videoCallable = new VideoCallable(videoContext);
				final ListenableFuture<Integer> listenableFuture = videoContext.getExecPool().submit(videoCallable);
				VideoListener videoListener = new VideoListener(listenableFuture, videoContext);
				listenableFuture.addListener(videoListener, videoContext.getExecPool());
				
			}else if (CommonConstants.XS3_FAILED == downloadFuture.get()) {
				logger.info("download failed");
				// 调用回调函数处理，并释放资源
				videoContext.handleResult(CommonConstants.XS3_FAILED, CommonConstants.TASK_STEP_DOWNLOAD);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
			//TODO 异常场景中的处理 --比如超时等
			videoContext.handleResult(CommonConstants.XS3_FAILED, CommonConstants.TASK_STEP_DOWNLOAD);
		}		
	}
	
	public void setDownloadFuture(ListenableFuture<Integer> downloadFuture) {
		this.downloadFuture = downloadFuture;
	}

	public ListenableFuture<Integer> getDownloadFuture() {
		return downloadFuture;
	}
	
	public VideoContext getVideoContext() {
		return videoContext;
	}

	public void setVideoContext(VideoContext videoContext) {
		this.videoContext = videoContext;
	}
}
