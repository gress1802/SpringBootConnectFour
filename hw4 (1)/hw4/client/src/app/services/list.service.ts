import { Injectable, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap, of, catchError } from 'rxjs';
import { User } from '../models/user';
import { Metadata } from '../models/metadata';
import { Token } from '../models/token';
import { Game } from '../models/game';

@Injectable({
  providedIn: 'root'
})

export class ListService implements OnInit {
  private user? : User = undefined;
  private URL : string = "http://localhost:8080/api/v2";
  public metaSubject : BehaviorSubject<Metadata|undefined> = new BehaviorSubject<Metadata|undefined>( undefined );
  private meta? : Metadata = undefined;

  constructor(private http : HttpClient) { 
    this.ngOnInit();
  }

  ngOnInit(): void {
    this.getAuthenticatedUser().subscribe( () => {
      this.getMeta().subscribe();
    } );
  }

/*
 * The following methods are used to check if the user is authenticated or not and to get the authenticated user
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

  fetchUser() : Observable<User> {
    const options = { withCredentials: true };
    return this.http.get<User>(this.URL + "/who", options).pipe( tap( user => {
      this.setUser( user );
    }) );
  }

  setUser( user : User | undefined  ) : void {
    this.user = user;
  }
// End of user authentication and getting user methods

//The following methods are used to get the metadata and to set the metadata
/*
 * This method makes an HTTP GET request to the /meta endpoint in order to retrieve the metadata
*/
  getMeta() : Observable<Metadata> {
    return this.http.get<Metadata>(this.URL + "/meta", { withCredentials : true }).pipe( tap( meta => {
      this.setMeta( meta );
      console.log("meta: "+meta);
    }) );
  }

  setMeta( meta : Metadata | undefined  ) : void {
    this.meta = meta;
    this.metaSubject.next(meta);
  }

/*
 * This function hits the /api/v2/sids endpoint to create a new game object (POST)
*/
  createGame(selectedPlayer : string, selectedComputer : string, selectedColor : string, tokenList : Token[]) : Observable<Game> {
    const reqBody = {
      playerToken : selectedPlayer,
      computerToken : selectedComputer,
    };

    const curURL = this.URL + '/sids' + '?' + "color=" + encodeURIComponent(selectedColor);
    return this.http.post<Game>(curURL, reqBody, { withCredentials : true
    });
  }

/*
 * This is a function that gets a list of games that are related to the authenticated user ID
*/
  gameList() {
    return this.http.get<Game>(this.URL + "/sids", { withCredentials : true
    });
  }

  /*
   *  This function gets a list of games that are related to the authenticated user ID
  */
 getGames() : Observable<Game[]> {
  return this.http.get<Game[]>(this.URL + "/sids", { withCredentials : true
  });
 }

 /*
  * This is a function that hits /api/v2/gids/:gid endpoint to get a game object
 */
  getGame(gid : string) : Observable<Game> {
    return this.http.get<Game>(this.URL + "/gids/" + gid, { withCredentials : true
    });
  }
}
