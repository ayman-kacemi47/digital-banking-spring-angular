package net.kacemi.digtalbankbackend.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AccountHistoryDTO {
    private String id;
    private BigDecimal balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<OperationDTO> operationDTOS    ;

}
