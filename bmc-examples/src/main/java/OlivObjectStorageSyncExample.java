/**
 * Copyright (c) 2016, 2019, Oracle and/or its affiliates. All rights reserved.
 */
import com.google.common.base.Supplier;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.BucketSummary;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest;
import com.oracle.bmc.objectstorage.requests.ListBucketsRequest.Builder;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.ListBucketsResponse;

import java.io.IOException;
import java.io.InputStream;

public class OlivObjectStorageSyncExample {

    public static void main(String[] args) throws Exception {

        String configurationFilePath = "~/.oci/config";
        String profile = "DEFAULT";

//        ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configurationFilePath);
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configurationFilePath, profile);

//        Supplier<InputStream> privateKeySupplier
//                = new SimplePrivateKeySupplier("~/.oci/oci_api_key.pem");
        Supplier<InputStream> privateKeySupplier = new SimplePrivateKeySupplier(config.get("key_file"));

        AuthenticationDetailsProvider provider;
        try {
//            provider = new ConfigFileAuthenticationDetailsProvider(configurationFilePath, profile);
            provider = SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(config.get("tenancy"))
                    .userId(config.get("user"))
                    .fingerprint(config.get("fingerprint"))
                    .privateKeySupplier(privateKeySupplier)
                    .build();
        } catch (Exception ioe) {
            throw new RuntimeException("AnalyticsError.OCI_CONFIG_FILE_NOT_FOUND_OR_REACHABLE");
        }

        System.out.println("------------------------------");
        System.out.println(String.format(">> Tenant ID: %s", provider.getTenantId()));
        System.out.println(String.format(">> User ID: %s", provider.getUserId()));
        System.out.println("------------------------------");

        ObjectStorage client = new ObjectStorageClient(provider); // This one know about operation on ObjectStorage
        client.setRegion(Region.US_ASHBURN_1);

        GetNamespaceRequest getNamespaceRequest = GetNamespaceRequest.builder()
                .compartmentId("ocid1.tenancy.oc1..aaaaaaaa46rfbmuj2cciouoyz6sbmsbxkzjb3ibkfvhd5vttptgcpjruskda")
                .build();

        String namespace = client.getNamespace(getNamespaceRequest).getValue();
        System.out.println(String.format("NameSpace: %s", namespace));

        GetNamespaceResponse namespaceResponse = client.getNamespace(getNamespaceRequest);

        String namespaceName = namespaceResponse.getValue();
        System.out.println("Using namespace: " + namespaceName);

        Builder listBucketsBuilder =
                ListBucketsRequest.builder()
                        .namespaceName(namespaceName)
                        .compartmentId(provider.getTenantId());

        String nextToken = null;
        do {
            listBucketsBuilder.page(nextToken);
            ListBucketsResponse listBucketsResponse =
                    client.listBuckets(listBucketsBuilder.build());
            for (BucketSummary bucket : listBucketsResponse.getItems()) {
                System.out.println("Found bucket: " + bucket.getName());
            }
            nextToken = listBucketsResponse.getOpcNextPage();
        } while (nextToken != null);

        client.close();
    }
}
