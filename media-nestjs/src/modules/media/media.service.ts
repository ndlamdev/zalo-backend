import { Injectable } from '@nestjs/common';
import { CloudinaryResponse } from './dto/responses/cloudinary.response';
import { v2 as cloudinary } from 'cloudinary';
import streamifier from 'streamifier';
import { ApplicationException } from '../../common/exceptions/ApplicationException';
import { ExceptionEnums } from '../../common/exceptions/exception.enums';

@Injectable()
export class MediaService {
  upload(file: Express.Multer.File): Promise<CloudinaryResponse> {
    return new Promise<CloudinaryResponse>((resolve, reject) => {
      const uploader = cloudinary.uploader;
      const uploadStream = uploader.upload_stream(
        {
          resource_type: 'raw',
        },
        (error, result) => {
          if (error) reject(ApplicationException.create(ExceptionEnums.FILE_INVALID));
          else resolve(result!);
        },
      );

      streamifier.createReadStream(file.buffer).pipe(uploadStream);
    });
  }
}
