import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  console.log(`🚀 [main] listening on localhost:${process.env.PORT}`);
  await app.listen(process.env.PORT ?? 3030);
}
bootstrap();
