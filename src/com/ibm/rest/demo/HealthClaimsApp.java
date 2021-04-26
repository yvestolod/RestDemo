package com.ibm.rest.demo;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/claim")
public class HealthClaimsApp extends Application {
	
	public HealthClaimsApp() {
		System.out.println("=======================================");
		System.out.println("= ENVIRONMENT INFORMATION             =");
		System.out.println("=======================================");
		System.out.println("= Operating System: " + System.getProperty("os.name"));
		System.out.println("= OS Arch: " + System.getProperty("os.arch"));
		System.out.println("= Java Runtime version: " + System.getProperty("java.runtime.version"));
		System.out.println("=======================================");
	}

}
