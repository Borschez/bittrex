import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {Settings} from "../model/settings";
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable()
export class CommonService {

  private settingsUrl = 'http://localhost:8988/api/settings';
  private settings : Settings;

  constructor(
    private http: HttpClient
  ) { }

  getSettings(): Observable<Settings>{
      const url = `${this.settingsUrl}/`;
      return this.http.get<Settings>(url)
        .pipe(
          tap(settings => this.log(`fetched settings`)),
          catchError(this.handleError('getSettings', null))
        );
    }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    console.log(message);
  }

}
