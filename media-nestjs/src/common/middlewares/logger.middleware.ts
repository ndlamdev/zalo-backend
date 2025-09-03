/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:46 PM - 27/08/2025
 * User: kimin
 **/
import { NestMiddleware } from '@nestjs/common';
import { Request } from 'express';

export class LoggerMiddleware implements NestMiddleware {
  use(req: Request, res: any, next: (error?: any) => void): any {
    console.log(req.method, req.url, req.headers);
    next();
  }
}
