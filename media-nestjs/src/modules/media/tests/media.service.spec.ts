import { Test, TestingModule } from '@nestjs/testing';
import { MediaService } from '../media.service';
import * as path from 'node:path';
import * as fs from 'node:fs';
import { CloudinaryProvider } from '../media.provider';
import { EnvModule } from '../../../shared/constant/env/env.module';

describe('MediaService', () => {
  let service: MediaService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      imports: [EnvModule],
      providers: [CloudinaryProvider, MediaService],
    }).compile();

    service = module.get<MediaService>(MediaService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });

  it('should be upload success', async () => {
    const pathFile = path.join(__dirname, 'files', 'test_file.txt');
    const file: Express.Multer.File = {
      fieldname: 'file',
      originalname: 'test_file.txt',
      encoding: '7bit',
      mimetype: 'text/plain',
      size: 14,
      buffer: Buffer.from('Hello world!'),
      destination: '', // Multer thường điền, có thể để trống
      filename: 'test_file.txt',
      path: pathFile,
      stream: fs.createReadStream(pathFile),
    };
    await expect(service.upload(file)).resolves.toBeDefined();
  });
});
