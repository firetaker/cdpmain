package com.ws.cdp.cdpprovider.service.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ws.cdp.cdpprovider.service.video.VideoCallbackServiceImpl;

public class MainEntry {
	private static Logger logger = Logger.getLogger(MainEntry.class);
	
	void start() {
        String configLocation = "classpath*:/dubbo-provider.xml";
		//String configLocation = "classpath*:/*.xml";
        ApplicationContext context = new ClassPathXmlApplicationContext(
        		configLocation);       
        String[] names = context.getBeanDefinitionNames();
        logger.info("Beans:");
        for (String string : names)
        {
        	logger.info(string);
        }

	}
	
	public static void main(String[] args) throws InterruptedException {		
		//com.alibaba.dubbo.container.Main.main(new String[]{"classpath*:/*.xml"});
		
		MainEntry luncher = new MainEntry();
        luncher.start();
        logger.info("stated .....");
        
//      try {
//			System.in.read();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
        
        // TODO 关闭
        //  VideoCallbackServiceImpl callImpl = (VideoCallbackServiceImpl)context.getBean("videoCallbackService");
        //  callImpl.shutdown();
        
        synchronized (MainEntry.class) {
			while (true) {
				try {
					MainEntry.class.wait();
					System.out.println("STOPPPPPPPPPPPPPPPPPPPPPPP----------");
				} catch (InterruptedException e) {
					logger.info("== synchronized error:", e);
				}
			}
		}
	}
}
