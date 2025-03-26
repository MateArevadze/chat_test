#!/bin/bash

# Set RSocket server address
URL="ws://localhost:7000"

# Send request using rsc
echo "$MESSAGE_JSON" | java -jar "C:\Users\u\Downloads\rsc-0.9.1.jar" --stream --route api.v1.messages.stream.game-3456 "$URL"
