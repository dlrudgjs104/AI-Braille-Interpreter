#include <SoftwareSerial.h>

#define RxD 11
#define TxD 10
int statePIN = 12; // 블루투스 상태핀
SoftwareSerial BTSerial(TxD, RxD); // 아두이노 핀 10번(TX1), 11번(RX1)을 사용하여 소프트웨어 시리얼 통신 설정
int RGBLedpin[] = {2,3,4}; 
int FSRsensor[] = {A5,A4,A3,A2,A1,A0};                           // 센서값을 아나로그 A0핀 설정
String value;                                      // loop에서 사용할 변수 설정
int limit_value = 5 ; // 실제 센서 경계값
int sensor_count = 6, sum; // 센서 개수
bool senser_input_state = false;

void FSRsensor_check();

void setup() {
  Serial.begin(9600); // 시리얼 모니터 설정
  BTSerial.begin(9600); // 블루투스 모듈 설정

  pinMode(12,INPUT);//블루투스 상태 확인핀
  for(int i = 0; i< 3; i++){
    pinMode(RGBLedpin[i], OUTPUT); //LED 출력 설정
    digitalWrite(RGBLedpin[i], LOW); // LED초기 값 설정
  }
  digitalWrite(RGBLedpin[0], HIGH); // 기본 실행 시 블루투스 비연결상태임으로 RedLED_on 
}

void loop() {
  int state = digitalRead(statePIN); // 블루투스연결 상태 확인
  if(state == 1){ //연결 확인 시 RedLED_off GreenLED_on
    digitalWrite(RGBLedpin[0], LOW);
    digitalWrite(RGBLedpin[1], HIGH);
  }
  else{ //연결 해지 시 RedLED_on GreenLED_off
    digitalWrite(RGBLedpin[0], HIGH);
    digitalWrite(RGBLedpin[1], LOW);
  }
  sum = 0;
  for (int i = 0; i < sensor_count; i++) {
    if (analogRead(FSRsensor[i]) >= limit_value) senser_input_state = true;
  }
  if(senser_input_state == 1){
    FSRsensor_check();
    senser_input_state = false;
  }
  
  delay(1000);
}

void FSRsensor_check() { 
  value = "";
  for (int i = 0; i < sensor_count; i++) {
    if (analogRead(FSRsensor[i]) >= limit_value) {
      value += "1";
    } else {
      value += "0";
    }
  }
  Serial.print(value);
  BTSerial.print(value);
  Serial.println();
}