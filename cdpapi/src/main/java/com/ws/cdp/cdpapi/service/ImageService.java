package com.ws.cdp.cdpapi.service;

import com.ws.cdp.cdpapi.message.ReqestMessage;
import com.ws.cdp.cdpapi.message.RespMessage;

public interface ImageService {
	RespMessage processImage(ReqestMessage req);
}
