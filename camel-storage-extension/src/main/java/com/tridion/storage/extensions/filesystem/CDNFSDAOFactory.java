package com.tridion.storage.extensions.filesystem;

import com.tridion.broker.StorageException;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.data.CharacterData;
import com.tridion.storage.filesystem.FSDAOFactory;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

@Component("CDNFSDAOFactory")
@Scope("singleton")
public class CDNFSDAOFactory
        extends FSDAOFactory
        implements ApplicationContextAware {

    /**
     * Static variable which will be set by the Singleton instance managed by Spring
     * and used by the instance created through Reflection by Tridion code during
     * initialization.
     */
    private static ApplicationContext APPLICATION_CONTEXT;
    private static CamelContext CAMEL_CONTEXT;

    private Logger log = LoggerFactory.getLogger(CDNFSDAOFactory.class);
	private static ConcurrentHashMap<String, ConcurrentHashMap<String,Action>> notificationRegister  = new ConcurrentHashMap<String, ConcurrentHashMap<String, Action>>();

    public CDNFSDAOFactory() {
        super("", "");
   	}


    public CDNFSDAOFactory(String storageId,
   	        String tempFileSystemTransactionLocation) {
   		super(storageId, tempFileSystemTransactionLocation);
        CAMEL_CONTEXT = (CamelContext)APPLICATION_CONTEXT.getBean("camelContext");
        System.out.println("essential -- constructor");
   	}

	public static void registerAction(String transactionId, String itemUrl, Action action)
	{


		if (!notificationRegister.containsKey(transactionId))
		{
			notificationRegister.put(transactionId, new ConcurrentHashMap<String, Action>());
		}
		ConcurrentHashMap<String,Action> transactionActions = notificationRegister.get(transactionId);
		if (!transactionActions.containsKey(itemUrl))
		{
			transactionActions.put(itemUrl, action);

		}
		//UPDATED
		else
		{
			//Special case where a publish transaction contains a renamed file plus a file
			//with the same name as the renamed file's old name, we ensure that it is not
			//removed, but only re-persisted (a rename will trigger a remove and a persist)
			if (action==Action.PERSIST)
			{
				transactionActions.put(itemUrl, action);
			}
		}

        //END UPDATED
	}

    public static void registerAction(String transactionId, String itemUrl, Action action, CharacterData page) throws IOException {


   		if (!notificationRegister.containsKey(transactionId))
   		{
   			notificationRegister.put(transactionId, new ConcurrentHashMap<String, Action>());
   		}
   		ConcurrentHashMap<String,Action> transactionActions = notificationRegister.get(transactionId);
   		if (!transactionActions.containsKey(itemUrl))
   		{
   			transactionActions.put(itemUrl, action);
   		}
   		//UPDATED
   		else
   		{
   			//Special case where a publish transaction contains a renamed file plus a file
   			//with the same name as the renamed file's old name, we ensure that it is not
   			//removed, but only re-persisted (a rename will trigger a remove and a persist)
   			if (action==Action.PERSIST)
   			{
   				transactionActions.put(itemUrl, action);
   			}
   		}

   		//END UPDATED

   	}

    // this is some Camel intrusion .. love it
    public static CamelContext getCamelContext() throws StorageException {
        if (CAMEL_CONTEXT != null) {
            System.out.println("Thx to the Context Awareness we can feed it back to the Storage extensions!! ");
        } else {
            throw new StorageException("no camel context found");
        }
        return CAMEL_CONTEXT;
    }

	public void commitTransaction(String transactionId) throws StorageException {
        log.debug("Created page so registered PERSIST action: transaction " + transactionId /*+ ", itemurl " + itemUrl + ", action " + action*/  );
	    try
	    {
	    	super.commitTransaction(transactionId);
	    	notifyCDN(transactionId);
	    }
	    catch (StorageException storageException) {
	        throw storageException;
	    }
	    catch (Exception cdnException)
	    {
	    	log.error("Error notifying CDN: " + cdnException.getMessage());
	    }
	    finally {
	    	log.debug("Clearing register for transaction" + transactionId);
	        cleanupRegister(transactionId);
	    }
	  }
	
	
	private  static void cleanupRegister(String transactionId) {
		if (notificationRegister.containsKey(transactionId))
		{
			notificationRegister.remove(transactionId);
		}
	}

	private void notifyCDN(String transactionId) {
		log.debug("Notifying CDN for transaction" + transactionId);
		if (notificationRegister.containsKey(transactionId))
		{
			ConcurrentHashMap<String,Action> actions = notificationRegister.get(transactionId);
			for(String itemUrl : actions.keySet())
			{
				log.debug("Notifying CDN for item: " + itemUrl + ", action: " + actions.get(itemUrl));
			}
		}
	}

    /**
     * Override this method so we can set the application context on the
     * private field we inherit from JPADAOFactory.
     */
    public void configureBundle(Configuration storageDAOBundleConfiguration)
                                              throws ConfigurationException {
        // first set the right value for the private field
        // called 'applicationContext'
        try {
            setPrivateField(this, "applicationContext", APPLICATION_CONTEXT);
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
        }

        // configure the bundle like we normally do
        super.configureBundle(storageDAOBundleConfiguration);
    }

    /**
     * Method called by Spring.
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
                                                           throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
        //APPLICATION_CONTEXT = new ClassPathXmlApplicationContext(new String[] {"application-context.xml"});
    }

    /**
     * Utility method using Reflection to be able to set the private field
     * we inherit from our parent class.
     */
    private static void setPrivateField(final Object fieldOwner,
                                        final String fieldName, final Object value)
                              throws NoSuchFieldException, IllegalAccessException {
        final Field privateField = getPrivateFieldRec(fieldOwner.getClass(),
                                                      fieldName);

        if (privateField != null) {
            final boolean accesible = privateField.isAccessible();
            privateField.setAccessible(true);

            privateField.set(fieldOwner, value);

            privateField.setAccessible(accesible);
        }
    }

    private static Field getPrivateFieldRec(final Class<?> clazz,
                                            final String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        final Class<?> superClazz = clazz.getSuperclass();

        if (superClazz != null) {
            return getPrivateFieldRec(superClazz, fieldName);
        }

        return null;
    }

    public static enum Action
	{
	    PERSIST, REMOVE;
	}

}
