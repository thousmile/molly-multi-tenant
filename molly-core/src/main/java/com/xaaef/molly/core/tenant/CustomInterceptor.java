package com.xaaef.molly.core.tenant;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.metamodel.RepresentationMode;
import org.hibernate.metamodel.spi.EntityRepresentationStrategy;
import org.hibernate.type.Type;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.INTERCEPTOR;


/**
 * <p>
 * </p>
 *
 * @author WangChenChen
 * @version 1.1
 * @date 2022/12/12 18:25
 */

@Slf4j
@Component
public class CustomInterceptor implements Interceptor, HibernatePropertiesCustomizer, java.io.Serializable {

    @Override
    public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        log.info("onLoad...");
        return Interceptor.super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        log.info("onFlushDirty...");
        return Interceptor.super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onSave(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        log.info("onSave...");
        return Interceptor.super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void onDelete(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        log.info("onDelete...");
        Interceptor.super.onDelete(entity, id, state, propertyNames, types);
    }

    @Override
    public void onCollectionRecreate(Object collection, Object key) throws CallbackException {
        log.info("onCollectionRecreate...");
        Interceptor.super.onCollectionRecreate(collection, key);
    }

    @Override
    public void onCollectionRemove(Object collection, Object key) throws CallbackException {
        log.info("onCollectionRemove...");
        Interceptor.super.onCollectionRemove(collection, key);
    }

    @Override
    public void onCollectionUpdate(Object collection, Object key) throws CallbackException {
        log.info("onCollectionUpdate...");
        Interceptor.super.onCollectionUpdate(collection, key);
    }

    @Override
    public void preFlush(Iterator<Object> entities) throws CallbackException {
        log.info("preFlush...");
        Interceptor.super.preFlush(entities);
    }

    @Override
    public void postFlush(Iterator<Object> entities) throws CallbackException {
        log.info("postFlush...");
        Interceptor.super.postFlush(entities);
    }

    @Override
    public Boolean isTransient(Object entity) {
        log.info("isTransient...");
        return Interceptor.super.isTransient(entity);
    }

    @Override
    public int[] findDirty(Object entity, Object id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        log.info("findDirty...");
        return Interceptor.super.findDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public Object instantiate(String entityName, EntityRepresentationStrategy representationStrategy, Object id) throws CallbackException {
        log.info("instantiate...");
        return Interceptor.super.instantiate(entityName, representationStrategy, id);
    }

    @Override
    public Object instantiate(String entityName, RepresentationMode representationMode, Object id) throws CallbackException {
        log.info("instantiate...");
        return Interceptor.super.instantiate(entityName, representationMode, id);
    }

    @Override
    public String getEntityName(Object object) throws CallbackException {
        log.info("getEntityName...");
        return Interceptor.super.getEntityName(object);
    }

    @Override
    public Object getEntity(String entityName, Object id) throws CallbackException {
        log.info("getEntity...");
        return Interceptor.super.getEntity(entityName, id);
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(INTERCEPTOR, this);
    }

}
