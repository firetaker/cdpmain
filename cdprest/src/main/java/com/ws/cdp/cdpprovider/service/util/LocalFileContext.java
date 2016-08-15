package com.ws.cdp.cdpprovider.service.util;

public class LocalFileContext {

	public LocalFileContext() {
	}

	public LocalFileContext(String accessKey, String secretKey,
			String endPoint, String bucketName, String originObjName,
			String newObjName, String fileFromPath, String fileToPath) {
		this.opType = "1";
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.endPoint = endPoint;
		this.bucketName = bucketName;
		this.originObjName = originObjName;
		this.newObjName = newObjName;
		this.fileFromPath = fileFromPath;
		this.fileToPath = fileToPath;

	}

	public LocalFileContext(String downloadUrlPath, String uploadUrlPath,
			String fileFromPath, String fileToPath) {
		this.opType = "0";
		this.downloadUrlPath = downloadUrlPath;
		this.uploadUrlPath = uploadUrlPath;
		this.fileFromPath = fileFromPath;
		this.fileToPath = fileToPath;
	}

	private String opType; // HTTP 0 或 S3 1 ,FTP 2

	// 共用的字段
	private String fileToPath; // 下载时本地存放的目录
	private String fileFromPath; // 上传时本地文件存放的目录
    
	// TODO 水印图片的地址 
	private boolean isFileWaterMark; // TRUE : 图片水印 ， False: 文字水印
	private String waterMarkUrl; // 水印图片的下载地址 
	private String waterMarkPath; // 水印图片的下载地址
 
	// S3方式的
	private String accessKey;
	private String secretKey;
	private String endPoint;
	private String bucketName; // 桶名
	private String originObjName; // 对象下载时使用的名字
	private String newObjName; // 对象上传时使用的名字

	// 带有用户验证的上传和下载方式
	private String downloadUrlPath; // 下载URL
	private String uploadUrlPath; // 上传时URL
	private String username; // TODO
	private String password; // TODO

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getFileToPath() {
		return fileToPath;
	}

	public void setFileToPath(String fileToPath) {
		this.fileToPath = fileToPath;
	}

	public String getFileFromPath() {
		return fileFromPath;
	}

	public void setFileFromPath(String fileFromPath) {
		this.fileFromPath = fileFromPath;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getOriginObjName() {
		return originObjName;
	}

	public void setOriginObjName(String originObjName) {
		this.originObjName = originObjName;
	}

	public String getNewObjName() {
		return newObjName;
	}

	public void setNewObjName(String newObjName) {
		this.newObjName = newObjName;
	}

	public String getDownloadUrlPath() {
		return downloadUrlPath;
	}

	public void setDownloadUrlPath(String downloadUrlPath) {
		this.downloadUrlPath = downloadUrlPath;
	}

	public String getUploadUrlPath() {
		return uploadUrlPath;
	}

	public void setUploadUrlPath(String uploadUrlPath) {
		this.uploadUrlPath = uploadUrlPath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isFileWaterMark() {
		return isFileWaterMark;
	}

	public void setFileWaterMark(boolean isFileWaterMark) {
		this.isFileWaterMark = isFileWaterMark;
	}

	public String getWaterMarkUrl() {
		return waterMarkUrl;
	}

	public void setWaterMarkUrl(String waterMarkUrl) {
		this.waterMarkUrl = waterMarkUrl;
	}

	public String getWaterMarkPath() {
		return waterMarkPath;
	}

	public void setWaterMarkPath(String waterMarkPath) {
		this.waterMarkPath = waterMarkPath;
	}
}
