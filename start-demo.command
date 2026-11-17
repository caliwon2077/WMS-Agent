#!/bin/bash
set -euo pipefail
WMS_ROOT="$(cd "$(dirname "$0")" && pwd)"
WMS_BACKEND_PORT="${WMS_BACKEND_PORT:-8080}"
WMS_FRONTEND_PORT="${WMS_FRONTEND_PORT:-5173}"
WMS_DEMO_DB="${WMS_DEMO_DB:-$WMS_ROOT/db/wms_demo.sqlite}"
export WMS_BACKEND_PORT
if [[ -x "$WMS_ROOT/.tools/jdk-17.0.21+9/Contents/Home/bin/java" ]]; then
  export JAVA_HOME="$WMS_ROOT/.tools/jdk-17.0.21+9/Contents/Home"
fi
WMS_JAVA="${JAVA_HOME:+$JAVA_HOME/bin/}java"
WMS_MVN="$WMS_ROOT/.tools/apache-maven-3.9.9/bin/mvn"
WMS_MAVEN_REPO="${WMS_MAVEN_REPO:-$WMS_ROOT/.tools/m2}"
if command -v mvn >/dev/null 2>&1; then WMS_MVN="$(command -v mvn)"; fi
if [[ ! -x "$WMS_MVN" ]]; then
  echo "未找到 Maven。请安装 Maven 3.9+ 并确保 mvn 位于 PATH。"
  exit 1
fi
if ! "$WMS_JAVA" -version >/dev/null 2>&1; then
  echo "未找到可运行的 Java。请安装 JDK 17，必要时设置 JAVA_HOME。"
  exit 1
fi
for WMS_TOOL in node npm curl lsof; do
  if ! command -v "$WMS_TOOL" >/dev/null 2>&1; then
    echo "缺少启动依赖：$WMS_TOOL。请先按 QUICKSTART.md 安装运行环境。"
    exit 1
  fi
done
mkdir -p "$WMS_ROOT/project/runtime" "$WMS_ROOT/project/local" "$WMS_ROOT/db"
if [[ ! -f "$WMS_ROOT/project/local/deepseek.env" ]]; then
  cp "$WMS_ROOT/project/deepseek.env.example" "$WMS_ROOT/project/local/deepseek.env"
  chmod 600 "$WMS_ROOT/project/local/deepseek.env"
fi
for WMS_PORT in "$WMS_BACKEND_PORT" "$WMS_FRONTEND_PORT"; do
  if lsof -nP -iTCP:"$WMS_PORT" -sTCP:LISTEN >/dev/null 2>&1; then
    echo "端口 $WMS_PORT 已被使用。请先关闭对应服务，或更换 WMS_BACKEND_PORT / WMS_FRONTEND_PORT。"
    exit 1
  fi
done
cd "$WMS_ROOT/wms_system_javabackend"
"$WMS_MVN" -q -Dmaven.repo.local="$WMS_MAVEN_REPO" -DskipTests package
"$WMS_JAVA" -jar target/wms-system-backend-1.0.0.jar \
  "--server.address=127.0.0.1" "--server.port=$WMS_BACKEND_PORT" \
  "--app.database.path=$WMS_DEMO_DB" "--app.demo.enabled=true" \
  "--app.demo.config-path=$WMS_ROOT/project/local/deepseek.env" \
  >"$WMS_ROOT/project/runtime/backend-$WMS_BACKEND_PORT.log" 2>&1 &
WMS_BACKEND_PID=$!
WMS_FRONTEND_PID=""
cleanup() {
  kill "$WMS_BACKEND_PID" ${WMS_FRONTEND_PID:+"$WMS_FRONTEND_PID"} 2>/dev/null || true
}
trap cleanup EXIT INT TERM
WMS_READY=0
for ((WMS_TRY=0;WMS_TRY<60;WMS_TRY++)); do
  if curl --silent --fail "http://127.0.0.1:$WMS_BACKEND_PORT/api/v1/system/health" >/dev/null; then WMS_READY=1; break; fi
  if ! kill -0 "$WMS_BACKEND_PID" 2>/dev/null; then break; fi
  sleep 1
done
if [[ "$WMS_READY" != 1 ]]; then
  echo "后端未就绪，请查看 project/runtime/backend-$WMS_BACKEND_PORT.log。"
  exit 1
fi
cd "$WMS_ROOT/wms_system_frontend"
if [[ ! -f node_modules/vite/bin/vite.js ]]; then npm ci; fi
node node_modules/vite/bin/vite.js --host 127.0.0.1 --port "$WMS_FRONTEND_PORT" \
  >"$WMS_ROOT/project/runtime/frontend-$WMS_FRONTEND_PORT.log" 2>&1 &
WMS_FRONTEND_PID=$!
WMS_READY=0
for ((WMS_TRY=0;WMS_TRY<15;WMS_TRY++)); do
  if curl --silent --fail "http://127.0.0.1:$WMS_FRONTEND_PORT" >/dev/null; then WMS_READY=1; break; fi
  if ! kill -0 "$WMS_FRONTEND_PID" 2>/dev/null; then break; fi
  sleep 1
done
if [[ "$WMS_READY" != 1 ]]; then
  echo "前端未就绪，请查看 project/runtime/frontend-$WMS_FRONTEND_PORT.log。"
  exit 1
fi
echo "WMS Agent Demo V1.0.0 已启动"
echo "打开 http://127.0.0.1:$WMS_FRONTEND_PORT"
echo "演示账号：admin / Demo@2026（其他身份见文档）"
echo "仅合成演示数据；Ctrl+C 停止本次启动的服务。"
wait "$WMS_BACKEND_PID" "$WMS_FRONTEND_PID"
