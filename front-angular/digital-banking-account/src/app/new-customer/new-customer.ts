import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Customer} from '../models/customer';
import {CustomerService} from '../services/customer-service';
import {catchError, throwError} from 'rxjs';
import {Router} from '@angular/router';

@Component({
  selector: 'app-new-customer',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './new-customer.html',
  styleUrl: './new-customer.css'
})
export class NewCustomer implements OnInit{

  newCustomerFormGroup!: FormGroup;
  errorMessage!: string;

  constructor(private fb:FormBuilder, private customerService :CustomerService, private  router:Router) {
    this.newCustomerFormGroup=this.fb.group(
      {
        name: this.fb.control("",[Validators.required, Validators.minLength(2)]),
        email: this.fb.control("",[Validators.required, Validators.email])
      }
    );
  }

  ngOnInit(): void {

    }



  handleSaveCustomer() {
    let customer:Customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next:data =>{
        alert("Customer saved successfully !!");
        //this.newCustomerFormGroup.reset();
        this.router.navigateByUrl("/customers");
      },
      error:err => {
        this.errorMessage = err.error["error"];
        console.error(err);
      }
    }


    );
  }
}
