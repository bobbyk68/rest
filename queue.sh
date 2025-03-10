#!/bin/bash

# Replace QM1 with your queue manager name
QUEUE_MANAGER="QM1"

# Function to create queues
create_queues() {
  cat <<EOF > create_queues.mqsc
DEFINE QLOCAL('Dev.Queue.X') REPLACE
DEFINE QLOCAL('Dev.Queue.Y') REPLACE
DEFINE QLOCAL('Dev.Queue.Z') REPLACE
END
EOF

  runmqsc $QUEUE_MANAGER < create_queues.mqsc
  rm create_queues.mqsc
  echo "Queues created."
}

# Function to clear queues
clear_queues() {
  cat <<EOF > clear_queues.mqsc
CLEAR QLOCAL('Dev.Queue.X')
CLEAR QLOCAL('Dev.Queue.Y')
CLEAR QLOCAL('Dev.Queue.Z')
END
EOF

  runmqsc $QUEUE_MANAGER < clear_queues.mqsc
  rm clear_queues.mqsc
  echo "Queues cleared."
}

# Main execution logic
if [ "$1" == "create" ]; then
  create_queues
elif [ "$1" == "clear" ]; then
  clear_queues
else
  echo "Usage: $0 {create|clear}"
  exit 1
fi

exit 0
