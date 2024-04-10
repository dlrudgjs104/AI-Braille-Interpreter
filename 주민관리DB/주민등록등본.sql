#1 개요.
select certificate_code_name as 발급서종류, DATE(issue_date) as 발급일자, concat(substr(lpad( certificate_print_id,16,'0'), 1, 8), '-', substr(lpad( certificate_print_id,16,'0'), 9, 16)) as 증명서확인번호
from certificate_print as A
inner join certificate_code as B on A.certificate_code_id = B.certificate_code_id
inner join citizen as C on A.citizen_id = C.citizen_id
where A.citizen_id = 1 -- 발급자 아이디 
and certificate_code_name = '주민등록등본'
Order by 발급일자 desc
Limit 1;


 #2 세대주의 이름과 세대구성사유, 세대구성일자.
select C.citizen_name, change_reason_name as 세대구성사유,DATE(report_date) as 세대구성일자
from citizen as C
inner join household as H on C.address_id = H.address_id
inner join household_member as HM on C.citizen_id = HM.citizen_id
inner join household_change_reason as HCR on HM.change_reason_id = HCR.change_reason_id
where C.citizen_id=(
select H.citizen_id
from household as H
inner join citizen as C2 on H.address_id = C2.address_id
where C2.citizen_id = 1); -- 발급자 아이디 


#3 발급자의 세대주 정보 보기.  -
select address_name as 주소, TAR.report_date as 신고일
from citizen as C
inner join trans_address_record as TAR on C.citizen_id = TAR.citizen_id
inner join address as A on A.address_id = TAR.address_id
where TAR.citizen_id = (
select H.citizen_id
from household as H
inner join citizen as C2 on H.address_id = C2.address_id
where C2.citizen_id = 1 -- 발급자 아이디 
)
order by report_date desc
limit 3;


# 4발급자와 같은 주소(같은 세대) 모든 사람 정보.
select relation_name as 세대주관계, citizen_name as 성명 , citizen_reg_num, DATE(report_date) as 신고일, change_reason_name as 변동사유
from citizen as C
inner join household_member as HM on C.citizen_id = HM.citizen_id
inner join household_change_reason as HCR on HM.change_reason_id = HCR.change_reason_id
inner join household_relation as HR on HM.relation_id = HR.household_relation_id
where C.address_id in(
select address_id
from citizen as C2
where citizen_id = 1 -- 발급자 아이디
)
order by report_date desc;
