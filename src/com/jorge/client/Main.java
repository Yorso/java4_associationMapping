package com.jorge.client;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jorge.entity.Guide;
import com.jorge.entity.Student;
import com.jorge.util.HibernateUtil;

/**
 * Asociation mappings => MANY TO ONE
 * Student class and Guide class are both entities
 *
 * Deleting rows in DB: First delete row student, then delete row guide
 *
 */
public class Main {

	public static void main(String[] args) {
		BasicConfigurator.configure(); // Necessary for configure log4j. It must be the first line in main method
								       // log4j.properties must be in /src directory
		
		Logger  logger = Logger.getLogger(Main.class.getName());
		logger.debug("log4j configured correctly and logger set");

		logger.debug("getting session factory from HibernateUtil.java");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction txn = session.getTransaction();
		
		try {
			
			logger.debug("beginning transaction");
			txn.begin(); // Beginning transaction

			logger.debug("setting data in Guide and Student objects");
			Guide guide = new Guide("GD200331", "Homer Simpson", 1200);
			Student student = new Student("ST109883", "Moe Szyslak", guide);
			
			logger.debug("first, saving guide data in DB");
			session.save(guide);
			
			logger.debug("then, saving student data in DB");
			session.save(student);

			logger.debug("making commit of transactions");
			txn.commit(); // Making commit
			
		} catch (Exception e) {
			if (txn != null) {
				logger.error("something was wrong, making rollback of transactions");
				txn.rollback(); // If something was wrong, we make rollback
			}
			logger.error("Exception: " + e.getMessage().toString());
		} finally {
			if (session != null) {
				logger.debug("close session");
				session.close();
			}
		}
	}

}
