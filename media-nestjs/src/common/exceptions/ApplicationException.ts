/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:10 PM - 03/09/2025
 * User: kimin
 **/
import { HttpException } from '@nestjs/common';
import { ExceptionEnums } from './exception.enums';

export class ApplicationException extends HttpException {
  readonly detail?: string;
  readonly code: number;

  private constructor(exceptionEnum: ExceptionEnums, status: number = 400, detail?: string) {
    super(exceptionEnum.message, status);
    this.detail = detail;
    this.code = exceptionEnum.code;
  }

  public static create(exceptionEnum: ExceptionEnums, status: number = 400, detail?: string): ApplicationException {
    return new ApplicationException(exceptionEnum, status, detail);
  }
}
