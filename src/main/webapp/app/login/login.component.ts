import { Component, ViewChild, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';
import { UserManagementService } from '../admin/user-management/service/user-management.service';
import { HttpResponse } from '@angular/common/http';
import { User } from '../admin/user-management/user-management.model';

@Component({
  selector: 'jhi-login',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('username', { static: false })
  username!: ElementRef;

  authenticationError = false;
  userBlocked = false;

  loginForm = this.fb.group({
    username: [null, [Validators.required]],
    password: [null, [Validators.required]],
    rememberMe: [false],
  });

  constructor(
    private userService: UserManagementService,
    private accountService: AccountService,
    private loginService: LoginService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.username.nativeElement.focus();
  }

  login(): void {
    let blockg = this.loginForm.get('username')!.value;
    blockg = blockg.concat('_block');
    const bloqueado = sessionStorage.getItem(blockg);
    if (bloqueado === 'true') {
      this.authenticationError = true;
      this.userBlocked = true;
    } else {
      this.loginService
        .login({
          username: this.loginForm.get('username')!.value,
          password: this.loginForm.get('password')!.value,
          rememberMe: this.loginForm.get('rememberMe')!.value,
        })
        .subscribe(
          () => {
            let fails = this.loginForm.get('username')!.value;
            fails = fails.concat('_fail');
            sessionStorage.removeItem(fails);

            this.authenticationError = false;
            if (!this.router.getCurrentNavigation()) {
              // There were no routing during login (eg from navigationToStoredUrl)
              this.router.navigate(['']);
            }
          },
          () => {
            this.authenticationError = true;
            let fails = this.loginForm.get('username')!.value;
            fails = fails.concat('_fail');

            let numeroFallas = Number(sessionStorage.getItem(fails));
            if (numeroFallas) {
              numeroFallas = numeroFallas + 1;
              sessionStorage.removeItem(fails);
              sessionStorage.setItem(fails, numeroFallas.toString());
              if (numeroFallas >= 3) {
                this.userBlocked = true;
                let block = this.loginForm.get('username')!.value;
                block = block.concat('_block');
                sessionStorage.removeItem(block);
                sessionStorage.setItem(block, 'true');
              }
            } else {
              numeroFallas = 1;
              sessionStorage.setItem(fails, numeroFallas.toString());
            }
          }
        );
    }
  }
}
