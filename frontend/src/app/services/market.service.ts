import { Injectable } from '@angular/core';

import {Market} from "../model/market";
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import { catchError, map, tap } from 'rxjs/operators';
import {Ticker} from "../model/ticker";

@Injectable()
export class MarketService {

  private marketUrl = 'http://localhost:8988/api/market';

  constructor(
    private http: HttpClient
  ) { }


  getMarkets(maxCount: number): Observable<Market[]>{
    const url = `${this.marketUrl}/count=${maxCount}`;
    return this.http.get<Market[]>(url)
      .pipe(
        tap(markets => this.log(`fetched markets`)),
        catchError(this.handleError('getMarkets', []))
      );
  }

  getMarket(id: number): Observable<Market>{
    const url = `${this.marketUrl}/${id}`;
    return this.http.get<Market>(url).pipe(
      tap(_ => this.log(`fetched Market id=${id}`)),
      catchError(this.handleError<Market>(`getMarket id=${id}`))
    );
  }

  getTickers(marketId: number): Observable<Ticker[]>{
    const url = `${this.marketUrl}/${marketId}/tickers`;
    return this.http.get<Ticker[]>(url)
      .pipe(
        tap(markets => this.log(`fetched tickers`)),
        catchError(this.handleError('getTickers', []))
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
