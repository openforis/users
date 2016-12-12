package org.openforis.users.manager;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AbstractEntityManager<E extends Object> {
	
	private Class<E> type;
	
	public AbstractEntityManager(Class<E> type) {
		super();
		this.type = type;
	}

	public List<E> listAll() {
		return runInTransaction(new TransactionRunnable<List<E>>() {
			public List<E> run(Session session) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<E> criteria = builder.createQuery(type);
				Root<E> root = criteria.from(type);
				criteria.select( root );
				List<E> result = session.createQuery( criteria ).getResultList();
				return result;
			}
		});
	}

	public E loadById(BigInteger id) {
		return runInTransaction(new TransactionRunnable<E>() {
			public E run(Session session) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<E> criteria = builder.createQuery(type);
				Root<E> root = criteria.from(type);
				criteria.select( root );
				criteria.where(builder.equal(root.get("id"), id));
				return session.createQuery( criteria ).getSingleResult();
			}
			
		});
	}
	
	public boolean deleteById(BigInteger id) {
		return runInTransaction(new TransactionRunnable<Boolean>() {
			public Boolean run(Session session) {
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaDelete<E> criteriaDelete = builder.createCriteriaDelete(type);
				Root<E> root = criteriaDelete.from(type);
				criteriaDelete.where(builder.equal(root.get("id"), id));
				int result = session.createQuery(criteriaDelete).executeUpdate();
				return result > 0;
			}
		});
	}

	public void save(E entity) {
		runInTransaction(new TransactionRunnable<Void>() {
			public Void run(Session session) {
				session.save(entity);
				return null;
			}
		});
	}
	
	public void delete(E entity) {
		runInTransaction(new TransactionRunnable<Void>() {
			public Void run(Session session) {
				session.delete(entity);
				return null;
			}
		});
	}
	
	private SessionFactory getSessionFactory() {
		return SessionFactoryUtils.getSessionFactory();
	}

	private <R extends Object> R runInTransaction(TransactionRunnable<R> runnable) {
		R result = null;
		Session session = getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			result = runnable.run(session);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
		}
		return result;
	}
	
	private interface TransactionRunnable<R extends Object> {
		
		R run(Session session);
		
	}
	
}
