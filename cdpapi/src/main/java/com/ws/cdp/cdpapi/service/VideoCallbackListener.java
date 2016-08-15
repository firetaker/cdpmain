package com.ws.cdp.cdpapi.service;

import com.ws.cdp.cdpapi.message.RespMessage;

public interface VideoCallbackListener {
	void changed(RespMessage msg);
}
