import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Customer} from '../models/customer';
import {Account} from '../models/account';
import {AccountService} from '../services/account-service';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';

@Component({
  selector: 'app-customer-accounts',
  imports: [
    AsyncPipe
  ],
  templateUrl: './customer-accounts.html',
  styleUrl: './customer-accounts.css'
})
export class CustomerAccounts implements OnInit{
  customerId!: string;
  customer!:Customer;
  customerAccounts!: Observable<Array<Account>>;
  constructor(private route: ActivatedRoute, private router: Router, private  accountService: AccountService) {
        this.customer = this.router.getCurrentNavigation()?.extras.state as Customer;
  }
    ngOnInit(): void {
        this.customerId = this.route.snapshot.params["id"];
        this.getCustomerAccounts();
    }

    public getCustomerAccounts(){
       this.customerAccounts = this.accountService.getCustomerAccounts(this.customerId);
    }

  handleViewAccount(account: Account) {
    if(account.id){
      this.router.navigateByUrl("/admin/accounts/"+account.id);
    }
  }
}
