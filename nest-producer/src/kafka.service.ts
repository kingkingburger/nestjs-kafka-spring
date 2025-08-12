// main service에서 kafka를 비동기로 연결시키려면 이걸 사용하면됨

// import { Injectable, Logger, OnApplicationBootstrap, OnApplicationShutdown } from '@nestjs/common';
// import { ConfigService } from '@nestjs/config';
// import { ClientKafkaProxy, ClientProxyFactory, Transport } from '@nestjs/microservices';
//
// @Injectable()
// export class KafkaService implements OnApplicationBootstrap, OnApplicationShutdown {
//   private readonly logger = new Logger(KafkaService.name);
//   public client: ClientKafkaProxy;
//
//   constructor(private readonly configService: ConfigService) {}
//
//   onApplicationBootstrap() {
//     const kafkaHost = this.configService.get<string>('KAFKA_HOST');
//     const kafkaPort = this.configService.get<string>('KAFKA_PORT');
//
//     if (!kafkaHost || !kafkaPort) {
//       this.logger.error('Kafka host or port is not defined in environment variables.');
//       return;
//     }
//
//     this.client = ClientProxyFactory.create({
//       transport: Transport.KAFKA,
//       options: {
//         client: {
//           clientId: 'nest-producer',
//           connectionTimeout: 1000, // 3초로 늘려보기
//           brokers: [
//             `${kafkaHost}:${kafkaPort}`,
//           ],
//         },
//         consumer: {
//           groupId: 'nest-producer-group',
//         },
//       },
//     });
//
//     // 연결을 백그라운드에서 시도. await을 사용하지 않아 시작을 블로킹하지 않음.
//     this.client.connect();
//     this.logger.log('Kafka client connection initiated in background...');
//   }
//
//   async onApplicationShutdown() {
//     if (this.client) {
//       await this.client.close();
//       this.logger.log('Kafka client disconnected.');
//     }
//   }
//
//   // 메시지 전송 예시 (emit - 단방향)
//   emit(topic: string, message: any) {
//     if (!this.client) {
//       this.logger.error('Kafka client is not initialized.');
//       return;
//     }
//     return this.client.emit(topic, message);
//   }
//
//   // 메시지 전송 및 응답 대기 예시 (send - 양방향)
//   send<TResult = any, TInput = any>(topic: string, message: TInput) {
//     if (!this.client) {
//       this.logger.error('Kafka client is not initialized.');
//       throw new Error('Kafka client is not initialized.');
//     }
//     return this.client.send<TResult, TInput>(topic, message);
//   }
// }