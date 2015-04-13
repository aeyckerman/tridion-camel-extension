package com.tridion.storage.extensions.filesystem;

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
import org.apache.camel.component.aws.s3.S3Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component("S3FSPageDAO")
@Scope("prototype")
public class S3FSPageDAO extends FSPageDAO implements PageDAO {

	private  Logger log = LoggerFactory.getLogger(S3FSPageDAO.class);

	public S3FSPageDAO(String storageId, String storageName,
                       File storageLocation) {
		super(storageId, storageName, storageLocation);
    }
	
	public S3FSPageDAO(String storageId, String storageName, File storageLocation, FSEntityManager entityManager)
	{
		super(storageId, storageName, storageLocation, entityManager);

	}
	
	public void create(CharacterData page, String relativePath)    throws StorageException
	{
        CamelContext camelContext;
        ProducerTemplate template;
        CharacterData cpPage = page;
        boolean feedback;
        try {
            log.info("New Page Creation - Interesting to know");
            log.info("Storage Id: " + getStorageId());
            log.info("Storage Location: " + getStorageLocation());
            log.debug("Binding Name: " + getBindingName());
            log.debug("Type Mapping: " + getTypeMapping());
            log.debug("Class: " + getClass());
            log.debug("Page Id: " + cpPage.getId());

            camelContext = CDNFSDAOFactory.getCamelContext();

            template = camelContext.createProducerTemplate();
            Exchange exchange = ExchangeBuilder.anExchange(camelContext)
                                .withProperty(Exchange.FILE_NAME, relativePath)
                                .withProperty(Exchange.FILE_LENGTH, transfer(cpPage.getInputStream()))
                                .withPattern(ExchangePattern.InOut)
                                .withBody(cpPage.getInputStream())
                                .withHeader(S3Constants.KEY, relativePath)
                                .build();
            Exchange result = template.send("direct:awss3", exchange);

            log.info("Result Headers: ");
            log.info("CamelAwsS3ETag: " + result.getOut().getHeader("CamelAwsS3ETag"));

            if (result.getOut().isFault()) {
                throw new Exception("Some bad result: " + result.getOut().getExchange().getException());
            }

        } catch (Exception e) {
            throw new StorageException("Camel route failed, abort the transaction");
        } finally {
            //AEY: Don't do the actual file storage, thx
            //super.create(page, relativePath);
        }


		String transactionId = LocalThreadTransaction.getTransactionId();
        CDNFSDAOFactory.registerAction(transactionId, relativePath, CDNFSDAOFactory.Action.PERSIST);
        log.debug("Created page so registered PERSIST action: transaction " + transactionId + ", path " + relativePath );
	}
    
	public void remove(int publicationId, int pageId, String relativePath)   throws StorageException
	{
		super.remove(publicationId, relativePath);
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

        } catch (Exception e) {
            throw new StorageException("Camel route failed, abort the extension");
        }
        //AEY: Don't do the actual file storage, thx
		//super.update(page, originalRelativePath, newRelativePath);
		String transactionId = LocalThreadTransaction.getTransactionId();
		//the super class will do a create, so we don't need to do anything unless the file name has changed 
		//(in which case we need to register a remove for the old path)
		if (originalRelativePath.compareTo(newRelativePath)!=0)
		{
			CDNFSDAOFactory.registerAction(transactionId, originalRelativePath, CDNFSDAOFactory.Action.REMOVE);
			log.debug("Updated page moved so registered REMOVE action: transaction " + transactionId + ", path " + originalRelativePath );
		}
	}

    private int transfer(InputStream inputStream) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {


            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
             output.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            output.close();
            inputStream.close();
        }

        return output.size();

    }

}
