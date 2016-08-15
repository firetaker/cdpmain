package com.ws.cdp.cdpprovider.service.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileUtil {

	public static String getFileName(String src_url) {
		String result = null;

		String[] parts = src_url.split("/");

		if (parts.length > 0) {
			result = parts[parts.length - 1];
		}
		return result;
	}

	public static void deleteFile(String filepath) {
		File file = new File(filepath);
		try {
			if(file.exists()){		
				FileUtils.forceDelete(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
