import { CanActivate, ExecutionContext, Injectable } from '@nestjs/common';
import { Observable } from 'rxjs';
import { Reflector } from '@nestjs/core';
import { HAS_AUTHORITY_KEY } from '../../decorators/has-authority/has-authority.decorator';
import { JwtAuthenticationToken } from '../../../shared/types/authenticated.type';
import { ApplicationException } from '../../exceptions/ApplicationException';
import { ExceptionEnums } from '../../exceptions/exception.enums';

@Injectable()
export class AuthorizationGuard implements CanActivate {
  constructor(private readonly reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean | Promise<boolean> | Observable<boolean> {
    const authorities = this.reflector.getAllAndOverride<string[]>(HAS_AUTHORITY_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);
    if (authorities == null) return true;

    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
    const req = context.switchToHttp().getRequest();
    // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
    const user = req.user as JwtAuthenticationToken;
    if (user == null) throw ApplicationException.create(ExceptionEnums.UNAUTHENTICATED, 401);
    return authorities.some((authority) => user.roles.includes(authority));
  }
}
