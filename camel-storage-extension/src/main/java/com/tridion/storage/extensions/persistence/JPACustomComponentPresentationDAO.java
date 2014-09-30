package com.tridion.storage.extensions.persistence;

import com.tridion.broker.StorageException;
import com.tridion.storage.ComponentPresentation;
import com.tridion.storage.dao.ComponentPresentationDAO;
import com.tridion.storage.persistence.JPAComponentPresentationDAO;
import com.tridion.storage.util.ComponentPresentationTypeEnum;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Collection;

@Component("JPACustomComponentPresentationDAO")
@Scope("prototype")
public class JPACustomComponentPresentationDAO
  extends JPAComponentPresentationDAO
  implements ComponentPresentationDAO
{
  public JPACustomComponentPresentationDAO(String storageId, EntityManagerFactory entityManagerFactory, String storageType)
  {
    super(storageId, entityManagerFactory, storageType);
  }
  
  public JPACustomComponentPresentationDAO(String storageId, EntityManagerFactory entityManagerFactory, EntityManager entityManager, String storageType)
  {
    super(storageId, entityManagerFactory, entityManager, storageType);
  }
  
  public void create(ComponentPresentation itemToCreate, ComponentPresentationTypeEnum componentPresentationType)
    throws StorageException
  {
    super.create(itemToCreate);
  }
  
  public void update(ComponentPresentation itemToUpdate, ComponentPresentationTypeEnum componentPresentationType)
    throws StorageException
  {
    super.update(itemToUpdate);
  }
  
  public void remove(ComponentPresentation itemToRemove, ComponentPresentationTypeEnum componentPresentationType)
    throws StorageException
  {
    remove(itemToRemove.getPublicationId(), itemToRemove.getComponentId(), itemToRemove.getTemplateId(), componentPresentationType);
  }
  
  public ComponentPresentation getComponentPresentation(int publicationId, int componentId, int templateId, ComponentPresentationTypeEnum componentPresentationType)
    throws StorageException
  {
    return super.getComponentPresentation(publicationId, componentId, templateId, componentPresentationType);
  }
  
  public Collection<ComponentPresentation> findAll(int publicationId, int componentId, ComponentPresentationTypeEnum componentPresentationType)
    throws StorageException
  {
    return super.findAll(publicationId, componentId, componentPresentationType);
  }
  
  public void remove(int publicationId, int componentId, int componentTemplateId, ComponentPresentationTypeEnum componentPresentationType)
    throws StorageException
  {
    super.remove(publicationId, componentId, componentTemplateId, componentPresentationType);
  }

@Override
public String getBindingName() {
	// TODO Auto-generated method stub
	return super.getBindingName();
}

@Override
public String getStorageId() {
	// TODO Auto-generated method stub
	return super.getStorageId();
}

@Override
public String getStorageType() {
	// TODO Auto-generated method stub
	return super.getStorageType();
}

@Override
public String getTypeMapping() {
	// TODO Auto-generated method stub
	return null;
}
}
