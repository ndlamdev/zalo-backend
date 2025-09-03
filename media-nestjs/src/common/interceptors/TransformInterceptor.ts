/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:44 PM - 03/09/2025
 * User: kimin
 **/

import { CallHandler, ExecutionContext, Injectable, NestInterceptor } from '@nestjs/common';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ApiResponse } from '../../shared/dto/ApiResponse';

export class TransformInterceptor<T> implements NestInterceptor<T, ApiResponse<T>> {
  intercept(context: ExecutionContext, next: CallHandler): Observable<ApiResponse<T>> {
    const request = context.switchToHttp().getRequest();

    return next.handle().pipe(
      map(data => ({
        code: context.switchToHttp().getResponse().statusCode,
        message: 'Operation successful', // Or derive from data/context
        data,
        timestamp: new Date().toISOString(),
        path: request.url,
      })),
    );
  }
}