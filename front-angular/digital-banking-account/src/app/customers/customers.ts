import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CustomerService} from '../services/customer-service';
import {AsyncPipe, JsonPipe, NgIf} from '@angular/common';
import {toSignal} from '@angular/core/rxjs-interop';
import {catchError, Observable, throwError} from 'rxjs';
import {Customer} from '../models/customer';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-customers',
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
    JsonPipe
  ],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit{
  customers! : Observable<Array<Customer>>;
  errorMessage!:string;
  searchFormGroup: FormGroup |undefined;
  constructor(private customerService: CustomerService, private fb: FormBuilder) {
  }

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({
      keyword:this.fb.control("")
    })
     this.customers = this.customerService.getCustomers().pipe(
       catchError(err => {
         this.errorMessage = err.message;
         return throwError(()=>err);
       })
     );
    }

  handleSearchCustomers(){
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomer(kw);
  }

}
