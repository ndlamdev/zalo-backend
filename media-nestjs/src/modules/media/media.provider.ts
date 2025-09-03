import { v2 as cloudinary } from 'cloudinary';
import { ENV_PROVIDER_KEY } from '../../shared/constant/env/env.provider';
import type { EnvType } from '../../shared/constant/env/dto/env.type';


export const CLOUDINARY_PROVIDER_KEY = 'CLOUDINARY';
export const CloudinaryProvider = {
  provide: CLOUDINARY_PROVIDER_KEY,
  useFactory: (env: EnvType) => {
    return cloudinary.config({
      cloud_name: env.cloudinary.name,
      api_key: env.cloudinary.apiKey,
      api_secret: env.cloudinary.apiSecret,
      url: env.cloudinary.url,
      signature_algorithm: 'sha256'
    });
  },
  inject: [ENV_PROVIDER_KEY],
};