import { ValueProvider } from '@nestjs/common/interfaces/modules/provider.interface';
import { EnvType } from './dto/env.type';
import { ConfigService } from '@nestjs/config';

export const ENV_PROVIDER_KEY = 'ENV';
export const EnvProvider = {
  provide: ENV_PROVIDER_KEY,
  useFactory: (configService: ConfigService): EnvType => {
    return {
      jwt: {
        publicKey: configService.get('JWT_PUBLIC_KEY'),
        algorithm: configService.get('JWT_ALG') ?? 'RS256',
        claimKey: configService.get('CLAIM_KEY'),
      },
      port: parseInt(configService.get('PORT') ?? '5051'),
      cloudinary: {
        name: configService.get('CLOUDINARY_NAME'),
        apiKey: configService.get('CLOUDINARY_API_KEY'),
        apiSecret: configService.get('CLOUDINARY_API_SECRET'),
        url: configService.get('CLOUDINARY_URL'),
      },
    };
  },
  inject: [ConfigService],
};
