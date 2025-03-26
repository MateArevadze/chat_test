#!/bin/bash

# Set RSocket server address
URL="ws://localhost:7000"

# Create JSON payload
MESSAGE_JSON=$(cat <<EOF
{
  "content": "sdhgsdkg!as",
  "gameId": "game-1234",
  "userId": "user-987651",
  "sent": "$(date --utc +%Y-%m-%dT%H:%M:%SZ)",
  "id": "message-001"
}
EOF
)

# Send request using rsc
echo "$MESSAGE_JSON" | java -jar "rsc-0.9.1.jar" --channel --route api.v1.messages.stream --data "$MESSAGE_JSON" "$URL"
