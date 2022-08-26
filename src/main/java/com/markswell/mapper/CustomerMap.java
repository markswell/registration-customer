package com.markswell.mapper;

import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.markswell.model.Customer;
import com.markswell.dto.CustomerResponse;

import java.time.Period;
import java.time.LocalDate;

import static java.time.LocalDate.now;

@Mapper(componentModel = "spring")
public interface CustomerMap {

    @Mapping(source = "born", target = "age", qualifiedByName = "bornToAge")
    CustomerResponse customerToCustomerResponse(Customer customer);

    @Named("bornToAge")
    static int convertBornToAge(LocalDate born) {
        return Period.between(born, now()).getYears();
    }

}
