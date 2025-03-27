#!/bin/bash

# Set RSocket server address
URL="ws://localhost:7000"
ROUTE="api.v1.messages.stream.game-12345"
RSC_JAR="rsc-0.9.1.jar"

# Function to continuously send requests every 2 seconds
send_receive_request() {
  while true; do
    echo "🔄 Sending request to receive messages..."

    # Send the RSocket request
    java -jar "$RSC_JAR" --stream --route "$ROUTE" "$URL" | while read -r line; do
      echo "📩 Received: $line"
    done
    echo "⏳ Waiting 2 seconds before sending the next request..."
    sleep 2
  done
}

# Start sending requests
send_receive_request
