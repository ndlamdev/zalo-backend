import { MiddlewareConsumer, Module, NestModule } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { MediaModule } from './modules/media/media.module';
import { DatabaseModule } from './core/database/database.module';
import { LoggerModule } from './core/logger/logger.module';
import { LoggerMiddleware } from './common/middlewares/logger.middleware';
import { APP_FILTER, APP_GUARD, APP_INTERCEPTOR } from '@nestjs/core';
import { AuthenticationGuard } from './common/guards/auth/authentication-guard.service';
import { AuthorizationGuard } from './common/guards/authority/authorization-guard.service';
import { JwtModule } from '@nestjs/jwt';
import { EnvModule } from './shared/constant/env/env.module';
import { GlobalException } from './common/exceptions/GlobalException';
import { TransformInterceptor } from './common/interceptors/TransformInterceptor';

@Module({
  imports: [EnvModule, JwtModule.register({}), MediaModule, DatabaseModule, LoggerModule],
  controllers: [AppController],
  providers: [
    AppService,
    {
      provide: APP_GUARD,
      useClass: AuthenticationGuard,
    },
    {
      provide: APP_GUARD,
      useClass: AuthorizationGuard,
    },
    {
      provide: APP_FILTER,
      useClass: GlobalException,
    },
    {
      provide: APP_INTERCEPTOR,
      useClass: TransformInterceptor,
    },
  ],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer.apply(LoggerMiddleware);
  }
}
