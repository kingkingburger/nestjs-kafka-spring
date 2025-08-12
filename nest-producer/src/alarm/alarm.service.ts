import { Injectable } from '@nestjs/common';

@Injectable()
export class AlarmService {
  getHello(): string {
    return 'Hello World!';
  }
}
