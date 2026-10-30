package com.nexion.backend.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MonthlyReportResponse {

    private String month;
    private BigDecimal income;
    private BigDecimal expense;

}
