/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:41 AM - 28/08/2025
 * User: kimin
 **/

export const base64ToPem = (base64String?: string, type: 'PUBLIC KEY' | 'PRIVATE KEY' = 'PUBLIC KEY'): string => {
  if (!base64String) {
    throw new Error('Invalid Base64 string provided.');
  }

  // Add the required headers and split the string into lines of 64 characters
  return `-----BEGIN ${type}-----\n${base64String.match(/.{1,64}/g)?.join('\n')}\n-----END ${type}-----`;
};
