-- 시민으로 추가
-- INSERT INTO citizen VALUES (8, '120317-3214567', '남기석', '남', '2021-03-17', 1, 2);

-- 가족 관계 추가
-- INSERT INTO family_relationship VALUES(1, '본인'), (2, '부'), (3, '모'), (4, '자녀');

-- 출생신고자를 기준으로 가족 관계 추가 
-- INSERT INTO family_relation_merge VALUES(8, 8, 1), (8, 1, 2), (8, 3, 3);

-- 출생신고서
-- INSERT INTO birth_registration VALUES(1, 'nam@nhnad.co.kr', '010-1234-5678', '2012-03-15 14:59:00', '부', '2012-03-17 15:00:', 8, 1, 1);

-- 출생자
-- SELECT br.report_date AS 출생신고서_신고일 
-- , c.citizen_name AS 성명, c.citizen_gender AS 성별
-- , br.birth_date AS 출생일시
-- , pb.place_name AS 출생장소
-- , a.address_name AS 등록기준지_본적

-- FROM birth_registration AS br
-- JOIN citizen AS c ON c.citizen_id = br.citizen_id
-- JOIN place_birth AS pb ON pb.place_birth_id = br.place_birth_id
-- JOIN address AS a ON a.address_id = br.address_id
-- WHERE citizen_name = '남기석'

-- 부모
-- SELECT c2.citizen_name AS 부_성명, c2.citizen_reg_num AS 주민등록번호
-- , c3.citizen_name AS 모_성명, c3.citizen_reg_num AS 주민등록번호

-- FROM citizen AS c1
-- JOIN family_relation_merge AS frm ON frm.relative_subject_id = c1.citizen_id
-- JOIN family_relationship AS fr ON frm.relationship_id = fr.family_relationship_id AND fr.relationship_name = '부'
-- JOIN citizen AS c2 ON frm.relative_object_id = c2.citizen_id

-- JOIN family_relation_merge AS frm2 ON frm2.relative_subject_id = c1.citizen_id
-- JOIN family_relationship AS fr2 ON frm2.relationship_id = fr2.family_relationship_id AND fr2.relationship_name = '모'
-- JOIN citizen AS c3 ON frm2.relative_object_id = c3.citizen_id
-- WHERE c1.citizen_name = '남기석';

-- 신고인
-- SELECT c2.citizen_name AS 성명, c2.citizen_reg_num AS 주민등록번호
-- , br.reporter_Qualifications AS 자격
-- , br.email AS 이메일, br.phone_number AS 전화번호
-- FROM birth_registration AS br
-- JOIN citizen AS c ON c.citizen_id = br.citizen_id
-- JOIN family_relation_merge AS frm ON frm.relative_subject_id = c.citizen_id
-- JOIN family_relationship AS fr ON fr.family_relationship_id = frm.relationship_id AND fr.relationship_name = br.reporter_Qualifications
-- JOIN citizen AS c2 ON frm.relative_object_id = c2.citizen_id
-- WHERE c.citizen_name = '남기석';


