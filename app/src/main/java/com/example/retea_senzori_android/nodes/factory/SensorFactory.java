package com.example.retea_senzori_android.nodes.factory;

import com.example.retea_senzori_android.models.SensorModel;
import com.example.retea_senzori_android.sensor.SensorType;

public class SensorFactory {
    public static Sensor fromModel(SensorModel sensorModel) {
        return new Sensor(sensorModel, getSensorValueMapper(sensorModel.sensorType));
    }

    public static Sensor fromType(SensorType sensorType) {
        SensorModel sensorModel = new SensorModel(sensorType);
        return new Sensor(sensorModel, getSensorValueMapper(sensorType));
    }

    private static SensorValueMapper getSensorValueMapper(SensorType sensorType) {
        SensorValueMapper sensorValueMapper = new IdentityMapper();

        switch (sensorType) {

            case RAIN_SENSOR:
                break;
            case GAS_SENSOR:
                break;
            case SOIL_MOISTURE_SENSOR:
                break;
            case LIGHT_SENSOR:
                break;
            case TEMP_SENSOR:
                break;
            case SIMPLE_TEMP_SENSOR:
                break;
            case NO_TYPE:
            default:
                sensorValueMapper = new IdentityMapper();
                break;
        }
        return sensorValueMapper;
    }
}
