package net.kacemi.digtalbankbackend.mappers;

import lombok.extern.slf4j.Slf4j;
import net.kacemi.digtalbankbackend.dtos.OperationDTO;
import net.kacemi.digtalbankbackend.entities.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OperationMapper {

    public OperationDTO fromOperation(final Operation operation) {
        OperationDTO operationDTO = new OperationDTO();
        BeanUtils.copyProperties(operation, operationDTO);
        log.info("operation dto :"+operationDTO.toString());
        return operationDTO;
    }

    public Operation fromOperationDTO(final OperationDTO operationDTO) {
        Operation operation = new Operation();
        BeanUtils.copyProperties(operationDTO, operation);
        log.info("operation :"+operation.toString());
        return operation;

    }
}
