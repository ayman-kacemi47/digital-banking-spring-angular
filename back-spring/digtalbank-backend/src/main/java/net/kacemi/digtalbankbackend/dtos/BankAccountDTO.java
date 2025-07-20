package net.kacemi.digtalbankbackend.dtos;

import jakarta.persistence.*;
import lombok.Data;
import net.kacemi.digtalbankbackend.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class BankAccountDTO {
    private String id;
    private Date createdAt;
    private BigDecimal balance;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customer;

    private String type;
}
