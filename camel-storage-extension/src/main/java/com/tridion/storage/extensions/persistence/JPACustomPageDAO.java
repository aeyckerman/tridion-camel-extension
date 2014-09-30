package com.tridion.storage.extensions.persistence;

import com.tridion.broker.StorageException;
import com.tridion.data.CharacterData;
import com.tridion.storage.dao.PageDAO;
import com.tridion.storage.persistence.JPAPageDAO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
@Component("JPACustomPageDAO")
@Scope("prototype")
public class JPACustomPageDAO
        extends JPAPageDAO
        implements PageDAO {

    // TODO: Implement methods for S3 and Mongo
    public JPACustomPageDAO(String storageId, EntityManagerFactory entityManagerFactory, EntityManager entityManager, String storageName) {
        super(storageId, entityManagerFactory, entityManager, storageName);
    }

    public void create(CharacterData page, String relativePath) throws StorageException
   	{}

    public void remove(int publicationId, int pageId, String relativePath)   throws StorageException
   	{}

}
