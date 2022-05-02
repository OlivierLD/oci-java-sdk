/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.http.signing.RequestSigningFilter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * List the users, using REST
 */
public class RawRestCallExampleThree {

	public static void main(String[] args) throws Exception {
		// TODO: fill this out, tenancy OCID
		String compartmentId = "ocid1.tenancy.oc1..aaaaaaaax52xyrkarepucv5e75bidtajb5lab6yt7kaqrpi3ko7m3xupaxua";

		String configurationFilePath = "~/.oci/config";
		String profile = "DEFAULT";

		ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configurationFilePath);
		System.out.println(String.format("Tenancy from config: %s", config.get("tenancy"))); // Can replace the compartmentId

		// Pre-Requirement: Allow setting of restricted headers. This is required to allow the SigningFilter
		// to set the host header that gets computed during signing of the request.
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");

		// 1) Create a request signing filter instance
		RequestSigningFilter requestSigningFilter =
				RequestSigningFilter.fromConfigFile(configurationFilePath, profile);

		// 2) Create a Jersey client and register the request signing filter
		Client client = ClientBuilder.newBuilder().build().register(requestSigningFilter);

		// 3) Target an endpoint. You must ensure that path arguments and query
		// params are escaped correctly yourself
		WebTarget target =
				client.target("https://identity.us-ashburn-1.oraclecloud.com")
				// client.target("https://iaas.us-phoenix-1.oraclecloud.com")
						.path("20160918")
				    .path("users")
//						.path("authenticationPolicies")
//						.path(compartmentId);
						.queryParam("compartmentId", compartmentId);

		// 4) Set the expected type and invoke the call
		Invocation.Builder ib = target.request();
		ib.accept(MediaType.APPLICATION_JSON);
		Response response = ib.get();


		System.out.println("------------------------------------------------------------");
		System.out.println(String.format("Request to %s", target.getUri().toURL().toString()));

		// 5) Print the response headers and the body (JSON) as a string
		MultivaluedMap<String, Object> responseHeaders = response.getHeaders();
		System.out.println("--------- Response Headers ---------------------------------");
		responseHeaders.forEach((key, list) -> {
			System.out.println(String.format("%s: %s",
					key,
					list.stream()
							.map(Object::toString)
							.collect(Collectors.joining(","))));
		});
		System.out.println("------------------------------------------------------------");

		InputStream responseBody = (InputStream) response.getEntity();
		try (final BufferedReader reader =
				     new BufferedReader(new InputStreamReader(responseBody, StandardCharsets.UTF_8))) {
			StringBuilder jsonBody = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBody.append(line);
			}
			System.out.println(jsonBody.toString());
		}
	}
}
