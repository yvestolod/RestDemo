// (C) Copyright IBM Corp. 2020 All Rights Reserved
//
// Licensed under the Apache License, Version 2.0
// which you can read at https://www.apache.org/licenses/LICENSE-2.0
//
// This Java program returns a claims processing service.
//
// For this sample, we use another program in z/OS written in COBOL.
// The COBOL batch program is extended to call a claims service hosted in
// Liberty. The claims service is accessible as a REST API and the COBOL 
// program uses the API requester function of z/OS Connect Enterprise
// Edition to call the REST API hosted on Liberty on z/OS.
// 
// The insurance claims service will reject requests if the amount
// exceeded the limit:
//
//   Drug claim - amount exceeded claim limit of $1000
//   Dental claim - amount exceeded claim limit of $800
//   Medical claim - amount exceeded claim limit of $500 
//

package com.ibm.rest.demo;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.math.BigDecimal;

@Path("/rule")
public class HealthClaimsServices extends Application {

	private static final String MEDICAL = "MEDICAL";
	private static final String DENTAL = "DENTAL";
	private static final String DRUG = "DRUG";
	private static final int EXCEEDED_LIMIT = -1;
	
	@GET
	@Produces("application/json")
	public Response getParameters(@QueryParam("claimAmount") BigDecimal claimAmount, @QueryParam("claimType") String claimType) {
		
		String jsonResponse = "";
		String reason = "Normal claim";
		String status = "Accepted";

	    //  Drug claim - amount exceeded claim limit of $1000
	    //  Dental claim - amount exceeded claim limit of $800
	    //  Medical claim - amount exceeded claim limit of $500 		
		BigDecimal drugLimit = new BigDecimal(1000.00);
		BigDecimal medicalLimit = new BigDecimal(500.00);
		BigDecimal dentalLimit = new BigDecimal(800.00);
		
		if (claimType != null && claimAmount != null) {
			
			if (MEDICAL.equalsIgnoreCase(claimType) && 
				medicalLimit.compareTo(claimAmount) == EXCEEDED_LIMIT) {
				status = "Rejected";
				reason = "Amount exceeded $500. Claim require further review.";
			}
			else if (DENTAL.equalsIgnoreCase(claimType) &&
					 dentalLimit.compareTo(claimAmount) == EXCEEDED_LIMIT) {
				status = "Rejected";
				reason = "Amount exceeded $800. Claim require further review.";
			} 
			else if (DRUG.equalsIgnoreCase(claimType) &&
					 drugLimit.compareTo(claimAmount) == EXCEEDED_LIMIT) {
				status = "Rejected";
				reason = "Amount exceeded $1000. Claim require further review.";
			} 
			else {
				status = "Accepted";
				reason = "Normal claim";
			}
			
			jsonResponse = "{\"claimType\": \"" + claimType.toUpperCase() + 
					       "\", \"claimAmount\" : " + claimAmount + 
                           ", \"status\" : \"" + status + 
                           "\", \"reason\" : \"" + reason + 
					       "\"}" ;
		}
		else {
			status = "Rejected";
			reason = "Missing query parameter claimAmount and/or claimType";
			jsonResponse = "{\"status\" : \"" + status + 
                           "\", \"reason\" : \"" + reason + 
				           "\"}" ;
		}
		
		return Response
		  .status(Response.Status.OK)
		  .entity(jsonResponse)
		  .build();
	}
}
