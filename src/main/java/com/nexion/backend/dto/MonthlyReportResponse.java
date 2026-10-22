package com.nexion.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data 
public class MonthlyReportResponse {
    
    @NotBlank
    String month;    
    
}
