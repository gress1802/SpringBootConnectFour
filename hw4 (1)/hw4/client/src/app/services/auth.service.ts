import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, of } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements OnInit {
  private URL : string = "http://localhost:8080/api/v2";
  private user? : User;
  public userSubject : BehaviorSubject<User|undefined> = new BehaviorSubject<User|undefined>( undefined );

  constructor(private http: HttpClient) {
    this.ngOnInit();
  }

  //This method is called when the component is initialized. It calls the getAuthenticatedUser() method.
  ngOnInit() {
    this.getAuthenticatedUser().subscribe();
  }

  /*
   * This method returns a boolean value indicating whether the user is authenticated
  */
  isAuthenticated() : boolean {
    return this.user != undefined;
  }

  /*
   * This method returns the authenticated user whether the authenticated user is stored in the local storage or not.
  */
  getAuthenticatedUser() : Observable<User> {
    let txt = window.localStorage.getItem('user');
    if( txt ) {
      let user : User = JSON.parse(txt as string) as User;
      this.setUser( user );
      return of(user);
    } else {
      return this.fetchUser();
    }
  }

  /*
   * This method makes an HTTP GET request to the /who endpoint in order to retrieve the authenticated user
  */
  fetchUser() : Observable<User> {
    return this.http.get<User>(this.URL + "/who", { withCredentials : true }).pipe( tap( user => {
      this.setUser( user );
    }) );
  }

  /*
   * This method takes a parameter us which is either of User type or undefined and sets the user attribute of this class to the value of the parameter.
   * It also stores the user in the local storage only if the user exits. 
   * Otherwise, if the user is undefined it is removed from local storage.
  */
  setUser( user : User | undefined  ) : void {
    this.user = user;
    if( this.user ) {
      window.localStorage.setItem( 'user', JSON.stringify( user ) );
    } else {
      window.localStorage.removeItem( 'user' );
    }
    this.userSubject.next( user );
  }

  /*
   * This is a method takes two parameters named username and password that are both of type string.
   * It makes a HTTP POST request to the endpoint "/login" and passes the username and password as the body of the request.
   * It returns an Observable of type User.
  */
login(username: string, password: string): Observable<User> {
  const API = this.URL + '/login';
  const body = new HttpParams()
    .set('username', username)
    .set('password', password);

  const headers = new HttpHeaders({
    'Content-Type': 'application/x-www-form-urlencoded'
  });

  const options = { headers: headers, withCredentials: true };

  return this.http
    .post<User>(API, body.toString(), options)
    .pipe<User>(tap(u => {
      this.setUser(u);
    }));
}

  /*
   * This method makes a HTTP POST request to the endpoint "/logout".
  * It returns an Observable of type User.
  */
  logout() {
    const API = this.URL + "/logout";
    console.log(API);
    return this.http.post<User>( API, { }, { withCredentials : true } ).pipe( tap( () => this.setUser( undefined ) ) );
  }
}
