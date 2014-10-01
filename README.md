# Tridion Storage Extension

In this solution a background service will start upon publishing or unpublishing data from Tridion. In order to configure this service, the default application context will be overwritten so we can call our custom configuration files.

In this sample two storage extensions of the filesystem storage type are implemented, being the Page and Binary item types.  As the camel routes are set up as a background service, also other extensions for e.g. JPA storage can call these routes to perform routing to S3 and MongoDB.

## Alter application context with spring configuration

Some additional files to configure in the ${installation_path}/config folder are:

### 1. cd\_deployer\_conf.xml

This configuration will be unchanged, configure the Location Path for incoming packages.

### 2. cd\_storage\_confsteps.xml

This file will alter the default SpringLoader class, to point to a customized application context.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
	<Steps>
		<Step Name="SpringLoader" Class="com.tridion.storage.extensions.configuration.SpringConfigurationLoader"/>
		<Step Name="BundleLoader" Class="com.tridion.storage.configuration.BundleConfigurationLoader"/>
		<Step Name="FactoryLoader" Class="com.tridion.storage.configuration.StorageFactoryConfigurationLoader"/>
		<Step Name="TypeMappingCheckLoader" Class="com.tridion.storage.configuration.ConfigurationMetadataCheckLoader"/>
		<Step Name="ConfigurationItemTypeLoader" Class="com.tridion.storage.configuration.ConfigurationItemTypeLoader"/>
		<Step Name="CacheLoader" Class="com.tridion.storage.configuration.CacheConfigurationLoader"/>
		<Step Name="StorageWrapperLoader" Class="com.tridion.storage.configuration.StorageWrapperLoader"/>
	</Steps>
</Configuration>
```

### 3. cd\_storage\_conf.xml

In the storage configuration, it's all about Publications, ItemTypes and .. Extensions in this case.
Currently the project is build for the storage type filesystem, to keep the setup easy and local without the need for database connections.
The functionality can easily be extended for JPA customizations.

This configuration file contains:

  a) a link to a StorageBinding configuration

```xml
<Storages>
    …
      <StorageBindings>
           <Bundle src="CDNDAOBundle.xml"/>
      </StorageBindings>
	…
</Storages>
```

  b) named storage configurations of file directories, with a custom File System DAO Factory

```xml
    …
<Storage Type="filesystem"
         Class="com.tridion.storage.extensions.filesystem.CDNFSDAOFactory"
         Id="defaultFile" defaultFilesystem="true">
    <Root Path="D:\TridionData\Default" />
</Storage>
<Storage Type="filesystem"
         Class="com.tridion.storage.extensions.filesystem.CDNFSDAOFactory"
         Id="defaultHtmlFile" defaultFilesystem="true">
    <Root Path="D:\TridionData\Pages" />
</Storage>
<Storage Type="filesystem"
         Class="com.tridion.storage.extensions.filesystem.CDNFSDAOFactory"
         Id="defaultJsonFile" defaultFilesystem="true">
    <Root Path="D:\TridionData\Pages" />
</Storage>
<Storage Type="filesystem"
         Class="com.tridion.storage.extensions.filesystem.CDNFSDAOFactory"
         Id="defaultDataFile" defaultFilesystem="true">
    <Root Path="D:\TridionData\Binary" />
</Storage>
<Storage Type="filesystem"
         Class="com.tridion.storage.extensions.filesystem.CDNFSDAOFactory"
         Id="defaultResources" defaultFilesystem="true" defaultStorage="false">
    <Root Path="D:\TridionData\Resources" />
</Storage>
    …
```

  c) Type mappings linked to previous storage id's

```xml
    …
    <ItemTypes defaultStorageId="defaultResources" cached="false">
        <Item typeMapping="Page" itemExtension=".html" cached="true" storageId="defaultHtmlFile"/>
        <Item typeMapping="Page" itemExtension=".json" cached="true" storageId="defaultJsonFile"/>
        <Item typeMapping="Binary" storageId="defaultDataFile" cached="false" />
    </ItemTypes>
    …
```

### 4. CDNDAOBundle.xml

Two extensions are customized in this configuration file. Pages can get published to S3 and MongoDB, Binaries can get published to S3.

For the itemType Page, you find the classes:

  * com.tridion.storage.extensions.filesystem.MongoFSPageDAO
  * com.tridion.storage.extensions.filesystem.S3FSPageDAO

For the itemType Binary, you find the class

  * com.tridion.storage.extensions.filesystem.S3FSBinaryContentDAO

```xml
<?xml version="1.0" encoding="UTF-8"?>
<StorageDAOBundles>
    <StorageDAOBundle type="filesystem">
        <StorageDAO typeMapping="Page" class="com.tridion.storage.extensions.filesystem.MongoFSPageDAO"/>
		<StorageDAO typeMapping="ComponentPresentation" class="com.tridion.storage.filesystem.FSComponentPresentationDAO"/>
		<StorageDAO typeMapping="Binary" class="com.tridion.storage.extensions.filesystem.S3FSBinaryContentDAO"/>
		<StorageDAO typeMapping="BinaryVariant" class="com.tridion.storage.filesystem.binaryvariant.FSBinaryVariantDAO"/>
		<StorageDAO typeMapping="Reference" class="com.tridion.storage.filesystem.FSReferenceEntryDAO"/>
		<StorageDAO typeMapping="Schema" class="com.tridion.storage.filesystem.FSSchemaDAO"/>
		<StorageDAO typeMapping="Publication" class="com.tridion.storage.filesystem.FSPublicationDAO"/>
		<StorageDAO typeMapping="XSLT" class="com.tridion.storage.filesystem.FSXSLTDAO"/>
		<StorageDAO typeMapping="LinkInfo" class="com.tridion.storage.filesystem.linkinfo.FSLinkInfoDAO"/>
    </StorageDAOBundle>
</StorageDAOBundles>
```

## Configure Camel routes for AWS S3 and MongoDB

###camel.properties

```
# Add any routing variables here

# AWS S3
# bucket name is always lower case
aws.bucketName=<bucket-name>
aws.accessKey=<access-key>
aws.secretKey=<secret-key>
aws.region=eu-west-1
# for corporate network, proxy implementation can get uncommented in application-context.xml
#aws.proxyHost=10.x.x.x
#aws.proxyPort=3128

# MongoDB Details
mongo.uri=mongodb://<uri>
mongo.db=<db>
mongo.collection=<collection>
mongo.host=<host>
mongo.port=<port>
mongo.user=<user>
mongo.password=<pwd>
```
