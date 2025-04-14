#!/bin/bash

# Define the Docker container name and the number of repetitions
CONTAINER_NAME=$1

# Loop to repeatedly execute the Docker container
i=1
while true
do
    echo "Execution #$i"
    docker run --rm $CONTAINER_NAME
    if [ $? -ne 0 ]; then
        echo "Error: Docker container execution failed on attempt #$i"
        exit 1
    fi
    i=$((i + 1))
done