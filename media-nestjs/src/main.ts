import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { DocumentBuilder, SwaggerModule } from '@nestjs/swagger';

async function bootstrap() {
  const app = await NestFactory.create(AppModule, { cors: true });
  const config = new DocumentBuilder()
    .setTitle('Zalo media')
    .setDescription('The zalo API description')
    .setVersion('1.0')
    .addTag('Zalo')
    .build();
  const documentFactory = () => SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('swagger-ui', app, documentFactory);
  await app.listen(process.env.PORT ?? 3000);
}

bootstrap().then(r => console.log(`Dự án đang chạy ở port: ${process.env.PORT ?? 3000}`));
