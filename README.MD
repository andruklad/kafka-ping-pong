# kafka-ping-pong

Version: 1.0.

Author: Andrei Kladkevich

2024

## Бизнес-требования.
ping-pong приложение:
1) ping-service с определенной периодичностью (или через http запрос) публикует в
топик ping.out сообщение с полями ping_id, ping_date_time
2) pong-service слушает ping.out, сохраняет событие и выводит сообщение в лог и
публикует в топик pong.out сообщение с полями pong_id, pong_date_time
3) ping-service слушает pong.out, сохраняет событие и выводит сообщение в лог

Требования:
● В рамках consumer group должны вычитываться только новые сообщения
● Предусмотреть возможность безболезненного перезапуска сервиса

## Реализация.
Настройки приложения:
1. enablePingMessage - настройка включения отправки сообщения PING в Kafka (1 - включить).
2. pingMessageDelay - интервал отправок сообщений PING в Kafka (в миллисекундах).
3. enablePublishPongReceiveEvent - настройка включения публикации события в приложении
о получении сообщения PING сервисом PongService (1 - включить).
Настройка 0 используется для демонстрации обработки необработанных событий при повторном запуске приложения.