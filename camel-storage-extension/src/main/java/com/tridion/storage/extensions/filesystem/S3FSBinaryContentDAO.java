package com.tridion.storage.extensions.filesystem;

import com.tridion.broker.StorageException;
import com.tridion.storage.BinaryContent;
import com.tridion.storage.dao.BinaryContentDAO;
import com.tridion.storage.filesystem.FSBinaryContentDAO;
import com.tridion.storage.filesystem.FSEntityManager;
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
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Component("S3FSBinaryContentDAO")
@Scope("prototype")
public class S3FSBinaryContentDAO extends FSBinaryContentDAO implements BinaryContentDAO {
	private  Logger log = LoggerFactory.getLogger(S3FSBinaryContentDAO.class);
	private  String DCPINFO_EXTENSION = ".dcpinfo";
	
	public S3FSBinaryContentDAO(String storageId, String storageName, File storageLocation, FSEntityManager entityManager) {
	    super(storageId, storageName, storageLocation, entityManager);
	  }

	  public S3FSBinaryContentDAO(String storageId, String storageName, File storageLocation) {
	    super(storageId, storageName, storageLocation);
	  }

	  public void create(BinaryContent binaryContent, String relativePath)  throws StorageException
	  {
        BinaryContent cpBinaryContent = binaryContent;
        ByteArrayInputStream is = new ByteArrayInputStream(cpBinaryContent.getContent());

        CamelContext camelContext;
        ProducerTemplate template;

        try {
            log.info("New Binary Creation - Interesting to know");
            log.info("Storage Id: " + getStorageId());
            log.info("Storage Location: " + getStorageLocation());
            log.debug("Binding Name: " + getBindingName());
            log.debug("Type Mapping: " + getTypeMapping());
            log.debug("Class: " + getClass());
            log.info("Object Id: " + cpBinaryContent.getBinaryId());
            log.info("Object Size: " + cpBinaryContent.getObjectSize());

            camelContext = CDNFSDAOFactory.getCamelContext();
            //s3Plugin = new S3Plugin(camelContext);

            template = camelContext.createProducerTemplate();
            Exchange exchange = ExchangeBuilder.anExchange(camelContext)
                                .withProperty(Exchange.FILE_NAME, relativePath)
                                /*.withProperty(Exchange.FILE_LENGTH, cpBinaryContent.getObjectSize())*/
                                .withPattern(ExchangePattern.InOut)
                                .withBody(is)
                                .withHeader(S3Constants.KEY, relativePath)
                                .build();
            Exchange result = template.send("direct:awss3", exchange);

            log.debug("Result Headers: ");
            log.debug("CamelAwsS3ETag: " + result.getOut().getHeader("CamelAwsS3ETag"));

            if (result.getOut().isFault()) {
                throw new Exception("Some bad result: " + result.getOut().getExchange().getException());
            }

        } catch (Exception e) {
            throw new StorageException("something awful happened");
        } finally {
            super.create(binaryContent, relativePath);
        }

        String transactionId = LocalThreadTransaction.getTransactionId();

        if (relativePath.endsWith(DCPINFO_EXTENSION))
        {
          registerDcpInfo(binaryContent,transactionId);
        }
        else
        {
          CDNFSDAOFactory.registerAction(transactionId, relativePath, CDNFSDAOFactory.Action.PERSIST);
          log.debug("Created binary so registered PERSIST action: transaction " + transactionId + ", path " + relativePath );
        }
	  }

	  public void update(BinaryContent binaryContent, String originalRelativePath, String newRelativePath) throws StorageException
	  {
          BinaryContent cpBinaryContent = binaryContent;
          ByteArrayInputStream is = new ByteArrayInputStream(cpBinaryContent.getContent());

          CamelContext camelContext;
          ProducerTemplate template;

          try {
              log.info("Existing Binary Update - Interesting to know");
              log.info("Storage Id: " + getStorageId());
              log.info("Storage Location: " + getStorageLocation());
              log.debug("Binding Name: " + getBindingName());
              log.debug("Type Mapping: " + getTypeMapping());
              log.debug("Class: " + getClass());
              log.info("Object Id: " + cpBinaryContent.getBinaryId());
              log.info("Object Size: " + cpBinaryContent.getObjectSize());

              camelContext = CDNFSDAOFactory.getCamelContext();

              template = camelContext.createProducerTemplate();
              Exchange exchange = ExchangeBuilder.anExchange(camelContext)
                                  .withProperty(Exchange.FILE_NAME, newRelativePath)
                                  /*.withProperty(Exchange.FILE_LENGTH, cpBinaryContent.getObjectSize())*/
                                  .withPattern(ExchangePattern.InOut)
                                  .withBody(is)
                                  .withHeader(S3Constants.KEY, newRelativePath)
                                  .build();
              Exchange result = template.send("direct:awss3", exchange);

              log.debug("Result Headers: ");
              log.debug("CamelAwsS3ETag: " + result.getOut().getHeader("CamelAwsS3ETag"));

              if (result.getOut().isFault()) {
                  throw new Exception("Some bad result: " + result.getOut().getExchange().getException());
              }

          } catch (Exception e) {
              throw new StorageException("something awful happened");
          }

		  super.update(binaryContent, originalRelativePath, newRelativePath);
		  String transactionId = LocalThreadTransaction.getTransactionId();
		  if (newRelativePath.endsWith(DCPINFO_EXTENSION))
		  {
			  registerDcpInfo(binaryContent,transactionId);
		  }
		  else
		  {
		  	if (originalRelativePath.compareTo(newRelativePath)!=0)
		  	{
		  		CDNFSDAOFactory.registerAction(transactionId, originalRelativePath, CDNFSDAOFactory.Action.REMOVE);
		  		log.debug("Updated binary moved so registered REMOVE action: transaction " + transactionId + ", path " + originalRelativePath );
		  	}
			CDNFSDAOFactory.registerAction(transactionId, newRelativePath, CDNFSDAOFactory.Action.PERSIST);
			log.debug("Updated binary so registered PERSIST action: transaction " + transactionId + ", path " + newRelativePath );
		  }
		
	  }

	  

	public void remove(int publicationId, int binaryId, String variantId, String relativePath)  throws StorageException
	  {
		  super.remove(publicationId, relativePath);
		  String transactionId = LocalThreadTransaction.getTransactionId();
		  if (relativePath.endsWith(DCPINFO_EXTENSION))
		  {
			  File storageRoot = getStorageLocation(publicationId);
			  File target = new File(storageRoot, relativePath);
			  registerDcpInfo(target,transactionId);
		  }
		  else
		  {
			  CDNFSDAOFactory.registerAction(transactionId, relativePath, CDNFSDAOFactory.Action.REMOVE);
			  log.debug("Removed binary so registered REMOVE action: transaction " + transactionId + ", path " + relativePath );
		  }
	  }
	  
	  private void registerDcpInfo(File dcpInfoFile, String transactionId) 
	  {
		  DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder;
          try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			try {
				Document doc = docBuilder.parse (dcpInfoFile);
				registerDcpInfo(doc,transactionId);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  
	  private void registerDcpInfo(BinaryContent binaryContent, String transactionId) 
	  {
		  DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder;
          try {
			docBuilder = docBuilderFactory.newDocumentBuilder();
			try {
				Document doc = docBuilder.parse(new ByteArrayInputStream(binaryContent.getContent()));
				registerDcpInfo(doc,transactionId);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  
	  private void registerDcpInfo(Document doc, String transactionId)
	  {
		  NodeList listOfPages = doc.getElementsByTagName("page");
          for(int i=0; i<listOfPages.getLength() ; i++)
          {
              Node page = listOfPages.item(i);
              String url = page.getAttributes().getNamedItem("url").getNodeValue();
              CDNFSDAOFactory.registerAction(transactionId, url, CDNFSDAOFactory.Action.PERSIST);
			  log.debug("Found DCP info binary so registered PERSIST action: transaction " + transactionId + ", page " + url );
          }
	  }
}
