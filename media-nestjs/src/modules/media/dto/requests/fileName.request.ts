/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:18 PM - 05/09/2025
 * User: kimin
 **/
import { IsNotEmpty, IsString, Matches } from 'class-validator';

export class FileNameRequest {
  @IsString({ message: 'fileName phải là chuỗi' })
  @IsNotEmpty({ message: 'fileName không được rổng' })
  @Matches(/^\w+.\w+$/, {
    message: 'fileName không hợp lệ',
  })
  fileName: string;
}
