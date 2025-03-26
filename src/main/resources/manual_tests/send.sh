#!/bin/bash

# Set your RSocket URL and metadata if needed
URL="tcp://localhost:8080"  # Adjust to your RSocket server address
REQUEST_METADATA="your-metadata"  # Optional metadata, adjust accordingly

# Create the JSON payload for MessageVM
MESSAGE_JSON=$(cat <<EOF
{
  "content": "Hello from shell script!",
  "user": {
    "username": "user1",
    "email": "user1@example.com"
  },
  "sent": "$(date --utc +%Y-%m-%dT%H:%M:%SZ)",
  "id": "1235"
}
EOF
)

# Send the JSON payload as a request stream
echo "$MESSAGE_JSON" | java -jar rsc-0.9.1.jar $URL \
  --route api.v1.messages.stream \
  --metadata "$REQUEST_METADATA" \
  --data "$MESSAGE_JSON" \
  --stream
