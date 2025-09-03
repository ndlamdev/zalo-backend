import { Global, Module } from '@nestjs/common';
import { EnvProvider } from './env.provider';
import { ConfigModule } from '@nestjs/config';

@Global()
@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
    }),
  ],
  providers: [EnvProvider],
  exports: [EnvProvider],
})
export class EnvModule {}
