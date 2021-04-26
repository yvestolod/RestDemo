// (C) Copyright IBM Corp. 2021 All Rights Reserved
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
//   Drug claim - amount exceeded claim limit of $800
//   Dental claim - amount exceeded claim limit of $500
//   Medical claim - amount exceeded claim limit of $300 
//

package com.ibm.rest.demo;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Path("/rule")
public class HealthClaimsServices extends Application {

	private static final String MEDICAL = "MEDICAL";
	private static final String DENTAL = "DENTAL";
	private static final String DRUG = "DRUG";
	private static final String MEDICAL_LIMIT = "300.00";
	private static final String DENTAL_LIMIT = "500.00";
	private static final String DRUG_LIMIT = "800.00";
	private static final String VERSION = "5.0";
	private static final int EXCEEDED_LIMIT = -1;
	
	@GET
	@Produces("application/json")
	public Response getParameters(@QueryParam("claimAmount") BigDecimal claimAmount, @QueryParam("claimType") String claimType) {
		
		String jsonResponse = "";
		String reason = "Normal claim";
		String status = "Accepted";
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		
	    //  Drug claim - amount exceeded claim limit of $800
	    //  Dental claim - amount exceeded claim limit of $500
	    //  Medical claim - amount exceeded claim limit of $300 		
		BigDecimal drugLimit = new BigDecimal(DRUG_LIMIT);
		BigDecimal dentalLimit = new BigDecimal(DENTAL_LIMIT);
		BigDecimal medicalLimit = new BigDecimal(MEDICAL_LIMIT);
		
		if (claimType != null && claimAmount != null) {
			
			if (MEDICAL.equalsIgnoreCase(claimType) && 
				medicalLimit.compareTo(claimAmount) == EXCEEDED_LIMIT) {
				status = "Rejected";
				reason = "Amount exceeded " + MEDICAL_LIMIT + ". Claim require further review.";
			}
			else if (DENTAL.equalsIgnoreCase(claimType) &&
					 dentalLimit.compareTo(claimAmount) == EXCEEDED_LIMIT) {
				status = "Rejected";
				reason = "Amount exceeded " + DENTAL_LIMIT + ". Claim require further review.";
			} 
			else if (DRUG.equalsIgnoreCase(claimType) &&
					 drugLimit.compareTo(claimAmount) == EXCEEDED_LIMIT) {
				status = "Rejected";
				reason = "Amount exceeded " + DRUG_LIMIT + ". Claim require further review.";
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

			System.out.println("[CLAIMAPP] Version " + VERSION + " " + ts + ": Claim = " + claimType.toUpperCase() + 
					           ", Claim Amount = " + claimAmount +
					           ", Status = " + status +
					           ", Reason = " + reason);
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
