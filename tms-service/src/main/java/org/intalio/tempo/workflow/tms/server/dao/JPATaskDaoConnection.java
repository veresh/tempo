/**
 * Copyright (c) 2005-2008 Intalio inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */
package org.intalio.tempo.workflow.tms.server.dao;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.intalio.tempo.workflow.auth.UserRoles;
import org.intalio.tempo.workflow.dao.AbstractJPAConnection;
import org.intalio.tempo.workflow.task.CustomColumn;
import org.intalio.tempo.workflow.task.PIPATask;
import org.intalio.tempo.workflow.task.Task;
import org.intalio.tempo.workflow.tms.TaskIDConflictException;
import org.intalio.tempo.workflow.tms.UnavailableAttachmentException;
import org.intalio.tempo.workflow.tms.UnavailableTaskException;
import org.intalio.tempo.workflow.util.jpa.AttachmentFetcher;
import org.intalio.tempo.workflow.util.jpa.TaskFetcher;

/**
 * Persistence for task using JPA.
 */
public class JPATaskDaoConnection extends AbstractJPAConnection implements ITaskDAOConnection {

    private TaskFetcher _fetcher;
    private AttachmentFetcher _attachmentFetcher;

    public JPATaskDaoConnection(EntityManager createEntityManager) {
        super(createEntityManager);
        _fetcher = new TaskFetcher(createEntityManager);
        _attachmentFetcher = new AttachmentFetcher( createEntityManager );
    }

    public void createTask(Task task) throws TaskIDConflictException {
        checkTransactionIsActive();
        entityManager.persist(task);
    }

    public boolean deleteTask(int internalTaskId, String taskID) throws UnavailableTaskException {
        checkTransactionIsActive();
        entityManager.remove(_fetcher.fetchTaskIfExists(taskID));
        return true;
    }

    public Task[] fetchAllAvailableTasks(UserRoles user) {
        return _fetcher.fetchAllAvailableTasks(user);
    }

    public Task fetchTaskIfExists(String taskID) throws UnavailableTaskException {
        return _fetcher.fetchTaskIfExists(taskID);
    }

    public void updateTask(Task task) {
        checkTransactionIsActive();
        entityManager.persist(task);
    }
    public void updatePipaTask(PIPATask pipaTask){
    	checkTransactionIsActive();
        entityManager.persist(pipaTask);    	
    }
    
    
    public List<Task> fetchTaskfromInstanceID(String instanceid) throws UnavailableTaskException {
        List<Task> pat=_fetcher.fetchTaskIfExistsfrominstanceID(instanceid);
        return pat;
    }
    

    public void deletePipaTask(String formUrl) {
        try {
            PIPATask toDelete = _fetcher.fetchPipaFromUrl(formUrl);
            checkTransactionIsActive();
            entityManager.remove(toDelete);
        } catch (Exception nre) {
            throw new NoResultException(nre.getMessage());
        }
    }
    
    //Fix for WF-1479
    public boolean deleteAttachment(String attachmentUrl) throws UnavailableAttachmentException{
        checkTransactionIsActive();
        entityManager.remove(_attachmentFetcher.fetchAttachmentIfExists(attachmentUrl));
        return true;
    }

    public void storePipaTask(PIPATask task) {
        _logger.info("store pipa task:" + task.getFormURL());
        checkTransactionIsActive();
        entityManager.persist(task);
    }

    public PIPATask fetchPipa(String formUrl) throws UnavailableTaskException {
        PIPATask pipa = _fetcher.fetchPipaFromUrl(formUrl);
        return pipa;
    }

    public Task[] fetchAvailableTasks(UserRoles user, Class className, String subQuery) {
        return _fetcher.fetchAvailableTasks(user, className, subQuery);
    }

    public Task[] fetchAvailableTasks(HashMap parameters) {
        return _fetcher.fetchAvailableTasks(parameters);
    }

    public Long countAvailableTasks(HashMap parameters) {
        return _fetcher.countTasks(parameters);
    }
    
    public List<String> fetchCustomColumns(){
        List<String> customColumns = _fetcher.fetchCustomColumns();
        return customColumns;
    }
    
    public List<CustomColumn> fetchCustomColumnfromProcessName(String processName) throws UnavailableTaskException {
        List<CustomColumn> customMetadata=_fetcher.fetchCustomColumnIfExistsfromprocessname(processName);
        return customMetadata;
    }
    
    @Override
	public void deleteCustomColumn(CustomColumn toDeleteCustomColumn) {
	   try {
            checkTransactionIsActive();
           	entityManager.remove(toDeleteCustomColumn);
        } catch (Exception nre) {
            throw new NoResultException(nre.getMessage());
        }
	
	}

	@Override
	public void storeCustomColumn(CustomColumn customColumn) {
        checkTransactionIsActive();
        entityManager.persist(customColumn);
		
	}

}
