import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Customer} from '../models/customer';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  constructor(private http:HttpClient) {
  }

  public getCustomers():Observable<Array<Customer>>{
    return this.http.get<Array<Customer>>(environment.apiUrl+"/customers");
  }

  public searchCustomer(keyword: String):Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(environment.apiUrl+"/customers/search?keyword="+keyword);
  }

  public saveCustomer(customer:Customer):Observable<Customer>{
    return  this.http.post<Customer>(environment.apiUrl+"/customers/save", customer);
  }

  public deleteCustomer(id: string) {
    console.log("deleting ... " + id);
    return this.http.delete(environment.apiUrl+"/customers/"+id);
  }
}
