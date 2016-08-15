package com.ws.cdp.cdpprovider.service.video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ws.cdp.cdpprovider.service.config.CommonConfig;
import com.ws.cdp.cdpprovider.service.config.CommonConstants;

public class FFmpegHandler {
	private static Logger logger = Logger.getLogger(FFmpegHandler.class);

	private final String ffmpegPath;
	private final ArrayList<String> args = new ArrayList<String>();
	private Process ffmpeg = null;
	// private ProgressListener listener = null;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private InputStream errorStream = null;

	public FFmpegHandler(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}

	public FFmpegHandler() {
		this.ffmpegPath = getFfmpegPath();
		args.add(ffmpegPath);
		logger.info(args);
	}

	public String getFfmpegPath() {
		String os = System.getProperty("os.name").toLowerCase();
		boolean isWindows = (os.indexOf("windows") != -1) ? true : false;
		String ffmpegPath = "";
		ffmpegPath = isWindows ? CommonConfig.getConf().getfmpegWinPath()
				: CommonConfig.getConf().getfmpegLinuxPath();
		logger.info("Encoder executor path ==========>>> " + ffmpegPath);
		return ffmpegPath;
	}

	public void addArgument(String arg) {
		args.add(arg);
	}

	// public void addListener(ProgressListener listener) {
	// this.listener = listener;
	// }

	// public int pb_execute() throws TransException {
	public int pb_execute() {
		logger.info("========>> ffmpeg execute cmds: " + args);
		int argsSize = args.size();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < argsSize; i++) {
			sb.append(args.get(i) + " ");
		}
		logger.info(sb.toString());

		final ProcessBuilder pb = new ProcessBuilder();
		pb.redirectErrorStream(true);
		pb.command(args);
		try {
			final Process process = pb.start();
			inputStream = process.getInputStream();
			outputStream = process.getOutputStream();
			errorStream = process.getErrorStream();

			// if (null != listener) {
			// listener.message(inputStream);
			// }
			// 打印输出的结果
			handleImputStream(inputStream);
			int waitfor = process.waitFor();
			if (waitfor != 0) {
				// p.exitValue()==0表示正常结束，1：非正常结束
				if (process.exitValue() == 1) {
					// if(null != listener)
					// {
					// listener.progress(1);
					// }
					logger.info("========>> ffmpeg tranfer failed!");
					// throw new TransException("ffmpeg tranfer failed");
					return CommonConstants.TASK_FAILED;
				} else {
					logger.info("========>> ffmpeg tranfer successed");
					// if (null != listener) {
					// listener.progress(0);
					// }
					return CommonConstants.TASK_SUCCESS;
				}
			} else {
				logger.info("========>> ffmpeg tranfer successed");
				// if (null != listener) {
				// listener.progress(0);
				// }

				return CommonConstants.TASK_SUCCESS;
			}

		} catch (IOException e) {
			logger.error("========>> ffmpeg tranfer failed message: "
					+ e.getMessage());
			logger.error("========>> ffmpeg tranfer failed cause: "
					+ e.getCause());
			e.printStackTrace();
		} catch (InterruptedException e) {
			logger.error("========>> ffmpeg tranfer failed message: "
					+ e.getMessage());
			logger.error("========>> ffmpeg tranfer failed cause: "
					+ e.getCause());
			e.printStackTrace();
			Thread.currentThread().interrupt();
		} finally {
			destroy();
		}
		return CommonConstants.TASK_FAILED;
	}

	public void handleImputStream(InputStream in) {
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			br = new BufferedReader(new InputStreamReader(in, "utf-8"), 500);
			String line = "";
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				sb.append(line + "\n");
			}
			br.close();
		} catch (Exception e) {
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
			}
		}
		logger.info(sb.toString());
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

	public InputStream getErrorStream() {
		return errorStream;
	}

	public void destroy() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable t) {
				logger.warn("Error closing input stream", t);
			}
			inputStream = null;
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Throwable t) {
				logger.warn("Error closing output stream", t);
			}
			outputStream = null;
		}
		if (errorStream != null) {
			try {
				errorStream.close();
			} catch (Throwable t) {
				logger.warn("Error closing error stream", t);
			}
			errorStream = null;
		}
		if (ffmpeg != null) {
			ffmpeg.destroy();
			ffmpeg = null;
		}
	}

}
