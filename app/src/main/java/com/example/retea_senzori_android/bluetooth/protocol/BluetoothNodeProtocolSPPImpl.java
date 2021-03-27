package com.example.retea_senzori_android.bluetooth.protocol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class BluetoothNodeProtocolSPPImpl implements BluetoothNodeProtocol {

    public static final String CONNECT_STRING = "CONN";
    public static final String DISCONNECT_STRING = "STOP";
    public static final String REQUEST_DATA_STRING = "RQDT";
    public static final String REQUEST_SENSOR_TYPES_STRING = "RQST";
    public static final String SEND_SENSOR_TYPE_STRING = "STYP";
    public static final String DATA_PACKET_STRING = "DATA";
    public static final String START_SENDING_DATA_STRING = "STSD";
    public static final String STOP_SENDING_DATA_STRING = "SPSD";
    public static final int MESSAGE_HEADER_LENGHT = 4;

    private final UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothDevice connectedDevice;
    private BluetoothSocket bluetoothSocket;
    private Thread communicationThread;
    private OutputStream bluetoothOutputStream;
    private InputStream bluetoothInputStream;
    private AtomicBoolean running = new AtomicBoolean();
    private Consumer<String> onConnectedDeviceListener;
    private Consumer<String> updateTextListener;

    public BluetoothNodeProtocolSPPImpl(Consumer<String> onConnectedDeviceListener, Consumer<String> updateTextListener) {
        this.onConnectedDeviceListener = onConnectedDeviceListener;
        this.updateTextListener = updateTextListener;
    }

    @Override
    public void connect(BluetoothDevice device) {

        disconnect();

        connectedDevice = device;
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(sppUuid);
        } catch (IOException e) {
            e.printStackTrace();
        }

        communicationThread = new Thread(() -> {
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

                byte[] headerMessage = new byte[4];

                while (running.get()) {
                    for (int i = 0; i < MESSAGE_HEADER_LENGHT; i++) {
                        headerMessage[i] = (byte) bluetoothInputStream.read();
                    }

                    String headerMessageString = new String(headerMessage);

                    if (headerMessageString.equals(SEND_SENSOR_TYPE_STRING)) {
                        System.out.println(SEND_SENSOR_TYPE_STRING);

                        String sensorType = "";
                        byte sensorTypeByte = (byte) bluetoothInputStream.read();
                        System.out.println(sensorTypeByte);
                        sensorType += sensorTypeByte;

                        updateTextListener.accept(SEND_SENSOR_TYPE_STRING);
                        updateTextListener.accept(sensorType);
                    } else if (headerMessageString.equals(DATA_PACKET_STRING)) {

                        System.out.println(DATA_PACKET_STRING);
                        updateTextListener.accept(DATA_PACKET_STRING);
                        byte[] sensorData = new byte[10];
                        for (int i = 0; i < 10; i++) {
                            sensorData[i] = (byte) bluetoothInputStream.read();
                        }

                        short sensorType = ByteBuffer.wrap(sensorData, 0, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
                        float sensorValue = ByteBuffer.wrap(sensorData, 2, 4).order(ByteOrder.LITTLE_ENDIAN).getFloat();
                        int time = ByteBuffer.wrap(sensorData, 6, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();

                        System.out.println(Arrays.toString(sensorData));
                        System.out.println(sensorType + " " + sensorValue + " " + time);
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
    public void sendCommand(String command) {
        try {
            if (bluetoothOutputStream != null) {
                System.out.println("Sending " + command);
                bluetoothOutputStream.write(command.getBytes());
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
