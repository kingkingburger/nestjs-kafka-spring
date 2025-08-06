import { OnModuleInit } from '@nestjs/common';
import { ClientKafka } from '@nestjs/microservices';
export declare class AppController implements OnModuleInit {
    private readonly kafkaClient;
    constructor(kafkaClient: ClientKafka);
    onModuleInit(): Promise<void>;
    sendMessage(): {
        status: string;
        data: {
            text: string;
            id: number;
        };
    };
}
