#!/usr/bin/env bash
DELAY=1
AMQP_PORT_5672_TCP_PORT=5672
AMQP_PORT_5672_TCP_PROTO=tcp

while ! echo exit | nc $AMQP_PORT_5672_TCP_ADDR $AMQP_PORT_5672_TCP_PORT  > /dev/null;
do
echo "waiting for broker to wake up, reconnecting in "$DELAY;
sleep $DELAY;
DELAY=$((DELAY*2))
done;
echo "broker seams redy, launchy celery worker"
export C_FORCE_ROOT=true
celery -A adaptation.commons worker --loglevel=info --concurrency=1
