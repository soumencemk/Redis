package com.soumen.redispoc;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@Data
@RedisHash("Student")
@ToString
public class Student implements Serializable {
    private String id;
    private String name;
    private Gender gender;
    private int grade;
}
