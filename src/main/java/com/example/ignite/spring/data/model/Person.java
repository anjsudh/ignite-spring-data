
package com.example.ignite.spring.data.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;


public class Person implements Serializable {
    private static final AtomicLong ID_GEN = new AtomicLong();

    @QuerySqlField(index = true)
    public Long id;

    @QuerySqlField(index = true)
    public Long orgId;

    @QuerySqlField
    public String firstName;

    @QuerySqlField
    public String lastName;

    @QueryTextField
    public String resume;

    @QuerySqlField(index = true)
    public double salary;

    private transient AffinityKey<Long> key;


    public Person() {
        // No-op.
    }


    public Person(Organization org, String firstName, String lastName, double salary, String resume) {
        // Generate unique ID for this person.
        id = ID_GEN.incrementAndGet();

        orgId = org.id();

        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }


    public Person(Long id, Long orgId, String firstName, String lastName, double salary, String resume) {
        this.id = id;
        this.orgId = orgId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }


    public Person(Long id, String firstName, String lastName) {
        this.id = id;

        this.firstName = firstName;
        this.lastName = lastName;
    }


    public AffinityKey<Long> key() {
        if (key == null)
            key = new AffinityKey<>(id, orgId);

        return key;
    }


    @Override public String toString() {
        return "Person [id=" + id +
                ", orgId=" + orgId +
                ", lastName=" + lastName +
                ", firstName=" + firstName +
                ", salary=" + salary +
                ", resume=" + resume + ']';
    }
}