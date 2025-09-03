import { Module } from '@nestjs/common';
import { CloudinaryProvider } from './media.provider';
import { MediaService } from './media.service';
import { MediaController } from './media.controller';
import { ConfigModule } from '@nestjs/config';

@Module({
  controllers: [MediaController],
  providers: [CloudinaryProvider, MediaService, ConfigModule],
})
export class MediaModule {}
