package com.example.jmdns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

public class Main {

	final static String DEFAULT_DOMAIN="local.";
	final static String SERVICE_TYPE="_personal._tcp."+DEFAULT_DOMAIN;
	static String HOST_NAME;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//get local computer name.
		try {
			HOST_NAME=InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		JmDNS jmdns = null;
		try {
		boolean log = false;
        if (log) {
            //ConsoleHandler handler = new ConsoleHandler();
        	FileHandler handler = new FileHandler("myapp-log.%u.%g.txt");
        	handler.setFormatter(new SimpleFormatter());
        	handler.setLevel(Level.FINER);
            for (Enumeration<String> enumerator = LogManager.getLogManager().getLoggerNames(); enumerator.hasMoreElements();) {
                String loggerName = enumerator.nextElement();
                Logger logger = Logger.getLogger(loggerName);
                logger.addHandler(handler);
                logger.setLevel(Level.FINER);
            }
        }
		
		
		
		
			jmdns = JmDNS.create();
			jmdns.addServiceListener(SERVICE_TYPE, new SampleListener());
			
			listAllServiceOnLocalLink(jmdns, SERVICE_TYPE);
			
			
			ServiceInfo myService=ServiceInfo.create(SERVICE_TYPE, HOST_NAME, 12345, "test service from win7.");
			registHostName(jmdns, myService);
			
			System.out.println("Press q and Enter, to quit");
			int b;
			while ((b = System.in.read()) != -1 && (char) b != 'q') {
                /* Stub */
            }
			
			
			jmdns.unregisterService(myService);
			jmdns.unregisterAllServices();
			jmdns.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (jmdns != null){
				try {
					jmdns.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private static void registHostName(JmDNS jmdns, ServiceInfo serviceInfo){
		
		output("Ready to register...");

		try {
			jmdns.registerService(serviceInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		output("register service:\n"+serviceInfo);

		output("");
	}
	
	private static void listAllServiceOnLocalLink(JmDNS jmdns, String type){
		
			output("Ready to list service info...");
			ServiceInfo[] infos = jmdns.list(SERVICE_TYPE);
			output("List "+SERVICE_TYPE);
            for (int i = 0; i < infos.length; i++) {
            	output(infos[i].toString());
            	output("Testing "+infos[i].getName()+"."+infos[i].getDomain()+" isReachable? ");
            	
            	try {
					InetAddress inet=InetAddress.getByName(infos[i].getName()+"."+infos[i].getDomain());
					output(inet.getHostAddress());
					output(Boolean.toString(inet.isReachable(5000)));
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					output("UnknownHostException");
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					output("IOException");
					e.printStackTrace();
				}
            	
            }
            output("");
            
            
		
	}
	
	
	private static void listService(final String msg) {
		
		output(msg);
	
	}
	
	private static void output(String message){
		System.out.println(message);
	}
	
	private static final char[] _nibbleToHex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	private static String toHex(byte[] code) {
        StringBuilder result = new StringBuilder(2 * code.length);

        for (int i = 0; i < code.length; i++) {
            int b = code[i] & 0xFF;
            result.append(_nibbleToHex[b / 16]);
            result.append(_nibbleToHex[b % 16]);
        }

        return result.toString();
    }

}
