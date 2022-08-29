package com.markswell.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseBodyDTO {

    private List<CustomerResponse> content;
}
