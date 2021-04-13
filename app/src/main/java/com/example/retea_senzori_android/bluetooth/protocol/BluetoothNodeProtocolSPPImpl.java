package com.example.retea_senzori_android.bluetooth.protocol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.example.retea_senzori_android.sensor.NoArgConsumer;
import com.example.retea_senzori_android.sensor.SensorLogData;
import com.example.retea_senzori_android.sensor.SensorTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.CLOSE_LOG_FILE_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.CONNECT_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.GO_TO_SLEEP_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.LIVE_DATA_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.LOG_DATA_PACKET_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.LOG_REPLAY_DONE_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.MESSAGE_HEADER_LENGTH;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.OPEN_LOG_FILE_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.REPLAY_ALL_DATA_FROM_ALL_LOGS_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.REPLAY_DATA_FROM_CURRENT_LOG_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.REQUEST_SENSOR_COUNT_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.REQUEST_SENSOR_TYPES_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.SD_CARD_ERROR_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.SEND_SENSOR_COUNT_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.SEND_SENSOR_TYPE_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.SET_SAVE_TO_LOG_TIME_INTERVAL_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.SET_UNIX_TIME_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.STOP_STRING;
import static com.example.retea_senzori_android.bluetooth.protocol.BluetoothNodeProtocolStrings.TIMEOUT_OCCURRED_STRING;

public class BluetoothNodeProtocolSPPImpl implements BluetoothNodeProtocol {

    private final UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket bluetoothSocket;
    private OutputStream bluetoothOutputStream;
    private InputStream bluetoothInputStream;
    private final AtomicBoolean running = new AtomicBoolean();
    private final Consumer<String> onConnectedDeviceListener;

    private boolean shouldSendConnectMessage;
    private Consumer<Integer> onSensorCountRead;
    private Consumer<SensorTypes[]> onSensorTypesRead;
    private SensorTypes[] sensorTypes;
    private int sensorCount;
    private Consumer<SensorLogData> onDataRead;
    private NoArgConsumer onLogFileOpen, onLogFileClosed;
    private NoArgConsumer onLogReplayDone;

    public BluetoothNodeProtocolSPPImpl(Consumer<String> onConnectedDeviceListener) {
        this.onConnectedDeviceListener = onConnectedDeviceListener;

        shouldSendConnectMessage = false;
    }

    @Override
    public void connect(BluetoothDevice device, Consumer<SDCardErrors> onSDCardErrorReceived) {

        disconnect();

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(sppUuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread communicationThread = new Thread(() -> {
            try {

                try {
                    bluetoothSocket.connect();
                    onConnectedDeviceListener.accept(device.getName());
                } catch (IOException e) {
                    System.out.println("Already connected to + " + device.getName());
                }

                bluetoothInputStream = bluetoothSocket.getInputStream();
                bluetoothOutputStream = bluetoothSocket.getOutputStream();
                running.set(true);

                byte[] headerMessage = new byte[MESSAGE_HEADER_LENGTH];

                while (running.get()) {
                    for (int i = 0; i < MESSAGE_HEADER_LENGTH; i++) {
                        headerMessage[i] = (byte) bluetoothInputStream.read();
                    }

                    String headerMessageString = new String(headerMessage);

                    switch (headerMessageString) {
                        case SD_CARD_ERROR_STRING: {
                            byte errorType = (byte) bluetoothInputStream.read();
                            if (onSDCardErrorReceived != null) {
                                onSDCardErrorReceived.accept(SDCardErrors.convert(errorType));
                            }
                        }
                        break;

                        case TIMEOUT_OCCURRED_STRING: {
                            shouldSendConnectMessage = true;
                        }
                        break;

                        case SEND_SENSOR_COUNT_STRING: {
                            byte sensorCount = (byte) bluetoothInputStream.read();
                            if (onSensorCountRead != null) {
                                onSensorCountRead.accept((int) sensorCount);
                            }
                        }
                        break;

                        case SEND_SENSOR_TYPE_STRING: {
                            byte sensorType = (byte) bluetoothInputStream.read();

                            if (onSensorTypesRead != null && sensorCount > 0) {
                                sensorTypes[sensorCount - 1] = SensorTypes.convert(sensorType);
                                sensorCount--;
                                if (sensorCount == 0) {
                                    onSensorTypesRead.accept(sensorTypes);
                                }
                            }
                            break;
                        }
                        case LOG_DATA_PACKET_STRING: {

                            byte[] sensorData = new byte[16];
                            for (int i = 0; i < sensorData.length; i++) {
                                sensorData[i] = (byte) bluetoothInputStream.read();
                            }
                            byte logType = ByteBuffer.wrap(sensorData, 0, Byte.BYTES).order(ByteOrder.LITTLE_ENDIAN).get();
                            byte sensorType = ByteBuffer.wrap(sensorData, Byte.BYTES, Byte.BYTES).order(ByteOrder.LITTLE_ENDIAN).get();
                            short batchIndex = ByteBuffer.wrap(sensorData, 2 * Byte.BYTES, Short.BYTES).order(ByteOrder.LITTLE_ENDIAN).getShort();
                            float sensorValue = ByteBuffer.wrap(sensorData, 2 * Byte.BYTES + Short.BYTES, Float.BYTES).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                            long time = ByteBuffer.wrap(sensorData, 2 * Byte.BYTES + Short.BYTES + Float.BYTES, Long.BYTES).order(ByteOrder.LITTLE_ENDIAN).getInt();

                            if (onDataRead != null) {
                                onDataRead.accept(new SensorLogData(logType, sensorType, batchIndex, sensorValue, time));
                            }

                            break;
                        }

                        case OPEN_LOG_FILE_STRING: {
                            if (onLogFileOpen != null) {
                                onLogFileOpen.accept();
                            }
                        }
                        break;

                        case CLOSE_LOG_FILE_STRING: {
                            if (onLogFileClosed != null) {
                                onLogFileClosed.accept();
                            }
                        }

                        case LOG_REPLAY_DONE_STRING: {
                            if (onLogReplayDone != null) {
                                onLogReplayDone.accept();
                            }
                        }
                        break;
                    }
                }

                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    bluetoothSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        communicationThread.start();
    }

    @Override
    public void disconnect() {
        try {
            if (bluetoothSocket != null) {
                sendCommand(GO_TO_SLEEP_STRING);

                bluetoothSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        running.set(false);
        bluetoothOutputStream = null;
        bluetoothInputStream = null;
    }

    @Override
    public void readSensorCount(Consumer<Integer> onSensorCountRead) {
        sendCommand(REQUEST_SENSOR_COUNT_STRING);
        this.onSensorCountRead = onSensorCountRead;
    }

    @Override
    public void readSensorTypes(int sensorCount, Consumer<SensorTypes[]> onSensorTypesRead) {
        sendCommand(REQUEST_SENSOR_TYPES_STRING);
        sensorTypes = new SensorTypes[sensorCount];
        this.sensorCount = sensorCount;
        this.onSensorTypesRead = onSensorTypesRead;
    }

    @Override
    public void readLiveData(Consumer<SensorLogData> onLiveDataRead) {
        sendCommand(LIVE_DATA_STRING);
        this.onDataRead = onLiveDataRead;
    }

    @Override
    public void readCurrentLogData(Consumer<SensorDataLogFile> onCurrentLogRead) {
        sendCommand(REPLAY_DATA_FROM_CURRENT_LOG_STRING);

        final SensorDataLogFile sensorDataLogFile = new SensorDataLogFile();
        onLogFileOpen = sensorDataLogFile::openLogFile;
        onLogFileClosed = sensorDataLogFile::closeLogFile;

        onDataRead = sensorDataLogFile::addSensorLogData;
    }

    @Override
    public void readAllLogData(Consumer<List<SensorDataLogFile>> onCurrentLogRead) {
        sendCommand(REPLAY_ALL_DATA_FROM_ALL_LOGS_STRING);

        final LinkedList<SensorDataLogFile> logFiles = new LinkedList<>();

        onLogFileOpen = () -> {
            SensorDataLogFile logFile = new SensorDataLogFile();
            logFile.openLogFile();
            logFiles.add(logFile);
        };

        onLogFileClosed = () -> {
            SensorDataLogFile last = logFiles.getLast();
            last.closeLogFile();
        };

        onDataRead = sensorLogData -> {
            SensorDataLogFile last = logFiles.getLast();
            last.addSensorLogData(sensorLogData);
        };

        onLogReplayDone = () -> onCurrentLogRead.accept(logFiles);
    }

    @Override
    public void setUnixTime() {
        long unixTime = System.currentTimeMillis() / 1000L;
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(unixTime);
        sendCommand(SET_UNIX_TIME_STRING, buffer.array());
    }

    @Override
    public void setSaveToLogInterval(int interval) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(interval);
        sendCommand(SET_SAVE_TO_LOG_TIME_INTERVAL_STRING, buffer.array());
    }

    @Override
    public void stopCurrentCommunication() {
        sendCommand(STOP_STRING);
    }

    private void sendCommand(String command) {
        try {
            if (bluetoothOutputStream != null) {

                if (shouldSendConnectMessage) {
                    shouldSendConnectMessage = false;

                    System.out.println("Sending " + CONNECT_STRING);
                    bluetoothOutputStream.write(CONNECT_STRING.getBytes());
                }

                System.out.println("Sending " + command);
                bluetoothOutputStream.write(command.getBytes());
                bluetoothOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String command, byte[] arguments) {
        try {
            if (bluetoothOutputStream != null) {

                if (shouldSendConnectMessage) {
                    shouldSendConnectMessage = false;

                    System.out.println("Sending " + CONNECT_STRING);
                    bluetoothOutputStream.write(CONNECT_STRING.getBytes());
                }

                System.out.println("Sending " + command);
                bluetoothOutputStream.write(command.getBytes());
                bluetoothOutputStream.write(arguments);
                bluetoothOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private boolean matchByteArrayWithString(byte[] array, String string) {
//        byte[] bytes = string.getBytes();
//        if (array.length != bytes.length) {
//            return false;
//        }
//
//        for (int i = 0; i < bytes.length; i++) {
//            if (bytes[i] != array[i]) {
//                return false;
//            }
//        }
//        return true;
//    }
}
