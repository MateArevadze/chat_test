#!/bin/bash

# Set RSocket server address
URL="ws://localhost:7000"

# Create JSON payload
MESSAGE_JSON=$(cat <<EOF
{
  "content": "Hello from shell script!",
  "gameId": "game-12345",
  "userId": "user-98765",
  "sent": "$(date --utc +%Y-%m-%dT%H:%M:%SZ)",
  "id": "message-001"
}
EOF
)

# Send request using rsc
echo "$MESSAGE_JSON" | java -jar "C:\Users\u\Downloads\rsc-0.9.1.jar" --channel --route api.v1.messages.stream --data "$MESSAGE_JSON" "$URL"
