package com.ws.cdp.cdpprovider.service.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.Upload;
import com.ws.cdp.cdpprovider.service.video.VideoServiceImpl;

public class Xs3Util {
	private static Logger logger = Logger.getLogger(Xs3Util.class);
	public static String xs3_endpoint_default = "10.3.194.227:7480";
	static AmazonS3 xs3_client;
	
	public static void main(String[] args) {
		String ak = "UF4EVVNNW63IXVKBKFWM";
		String sk = "dNQ6bcKptZBKBM6dAOooRNCMlMBoxUX9b0WnZWwE";
		String ep = "10.3.194.227:7480";
		Xs3Util xs3 = new Xs3Util(ak,sk,ep);
		//xs3.DownloadObject("mybuck", "0.mp4", "f:\\xxx.mp4");
		try {
			xs3.UploadObject("mybuck", "new.mp4", "f:\\xxx.mp4", true);
		} catch (Exception e) {
			logger.error("Exception is xxx: " + e);
		}
		
	}

	public Xs3Util(String xs3_access_key, String xs3_secret_key,
			String xs3_endpoint) {

		AWSCredentials xs3_credentials = new BasicAWSCredentials(
				xs3_access_key, xs3_secret_key);
		ClientConfiguration xs3_clientconfig = new ClientConfiguration();
		xs3_clientconfig.setProtocol(Protocol.HTTP);
		xs3_clientconfig.setConnectionTimeout(300);// 300s
		xs3_clientconfig.setMaxErrorRetry(2); // retry 3 times

		S3ClientOptions xs3_client_options = new S3ClientOptions();
		xs3_client_options.setPathStyleAccess(true);

		xs3_client = new AmazonS3Client(xs3_credentials, xs3_clientconfig);
		xs3_client.setEndpoint(null == xs3_endpoint ? xs3_endpoint_default
				: xs3_endpoint);
		xs3_client.setS3ClientOptions(xs3_client_options);
	}

	public String getFileMime(String filePath) {
		if (null == filePath || StringUtils.isEmpty(filePath)) {
			return "";
		}

		File file = new File(filePath);
		return new MimetypesFileTypeMap().getContentType(file);
	}

	public boolean PutObject(String bucketNm, String objName,
			String filePathFrom, boolean isPublic) {

		try {
			File file = new File(filePathFrom);
			ObjectMetadata xs3_meta = new ObjectMetadata();
			// 获取本地文件的mimetype
			xs3_meta.setContentType(getFileMime(filePathFrom));
			// xs3_meta.setHeader("x-amz-meta-your-key-here", "your-private-value");

			PutObjectRequest x3_req = new PutObjectRequest(bucketNm, objName,
					file);
			x3_req.setMetadata(xs3_meta);
			if (isPublic) {
				x3_req.setCannedAcl(CannedAccessControlList.PublicRead);
			}
			xs3_client.putObject(x3_req);
			return true;
		}catch (AmazonServiceException ase) {
			logger.error("xs3_svr_error_message:" + ase.getMessage());
			logger.error("xs3_svr_status_code:  " + ase.getStatusCode());
			logger.error("xs3_svr_error_code:   " + ase.getErrorCode());
			logger.error("xs3_svr_error_type:   " + ase.getErrorType());
			logger.error("xs3_svr_request_id:   " + ase.getRequestId());
			logger.error("Excetion is", ase);
		} catch (AmazonClientException ace) {
			logger.error("xs3_clt_error_message:" + ace.getMessage());
			logger.error("Exception is: ", ace);
		}catch (Exception e) {
			logger.error("Exception is: " + e.getMessage(), e);
		}
		return false;
	}

	public boolean GetObject(String bucketNm, String objectNm, String filePathTo){
		BufferedInputStream in_stream = null;
		BufferedOutputStream out_stream = null;
		try {
			S3Object xs3_object = xs3_client.getObject(new GetObjectRequest(
					bucketNm, objectNm));
			in_stream = new BufferedInputStream(xs3_object.getObjectContent());
			File xs3_local_file = new File(filePathTo);
			if (!xs3_local_file.getParentFile().exists()) {
				xs3_local_file.getParentFile().delete();
			}

			try {
				out_stream = new BufferedOutputStream(new FileOutputStream(
						xs3_local_file, false));
			} catch (FileNotFoundException e) {
				logger.error("Excetion is", e);
				return false;
			}

			byte[] buffer = new byte[1024];
			int xs3_offset = 0;
			try {
				while ((xs3_offset = in_stream.read(buffer)) != -1) {
					out_stream.write(buffer, 0, xs3_offset);
					out_stream.flush();
				}
			} catch (IOException e) {
				logger.error("Excetion is", e);
				return false;
			}

			return true;
		} catch (AmazonServiceException ase) {
			logger.error("xs3_svr_error_message:" + ase.getMessage());
			logger.error("xs3_svr_status_code:  " + ase.getStatusCode());
			logger.error("xs3_svr_error_code:   " + ase.getErrorCode());
			logger.error("xs3_svr_error_type:   " + ase.getErrorType());
			logger.error("xs3_svr_request_id:   " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			logger.error("xs3_clt_error_message:" + ace.getMessage());
		} catch (Exception e) {
			logger.error("Exception is: " + e.getMessage(), e);
		}finally {
			if (null != in_stream)
				try {
					in_stream.close();
				} catch (IOException e) {
					logger.error("Excetion is", e);
				}
			if (null != out_stream)
				try {
					out_stream.close();
				} catch (IOException e) {
					logger.error("Excetion is", e);
				}
		}
		return false;

	}

	public boolean UploadObject(String bucketNm, String objName,
			String filePathFrom, boolean isPublic){

		int threshold = 8 * 1024 * 1024;
		TransferManager tm = new TransferManager(xs3_client);
		TransferManagerConfiguration conf = tm.getConfiguration();
		conf.setMultipartUploadThreshold(threshold);
		tm.setConfiguration(conf);

		ObjectMetadata xs3_meta = new ObjectMetadata();
		xs3_meta.setContentType(getFileMime(filePathFrom));

		File file = new File(filePathFrom);
		PutObjectRequest x3_req = new PutObjectRequest(bucketNm, objName, file);
		x3_req.setMetadata(xs3_meta);
		if (isPublic) {
			x3_req.setCannedAcl(CannedAccessControlList.PublicRead);
		}
		Upload upload = tm.upload(x3_req);
		try {
			upload.waitForCompletion();
			return true;
		} catch (AmazonServiceException ase) {
			logger.error("xs3_svr_error_message:" + ase.getMessage());
			logger.error("xs3_svr_status_code:  " + ase.getStatusCode());
			logger.error("xs3_svr_error_code:   " + ase.getErrorCode());
			logger.error("xs3_svr_error_type:   " + ase.getErrorType());
			logger.error("xs3_svr_request_id:   " + ase.getRequestId());
		} catch (InterruptedException ie) {
			logger.error("xs3_ie_error_message:" + ie.getMessage());
			logger.error("Excetion is", ie);
		} catch (AmazonClientException ace) {
			logger.error("xs3_clt_error_message:" + ace.getMessage());
			logger.error("Exception is: " , ace);
		} catch (Exception e) {
			logger.error("Exception is: " +e.getMessage(), e);
		}finally {
			if(null != tm){
				tm.shutdownNow();
			}
			
		}
		return false;
	}

	public boolean DownloadObject(String bucketNm, String objName,
			String filePathTo){
		int threshold = 8 * 1024 * 1024;
		TransferManager tm = new TransferManager(xs3_client);
		TransferManagerConfiguration conf = tm.getConfiguration();
		conf.setMultipartUploadThreshold(threshold);
		tm.setConfiguration(conf);

		GetObjectRequest getReq = new GetObjectRequest(bucketNm, objName);
		File file = new File(filePathTo);
		// 如果本地文件已经存在删除当前文件
		File xs3_local_file = new File(filePathTo);
		if (!xs3_local_file.getParentFile().exists()) {
			xs3_local_file.getParentFile().delete();
		}
		Download down = tm.download(getReq, file);

		try {
			down.waitForCompletion();
			return true;
		} catch (AmazonServiceException ase) {
			logger.error("xs3_svr_error_message:" + ase.getMessage());
			logger.error("xs3_svr_status_code:  " + ase.getStatusCode());
			logger.error("xs3_svr_error_code:   " + ase.getErrorCode());
			logger.error("xs3_svr_error_type:   " + ase.getErrorType());
			logger.error("xs3_svr_request_id:   " + ase.getRequestId());
		} catch (InterruptedException ie) {
			logger.error("xs3_ie_error_message:" + ie.getMessage());
			logger.error("Excetion is", ie);
		} catch (AmazonClientException ace) {
			logger.error("xs3_clt_error_message:" + ace.getMessage());
			logger.error("Exception is:", ace);
		} catch (Exception e) {
			logger.error("Exception is: " + e.getMessage(), e);
		}finally {
			if(null != tm){
				tm.shutdownNow();
			}
		}

		return false;
	}
}
