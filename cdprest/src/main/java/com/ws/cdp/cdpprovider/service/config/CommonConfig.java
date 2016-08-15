package com.ws.cdp.cdpprovider.service.config;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

public class CommonConfig {
	private static Logger logger = Logger.getLogger(CommonConfig.class);

	private static final String FFMPEG_ENV_CONF_FILE = "ffmpegenvr.properties";

	// === ffmpeg 可执行文件 path ====
	private static final String FFMPEG_WIN_PATH = "ffmpeg.win.path";
	private static final String FFMPEG_LINUX_PATH = "ffmpeg.linux.path";
	
	// === 下载文件保存目录 ====
	private static final String FILE_SAVE_TO_PATH = "file.save.to.path";
	// === 转码后的文件保存目录 ====
	private static final String FILE_TRAN_TO_PATH = "file.tran.to.path";	
	// === 水印图片存放的位置
	private static final String WATERMARK_TO_PATH = "watermark.to.path";
	
	
	private static Map<String, String> ffmpegenvMap = new HashMap<String, String>();
	private static CommonConfig envconf = new CommonConfig();

	public CommonConfig() {
		loadFfmpegEvcConf();
	}

	public static CommonConfig getConf() {
		return envconf;
	}
	
	private void loadFfmpegEvcConf() {
		Properties props = new Properties();
		InputStreamReader is = null;
		try {
			is = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(FFMPEG_ENV_CONF_FILE), "utf-8");
			props.load(is);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
		try {
			Set<Object> set = props.keySet();
			Iterator<Object> it = set.iterator();
			logger.info("========> Load Config:" + FFMPEG_ENV_CONF_FILE);
			while (it.hasNext()) {
				String key = it.next().toString();
				Object value = props.get(key);
				ffmpegenvMap.put((String) key, (String) value);
				logger.info(key + "=" + value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getfmpegWinPath() {
		return ffmpegenvMap.get(FFMPEG_WIN_PATH) == null ? "" : ffmpegenvMap.get(FFMPEG_WIN_PATH);
	}
	
	public String getfmpegLinuxPath() {
		return ffmpegenvMap.get(FFMPEG_LINUX_PATH) == null ? "" : ffmpegenvMap.get(FFMPEG_LINUX_PATH);
	}
	
	public String getSavePath(){
		return ffmpegenvMap.get(FILE_SAVE_TO_PATH) == null ? "" : ffmpegenvMap.get(FILE_SAVE_TO_PATH);
	}
	
    public String getTranPath(){
	    return ffmpegenvMap.get(FILE_TRAN_TO_PATH) == null ? "" : ffmpegenvMap.get(FILE_TRAN_TO_PATH);
	}

    public String getWatermarkPath(){
    	return ffmpegenvMap.get(WATERMARK_TO_PATH) == null ? "" : ffmpegenvMap.get(FILE_TRAN_TO_PATH);
    }
}
