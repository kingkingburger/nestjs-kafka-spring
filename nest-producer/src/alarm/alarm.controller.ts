import { Controller, Get, Inject, OnModuleInit } from '@nestjs/common';
import { ClientKafka } from '@nestjs/microservices';

@Controller('alarm')
export class AlarmController implements OnModuleInit {
  constructor(
    @Inject('KAFKA_SERVICE') private readonly kafkaClient: ClientKafka,
  ) {}

  async onModuleInit() {
    this.kafkaClient.subscribeToResponseOf('my-first-topic');
    await this.kafkaClient.connect();
  }

  @Get('send')
  sendAlarm() {
    const message = {
      text: `alarm Message it is ${new Date().toISOString()}`,
      id: Math.floor(Math.random() * 1000),
    };

    this.kafkaClient.emit('alarm', JSON.stringify(message));

    return { status: 'Message send!', data: message };
  }
}
