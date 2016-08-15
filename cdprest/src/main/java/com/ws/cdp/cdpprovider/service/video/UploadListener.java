package com.ws.cdp.cdpprovider.service.video;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ListenableFuture;
import com.ws.cdp.cdpprovider.service.config.CommonConstants;
import com.ws.cdp.cdpprovider.service.util.FileUtil;

/**
 * 跟踪上传任务的结果
 * @author ZhuQiang 20160819
 */
public class UploadListener implements Runnable {
	private static Logger logger = Logger.getLogger(UploadListener.class);

	ListenableFuture<Integer> uploadFuture;
	private VideoContext videoContext;
	
	public UploadListener(ListenableFuture<Integer> uploadFuture, VideoContext videoContext) {
		this.uploadFuture = uploadFuture;
		this.videoContext = videoContext;
	}

	public void run() {
		try {
			if(CommonConstants.XS3_SUCCESS ==  uploadFuture.get())
			{
				logger.info("upload successed");
				videoContext.handleResult(CommonConstants.XS3_SUCCESS, CommonConstants.TASK_STEP_UPLOAD);
			} else if (CommonConstants.XS3_FAILED == uploadFuture.get()) {
		        logger.info("upload failed");
		        videoContext.handleResult(CommonConstants.XS3_FAILED, CommonConstants.TASK_STEP_UPLOAD);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			//e.printStackTrace();
			videoContext.handleResult(CommonConstants.XS3_FAILED, CommonConstants.TASK_STEP_UPLOAD);
		}finally{
			FileUtil.deleteFile(videoContext.getLocalFileContext().getFileFromPath());
			FileUtil.deleteFile(videoContext.getLocalFileContext().getFileToPath());
		}

	}

	public ListenableFuture<Integer> getUploadFuture() {
		return uploadFuture;
	}

	public void setUploadFuture(ListenableFuture<Integer> uploadFuture) {
		this.uploadFuture = uploadFuture;
	}
	
	public VideoContext getVideoContext() {
		return videoContext;
	}

	public void setVideoContext(VideoContext videoContext) {
		this.videoContext = videoContext;
	}

}
