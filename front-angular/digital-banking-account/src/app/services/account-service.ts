import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {AccountDetails} from '../models/accountDetails';
import {Account} from '../models/account';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  constructor(private http: HttpClient) {
  }

  public getAccount(accountId: string, page:number, size:number,):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(environment.apiUrl+"/accounts/"+accountId+"/pageOperations?page="+page+"&size="+size);
  }

  public debit(accountId:string, amount:number, description:string){
    let data = {accountId,amount,description};
  console.log("service data ", data);
    return this.http.post<AccountDetails>(environment.apiUrl+"/accounts/debit",data)
  }

 public credit(accountId:string, amount:number, description:string){
    let data = {accountId,amount,description};
    return this.http.post<AccountDetails>(environment.apiUrl+"/accounts/credit",data)
  }

  public transfer(fromAccount:string,toAccount:string,  amount:number, description:string){
    let data = { fromAccount,toAccount,amount,description};
    return this.http.post<AccountDetails>(environment.apiUrl+"/accounts/transfer",data)
  }

  public getCustomerAccounts(customerId:string):Observable<Array<Account>>{
    return this.http.get<Array<Account>>(environment.apiUrl+"/accounts/customer/"+customerId);
  }

}
