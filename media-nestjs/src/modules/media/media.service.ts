import { Injectable } from '@nestjs/common';
import { CloudinaryResponse } from './dto/responses/cloudinary.response';
import { v2 as cloudinary } from 'cloudinary';
import streamifier from 'streamifier';
import { ApplicationException } from '../../common/exceptions/ApplicationException';
import { ExceptionEnums } from '../../common/exceptions/exception.enums';
import type { FileNameRequest } from './dto/requests/fileName.request';
import { UuidService } from 'nestjs-uuid';

@Injectable()
export class MediaService {
  constructor(private readonly uuidService: UuidService) {}

  upload(file: Express.Multer.File): Promise<CloudinaryResponse> {
    const fileName = file.filename;
    const lastIndexDot = fileName.lastIndexOf('.');
    return new Promise<CloudinaryResponse>((resolve, reject) => {
      const uploadStream = cloudinary.uploader.upload_stream(
        {
          resource_type: 'raw',
          folder: `zalo/${fileName.substring(lastIndexDot + 1)}`,
        },
        (error, result) => {
          if (error) reject(ApplicationException.create(ExceptionEnums.FILE_INVALID));
          else resolve(result!);
        },
      );

      streamifier.createReadStream(file.buffer).pipe(uploadStream);
    });
  }

  sign(request: FileNameRequest): object {
    const timestamp = Math.round(new Date().getTime() / 1000);
    const fileName = request.fileName;
    const lastIndexDot = fileName.lastIndexOf('.');
    const extension = fileName.substring(lastIndexDot + 1);
    const folder = `zalo/${extension}`;
    const signData = {
      timestamp,
      folder: folder,
      public_id: `${fileName.substring(0, lastIndexDot)}_${this.uuidService.generate()}.${extension}`,
    };
    const signature = cloudinary.utils.api_sign_request(signData, cloudinary.config().api_secret!);
    return {
      ...signData,
      signature: signature,
      apiKey: cloudinary.config().api_key,
      url: `https://api.cloudinary.com/v1_1/${cloudinary.config().cloud_name}/raw/upload`,
    };
  }
}
