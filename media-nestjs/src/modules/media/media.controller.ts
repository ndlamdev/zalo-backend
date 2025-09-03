import { Body, Controller, HttpException, Post, UploadedFile, UseInterceptors } from '@nestjs/common';
import { MediaService } from './media.service';
import { FileInterceptor } from '@nestjs/platform-express';
import { GlobalException } from '../../common/exceptions/GlobalException';
import { ApplicationException } from '../../common/exceptions/ApplicationException';
import { ExceptionEnums } from '../../common/exceptions/exception.enums';

@Controller('/media')
export class MediaController {
  constructor(private readonly mediaService: MediaService) {
  }

  @Post('/upload')
  @UseInterceptors(FileInterceptor('file'))
  upload(@UploadedFile() file: Express.Multer.File) {
    if (!file) throw ApplicationException.create(ExceptionEnums.FILE_REQUIRED);
    return this.mediaService.upload(file);
  }
}
