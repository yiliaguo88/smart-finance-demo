#!/usr/bin/env bash

HOST="http://localhost:8082"
ACCOUNT="1002"
AMOUNT=100
REQUESTS=50

echo "=== BUG-D 触发脚本 ==="
echo "并发发送 ${REQUESTS} 笔记账请求，每笔借方 ${AMOUNT}..."
echo ""

for i in $(seq 1 $REQUESTS); do
  curl -s -X POST "${HOST}/api/report/ledger/post?accountCode=${ACCOUNT}&debit=${AMOUNT}&credit=0" > /dev/null &
done
wait

echo "全部请求已发送，查询余额..."
echo ""

RESULT=$(curl -s "${HOST}/api/report/ledger")
BALANCE=$(echo "$RESULT" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d['balances'].get('${ACCOUNT}', 'N/A'))")

EXPECTED=$(echo "350000 + $REQUESTS * $AMOUNT" | bc)

echo "科目 ${ACCOUNT} 当前余额：${BALANCE}"
echo "理论预期余额：${EXPECTED}"
echo ""

if [ "$BALANCE" = "$EXPECTED" ] || [ "$BALANCE" = "${EXPECTED}.0" ]; then
  echo "[侥幸正确] 本次未触发竞态，可多运行几次"
else
  echo "[BUG 已触发] 余额 ${BALANCE} < 预期 ${EXPECTED}，丢失了 $(echo "$EXPECTED - ${BALANCE%.*}" | bc) 元更新"
fi
