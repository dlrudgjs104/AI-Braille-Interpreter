package org.nhnacademy.certificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.nhnacademy.controller.DatabaseController;

public class BirthCertificate {
    private int id;
    private DatabaseController controller;
    private String reportDate;
    private String citizenName;
    private String gender;
    private String birthDate;
    private String birthPlace;
    private String address;
    private String fatherName;
    private String fatherRegNum;
    private String motherName;
    private String motherRegNum;
    private String reporterName;
    private String reporterRegNum;
    private String reporterQualifications;
    private String email;
    private String phoneNumber;

    public BirthCertificate(int id, DatabaseController controller) {
        this.id = id;
        this.controller = controller;
        setBirthCertificateDetails();
    }

    private void setBirthCertificateDetails() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            String query = "SELECT * " +
                    "FROM birth_registration as br " +
                    "JOIN citizen AS c on br.person_birth_id = c.citizen_id " +
                    "JOIN place_birth as pb ON pb.place_birth_id = br.place_birth_id " +
                    "JOIN `address` as a ON c.address_id = a.address_id " +
                    "WHERE c.citizen_id = " + id + ";";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                reportDate = rs.getString("report_date");
                citizenName = rs.getString("citizen_name");
                gender = rs.getString("citizen_gender");
                birthDate = rs.getString("birth_date");
                birthPlace = rs.getString("place_name");
                address = rs.getString("address_name");
            }

            query = "SELECT frm1.relationship_id, c.citizen_name AS 이름, SUBSTRING(AES_DECRYPT(unhex(c.citizen_reg_num), 'p@ssW@rd'), 1, 6) AS 주민등록번호 " +
                    "FROM citizen c " +
                    "JOIN family_relation_merge frm1 ON c.citizen_id = frm1.relative_object_id " +
                    "JOIN family_relation_merge frm2 ON frm1.relative_subject_id = frm2.relative_subject_id " +
                    "WHERE frm2.relative_object_id = "+ id + " " +
                    "AND frm1.relationship_id IN (1, 3) ORDER BY frm1.relationship_id;";
            rs = stmt.executeQuery(query);
            for (int i = 0; rs.next(); ++i) {
                if (i == 0) {
                    fatherName = rs.getString("이름");
                    fatherRegNum = rs.getString("주민등록번호");
                } else {
                    motherName = rs.getString("이름");
                    motherRegNum = rs.getString("주민등록번호");
                }
            }

            query = "SELECT c.citizen_name, SUBSTRING(AES_DECRYPT(unhex(c.citizen_reg_num), 'p@ssW@rd'), 1, 6) AS 주민등록번호 " +
                    "FROM `citizen` as c " +
                    "WHERE c.citizen_id = (SELECT br.citizen_id FROM `birth_registration` as br WHERE br.person_birth_id = " + id + ");";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                reporterName = rs.getString("citizen_name");
                reporterRegNum = rs.getString("주민등록번호");
            }

            query = "SELECT * " +
                    "FROM `birth_registration` as br " +
                    "WHERE br.person_birth_id = " + id + ";";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                reporterQualifications = rs.getString("reporter_Qualifications");
                email = rs.getString("email");
                phoneNumber = rs.getString("phone_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "BirthCertificate Details:\n" +
                "Report Date: " + reportDate + "\n" +
                "Citizen Name: " + citizenName + "\n" +
                "Gender: " + gender + "\n" +
                "Birth Date: " + birthDate + "\n" +
                "Birth Place: " + birthPlace + "\n" +
                "Address: " + address + "\n" +
                "Father Name: " + fatherName + "\n" +
                "Father Registration Number: " + fatherRegNum + "\n" +
                "Mother Name: " + motherName + "\n" +
                "Mother Registration Number: " + motherRegNum + "\n" +
                "Reporter Name: " + reporterName + "\n" +
                "Reporter Registration Number: " + reporterRegNum + "\n" +
                "Reporter Qualifications: " + reporterQualifications + "\n" +
                "Email: " + email + "\n" +
                "Phone Number: " + phoneNumber;
    }
}