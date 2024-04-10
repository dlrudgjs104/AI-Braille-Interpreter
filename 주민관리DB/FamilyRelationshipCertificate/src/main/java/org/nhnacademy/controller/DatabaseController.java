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

public class DatabaseController {
    private Connector connector;

    public DatabaseController() {
        connector = new Connector("jdbc:mysql://localhost:3306/family_relationship_certificate_db", "root", "1q2w3e4r!");
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
        connector.executeQuery(Address.addData("경기도 성남시 분당구 대왕판교로 645번길"));
        connector.executeQuery(Address.addData("경기도 성남시 분당구 불정로 90번길"));
        connector.executeQuery(Address.addData("서울시 동작구 상도로 940번길"));
        connector.executeQuery(Address.addData("강원도 고성군 금강산로 290번길"));
        
        connector.executeQuery(PlaceBirth.addData("병원"));
        connector.executeQuery(PlaceBirth.addData("자택"));
        connector.executeQuery(PlaceBirth.addData("기타"));

        connector.executeQuery(State.addData("사망"));
        connector.executeQuery(State.addData("생존"));
        connector.executeQuery(State.addData("기타"));

        connector.executeQuery(Citizen.addData("790510-1234567", "남기준", '남', "1975-05-10", 1, 2));
        connector.executeQuery(Citizen.addData("540514-1234568", "남석환", '남', "1954-05-14", 2, 2));
        connector.executeQuery(Citizen.addData("551022-1234569", "박한나", '여', "1955-10-22", 2, 2));
        connector.executeQuery(Citizen.addData("820821-1234570", "이주은", '여', "1982-08-21", 1, 2));
        connector.executeQuery(Citizen.addData("120315-1234571", "남기석", '남', "2012-03-15", 1, 2));
        connector.executeQuery(Citizen.addData("851205-1234572", "이선미", '여', "1985-12-05", 1, 2));
        connector.executeQuery(Citizen.addData("130914-1234573", "남길동", '남', "1913-09-14", 4, 1));
    }
}
