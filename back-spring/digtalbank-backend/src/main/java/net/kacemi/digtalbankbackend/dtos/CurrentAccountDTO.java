package net.kacemi.digtalbankbackend.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrentAccountDTO extends BankAccountDTO {
    private BigDecimal overDraft;
}
