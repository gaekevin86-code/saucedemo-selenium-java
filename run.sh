#!/bin/bash

# Nhận tham số slowmo nếu có (vd: ./run.sh 1500)
SLOWMO=${1:-0}

echo "▶ Running tests (slowmo=${SLOWMO}ms)..."
mvn test -Dslowmo=$SLOWMO

echo ""
echo "📊 Opening Allure report..."
allure serve target/allure-results
