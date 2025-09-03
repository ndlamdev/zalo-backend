import { SetMetadata } from '@nestjs/common';

export const HAS_AUTHORITY_KEY = 'hasAuthority';
export const HasAuthority = (...args: string[]) => SetMetadata(HAS_AUTHORITY_KEY, args);
