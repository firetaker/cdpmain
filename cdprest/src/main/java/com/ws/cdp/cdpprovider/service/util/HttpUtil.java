package com.ws.cdp.cdpprovider.service.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.activation.MimetypesFileTypeMap;

import org.apache.log4j.Logger;

import com.alibaba.dubbo.common.utils.StringUtils;

public class HttpUtil {
	private static Logger logger = Logger.getLogger(HttpUtil.class);

	public String getFileMime(String filePath) {
		if (null == filePath || StringUtils.isEmpty(filePath)) {
			return "";
		}

		File file = new File(filePath);
		return new MimetypesFileTypeMap().getContentType(file);
	}
	
	public boolean uploadFromFile(String urlPath, String localPath) {
		HttpURLConnection conn = null;
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setDoOutput(true);
			conn.setDoInput(true);

			String contentType = getFileMime(localPath);
			conn.setRequestProperty("Content-Type", contentType);
			// conn.setRequestProperty("", "");
			// conn.setRequestProperty("", "");

			outputStream = new BufferedOutputStream(conn.getOutputStream());
			inputStream = new BufferedInputStream(new FileInputStream(localPath));

			byte[] buffer = new byte[1024];
			int p = 0;
			while ((p = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, p);
				outputStream.flush();
			}

			if (200 == conn.getResponseCode()) {
				System.out.println("failed");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {

			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			conn.disconnect();
		}
	}

	public boolean downloadToFile(String urlPath, String localPath) {
		HttpURLConnection conn = null;
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			//String contentType = "application/xml";
			//conn.setRequestProperty("Content-Type", contentType);
			
			if(200 != conn.getResponseCode())
			{
				logger.error("URL: " + urlPath + " Headers:"+ conn.getHeaderFields());
				return false;
			}
			inputStream = new BufferedInputStream(conn.getInputStream());
			File localFile = new File(localPath);
			if (!localFile.getParentFile().exists()) {
				localFile.getParentFile().mkdirs();
			}
			outputStream = new BufferedOutputStream(new FileOutputStream(localFile,
					false));

			byte[] buffer = new byte[1024];
			int p = 0;
			while ((p = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, p);
				outputStream.flush();
			}
			
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("URL: " + urlPath + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			conn.disconnect();
		}
	}

	/**
	 * Upload a file to a FTP server. A FTP URL is generated with the following
	 * syntax: ftp://user:password@host:port/filePath;type=i.
	 *
	 * @param ftpServer
	 *            , FTP server address (optional port ':portNumber').
	 * @param user
	 *            , Optional user name to login.
	 * @param password
	 *            , Optional password for user.
	 * @param fileName
	 *            , Destination file name on FTP server (with optional preceding
	 *            relative path, e.g. "myDir/myFile.txt").
	 * @param source
	 *            , Source file to upload.
	 * @throws MalformedURLException
	 *             , IOException on error.
	 */
	public void uploadToFTP(String ftpServer, String user, String password,
			String fileName, File source) {
		if (ftpServer != null && fileName != null && source != null) {
			StringBuffer sb = new StringBuffer("ftp://");
			// check for authentication else assume its anonymous access.
			if (user != null && password != null) {
				sb.append(user);
				sb.append(':');
				sb.append(password);
				sb.append('@');
			}
			sb.append(ftpServer);
			sb.append('/');
			sb.append(fileName);
			/*
			 * type ==&gt; a=ASCII mode, i=image (binary) mode, d= file
			 * directory listing
			 */
			sb.append(";type=i");

			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				URL url = new URL(sb.toString());
				URLConnection urlc = url.openConnection();

				bos = new BufferedOutputStream(urlc.getOutputStream());
				bis = new BufferedInputStream(new FileInputStream(source));

				int i;
				// read byte by byte until end of stream
				while ((i = bis.read()) != -1) {
					bos.write(i);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					try {
						bis.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				if (bos != null)
					try {
						bos.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
			}
		} else {
			System.out.println("Input not available.");
		}
	}

	/**
	 * Download a file from a FTP server. A FTP URL is generated with the
	 * following syntax: ftp://user:password@host:port/filePath;type=i.
	 *
	 * @param ftpServer
	 *            , FTP server address (optional port ':portNumber').
	 * @param user
	 *            , Optional user name to login.
	 * @param password
	 *            , Optional password for user.
	 * @param fileName
	 *            , Name of file to download (with optional preceeding relative
	 *            path, e.g. one/two/three.txt).
	 * @param destination
	 *            , Destination file to save.
	 * @throws MalformedURLException
	 *             , IOException on error.
	 */
	public void downloadFromFTP(String ftpServer, String user, String password,
			String fileName, File destination) {
		if (ftpServer != null && fileName != null && destination != null) {
			StringBuffer sb = new StringBuffer("ftp://");
			// check for authentication else assume its anonymous access.
			if (user != null && password != null) {
				sb.append(user);
				sb.append(':');
				sb.append(password);
				sb.append('@');
			}
			sb.append(ftpServer);
			sb.append('/');
			sb.append(fileName);
			/*
			 * type ==&gt; a=ASCII mode, i=image (binary) mode, d= file
			 * directory listing
			 */
			sb.append(";type=i");
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				URL url = new URL(sb.toString());
				URLConnection urlc = url.openConnection();

				bis = new BufferedInputStream(urlc.getInputStream());
				bos = new BufferedOutputStream(new FileOutputStream(
						destination.getName()));

				int i;
				while ((i = bis.read()) != -1) {
					bos.write(i);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (bis != null)
					try {
						bis.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				if (bos != null)
					try {
						bos.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
			}
		} else {
			System.out.println("Input not available");
		}
	}

	public static void main(String args[]) {
		HttpUtil util = new HttpUtil();
		util.downloadToFile("http://10.3.194.227:7480/mybuck/0.mp4", "f:\\ppp.mp4");

	}

}
