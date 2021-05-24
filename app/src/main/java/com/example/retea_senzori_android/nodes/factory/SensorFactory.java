package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.sensor.SensorType;

public class SensorFactory {
    public static Sensor fromModel(SensorModel sensorModel) {
        return new Sensor(sensorModel);
    }

    public static Sensor fromType(SensorType sensorType) {
        return new Sensor(new SensorModel(sensorType));
    }

    public static SensorValueDisplayer getSensorValueMapper(SensorType sensorType) {
        SensorValueDisplayer sensorValueDisplayer = new IdentityDisplayer();

        switch (sensorType) {

            case RAIN_SENSOR:
                sensorValueDisplayer = new IdentityDisplayer();
                break;
            case GAS_SENSOR:
                sensorValueDisplayer = new GasDisplayer();
                break;
            case SOIL_MOISTURE_SENSOR:
                sensorValueDisplayer = new SoilMoistureDisplayer();
                break;
            case LIGHT_SENSOR:
                sensorValueDisplayer = new LuminosityDisplayer();
                break;
            case TEMP_SENSOR:
                break;
            case SIMPLE_TEMP_SENSOR:
                break;
            case NO_TYPE:
            default:
                sensorValueDisplayer = new IdentityDisplayer();
                break;
        }
        return sensorValueDisplayer;
    }
}
