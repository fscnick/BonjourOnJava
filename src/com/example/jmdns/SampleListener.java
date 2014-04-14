package com.example.jmdns;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

public class SampleListener implements ServiceListener {

	@Override
    public void serviceAdded(ServiceEvent event) {
        System.out.println("Service added   : " + event.getName() + "." + event.getType()+":"+event.getInfo().getPort());
        
        System.out.println("Testing "+event.getInfo().getName()+"."+event.getInfo().getDomain()+" isReachable? ");
    	/* also we can use InetAddress class(event.getInfo().getInet4Addresses()) is more concrete way. */
    	try {
			InetAddress inet=InetAddress.getByName(event.getInfo().getName()+"."+event.getInfo().getDomain());
			System.out.println(inet.getHostAddress());
			System.out.println(Boolean.toString(inet.isReachable(5000)));
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("UnknownHostException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException");
			e.printStackTrace();
		}
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        System.out.println("Service removed : " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        System.out.println("Service resolved: " + event.getInfo());
    }

}
