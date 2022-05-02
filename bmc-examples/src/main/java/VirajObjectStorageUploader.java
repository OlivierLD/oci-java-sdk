import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.transfer.ProgressReporter;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadRequest;
import com.oracle.bmc.objectstorage.transfer.UploadManager.UploadResponse;
import lombok.Builder;

/**
 * Uses the UploadManager to upload objects.
 * <p>
 * UploadManager can be configured to control how/when it does multi-part uploads,
 * and manages the underlying upload method.  Clients construct a PutObjectRequest
 * similar to what they normally would.
 */
@Builder
public class VirajObjectStorageUploader {

    protected static final Logger logger = Logger.getLogger(VirajObjectStorageUploader.class.getName());
    private final Object ociConfigObject;
    private final String objectName;
    private final File fileHandle;
    private final String tenant;
    private final String agent;


//    public void upload() throws Exception {
//        OCIConfig ociConfig = (OCIConfig) ociConfigObject;
//        execute(ociConfig);
//    }

    public String execute() throws Exception {

        String configurationFilePath = "~/.oci/config";
        String profile = "DEFAULT";

        AuthenticationDetailsProvider provider;
        try {
            provider =
                    new ConfigFileAuthenticationDetailsProvider(configurationFilePath, profile);
        } catch (IOException ioException) {
            // We do not want to recover from the inability to reach the config file.
            throw new RuntimeException("AnalyticsError.OCI_CONFIG_FILE_NOT_FOUND_OR_REACHABLE");
        }
        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.US_ASHBURN_1);
        // Construct GetNamespaceRequest with the given compartmentId.
        GetNamespaceRequest getNamespaceRequest =
                GetNamespaceRequest.builder()
                        .compartmentId("ocid1.tenancy.oc1..aaaaaaaa46rfbmuj2cciouoyz6sbmsbxkzjb3ibkfvhd5vttptgcpjruskda")
                        .build();
        String namespace = client.getNamespace(getNamespaceRequest).getValue();
        // configure upload settings as desired
        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .lengthPerUploadPart(2)
                        .minimumLengthForMultipartUpload(2)
                        .build();
        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);
        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName("ociConfig.getBucketName()")
                        .namespaceName(namespace)
                        .objectName(tenant + "/" + agent + "/" + objectName)
                        .build();
        ProgressReporter progress =
                new ProgressReporter() {
                    long lastCompleted = 0L;

                    @Override
                    public void onProgress(long completed, long total) {
                        if (lastCompleted + 100 * 1024 < completed || completed == total) {
                            logger.log(Level.INFO, () -> "Completed " + completed + " out of " + total);
                            lastCompleted = completed;
                        } else if (lastCompleted > completed) {
                            lastCompleted = completed;
                        }
                    }
                };
        UploadRequest uploadDetails =
                UploadRequest.builder(fileHandle)
                        .allowOverwrite(true)
                        .progressReporter(progress)
                        .build(request);
        // upload request and print result
        // if multi-part is used, and any part fails, the entire upload fails and will throw BmcException
        UploadResponse response = uploadManager.upload(uploadDetails);
        return response.toString();
    }

    public static void main(String... args) throws Exception {
        new VirajObjectStorageUploader(null, "objectName", null, "tenant", "agent")
                .execute();
    }
}
