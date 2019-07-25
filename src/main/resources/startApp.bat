@ECHO OFF
rem #
rem # Copyright (c) 2019 Quinovas
rem #

echo "                                                                                                   "
echo "                                                                                                   "
echo "  _____       _                  __  __  ____ _______ _______    _____                _            "
echo " |  __ \     | |                |  \/  |/ __ \__   __|__   __|  / ____|              | |           "
echo " | |__) |   _| |___  __ _ _ __  | \  / | |  | | | |     | |    | (___   ___ _ __   __| | ___ _ __  "
echo " |  ___/ | | | / __|/ _` | '__| | |\/| | |  | | | |     | |     \___ \ / _ \ '_ \ / _` |/ _ \ '__| "
echo " | |   | |_| | \__ \ (_| | |    | |  | | |__| | | |     | |     ____) |  __/ | | | (_| |  __/ |    "
echo " |_|    \__,_|_|___/\__,_|_|    |_|  |_|\___\_\ |_|     |_|    |_____/ \___|_| |_|\__,_|\___|_|    "
echo "                                                                                                   "
echo "                                                               version: 1.0-SNAPSHOT               "

set "PULSAR_MQTT_SENDER_PATH=%cd%"

if not "%PULSAR_MQTT_SENDER_PATH%" == "" goto gotHome
if exist "%PULSAR_MQTT_SENDER_PATH%\startApp.bat" goto okHome

:gotHome
if exist "%PULSAR_MQTT_SENDER_PATH%\startApp.bat" goto okHome
    echo The PULSAR_MQTT_SENDER_PATH environment variable is not defined correctly
    echo This environment variable is needed to run this program
goto end

:okHome

rem Set JavaHome if it exists
if exist { "%JAVA_HOME%\bin\java" } (
    set JAVA="%JAVA_HOME%\bin\java"
)

echo Using JAVA_HOME:       "%JAVA_HOME%"
echo Using PULSAR_MQTT_SENDER_PATH:   "%PULSAR_MQTT_SENDER_PATH%"


rem create gc.log file
if not exist { "%PULSAR_MQTT_SENDER_PATH%\logs\gc.log" } (
    mkdir "%PULSAR_MQTT_SENDER_PATH%\logs" 2>nul
    type NUL > "%PULSAR_MQTT_SENDER_PATH%\logs\gc.log"
)

rem # This class has the main method to start the app
set MAIN_CLASS=com.echostreams.pulsar.mqtt.sender.PulsarMqttSender

set JAVA_OPTS=
set JAVA_OPTS_SCRIPT=-XX:+HeapDumpOnOutOfMemoryError -Djava.awt.headless=true
set PULSAR_MQTT_SENDER_PATH=%PULSAR_MQTT_SENDER_PATH%

rem # Use the Hotspot garbage-first collector.
set JAVA_OPTS=%JAVA_OPTS%  -XX:+UseG1GC

rem # Have the JVM do less remembered set work during STW, instead
rem # preferring concurrent GC. Reduces p99.9 latency.
set JAVA_OPTS=%JAVA_OPTS%  -XX:G1RSetUpdatingPauseTimePercent=5

rem # Main G1GC tunable: lowering the pause target will lower throughput and vise versa.
rem # 200ms is the JVM default and lowest viable setting
rem # 1000ms increases throughput. Keep it smaller than the timeouts.
set JAVA_OPTS=%JAVA_OPTS%  -XX:MaxGCPauseMillis=500

rem # Optional G1 Settings

rem  Save CPU time on large (>= 16GB) heaps by delaying region scanning
rem  until the heap is 70% full. The default in Hotspot 8u40 is 40%.
rem set JAVA_OPTS=%JAVA_OPTS%  -XX:InitiatingHeapOccupancyPercent=70

rem  For systems with > 8 cores, the default ParallelGCThreads is 5/8 the number of logical cores.
rem  Otherwise equal to the number of cores when 8 or less.
rem  Machines with > 10 cores should try setting these to <= full cores.
rem set JAVA_OPTS=%JAVA_OPTS%  -XX:ParallelGCThreads=16

rem  By default, ConcGCThreads is 1/4 of ParallelGCThreads.
rem  Setting both to the same value can reduce STW durations.
rem set JAVA_OPTS=%JAVA_OPTS%  -XX:ConcGCThreads=16

rem ## GC logging options -- uncomment to enable

set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCDetails
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCDateStamps
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintHeapAtGC
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintTenuringDistribution
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintGCApplicationStoppedTime
set JAVA_OPTS=%JAVA_OPTS% -XX:+PrintPromotionFailure
rem set JAVA_OPTS=%JAVA_OPTS% -XX:PrintFLSStatistics=1
set JAVA_OPTS=%JAVA_OPTS% -Xloggc:%PULSAR_MQTT_SENDER_PATH%\logs\gc.log
set JAVA_OPTS=%JAVA_OPTS% -XX:+UseGCLogFileRotation
set JAVA_OPTS=%JAVA_OPTS% -XX:NumberOfGCLogFiles=10
set JAVA_OPTS=%JAVA_OPTS% -XX:GCLogFileSize=10M

%JAVA% -server %JAVA_OPTS% %JAVA_OPTS_SCRIPT% -cp %PULSAR_MQTT_SENDER_PATH%\lib\* %MAIN_CLASS%