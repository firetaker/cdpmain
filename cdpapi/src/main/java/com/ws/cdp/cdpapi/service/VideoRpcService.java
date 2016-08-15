package com.ws.cdp.cdpapi.service;

import com.ws.cdp.cdpapi.message.ReqestMessage;
import com.ws.cdp.cdpapi.message.RespMessage;

public interface VideoRpcService {
	RespMessage processVideo(ReqestMessage req);
}
