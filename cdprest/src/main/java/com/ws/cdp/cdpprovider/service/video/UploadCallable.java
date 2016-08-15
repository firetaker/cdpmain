package com.ws.cdp.cdpprovider.service.video;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.ws.cdp.cdpprovider.service.config.CommonConstants;
import com.ws.cdp.cdpprovider.service.util.LocalFileContext;
import com.ws.cdp.cdpprovider.service.util.Xs3Util;

public class UploadCallable implements Callable<Integer> {
	private static Logger logger = Logger.getLogger(UploadCallable.class);	
	private VideoContext videoContext;
	
	public UploadCallable(VideoContext videoContext){
		this.videoContext = videoContext;
	}
	
	public Integer call() throws Exception {
		LocalFileContext localFileCtx = videoContext.getLocalFileContext();
		Xs3Util xs3 = null;
		try {
			xs3 = new Xs3Util(localFileCtx.getAccessKey(), localFileCtx.getSecretKey(),
					localFileCtx.getEndPoint());
			boolean result = xs3.PutObject(localFileCtx.getBucketName(),
					localFileCtx.getNewObjName(), localFileCtx.getFileToPath(), true);

			if (result) {
				logger.info("upload successed");
				return CommonConstants.XS3_SUCCESS;
			} else {
				logger.info("upload failed");
				return CommonConstants.XS3_FAILED;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return CommonConstants.XS3_FAILED;
	}

	public VideoContext getVideoContext() {
		return videoContext;
	}

	public void setVideoContext(VideoContext videoContext) {
		this.videoContext = videoContext;
	}
}
