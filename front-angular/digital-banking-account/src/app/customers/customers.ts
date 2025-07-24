import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CustomerService} from '../services/customer-service';
import {AsyncPipe, JsonPipe, NgIf} from '@angular/common';
import {toSignal} from '@angular/core/rxjs-interop';
import {catchError, map, Observable, throwError} from 'rxjs';
import {Customer} from '../models/customer';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';

@Component({
  selector: 'app-customers',
  imports: [
    AsyncPipe,
    ReactiveFormsModule,
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
 /*  this could be replaced with handleSrachCustomers("")
  this.customers = this.customerService.getCustomers().pipe(
       catchError(err => {
         this.errorMessage = err.message;
         return throwError(()=>err);
       })
     );*/
    this.handleSearchCustomers(); // cause it will search by defaul value ''
    }

  handleSearchCustomers(){
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.searchCustomer(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(()=>err);
      })
    );
  }

  handleDeleteCustomer(c: Customer) {
    if(confirm("veuillez suprrimer ce customer ?")){

    this.customerService.deleteCustomer(c.id).subscribe({
      next:data=>{
        //this.handleSearchCustomers(); // n'est pratique il actualise toute la page
        this.customers = this.customers.pipe(
          map(data=>{
            let index = data.indexOf(c);
            data.slice(index,1);
            return data;
          })
        );
      },
      error:err => {
        console.error("error deleting customer !\n",err);
      }
    })
    }
  }
}
