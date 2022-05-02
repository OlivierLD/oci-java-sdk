/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.identity.Identity;
import com.oracle.bmc.identity.IdentityClient;
import com.oracle.bmc.identity.model.Policy;
import com.oracle.bmc.identity.requests.ListPoliciesRequest;
import com.oracle.bmc.identity.responses.ListPoliciesResponse;

public class ListPoliciesExample {
	public static void main(String[] args) throws Exception {

		String configurationFilePath = "~/.oci/config";
		String profile = "DEFAULT";

		AuthenticationDetailsProvider provider =
				new ConfigFileAuthenticationDetailsProvider(configurationFilePath, profile);

		String compartmentId = provider.getTenantId(); // This is the root compartment, the OCID of the tenancy
//		final String tenantId = provider.getTenantId();
		Identity identityClient = new IdentityClient(provider);
		identityClient.setRegion(Region.US_ASHBURN_1);

		// List all policies within tenancy with Accessible compartment filter
		String nextPageToken = null;
		System.out.println(String.format("List Policies for compartment %s", compartmentId));
		do {
			ListPoliciesResponse response =
					identityClient.listPolicies(
							ListPoliciesRequest.builder()
									.limit(3)
									.compartmentId(compartmentId)
									.page(nextPageToken)
									.build());

			for (Policy policy : response.getItems()) {
				System.out.println(policy);
			}
			nextPageToken = response.getOpcNextPage();
		} while (nextPageToken != null);

	}

}
