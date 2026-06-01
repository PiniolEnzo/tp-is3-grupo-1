@echo off

echo =========================
echo Instalando dependencias del Backend...
echo =========================

call mvn clean install -DskipTests

echo =========================
echo Instalando dependencias del Frontend...
echo =========================

cd frontend
call npm install
cd ..

echo =========================
echo Levantando Backend...
echo =========================

start cmd /k "mvn spring-boot:run"

timeout /t 5 > nul

echo =========================
echo Levantando Frontend...
echo =========================

start cmd /k "cd frontend && npm run dev"

timeout /t 8 > nul

echo =========================
echo Abriendo navegador...
echo =========================

start http://localhost:3000

echo =========================
echo Proyecto iniciado correctamente
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo =========================