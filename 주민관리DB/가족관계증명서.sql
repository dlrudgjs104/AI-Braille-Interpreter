

#가족관계 
INSERT INTO family_relationship 
(relationship_id,relationship_name)
VALUES ('본인'),
       ('부'),
       ('모'),
       ('배우자'),
       ('자녀');
       

# 가족관계묶음 입력 
INSERT INTO family_relation_merge
(relative_subject_id,relative_object_id,relationship_id)
VALUES 	(1,1,1),
		(1,2,2),
		(1,3,3),
		(1,4,4),
		(1,5,5);

# 증명서 코드
INSERT INTO certificate_code
(certificate_code_name)
VALUES 	('가족관계증명서'),
		('주민등록등본'),
        ('출생신고서'),
        ('사망신고');

#증명서 발급
INSERT INTO certificate_print
    (issue_date, citizen_id, certificate_code_id)
VALUES ('2021-10-25 19:26', 1, 1);

#가족관계증명서 위 부분
SELECT 	certificate_print.issue_date AS '발급일',
		certificate_print. certificate_print_id AS '증명서확인번호',
        certificate_code.certificate_code_name AS '증명서서식명',
        address.address_name AS '등록기준지 (본적)'
FROM citizen
JOIN certificate_print ON citizen.citizen_id = certificate_print.citizen_id
JOIN certificate_code ON certificate_code.certificate_code_id = certificate_print.certificate_code_id
JOIN address ON citizen.address_id = address.address_id
WHERE certificate_print.citizen_id = 1;

#가족관계증명서 아래 부분 
SELECT 	family_relationship.relationship_name AS '구분',
		SUBSTRING(AES_DECRYPT(unhex(citizen_reg_num), 'p@ssW@rd'), 1, 6) AS 주민등록번호,
		citizen.citizen_name AS '성명',
        citizen.citizen_birth AS '출생연월일',
        citizen.citizen_gender AS '성별'
FROM citizen 
JOIN family_relation_merge ON citizen.citizen_id = family_relation_merge.relative_object_id
JOIN family_relationship ON family_relationship.family_relationship_id = family_relation_merge.relationship_id
WHERE family_relation_merge.relative_subject_id = 1;












