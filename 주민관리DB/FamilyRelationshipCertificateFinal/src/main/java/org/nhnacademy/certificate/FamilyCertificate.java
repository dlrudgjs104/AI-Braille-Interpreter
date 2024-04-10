package org.nhnacademy.certificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import org.nhnacademy.controller.DatabaseController;

import lombok.Data;

@Data
public class FamilyCertificate {
    @Data
    private class Family {
        private String relationName;
        private String citizenName;
        private String citizenRegNum;
        private String reportDate;
        private String gender;
    }

    private int id;
    private DatabaseController controller;
    private String issueDate;
    private String certificatePrintId;
    private String certificateCodeName;
    private String address;
    private List<Family> familyList;

    public FamilyCertificate(int id, DatabaseController controller) {
        this.id = id;
        this.controller = controller;
        setCertificateDetails();
        setFamilyList();
    }

    private void setCertificateDetails() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            String query = "SELECT certificate_print.issue_date, " +
                                    "CONCAT(SUBSTR(LPAD(certificate_print.certificate_print_id, 16, '0'), 1, 8), '-', SUBSTR(LPAD(certificate_print.certificate_print_id, 16, '0'), 9, 16)) AS `certificate_print_id`, " +
                                    "certificate_code.certificate_code_name, " +
                                    "address.address_name " +
                           "FROM citizen " +
                           "JOIN certificate_print ON citizen.citizen_id = certificate_print.citizen_id " +
                           "JOIN certificate_code ON certificate_code.certificate_code_id = certificate_print.certificate_code_id " +
                           "JOIN address ON citizen.address_id = address.address_id " +
                           "WHERE certificate_print.citizen_id = " + id + " AND certificate_code.certificate_code_name = \'가족관계증명서\' " +
                           "ORDER BY issue_date DESC " +
                           "LIMIT 1;";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                issueDate = rs.getString("issue_date");
                certificatePrintId = rs.getString("certificate_print_id");
                certificateCodeName = rs.getString("certificate_code_name");
                address = rs.getString("address_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setFamilyList() {
        familyList = new ArrayList<>();
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            String query = "SELECT family_relationship.relationship_name AS relationName, " +
                           "SUBSTRING(AES_DECRYPT(unhex(citizen_reg_num), 'p@ssW@rd'), 1, 6) AS citizenRegNum, " +
                           "citizen.citizen_name AS citizenName, " +
                           "citizen.citizen_birth AS citizen_birth, " +
                           "citizen.citizen_gender AS gender " +
                           "FROM citizen " +
                           "JOIN family_relation_merge ON citizen.citizen_id = family_relation_merge.relative_object_id " +
                           "JOIN family_relationship ON family_relationship.family_relationship_id = family_relation_merge.relationship_id " +
                           "WHERE family_relation_merge.relative_subject_id = " + id + ";";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Family family = new Family();
                family.setRelationName(rs.getString("relationName"));
                family.setCitizenName(rs.getString("citizenName"));
                family.setCitizenRegNum(rs.getString("citizenRegNum"));
                family.setReportDate(rs.getString("citizen_birth"));
                family.setGender(rs.getString("gender"));
                familyList.add(family);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FamilyCertificate Details:").append("\n");
        sb.append("Issue Date: ").append(issueDate).append("\n");
        sb.append("Certificate Print ID: ").append(certificatePrintId).append("\n");
        sb.append("Certificate Code Name: ").append(certificateCodeName).append("\n");
        sb.append("Address: ").append(address).append("\n");

        sb.append("Family List:").append("\n");
        for (Family family : familyList) {
            sb.append("Relation Name: ").append(family.getRelationName()).append("\n");
            sb.append("Citizen Name: ").append(family.getCitizenName()).append("\n");
            sb.append("Citizen Registration Number: ").append(family.getCitizenRegNum()).append("\n");
            sb.append("Report Date: ").append(family.getReportDate()).append("\n");
            sb.append("Gender: ").append(family.getGender()).append("\n");
            sb.append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}