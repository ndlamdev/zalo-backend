import { Test, TestingModule } from '@nestjs/testing';
import { ENV_PROVIDER_KEY, EnvProvider } from '../env.provider';
import { EnvType } from '../dto/env.type';
import { ConfigModule } from '@nestjs/config';

describe('EnvProvider', () => {
  let env: EnvType;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      imports: [ConfigModule.forRoot({ isGlobal: true })],
      providers: [EnvProvider],
    }).compile();

    env = module.get<EnvType>(ENV_PROVIDER_KEY);
  });

  it('should be defined', () => {
    expect(env).toBeDefined();
  });

  it('should have jwt config', () => {
    expect(env.jwt).toBeDefined();
    expect(env.jwt).toBeDefined();
    expect(env.jwt).toBeDefined();
  });

  it('should have cloudinary config', () => {
    expect(env.cloudinary).toBeDefined();
    expect(env.cloudinary).toBeDefined();
    expect(env.cloudinary).toBeDefined();
  });

  it('should have port', () => {
    expect(typeof env.port).toBe('number');
  });
});
