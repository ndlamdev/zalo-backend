import { CanActivate, ExecutionContext, Inject, Injectable } from '@nestjs/common';
import { Observable } from 'rxjs';
import { Reflector } from '@nestjs/core';
import { IS_PUBLIC_KEY } from '../../decorators/public/is-public.decorator';
import { JwtService } from '@nestjs/jwt';
import { ENV_PROVIDER_KEY } from '../../../shared/constant/env/env.provider';
import { base64ToPem } from '../../utils/pem';
import { JwtAuthenticationToken } from '../../../shared/types/authenticated.type';
import { Algorithm } from 'jsonwebtoken';
import type { EnvType } from '../../../shared/constant/env/dto/env.type';
import { ApplicationException } from '../../exceptions/ApplicationException';
import { ExceptionEnums } from '../../exceptions/exception.enums';

@Injectable()
export class AuthenticationGuard implements CanActivate {
  constructor(
    private readonly reflector: Reflector,
    private readonly jwtService: JwtService,
    @Inject(ENV_PROVIDER_KEY) private readonly env: EnvType,
  ) {}

  canActivate(context: ExecutionContext): boolean | Promise<boolean> | Observable<boolean> {
    const isPublic = this.reflector.getAllAndOverride<boolean>(IS_PUBLIC_KEY, [
      context.getHandler(),
      context.getClass(),
    ]);
    if (isPublic) return true;

    try {
      this.extractToken(context.switchToHttp().getRequest());
      return true;
    } catch {
      throw ApplicationException.create(ExceptionEnums.FORBIDDEN, 403);
    }
  }

  extractToken(request: any) {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-call,@typescript-eslint/no-unsafe-assignment,@typescript-eslint/no-unsafe-member-access
    const [type, token] = request.headers.authorization?.split(' ') ?? [];
    if (type !== 'Bearer' || token == null) return;
    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment,@typescript-eslint/no-unsafe-argument
    const jwt = this.jwtService.verify(token, {
      publicKey: base64ToPem(this.env.jwt.publicKey),
      algorithms: [this.env.jwt.algorithm as Algorithm],
    });
    // eslint-disable-next-line @typescript-eslint/no-unsafe-member-access
    request.user = {
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
      token: token,
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment,@typescript-eslint/no-unsafe-member-access
      phoneNumber: jwt.iss,
      // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment,@typescript-eslint/no-unsafe-member-access
      roles: jwt.payload.roles,
    } as JwtAuthenticationToken;
  }
}
