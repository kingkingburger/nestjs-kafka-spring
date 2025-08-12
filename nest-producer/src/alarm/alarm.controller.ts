import { Controller, Get, Inject, OnModuleInit } from '@nestjs/common';
import { ClientKafka } from '@nestjs/microservices';

@Controller()
export class AlarmController implements OnModuleInit {
  constructor(
    @Inject('KAFKA_SERVICE') private readonly kafkaClient: ClientKafka,
  ) {}

  async onModuleInit() {
    this.kafkaClient.subscribeToResponseOf('my-first-topic');
    await this.kafkaClient.connect();
  }

  @Get('send-message')
  sendMessage() {
    const message = {
      text: `Hello spring! it is ${new Date().toISOString()}`,
      id: Math.floor(Math.random() * 1000),
    };

    this.kafkaClient.emit('my-first-topic', JSON.stringify(message));

    return { status: 'Messsage send!', data: message };
  }

  @Get('send-alarm')
  sendAlarm() {
    const message = {
      text: `alarm Message it is ${new Date().toISOString()}`,
      id: Math.floor(Math.random() * 1000),
    };

    this.kafkaClient.emit('my-first-topic', JSON.stringify(message));

    return { status: 'Messsage send!', data: message };
  }
}
