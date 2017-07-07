package org.openforis.users.manager;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.jooq.DAO;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;
import org.openforis.users.dao.Daos;
import org.openforis.users.model.IdentifiableObject;

@SuppressWarnings("rawtypes")
public abstract class AbstractManager<E extends IdentifiableObject, D extends DAO> {
	
	protected D dao;
	private Class<E> itemType;
	
	public AbstractManager(D dao, Class<E> itemType) {
		this.dao = dao;
		this.itemType = itemType;
	}

	@SuppressWarnings("unchecked")
	public E findById(Long id) {
		Object ofItem = dao.findById(id);
		return ofItem != null ? convertToItemType(ofItem) : null;
	}

	private E convertToItemType(Object ofItem) {
		try {
			Constructor<E> constructor = itemType.getConstructor(ofItem.getClass());
			return constructor.newInstance(ofItem);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<E> findAll() {
		List all = dao.findAll();
		List<E> result = new ArrayList<E>(all.size());
		for (Object ofItem : all) {
			result.add(convertToItemType(ofItem));
		}
		return result;
	}
	
	public void save(E item) {
		runInTransaction(new Runnable() {
			public void run() {
				if (item.getId() == null) {
					insert(item);
				} else {
					update(item);
				}
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	protected void update(E item) {
		dao.update(item);
	}

	protected void insert(E item) {
		Daos.insertAndSetId(DSL.using(dao.configuration()), 
				dao.getTable(), 
				dao.getTable().field("ID"), 
				item);
	}
	
	@SuppressWarnings("unchecked")
	public void deleteById(final long id) {
		runInTransaction(new Runnable() {
			public void run() {
				dao.deleteById(id);
			}
		});
	}
	
	protected void runInTransaction(Runnable runnable) {
		DSL.using(dao.configuration()).transaction(new TransactionalRunnable() {
			public void run(Configuration configuration) throws Exception {
				runnable.run();
			}
		});
	}
}