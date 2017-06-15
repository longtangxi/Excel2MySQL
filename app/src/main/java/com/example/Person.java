package com.example;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

import java.math.BigDecimal;

/**
 * Created by ty on 2017/6/13.
 */

@Table("t_person") // 声明了Person对象的数据表
@TableIndexes({@Index(name = "age", fields = {"age","a"}, unique = true), @Index(name = "a", fields = {"a"}, unique = true)})
// 不会强制要求继承某个类
public class Person {
    @Id // 表示该字段为一个自增长的Id,注意,是数中自增!!
    @ColDefine(unsigned = true, width = 9)
    private int id; // @Id与属性名称id没有对应关系.
    @Name
    // 表示该字段可以用来标识此对象，或者是字符型主键，或者是唯一性约束
    private String name;
    @Column // 表示该对象属性可以映射到数据库里作为一个字段
    private int age;
    @ColDefine(type = ColType.FLOAT, width = 18, precision = 5)
    @Comment(value = "测量日期")
    @Column(hump = true)
    private BigDecimal aLogIn;
    @Column
    private boolean a;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}