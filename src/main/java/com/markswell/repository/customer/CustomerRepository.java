package com.markswell.repository.customer;

import com.markswell.model.Customer;
import org.springframework.data.domain.Page;
import com.markswell.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {

    @Query(value = "select c from Customer c where c.name like concat('%', :cname, '%')")
    Page<Customer> findByName(@Param("cname") String cname, Pageable page);

    @Query("select c from Customer c where c.born = :cborn")
    Page<Customer> findByBorn(@Param("cborn")LocalDate born, Pageable page);
}
