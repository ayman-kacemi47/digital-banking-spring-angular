package net.kacemi.digtalbankbackend.dtos;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import net.kacemi.digtalbankbackend.enums.OperationType;

import java.util.Date;

@Data
public class OperationDTO {
    private Long id;
    private Date date;
    private double amount;
    private String description;
    private OperationType operationType;
}
