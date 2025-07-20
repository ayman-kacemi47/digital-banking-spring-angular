package net.kacemi.digtalbankbackend.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kacemi.digtalbankbackend.dtos.AccountHistoryDTO;
import net.kacemi.digtalbankbackend.dtos.BankAccountDTO;
import net.kacemi.digtalbankbackend.dtos.OperationDTO;
import net.kacemi.digtalbankbackend.dtos.SavingAccountDTO;
import net.kacemi.digtalbankbackend.excepetions.BankAccountNotFoundException;
import net.kacemi.digtalbankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class BankAccountRestController {
    BankAccountService bankAccountService;

    @GetMapping("/accounts")
    public List<BankAccountDTO> getAccounts(){
        log.info("getAccounts "+ bankAccountService.listBankAccounts());
        return bankAccountService.listBankAccounts();
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

//    @PostMapping("/accounts/save")
//    public BankAccountDTO createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
//        if(bankAccountDTO instanceof SavingAccountDTO) {
//        bankAccountService.saveSavingAccount(bankAccountDTO.ge)
//
//        }
//    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<OperationDTO>  getAccountOperations(@PathVariable(name = "accountId") String id) throws BankAccountNotFoundException {
      return  bankAccountService.accountHistory(id);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO  getAccountHistory(@PathVariable String accountId,
                                                      @RequestParam(name = "page",defaultValue = "0") int page,
                                                      @RequestParam(name = "size", defaultValue = "5") int size) throws BankAccountNotFoundException {

    return bankAccountService.getAccountHistory(accountId, page, size);

    }
}
