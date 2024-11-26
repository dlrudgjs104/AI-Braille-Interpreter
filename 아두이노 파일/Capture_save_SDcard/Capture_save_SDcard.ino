/*********

  캡처하여 sd카드에 jpg형태로 저장되는 코드
*********/

#include "esp_camera.h"
#include "Arduino.h"
#include "soc/soc.h"           // Disable brownour problems
#include "soc/rtc_cntl_reg.h"  // Disable brownour problems
#include "driver/rtc_io.h"
#include <EEPROM.h>            // read and write from flash memory
#include "BluetoothSerial.h"

#define CAMERA_MODEL_AI_THINKER 

BluetoothSerial SerialBT;
#define EEPROM_SIZE 1


#include "camera_pins.h"

const int ledPin = 4; 
int pictureNumber = 0;

void congif_t();

void setup() {
  WRITE_PERI_REG(RTC_CNTL_BROWN_OUT_REG, 0); //disable brownout detector
 
  Serial.begin(115200);
  SerialBT.begin("ESP32-CAM");
  //Serial.setDebugOutput(true);
  //Serial.println();
  
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;
  config.frame_size = FRAMESIZE_QVGA; // 320x240
  config.jpeg_quality = 10;
  config.fb_count = 1;
  
  // Init Camera
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Camera init failed with error 0x%x", err);
    return;
  }

 pinMode(GPIO_NUM_13, INPUT_PULLUP);

}

void loop() {
  if (digitalRead(GPIO_NUM_13) == LOW) {
    captureAndSendImage();
  }
  delay(100);
}

void captureAndSendImage() {
  camera_fb_t * fb = NULL;
  // Take Picture with Camera
  fb = esp_camera_fb_get();
  if(!fb) {
    Serial.println("Camera capture failed");
    return;
  }

  //블루투스 전송부
  if (SerialBT.hasClient()) {
      size_t packetSize = 1024; // 패킷 크기 설정
      size_t packets = (fb->len + packetSize - 1) / packetSize; // 패킷 수 계산

      for (size_t i = 0; i < packets; i++) {
        size_t start = i * packetSize;
        size_t end = min(start + packetSize, fb->len);

        SerialBT.write(fb->buf + start, end - start);
      }
    Serial.print(fb->len);
    for(int i=0; i<fb->len; i++){
      Serial.print(" "); // 공백으로 구분
      Serial.print(fb->buf[i]);
      }
    Serial.println();
  }
  else {
    Serial.println("블루투스 연결 없음");
  }

  esp_camera_fb_return(fb); 
}