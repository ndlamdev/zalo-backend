/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:43 PM - 03/09/2025
 * User: kimin
 **/
import { ApiProperty } from '@nestjs/swagger';

export class ApiResponse<T> {
  @ApiProperty({ example: 200 })
  code: number;

  @ApiProperty({ example: 'Operation successful' })
  message: string;

  @ApiProperty({ description: 'The actual data payload' })
  data: T;

  @ApiProperty({ example: '2025-09-03T12:00:00.000Z' })
  timestamp: string;

  @ApiProperty({ example: '/api/v1/users' })
  path: string;
}
