/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:11 PM - 03/09/2025
 * User: kimin
 **/

export class ExceptionEnums {
  readonly code: number;
  readonly message: string;

  private constructor(code: number, message: string) {
    this.code = code;
    this.message = message;
  }

  static FORBIDDEN = new ExceptionEnums(1000, 'Không xác thực!');
  static UNAUTHENTICATED = new ExceptionEnums(1001, 'Không xác thực!');
  static FILE_INVALID = new ExceptionEnums(1002, 'File không hợp lệ!');
  static FILE_REQUIRED = new ExceptionEnums(1003, 'Yêu cầu file!');
}