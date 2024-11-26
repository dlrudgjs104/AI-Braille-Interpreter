//This example code is in the Public Domain (or CC0 licensed, at your option.)
//By Evandro Copercini - 2018
//
//This example creates a bridge between Serial and Classical Bluetooth (SPP)
//and also demonstrate that SerialBT have the same functionalities of a normal Serial

#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
}

void loop() {
  // 시리얼 모니터에서 메시지 수신 및 블루투스로 전송
  if (Serial.available()) {
    char data = Serial.read();
    SerialBT.write(data);
  }
  // 블루투스 모듈에서 메시지 수신 및 시리얼 모니터로 전송
  if (SerialBT.available()) {
    char data = SerialBT.read();
    Serial.write(data);
  }
  delay(20);
}