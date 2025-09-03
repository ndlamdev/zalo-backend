/**
 * Author: Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:22 AM - 28/08/2025
 * User: kimin
 **/

export type EnvType = {
  jwt: {
    publicKey?: string;
    algorithm?: string;
    claimKey?: string;
  };
  port?: number;
  cloudinary: {
    name?: string;
    apiKey?: string;
    apiSecret?: string;
    url?: string;
  };
};
