# Android-приложение Travel Tracker

## Инструкция для сборки apk-файла

1. Клонирование репозитория
``` Shell
git clone https://github.com/ArtemDaemon/TravelTrackerApp.git
```
2. Открытие директории
``` cmd
cd TravelTrackerApp
```
3. Установка переменных окружения (если не установлены в системе)
``` cmd
export JAVA_HOME=/путь/к/jdk
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```
4. Сборка
``` cmd
gradlew.bat assembleDebug
```
5. Готовый `.apk` файл будет лежать
``` lua
app\build\outputs\apk\debug\app-debug.apk
```