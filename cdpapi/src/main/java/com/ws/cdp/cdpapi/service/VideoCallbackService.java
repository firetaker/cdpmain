package com.ws.cdp.cdpapi.service;

import com.ws.cdp.cdpapi.message.ReqestMessage;

public interface VideoCallbackService {
	void proceeVideo(ReqestMessage req, VideoCallbackListener listener);

}
