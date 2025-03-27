#!/bin/bash

# Set RSocket server address
URL="ws://localhost:7000"

# Send request using rsc
echo "$MESSAGE_JSON" | java -jar "resources/manual_tests/rsc-0.9.1.jar" --stream --route api.v1.messages.stream.game-3456 "$URL"
