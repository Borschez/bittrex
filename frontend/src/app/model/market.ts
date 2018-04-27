import {MarketFunction} from "./market-function";
import {Ticker} from "./ticker";

export class Market {
  id: number;
  name: string;
  minTradeSize: number;
  last: number;
  created: Date;
  timeStamp: Date;
  notice: string;
  logoUrl: string;
  currencyName: string;
  active: boolean;
  sponsored: boolean;
  baseCurrencyName: string;
  marketFunction: MarketFunction;
  tickers: Ticker[];
  delta: number;
}
