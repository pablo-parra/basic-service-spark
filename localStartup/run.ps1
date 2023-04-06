#!/usr/bin/env bash

#./gradlew clean fatJar

docker-compose build

docker-compose -f docker-compose.local-dependencies.yml up -d wiremock

echo "Waiting for containers to start..."
sleep 20