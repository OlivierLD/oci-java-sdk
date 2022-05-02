## Oliv scratch-pad

First, compile it (use proxy options if you're within a firewall)
```
$ mvn [-Dhttp.proxyHost=www-proxy.us.oracle.com -Dhttp.proxyPort=80 -Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80] compile
```

package it
```
$ mvn [-Dhttp.proxyHost=www-proxy.us.oracle.com -Dhttp.proxyPort=80 -Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80] -Dmaven.test.skip=true package
```
Requested resources should be in the `target` directory.

Then:
```
$ cd bmc-examples
$ mvn [-Dhttp.proxyHost=www-proxy.us.oracle.com -Dhttp.proxyPort=80 -Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80] -Dmaven.test.skip=true exec:java -Dexec.mainClass="RawRestCallExample"
```

Pure Java way (no REST):
```
$ cd bmc-examples
$ mvn [-Dhttp.proxyHost=www-proxy.us.oracle.com -Dhttp.proxyPort=80 -Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80] -Dmaven.test.skip=true exec:java -Dexec.mainClass="ListWorkRequestsExample" -Dexec.args="ocid1.tenancy.oc1..aaaaaaaaxd6jemgcmdpb3pnctl7yf2hzswgi6g55gth7ioc2gbpbvqo5ecoa ocid1.instance.oc1.iad.abuwcljrnfre4xutla2jyep3yghuungzecbnigra7jzatfhzx7gb5dxxzlsq"
```

## Bulk notes, OCI & Co

Console:
- <https://console.us-ashburn-1.oraclecloud.com/>
- Tenant: `ocipaas1`, `paasdevoic`

To read, about the CLI:
- Install CLI SDK: <https://docs.cloud.oracle.com/iaas/Content/API/SDKDocs/cliinstall.htm>
- CLI: <https://docs.cloud.oracle.com/iaas/Content/API/Concepts/cliconcepts.htm>

To get your Token & Co:
- Token-based-Authentication for the CLI: <https://docs.cloud.oracle.com/iaas/Content/API/SDKDocs/clitoken.htm>

Create your local credentials:
```
$ oci session authenticate
$ oci iam region list --config-file ~/.oci/config --profile oliv-profile --auth security_token
```
In the above, `oliv-profile` is provided by **you** during the process.
FYI, most of - if not all - the examples here use `DEFAULT` instead.

Your credentials have a default TTL (Time To Live) of 60 minutes.

Sometime I have an error saying
```
UnboundLocalError: local variable 'profile_name' referenced before assignment
```
I did 
```
$ rm -rf ~/.oci/sessions
$ rm ~/.oci/config
```
And run `oci session authenticate` again, successfully after that. 

It generates a set of keys under `~/.oci/sessions/DEFAULT` (`DEFAULT` being the name of the profile), you need to update your user profile with the public key,
in the OCI Web console.

Example:
```
[DEFAULT]
user=ocid1.user.oc1..aaaaaaaa7xtgzt2s3govzqpmjd7n2g6o5ladgresnoxdsvxaedkldge7wxoq
fingerprint=53:0f:db:88:43:8a:a0:78:1e:46:50:fc:65:7f:fc:3f
key_file=/Users/olediour/.oci/sessions/DEFAULT/oci_api_key.pem
tenancy=ocid1.tenancy.oc1..aaaaaaaaxd6jemgcmdpb3pnctl7yf2hzswgi6g55gth7ioc2gbpbvqo5ecoa
region=us-ashburn-1
security_token_file=/Users/olediour/.oci/sessions/DEFAULT/token
```
> Note : I've had to add the `user` by hand.
```
oci iam region list --config-file /Users/olediour/.oci/config --profile DEFAULT --auth security_token
```
works without the user, but Java examples would not. Why?

Find OCIDs, tenant and user:

https://docs.cloud.oracle.com/iaas/Content/GSG/Tasks/gettingstartedwiththeCLI.htm
```
$ export T=ocid1.tenancy.oc1..aaaaaaaaxd6jemgcmdpb3pnctl7yf2hzswgi6g55gth7ioc2gbpbvqo5ecoa
$ oci iam compartment list -c $T --config-file ~/.oci/config --profile oliv-profile --auth security_token
```

```
$ oci iam policy list --compartment-id ocid1.tenancy.oc1..aaaaaaaaxd6jemgcmdpb3pnctl7yf2hzswgi6g55gth7ioc2gbpbvqo5ecoa
$ oci os bucket list -c ocid1.tenancy.oc1..aaaaaaaa46rfbmuj2cciouoyz6sbmsbxkzjb3ibkfvhd5vttptgcpjruskda
$ oci os bucket list --config-file /Users/olivierlediouris/.oci/config --profile oliv_profile --auth security_token -c ocid1.tenancy.oc1..aaaaaaaa46rfbmuj2cciouoyz6sbmsbxkzjb3ibkfvhd5vttptgcpjruskda
$ oci os bucket list -c ocid1.tenancy.oc1..aaaaaaaa46rfbmuj2cciouoyz6sbmsbxkzjb3ibkfvhd5vttptgcpjruskda --profile DEFAULT --namespace devdigital --auth security_token
```

OCI CLI Reference: <https://docs.cloud.oracle.com/en-us/iaas/tools/oci-cli/2.17.0/oci_cli_docs/index.html>

SDKs, and git repos:
- <https://docs.cloud.oracle.com/iaas/Content/API/Concepts/sdks.htm>
- Git Repo (CLI, in Python): <https://github.com/oracle/oci-cli>
- Java SDK last release: <https://github.com/oracle/oci-java-sdk/releases/tag/v1.5.9>
- Java SDK doc: <https://docs.cloud.oracle.com/iaas/Content/API/SDKDocs/javasdk.htm>

### Configuring Credentials
<https://docs.cloud.oracle.com/iaas/Content/API/SDKDocs/javasdkgettingstarted.htm#ConfigCreds>
which in turn refers to <https://docs.cloud.oracle.com/iaas/Content/API/Concepts/sdkconfig.htm>.


### Required Keys and OCIDs
<https://docs.cloud.oracle.com/iaas/Content/API/Concepts/apisigningkey.htm#Other>


#### OCI Instance
- Bucket `olediour-bucket-20201215-1103`
    - Namespace: `devdigital`
    - ETag: `631a034e-954d-4dd9-aad9-65081c9f3506`
    - OCID: `ocid1.bucket.oc1.iad.aaaaaaaalsr4k5nn42m5fmgcteadgof7fpbr7pezwafnlts4i3gwsqevemea`

##### BOAT entitlements, etc.
- <https://confluence.oraclecorp.com/confluence/pages/viewpage.action?pageId=1250297107>
- <https://permissions.oci.oraclecorp.com/?tab=myPermissions> (Requires YubiKey)
- <https://confluence.oraclecorp.com/confluence/display/IBS/Fleet+Management+VM#FleetManagementVM-BOATEntitlements>

#### Examples
After modifying an example, recompile before running
```
$ mvn -Dhttp.proxyHost=www-proxy.us.oracle.com -Dhttp.proxyPort=80 -Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80 -Dmaven.test.skip=true compile
$ mvn -Dhttp.proxyHost=www-proxy.us.oracle.com -Dhttp.proxyPort=80 -Dhttps.proxyHost=www-proxy.us.oracle.com -Dhttps.proxyPort=80 -Dmaven.test.skip=true exec:java -Dexec.mainClass="ObjectStorageSyncExample"
```


---
