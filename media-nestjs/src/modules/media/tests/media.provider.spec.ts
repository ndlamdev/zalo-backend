import { Test, TestingModule } from '@nestjs/testing';
import { CLOUDINARY_PROVIDER_KEY, CloudinaryProvider } from '../media.provider';
import { ENV_PROVIDER_KEY, EnvProvider } from '../../../shared/constant/env/env.provider';
import { ConfigOptions } from 'cloudinary';
import { EnvModule } from '../../../shared/constant/env/env.module';

describe('MediaProvider', () => {
  let provider: ConfigOptions;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      imports: [EnvModule],
      providers: [CloudinaryProvider],
    }).compile();

    provider = module.get<MediaProvider>(CLOUDINARY_PROVIDER_KEY);
  });

  it('should be defined', () => {
    expect(provider).toBeDefined();
    expect(provider.cloud_name).toBeDefined();
    expect(provider.api_key).toBeDefined();
    expect(provider.api_secret).toBeDefined();
  });
});
