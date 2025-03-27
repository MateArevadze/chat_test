#!/bin/bash

# Set RSocket server address
URL="ws://localhost:7000"

# Create JSON payload
MESSAGE_JSON=$(cat <<EOF
{
  "content": "sdhgsdkgasgdsg!",
  "gameId": "game-3456",
  "userId": "user-98765",
  "sent": "$(date --utc +%Y-%m-%dT%H:%M:%SZ)",
  "id": "message-001"
}
EOF
)

# Send request using rsc
echo "$MESSAGE_JSON" | java -jar "C:\Users\u\IdeaProjects\kotlin-spring-chat\src\main\resources\manual_tests\rsc-0.9.1.jar" --channel --route api.v1.messages.stream --data "$MESSAGE_JSON" "$URL"
