package com.ws.cdp.cdpapi.message;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

import com.alibaba.fastjson.JSONObject;
import com.ws.cdp.cdpapi.common.Constants;

@SuppressWarnings("restriction")
@XmlRootElement
public class RespMessage implements Serializable{
	private static final long serialVersionUID = -4906090050862084368L;

	@NotNull
	private int statuscode;
	
	@NotNull
	private String message;

	public RespMessage() {}

	public RespMessage(int statusCode, String message) {
		this.statuscode = statusCode;
		this.message = message;
	}

	public void setAll(int statusCode, String message) {
		this.statuscode = statusCode;
		this.message = message;
	}

	public int getStatuscode() {
		return statuscode;
	}

	public void setStatusCode(int statusCode) {
		this.statuscode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean ifSuccess() {
		return statuscode == Constants.SUCCESS_CODE;
	}

//	public String toString() {
//		JSONObject json = new JSONObject();
//		json.put("statusCode", statuscode);
//		json.put("message", message);
//
//		return json.toJSONString();
//	}
}
