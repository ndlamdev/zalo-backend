/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:01 PM - 03/09/2025
 * User: kimin
 **/
import { ArgumentsHost, Catch, ExceptionFilter } from '@nestjs/common';
import { Request, Response } from 'express';
import { ApplicationException } from './ApplicationException';

@Catch(ApplicationException)
export class GlobalException implements ExceptionFilter {
  catch(exception: ApplicationException, host: ArgumentsHost) {
    const ctx = host.switchToHttp();
    const response = ctx.getResponse<Response>();
    const request = ctx.getRequest<Request>();
    const status = exception.getStatus();

    response.status(status).json({
      code: exception.code,
      error: exception.message,
      message: exception.detail,
      timestamp: new Date().toISOString(),
      path: request.url,
      trace: exception.cause,
    });
  }
}
