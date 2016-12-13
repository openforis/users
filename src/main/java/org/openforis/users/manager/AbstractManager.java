package org.openforis.users.manager;

import java.util.List;

import org.jooq.DAO;
import org.openforis.users.model.IdentifiableObject;

public abstract class  AbstractManager<E extends IdentifiableObject> {
	
	private DAO<?, E, Long> dao;
	
	public <D extends DAO<?, E, Long>> AbstractManager(D dao) {
		this.dao = dao;
	}

	public E findById(Long id) {
		return dao.findById(id);
	}
	
	public List<E> findAll() {
		return dao.findAll();
	}
	
	public void save(E item) {
		if (item.getId() == null) {
			dao.insert(item);
		} else {
			dao.update(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void delete(E item) {
		dao.delete(item);
	}
	
	public boolean deleteById(Long id) {
		dao.deleteById(id);
		return true;
	}
}
