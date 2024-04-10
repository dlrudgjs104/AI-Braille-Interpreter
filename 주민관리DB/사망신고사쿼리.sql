INSERT INTO household_relation
VALUES ('본인'),
       ('부'),
       ('모'),
       ('배우자'),
       ('자녀');


INSERT INTO place_dead
    (`place_dead_division`)
VALUES ('주택'),
       ('의료기관'),
       ('사회복지시설(양로원, 고아원 등)'),
       ('산업장'),
       ('공공시설(학교, 운동장 등)'),
       ('도로'),
       ('상업/서비스시설(상점, 호텔 등'),
       ('농장(논밭, 축사, 양식장 등)'),
       ('병원 이송 중 사망'),
       ('기타');


# 사망 신고서 작성
INSERT INTO dead_registration
(person_dead_id, email, phone_number, dead_date, reporter_Qualifications, report_date, citizen_id, place_dead_id,
 address_id)
VALUES (7, NULL, '010-2345-6789', '2021-04-29 09:03', '비동거친족', '2020-05-02', 7, 2, 4);


#업데이트 국민 생존 -> 사망


#증명서발급
INSERT INTO certificate_print
    (issue_date, citizen_id, certificate_code_id)
VALUES ('2024-04-10 17:22', 1, 1);


#사망신고서
#조회대상 id = 7

#1. 신고일
SELECT report_date
FROM dead_registration
WHERE dead_registration.person_dead_id = 7
;


#2. 이름
SELECT citizen_name
FROM dead_registration
         JOIN citizen c on c.citizen_id = dead_registration.citizen_id
WHERE dead_registration.person_dead_id = 7
;

#3. 주민등록번호
SELECT SUBSTRING(AES_DECRYPT(unhex(citizen_reg_num), 'p@ssW@rd'), 1, 6) AS 주민등록번호
FROM dead_registration
         JOIN citizen c on c.citizen_id = dead_registration.citizen_id
WHERE dead_registration.person_dead_id = 7
;


#4. 사망일시
SELECT dead_date
FROM dead_registration
         JOIN citizen c on c.citizen_id = dead_registration.citizen_id
WHERE dead_registration.person_dead_id = 7
;

#5. 사망장소
SELECT pd.`place_dead_division`
FROM dead_registration
         JOIN place_dead pd on dead_registration.place_dead_id = pd.place_dead_id
WHERE dead_registration.person_dead_id = 7
;

#6 사망장소 주소
SELECT a.address_name
FROM dead_registration
         JOIN address a on dead_registration.address_id = a.address_id
WHERE dead_registration.person_dead_id = 7
;

#7.신고인 성명
SELECT c.citizen_name
FROM dead_registration
         JOIN citizen c on dead_registration.citizen_id = c.citizen_id
WHERE dead_registration.person_dead_id = 7
;

#8. 신고인 주민등록번호
SELECT SUBSTRING(AES_DECRYPT(unhex(c.citizen_reg_num), 'p@ssW@rd'), 1, 6) AS 주민등록번호
FROM dead_registration
         JOIN citizen c on dead_registration.citizen_id = c.citizen_id
WHERE dead_registration.person_dead_id = 7
;

#9.신고인 자격
SELECT reporter_Qualifications
FROM dead_registration
WHERE dead_registration.person_dead_id = 7
;


#10. 신고자 이메일
SELECT email
FROM dead_registration
WHERE dead_registration.person_dead_id = 7
;


#11. 신고자 전화번호
SELECT phone_number
FROM dead_registration
WHERE dead_registration.person_dead_id = 7
;



