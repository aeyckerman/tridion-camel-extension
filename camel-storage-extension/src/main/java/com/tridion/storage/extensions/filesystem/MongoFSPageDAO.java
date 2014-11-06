package com.tridion.storage.extensions.filesystem;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.tridion.broker.StorageException;
import com.tridion.data.CharacterData;
import com.tridion.storage.dao.PageDAO;
import com.tridion.storage.filesystem.FSEntityManager;
import com.tridion.storage.filesystem.FSPageDAO;
import com.tridion.storage.services.LocalThreadTransaction;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.component.mongodb.MongoDbConstants;
import org.apache.camel.component.mongodb.MongoDbOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;

@Component("MongoFSPageDAO")
@Scope("prototype")
public class MongoFSPageDAO extends FSPageDAO implements PageDAO {

	private  Logger log = LoggerFactory.getLogger(MongoFSPageDAO.class);

    private CamelContext camelContext;
    private ProducerTemplate template;
    private CharacterData cpPage;

    public MongoFSPageDAO(String storageId, String storageName,
                          File storageLocation) {
		super(storageId, storageName, storageLocation);
    }

	public MongoFSPageDAO(String storageId, String storageName, File storageLocation, FSEntityManager entityManager)
	{
		super(storageId, storageName, storageLocation, entityManager);
        log.debug("MongoFSPageDAO - How often does this happen ?");

	}
	
	public void create(CharacterData page, String relativePath) throws StorageException
	{

        DBObject dbObject = null;
        cpPage = page;

        log.info("New Page Creation - Interesting to know");
        log.info("Storage Id: " + getStorageId());
        log.info("Storage Location: " + getStorageLocation());
        log.debug("Binding Name: " + getBindingName());
        log.debug("Type Mapping: " + getTypeMapping());
        log.debug("Class: " + getClass());
        log.debug("Page Id: " + cpPage.getId());

        // TODO: fire the camel route and wait for feedback
        camelContext = CDNFSDAOFactory.getCamelContext();

        try {
            /* Temporary wrapper to make any Page fit into MongoDB ..
            String pageId = "" + cpPage.getId();
            String json = null;

            log.warn("Java Temp Dir: " + System.getProperty("java.io.tmpdir") );
            json = "{'_id' : 'tcm-" + pageId.replace(":", "_") + "', 'body' : '" + URLEncoder.encode(cpPage.getString()) + "'}";
            dbObject = (DBObject)JSON.parse(json);*/

            // Parse the actual json document
            dbObject = (DBObject)JSON.parse(cpPage.getString());

        } catch (IOException e) {
            log.error("Looks like json wasn't formatted correctly : " + e.getMessage());
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new StorageException("Camel route failed, abort the transaction");
        }


        try {
            template = camelContext.createProducerTemplate();
            Exchange exchange = ExchangeBuilder.anExchange(camelContext)
                                .withProperty(Exchange.FILE_NAME, relativePath)
                                //.withProperty(Exchange.FILE_LENGTH, s3Plugin.transfer(cpPage.getInputStream()))
                                .withPattern(ExchangePattern.InOut)
                                .withBody(dbObject)
                                //.withHeader(S3Constants.KEY, relativePath)
                                .withHeader(MongoDbConstants.OPERATION_HEADER, MongoDbOperation.save)
                                .build();
            Exchange result = template.send("direct:mongoObject", exchange);

            log.info("Result : ");
            log.info("<to be defined>: ");

            if (result.getOut().isFault() || result.getException() != null) {
                throw new Exception("Some bad result: " + result.getOut().getExchange().getException());
            }

        } catch (Exception e) {
            log.error("Camel route issue, investigate connection: " + e.getMessage());
            throw new StorageException("Camel route failed, abort the transaction");
        } finally {
            super.create(page, relativePath);
        }


		String transactionId = LocalThreadTransaction.getTransactionId();
        CDNFSDAOFactory.registerAction(transactionId, relativePath, CDNFSDAOFactory.Action.PERSIST);
        log.debug("Created page so registered PERSIST action: transaction " + transactionId + ", path " + relativePath );
	}
    
	public void remove(int publicationId, int pageId, String relativePath)   throws StorageException
	{


        // 64: Page Item Type
        String query = "{'_id':'" + "tcm-" + publicationId + "-" + pageId + "-64" +"'"+ "}";
        DBObject dbObject = (DBObject)JSON.parse(query);


        try {
            log.info("Page Removal - Interesting to know");
            log.info("Storage Id: " + getStorageId());
            log.info("Storage Location: " + getStorageLocation());
            log.debug("Binding Name: " + getBindingName());
            log.debug("Type Mapping: " + getTypeMapping());
            log.debug("Class: " + getClass());
            log.debug("Page Id: " + pageId);

            // TODO: fire the camel route and wait for feedback
            camelContext = CDNFSDAOFactory.getCamelContext();

            template = camelContext.createProducerTemplate();
            Exchange exchange = ExchangeBuilder.anExchange(camelContext)
                                .withProperty(Exchange.FILE_NAME, relativePath)
                                //.withProperty(Exchange.FILE_LENGTH, s3Plugin.transfer(cpPage.getInputStream()))
                                .withPattern(ExchangePattern.InOut)
                                //.withBody("{'_id':'" + pageId +"'"+ "}")
                                .withBody(dbObject)
                                .withHeader(MongoDbConstants.OPERATION_HEADER, MongoDbOperation.remove)
                                .build();
            Exchange result = template.send("direct:mongo", exchange);

            log.info("Result : ");
            log.info("<to be defined>: ");

            if (result.getOut().isFault() || result.getException() != null) {
                throw new Exception("Some bad result: " + result.getOut().getExchange().getException());
            }

        } catch (Exception e) {
            throw new StorageException("Camel route failed, abort the transaction");
        } finally {
		    super.remove(publicationId, relativePath);
        }


		String transactionId = LocalThreadTransaction.getTransactionId();
		CDNFSDAOFactory.registerAction(transactionId, relativePath, CDNFSDAOFactory.Action.REMOVE);
		log.debug("Removed page so registered REMOVE action: transaction " + transactionId + ", path " + relativePath );
	}
	
	public void update(CharacterData page, String originalRelativePath, String newRelativePath) throws StorageException 
	{
        CharacterData cpPage = page;
        try {
            log.info("Existing Page Update - Interesting to know");
            log.info("Storage Id: " + getStorageId());
            log.info("Storage Location: " + getStorageLocation());
            log.debug("Binding Name: " + getBindingName());
            log.debug("Type Mapping: " + getTypeMapping());
            log.debug("Class: " + getClass());
            log.debug("Page Id: " + cpPage.getId());

            /*System.out.println("Page String: " + cpPage.getString());*/

            // No action performed => calls create

        } catch (Exception e) {
            throw new StorageException("Camel route failed, abort the extension");
        }

		super.update(page, originalRelativePath, newRelativePath);
		String transactionId = LocalThreadTransaction.getTransactionId();
		//the super class will do a create, so we don't need to do anything unless the file name has changed 
		//(in which case we need to register a remove for the old path)
		if (originalRelativePath.compareTo(newRelativePath)!=0)
		{
			CDNFSDAOFactory.registerAction(transactionId, originalRelativePath, CDNFSDAOFactory.Action.REMOVE);
			log.debug("Updated page moved so registered REMOVE action: transaction " + transactionId + ", path " + originalRelativePath );
		}
	}

     public String buildString(InputStream is) throws IOException {
         StringBuilder sb = new StringBuilder();
         Reader reader = new InputStreamReader(is, "utf-8");
         BufferedReader bufRead = new BufferedReader(reader);
            try {

                String line = bufRead.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append('\n');
                    line = bufRead.readLine();
                }

            } finally {
                bufRead.close();
            }
        return sb.toString();
    }
}
