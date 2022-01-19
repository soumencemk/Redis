package com.soumen.redispoc;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@Repository
public interface TaskRepository extends CrudRepository<Task,String> {
}
