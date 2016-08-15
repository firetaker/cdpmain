package com.ws.cdp.cdpapi.message;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("restriction")
@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
public class ReqestMessage implements Serializable {
	private static final long serialVersionUID = -1792422011485665954L;

	@NotNull
	private String srcurl;

	@NotNull
	private String videocmds; // ""

	public ReqestMessage(){}

	public ReqestMessage(String srcurl, String videocmds) {
		this.srcurl = srcurl;
		this.videocmds = videocmds;
	}

	public String getSrcurl() {
		return srcurl;
	}

	public void setSrcurl(String srcurl) {
		this.srcurl = srcurl;
	}

	public String getVideocmds() {
		return videocmds;
	}

	public void setVideocmds(String videocmds) {
		this.videocmds = videocmds;
	}

//	public String toString() {
//		JSONObject json = new JSONObject();
//		json.put("srcurl", srcurl);
//		json.put("videocmds", videocmds);
//		return json.toJSONString();
//	}
}
