package com.ws.cdp.cdpprovider.service.video;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.ws.cdp.cdpprovider.service.config.CommonConstants;
import com.ws.cdp.cdpprovider.service.util.LocalFileContext;
import com.ws.cdp.cdpprovider.service.util.Xs3Util;

public class DownLoadCallable implements Callable<Integer> {
	private static Logger logger = Logger.getLogger(DownLoadCallable.class);

	private VideoContext videoContext;

	public DownLoadCallable(VideoContext videoContext) {
		this.videoContext = videoContext;
	}

	public Integer call() throws Exception {
		Xs3Util xs3 = null;
		LocalFileContext localFileCtx = videoContext.getLocalFileContext();
		try {
			xs3 = new Xs3Util(localFileCtx.getAccessKey(),
					localFileCtx.getSecretKey(), localFileCtx.getEndPoint());
			boolean result = xs3.GetObject(localFileCtx.getBucketName(),
					localFileCtx.getOriginObjName(),
					localFileCtx.getFileFromPath());

			if (result) {
				logger.info("download successed");
				return CommonConstants.XS3_SUCCESS;
			} else {
				logger.error("download failed");
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
