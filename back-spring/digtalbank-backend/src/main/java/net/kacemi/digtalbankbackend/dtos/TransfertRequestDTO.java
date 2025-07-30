package net.kacemi.digtalbankbackend.dtos;

import lombok.Data;

@Data
public class TransfertRequestDTO
{
    private String fromAccount;
    private String toAccount;
    private double amount;
    private String description;
}
