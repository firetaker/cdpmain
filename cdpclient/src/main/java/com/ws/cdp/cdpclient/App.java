package com.ws.cdp.cdpclient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.rpc.RpcContext;
import com.ws.cdp.cdpapi.message.ReqestMessage;
import com.ws.cdp.cdpapi.message.RespMessage;
import com.ws.cdp.cdpapi.service.VideoCallbackListener;
import com.ws.cdp.cdpapi.service.VideoCallbackService;
import com.ws.cdp.cdpapi.service.VideoRestService;
import com.ws.cdp.cdpapi.service.VideoRpcService;

public class App {
	@SuppressWarnings({ "unused", "restriction" })
	private static void processVideo(String url, String cmds,
			MediaType mediaType) {
		System.out.println("Process Video via " + url);
		String urltoget = "http://10.3.194.227:7480/mybuck/0.mp4";
		String bs64 = new sun.misc.BASE64Encoder().encode(urltoget.getBytes());
		System.out.println(bs64);
		ReqestMessage req = new ReqestMessage(bs64, cmds);
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(url);
		Entity<ReqestMessage> msg = Entity.entity(req, mediaType);

		// Response response = target.request().post(msg);
		Response response = target.request()
				.post(Entity.entity(req, mediaType));

		try {
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed with HTTP error code : "
						+ response.getStatus() + " msg:"
						+ response.getStatusInfo());
			}
			System.out.println("Successfully got result: "
					+ response.readEntity(String.class));
		} finally {
			response.close();
			client.close();
		}
	}

	@SuppressWarnings("resource")
	public static void ConsumerToRest() {
		try {
			String configLocation = "classpath*:/dubbo-client.xml";
			ApplicationContext context = new ClassPathXmlApplicationContext(
					configLocation);
			VideoRestService us = (VideoRestService) context
					.getBean("videoRestService");
			ReqestMessage req = new ReqestMessage("http://usrl", "duubox mesg");
			RespMessage res = us.processVideo(req);
			System.out.println(res.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("finished");
		}

	}

	@SuppressWarnings("resource")
	public static void VideoRpcDemo() {
		try {
			String configLocation = "classpath*:/dubbo-client.xml";
			ApplicationContext context = new ClassPathXmlApplicationContext(
					configLocation);
			VideoRpcService us = (VideoRpcService) context
					.getBean("videoRpcService");
			String[] names = context.getBeanDefinitionNames();
			ReqestMessage req = new ReqestMessage("http://usrl", "duubox mesg");
			RespMessage res = us.processVideo(req);
			System.out.println(res.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("finished");
		}
	}

	@SuppressWarnings("unused")
	public static void CallbakDemo() {
		try {
			String configLocation = "classpath*:/dubbo-client.xml";
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(
					configLocation);
			VideoCallbackService us = (VideoCallbackService) context
					.getBean("videoCallbackService");
			String[] names = context.getBeanDefinitionNames();
			ReqestMessage req = new ReqestMessage("http://usrl", "duubox mesg");
			us.proceeVideo(req, new VideoCallbackListener() {

				public void changed(RespMessage msg) {
					System.out.println(msg.getStatuscode() + " : "
							+ msg.toString());

				}
			});
			
//			Future<ReqestMessage> pFuture = RpcContext.getContext().getFuture();
//			ReqestMessage p = pFuture.get();
//	        System.out.println(p);

			// System.out.println(res.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("finished");
		}
	}

	public static void main(String[] args) {

		// ConsumerToRest();
		// VideoRpcDemo();
		CallbakDemo();
		// String url = "http://localhost:8080/user/";
		// Client client = ClientBuilder.newClient();
		// WebTarget target = client.target(url);
		// Response response = target.request().get();
		// try {
		// System.out.printf("save user response status: {}, body: {}",
		// response.getStatus(), response.readEntity(String.class));
		// } finally {
		// response.close();
		// client.close();
		// }

		// String port = "8888";
		// processVideo(
		// "http://localhost:" + port + "/services/video/encode.json",
		// "libacc|lib264", MediaType.APPLICATION_JSON_TYPE);
		// processVideo("http://localhost:" + port +
		// "/services/video/encode.xml",
		// "libacc_lib264", MediaType.TEXT_XML_TYPE);
	}
}
