package com.markswell.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.io.Serial;
import javax.persistence.*;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Comparator;

import static java.lang.String.format;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "CUSTOMER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable, Cloneable, Comparable<Customer> {

    @Serial
    private static final Long serialVersionUID = -2114830405119332271L;

    @Transient
    private static final Comparator<Customer> COMPARATOR = Comparator.comparing((Customer c) -> c.name).thenComparingLong(c -> c.id).thenComparing(c -> c.born);

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "CUSTOMER_NAME")
    private String name;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "BORN")
    private LocalDate born;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return format("Customer{ 'id': %d, 'name': '%s', 'born': '%s' }", id, name, born);
    }

    @Override
    public int compareTo(Customer customer) {
        return COMPARATOR.compare(this, customer);
    }

    @Override
    public Customer clone() {
        return Customer.builder().id(id).name(name).born(born).build();
    }
}
