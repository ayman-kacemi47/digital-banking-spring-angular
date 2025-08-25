import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {AccountService} from '../services/account-service';
import {AccountDetails} from '../models/accountDetails';
import {catchError, Observable, of, throwError} from 'rxjs';
import {AsyncPipe, DatePipe, DecimalPipe, JsonPipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {AuthServices} from '../services/auth-services';


@Component({
  selector: 'app-accounts',
  imports: [
    ReactiveFormsModule,
    AsyncPipe,
    JsonPipe,
    DecimalPipe,
    DatePipe
  ],
  templateUrl: './accounts.html',
  styleUrl: './accounts.css'
})
export class Accounts implements  OnInit{
  accountObservable!:Observable<AccountDetails | null> |null;
  accountFormGroup! : FormGroup;
  currentPage : number = 0;
  pageSize : number = 5;
  errorMessage!: string;
  operationFormGroup!: FormGroup;
  constructor(private  fb: FormBuilder, private accountService: AccountService, private route:ActivatedRoute, protected  authService:AuthServices) {
  }

  ngOnInit(): void {
        this.accountFormGroup= this.fb.group({
          accountId: this.fb.control(this.route.snapshot.params["id"]), // like ['', validation]

        })

        this.operationFormGroup= this.fb.group({
          operationType:this.fb.control<"debit"|"credit"|"transfer"|null>(null),
          amount:this.fb.control(0),
          description: this.fb.control(null),
          accountDestination: this.fb.control(null)
        })
    //this.handleSearchAccount();
    }


  handleSearchAccount() {
    let    accountId:string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.error;
        return of(null);
      })
    );

  }

  protected readonly Array = Array;

  goToPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation() {
    let accountId:string = this.accountFormGroup.value.accountId;
    let operationType = this.operationFormGroup.value.operationType.toLowerCase();
    let amount:number = this.operationFormGroup.value.amount;
    let description:string=this.operationFormGroup.value.description;
    let accountDestination:string= this.operationFormGroup.value.accountDestination;
    console.log("de ", description);
    if(operationType == "debit"){
      this.accountService.debit(accountId,amount,description).subscribe({
        next:(data)=>{
          alert("success debit");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error:err => {
          this.errorMessage = err.error;

          console.log("debit error: "+err);
        }
      })
    } else if(operationType == "credit"){
      this.accountService.credit(accountId,amount,description).subscribe({
        next:(data)=>{
          alert("success credit");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error:err => {
          this.errorMessage = err.error;

          console.log("credit error: "+err);
        }
      })
    }  else if(operationType == "transfer"){
      this.accountService.transfer(accountId,accountDestination,amount,description).subscribe({
        next:(data)=>{
          alert("success transfer");
          this.operationFormGroup.reset();
          this.handleSearchAccount();
        },
        error:err => {
          this.errorMessage = err.error;
          console.log("transfer error: "+err);
        }
      })
    }
  }
}
