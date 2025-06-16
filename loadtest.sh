#!/bin/bash

# List of product IDs to add to cart (customize as needed)
PRODUCT_IDS=(10097 10079 10324 10102 10301 10283 10319)

# Number of concurrent sessions
NUM_SESSIONS=5000

# Function to simulate a shopping session
shop_session() {
  SESSION_ID=$1
  COOKIE_FILE="cookies_${SESSION_ID}.txt"
  PRODUCT_ID=${PRODUCT_IDS[$((SESSION_ID % ${#PRODUCT_IDS[@]}))]}

  echo "Session $SESSION_ID: Adding product $PRODUCT_ID to cart"
  curl -s -c $COOKIE_FILE -X POST "http://localhost:9989/api/cart/add/$PRODUCT_ID"

  echo "Session $SESSION_ID: Viewing cart"
  curl -s -b $COOKIE_FILE "http://localhost:9989/api/cart"

  echo "Session $SESSION_ID: Checking out"
  curl -s -b $COOKIE_FILE -X POST "http://localhost:9989/api/checkout" \
    -d "name=User$SESSION_ID" \
    -d "shippingAddress=123 Main St, Apt $SESSION_ID" \
    -d "billingAddress=456 Elm St, Apt $SESSION_ID"

  # Clean up cookie file
  rm -f $COOKIE_FILE
}

# Run sessions concurrently
for ((i=0; i<$NUM_SESSIONS; i++)); do
  shop_session $i &
done

wait
echo "All sessions completed."