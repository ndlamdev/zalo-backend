import { Controller, Post, UploadedFile, UseInterceptors } from '@nestjs/common';
import { MediaService } from './media.service';
import { FileInterceptor } from '@nestjs/platform-express';
import { ApplicationException } from '../../common/exceptions/ApplicationException';
import { ExceptionEnums } from '../../common/exceptions/exception.enums';
import { HasAuthority } from '../../common/decorators/has-authority/has-authority.decorator';

@Controller('/media')
export class MediaController {
  constructor(private readonly mediaService: MediaService) {}

  @Post('/upload')
  @UseInterceptors(FileInterceptor('file'))
  @HasAuthority('ROLE_USER', 'ROLE_ADMIN', 'UPLOAD_FILE')
  upload(@UploadedFile() file: Express.Multer.File) {
    if (!file) throw ApplicationException.create(ExceptionEnums.FILE_REQUIRED);
    return this.mediaService.upload(file);
  }
}
