import { Module } from '@nestjs/common';
import { AlarmController } from './alarm.controller';
import { AlarmService } from './alarm.service';

@Module({
  imports: [],
  controllers: [AlarmController],
  providers: [AlarmService],
})
export class AlarmModule {}
