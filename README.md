# Tridion Storage Extension

In this solution a background service will start upon publishing or unpublishing data from Tridion. In order to configure this service, the default application context will be overwritten so we can call our custom configuration files.

In this sample two storage extensions of the filesystem storage type are implemented, being the Page and Binary item types.  As the camel routes are set up as a background service, also other extensions for e.g. JPA storage can call these routes to perform routing to S3 and MongoDB.

## Alter application context with spring configuration

Some additional files to configure in the ${installation_path}/config folder are:

1. cd\_storage\_confsteps.xml
2. camel.properties
3. CDNDAOBundle.xml

## Camel routes for AWS S3 and MongoDB

TBC
