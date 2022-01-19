package com.soumen.redispoc;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
public interface StudentRepository extends CrudRepository<Student, String> {
}
