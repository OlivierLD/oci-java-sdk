/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.google.common.net.UrlEscapers;

import com.oracle.bmc.http.signing.RequestSigningFilter;

public class OlivRawRestCallExampleTwo {

    public static void main(String... args) throws Exception {
        // TODO: fill this out
        String instanceId = "ocid1.instance.oc1.iad.abuwcljrnfre4xutla2jyep3yghuungzecbnigra7jzatfhzx7gb5dxxzlsq";

        String configurationFilePath = "~/.oci/config"; // Contains all the keys required by OCI
        String profile = "DEFAULT";
        //String compartmentId = "ocid1.tenancy.oc1..aaaaaaaax52xyrkarepucv5e75bidtajb5lab6yt7kaqrpi3ko7m3xupaxua"; // Tenancy (root compartment)
        String compartmentId = "ocid1.compartment.oc1..aaaaaaaax454wp2epzvz2c7g24rsrme7e2e6ypheo7ns2j5voo4bs5pfk3hq"; // MockManagedCompartment
        String namespace = "/";
        // Usage: TestClient <profile> <compartmentId>
        if (args.length > 0) {
            profile = args[0];
            if (args.length > 1) {
                compartmentId = args[1];
                namespace = args[2];
            }
        }
        System.out.println("PROFILE = " + profile);
        System.out.println("COMPARTMENT-ID = " + compartmentId);
        System.out.println("NAMESPACE = " + namespace);

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

        //String compartmentId = "ocid1.compartment.oc1..aaaaaaaa6svxcsai3azms7fs45v6upvcz7fmb5hldebstc4jf6wout2nwtiq";
        /*
        WebTarget target =
                client.target("https://iaas.us-ashburn-1.oraclecloud.com")
                        .path("20160918")
                        .path("instances")
                        .path(UrlEscapers.urlPathSegmentEscaper().escape(instanceId));
        //String compartmentId = "ocid1.tenancy.oc1..aaaaaaaaxd6jemgcmdpb3pnctl7yf2hzswgi6g55gth7ioc2gbpbvqo5ecoa";
        WebTarget target =
                client.target("https://identity.us-ashburn-1.oraclecloud.com")
                        .path("20160918")
                        .path("policies")
                        .queryParam("compartmentId", compartmentId);
        WebTarget target =
                client.target("http://localhost:19000/")
                        .path("ic/api/1.0.0")
                        .path("integrations");
                        */
        WebTarget target =
            client.target("http://localhost:24000/")
                .path("20180828")
                .path("integrations")
                .queryParam("compartmentId", compartmentId)
                .queryParam("namespace", namespace);

        // 4) Set the expected type and invoke the call
        Invocation.Builder ib = target.request();
        ib.accept(MediaType.APPLICATION_JSON);
        Response response = ib.get();

        // 5) Print the response headers and the body (JSON) as a string
        MultivaluedMap<String, Object> responseHeaders = response.getHeaders();
        responseHeaders.keySet().stream().forEach(k -> {
            System.out.println(String.format("Header: %s = %s", k, responseHeaders.get(k).stream().map(h -> h.toString()).collect(Collectors.joining(", ")) ));
        });

//        System.out.println(responseHeaders);
        System.out.println(String.format("Status: %d", response.getStatus()));

//        System.out.println(responseHeaders);
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
