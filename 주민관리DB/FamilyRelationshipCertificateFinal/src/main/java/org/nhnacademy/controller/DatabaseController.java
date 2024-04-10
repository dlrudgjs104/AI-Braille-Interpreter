package org.nhnacademy.controller;

import org.nhnacademy.database.Connector;
import org.nhnacademy.query.Address;
import org.nhnacademy.query.BirthRegistration;
import org.nhnacademy.query.CertificateCode;
import org.nhnacademy.query.CertificatePrint;
import org.nhnacademy.query.Citizen;
import org.nhnacademy.query.DeadRegistration;
import org.nhnacademy.query.FamilyRelationMerge;
import org.nhnacademy.query.FamilyRelationship;
import org.nhnacademy.query.Household;
import org.nhnacademy.query.HouseholdChangeReason;
import org.nhnacademy.query.HouseholdMember;
import org.nhnacademy.query.HouseholdRelation;
import org.nhnacademy.query.PlaceBirth;
import org.nhnacademy.query.PlaceDead;
import org.nhnacademy.query.State;
import org.nhnacademy.query.TransAddressRecord;

import lombok.Getter;

@Getter
public class DatabaseController {
    private Connector connector;

    public DatabaseController() {
        connector = new Connector("jdbc:mysql://localhost:3306/family_relationship_certificate_db", "root", "0319");
    }

    public void buildTable() {
        dropTable();
        createTable();
    }

    public void  dropTable() {
        connector.executeQuery(CertificatePrint.dropTable());
        connector.executeQuery(CertificateCode.dropTable());
        connector.executeQuery(FamilyRelationMerge.dropTable());
        connector.executeQuery(FamilyRelationship.dropTable());
        connector.executeQuery(HouseholdMember.dropTable());
        connector.executeQuery(HouseholdRelation.dropTable());
        connector.executeQuery(HouseholdChangeReason.dropTable());
        connector.executeQuery(Household.dropTable());
        connector.executeQuery(TransAddressRecord.dropTable());
        connector.executeQuery(DeadRegistration.dropTable());
        connector.executeQuery(PlaceDead.dropTable());
        connector.executeQuery(BirthRegistration.dropTable());
        connector.executeQuery(Citizen.dropTable());
        connector.executeQuery(State.dropTable());
        connector.executeQuery(Address.dropTable());
        connector.executeQuery(PlaceBirth.dropTable());
    }

    public void createTable() {
        connector.executeQuery(PlaceBirth.createTable());
        connector.executeQuery(Address.createTable());
        connector.executeQuery(State.createTable());
        connector.executeQuery(Citizen.createTable());
        connector.executeQuery(BirthRegistration.createTable());
        connector.executeQuery(PlaceDead.createTable());
        connector.executeQuery(DeadRegistration.createTable());
        connector.executeQuery(TransAddressRecord.createTable());
        connector.executeQuery(Household.createTable());
        connector.executeQuery(HouseholdChangeReason.createTable());
        connector.executeQuery(HouseholdRelation.createTable());
        connector.executeQuery(HouseholdMember.createTable());
        connector.executeQuery(FamilyRelationship.createTable());
        connector.executeQuery(FamilyRelationMerge.createTable());
        connector.executeQuery(CertificateCode.createTable());
        connector.executeQuery(CertificatePrint.createTable());
    }

    public void makeData() {
        connector.executeQuery(Address.insertData("경기도 성남시 분당구 대왕판교로 645번길"));
        connector.executeQuery(Address.insertData("경기도 성남시 분당구 불정로 90번길"));
        connector.executeQuery(Address.insertData("서울시 동작구 상도로 940번길"));
        connector.executeQuery(Address.insertData("강원도 고성군 금강산로 290번길"));
        
        connector.executeQuery(PlaceBirth.insertData("병원"));
        connector.executeQuery(PlaceBirth.insertData("자택"));
        connector.executeQuery(PlaceBirth.insertData("기타"));

        connector.executeQuery(State.insertData("사망"));
        connector.executeQuery(State.insertData("생존"));
        connector.executeQuery(State.insertData("기타"));
        
        connector.executeQuery(CertificateCode.insertData("사망신고서"));
        connector.executeQuery(CertificateCode.insertData("주민등록등본"));
        connector.executeQuery(CertificateCode.insertData("가족관계증명서"));
        connector.executeQuery(CertificateCode.insertData("출생신고서"));

        connector.executeQuery(Citizen.insertData("790510-1234567", "남기준", '남', "1975-05-10", 1, 2));
        connector.executeQuery(Citizen.insertData("540514-1234568", "남석환", '남', "1954-05-14", 2, 2));
        connector.executeQuery(Citizen.insertData("551022-1234569", "박한나", '여', "1955-10-22", 2, 2));
        connector.executeQuery(Citizen.insertData("820821-1234570", "이주은", '여', "1982-08-21", 1, 2));
        connector.executeQuery(Citizen.insertData("120315-1234571", "남기석", '남', "2012-03-15", 1, 2));
        connector.executeQuery(Citizen.insertData("851205-1234572", "이선미", '여', "1985-12-05", 1, 2));
        connector.executeQuery(Citizen.insertData("130914-1234573", "남길동", '남', "1913-09-14", 4, 1));

        connector.executeQuery(HouseholdRelation.insertData("본인"));
        connector.executeQuery(HouseholdRelation.insertData("부"));
        connector.executeQuery(HouseholdRelation.insertData("모"));
        connector.executeQuery(HouseholdRelation.insertData("배우자"));
        connector.executeQuery(HouseholdRelation.insertData("자녀"));
        connector.executeQuery(HouseholdRelation.insertData("동거인"));

        connector.executeQuery(PlaceDead.insertData("주택"));
        connector.executeQuery(PlaceDead.insertData("의료기관"));
        connector.executeQuery(PlaceDead.insertData("사회복지시설(양로원, 고아원 등)"));
        connector.executeQuery(PlaceDead.insertData("산업장"));
        connector.executeQuery(PlaceDead.insertData("공공시설(학교, 운동장 등)"));
        connector.executeQuery(PlaceDead.insertData("도로"));
        connector.executeQuery(PlaceDead.insertData("상업/서비스시설(상점, 호텔 등"));
        connector.executeQuery(PlaceDead.insertData("농장(논밭, 축사, 양식장 등)"));
        connector.executeQuery(PlaceDead.insertData("병원 이송 중 사망"));
        connector.executeQuery(PlaceDead.insertData("기타"));

        connector.executeQuery(FamilyRelationship.insertData("본인"));
        connector.executeQuery(FamilyRelationship.insertData("부"));
        connector.executeQuery(FamilyRelationship.insertData("모"));
        connector.executeQuery(FamilyRelationship.insertData("배우자"));
        connector.executeQuery(FamilyRelationship.insertData("자녀"));

        connector.executeQuery(HouseholdChangeReason.insertData("세대분리"));
        connector.executeQuery(HouseholdChangeReason.insertData("전입"));
        connector.executeQuery(HouseholdChangeReason.insertData("출생등록"));

        connector.executeQuery(DeadRegistration.insertData(7, null, "010-2345-6789", "2021-04-29 09:03", "비동거친족", "2020-05-02", 2, 2, 4));

        connector.executeQuery(FamilyRelationMerge.insertData(1,1,1));
        connector.executeQuery(FamilyRelationMerge.insertData(1,2,2));
        connector.executeQuery(FamilyRelationMerge.insertData(1,3,3));
        connector.executeQuery(FamilyRelationMerge.insertData(1,4,4));
        connector.executeQuery(FamilyRelationMerge.insertData(1,5,5));

        connector.executeQuery(TransAddressRecord.insertData("2013-03-05 14:00:00", 1, 1));
        connector.executeQuery(TransAddressRecord.insertData("2009-10-31 14:00:00", 1, 2));
        connector.executeQuery(TransAddressRecord.insertData("2007-10-31 14:00:00", 1, 3));

        connector.executeQuery(Household.insertData(1,1,1));

        connector.executeQuery(HouseholdMember.insertData(1, 1, "2009-10-02 00:00:00", 1,  1));
        connector.executeQuery(HouseholdMember.insertData(1, 4, "2010-02-15 00:00:00", 2,  4));
        connector.executeQuery(HouseholdMember.insertData(1, 5, "2012-03-17 00:00:00", 3,  5));
        connector.executeQuery(HouseholdMember.insertData(1, 6, "2015-11-29 00:00:00", 2,  6));

        connector.executeQuery(CertificatePrint.insertData("2024-04-10 17:22", 1, 1));
        connector.executeQuery(CertificatePrint.insertData("2024-10-25 17:22", 1, 2));
        connector.executeQuery(CertificatePrint.insertData("2024-10-25 17:22", 1, 3));

        connector.executeQuery(BirthRegistration.insertData(5, "nam@nhnad.co.kr", "010-1234-5678", "2012-03-15 14:59:00", "부", "2012-03-17 15:00:00", 1, 1, 1));
    }
}
