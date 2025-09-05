import { Module } from '@nestjs/common';
import { CloudinaryProvider } from './media.provider';
import { MediaService } from './media.service';
import { MediaController } from './media.controller';
import { ConfigModule } from '@nestjs/config';
import { UuidModule } from 'nestjs-uuid';

@Module({
  imports: [UuidModule],
  controllers: [MediaController],
  providers: [CloudinaryProvider, MediaService, ConfigModule],
})
export class MediaModule {}
