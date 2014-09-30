package com.tridion.storage.extensions.persistence;

import com.tridion.broker.StorageException;
import com.tridion.configuration.Configuration;
import com.tridion.configuration.ConfigurationException;
import com.tridion.storage.persistence.JPADAOFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * Created with IntelliJ IDEA.
 * User: AEY5753
 * Date: 29/07/14
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
@Component("JPACustomDAOFactory")
@Scope("prototype")
public class JPACustomDAOFactory
        extends JPADAOFactory
        implements ApplicationContextAware {
    /**
     * Static variable which will be set by the Singleton instance managed by Spring
     * and used by the instance created through Reflection by Tridion code during
     * initialization.
     */
    private static ApplicationContext APPLICATION_CONTEXT;
    // TODO: Add Camel Context

    /**
     * Constructor called by Spring when init is done.
     */
    public JPACustomDAOFactory() {
        //not important what we send. This instance is never going to be used
        super(null, "MongoSQL");
    }

    /**
     * Constructor called by Tridion at app start-up (after the Spring
     * init has happened).
     */
    public JPACustomDAOFactory(String storageId, String dialect) {
        super(storageId, dialect);
    }

    /**
     * The method we actually want to override.
     */
    public void commitTransaction(String transactionId) throws StorageException {
        super.commitTransaction(transactionId);
        // TODO: do my stuff
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
    }}
