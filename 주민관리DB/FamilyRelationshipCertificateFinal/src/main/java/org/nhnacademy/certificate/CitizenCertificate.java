package org.nhnacademy.certificate;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.nhnacademy.controller.DatabaseController;

import lombok.Data;

@Data
public class CitizenCertificate {
    @Data
    private class AddressHistory {
        private String addressName;
        private String reportDate;
    }
    
    @Data
    private class Family {
        private String relationName;
        private String citizenName;
        private String citizenRegNum;
        private String reportDate;
        private String changeReasonName;
    }

    private int id;
    private DatabaseController controller;
    private String certificateCodeName;
    private String issueDate;
    private String certificatePrintId;
    private String citizenName;
    private String changeReasonName;
    private String reportDate;
    private List<AddressHistory> addressList;
    private List<Family> familyList;


    public CitizenCertificate(int id, DatabaseController controller) {
        this.id = id;
        this.controller = controller;
        setAddressList();
        setFamilyList();
        setCertificateDetails();
    }

    private void setAddressList() {
        addressList = new ArrayList<>();
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            String query = "SELECT address_name, report_date FROM citizen AS C " +
                           "INNER JOIN trans_address_record AS TAR ON C.citizen_id = TAR.citizen_id " +
                           "INNER JOIN address AS A ON A.address_id = TAR.address_id " +
                           "WHERE TAR.citizen_id = (SELECT H.citizen_id FROM household AS H " +
                           "INNER JOIN citizen AS C2 ON H.address_id = C2.address_id WHERE C2.citizen_id = " + id + ") " +
                           "ORDER BY report_date DESC;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                AddressHistory history = new AddressHistory();
                history.setAddressName(rs.getString("address_name"));
                history.setReportDate(rs.getString("report_date"));
                addressList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setFamilyList() {
        familyList = new ArrayList<>();
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            String query = "SELECT relation_name, citizen_name, SUBSTRING(AES_DECRYPT(unhex(citizen_reg_num), \'p@ssW@rd\'), 1, 6) AS 주민등록번호, report_date, change_reason_name " +
                           "FROM citizen AS C " +
                           "INNER JOIN household_member AS HM ON C.citizen_id = HM.citizen_id " +
                           "INNER JOIN household_change_reason AS HCR ON HM.change_reason_id = HCR.change_reason_id " +
                           "INNER JOIN household_relation AS HR ON HM.relation_id = HR.household_relation_id " +
                           "WHERE C.address_id IN (SELECT address_id FROM citizen AS C2 WHERE citizen_id = " + id + ") " +
                           "ORDER BY report_date;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Family family = new Family();
                family.setRelationName(rs.getString("relation_name"));
                family.setCitizenName(rs.getString("citizen_name"));
                family.setCitizenRegNum(rs.getString("주민등록번호"));
                family.setReportDate(rs.getString("report_date"));
                family.setChangeReasonName(rs.getString("change_reason_name"));
                familyList.add(family);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCertificateDetails() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            String query = "SELECT certificate_code_name, DATE(issue_date), " +
                           "CONCAT(SUBSTR(LPAD(certificate_print_id, 16, '0'), 1, 8), '-', SUBSTR(LPAD(certificate_print_id, 16, '0'), 9, 16)) " +
                           "FROM certificate_print AS A " +
                           "INNER JOIN certificate_code AS B ON A.certificate_code_id = B.certificate_code_id " +
                           "INNER JOIN citizen AS C ON A.citizen_id = C.citizen_id " +
                           "WHERE A.citizen_id = " + id + " " +
                           "AND certificate_code_name = \'주민등록등본\' " +
                           "ORDER BY issue_date DESC " +
                           "LIMIT 1;";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                certificateCodeName = rs.getString(1);
                issueDate = rs.getString(2);
                certificatePrintId = rs.getString(3);
            }
            query = "select C.citizen_name, change_reason_name as 세대구성사유,DATE(report_date) as 세대구성일자 " +
                    "from citizen as C " +
                    "inner join household as H on C.address_id = H.address_id " +
                    "inner join household_member as HM on C.citizen_id = HM.citizen_id " +
                    "inner join household_change_reason as HCR on HM.change_reason_id = HCR.change_reason_id " +
                    "where C.citizen_id=( " +
                    "select H.citizen_id " +
                    "from household as H " +
                    "inner join citizen as C2 on H.address_id = C2.address_id " +
                    "where C2.citizen_id = " + id + ");";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                citizenName = rs.getString(1);
                changeReasonName = rs.getString(2);
                reportDate = rs.getString(3);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Citizen Certificate Information:\n");
        builder.append("Certificate Code Name: ").append(certificateCodeName).append("\n");
        builder.append("Issue Date: ").append(issueDate).append("\n");
        builder.append("Certificate Print ID: ").append(certificatePrintId).append("\n");
        builder.append("Citizen Name: ").append(citizenName).append("\n");
        builder.append("Change Reason Name: ").append(changeReasonName).append("\n");
        builder.append("Report Date: ").append(reportDate).append("\n");
        builder.append("Address History:\n");
        for (AddressHistory address : addressList) {
            builder.append("\t- Address Name: ").append(address.getAddressName()).append(", Report Date: ").append(address.getReportDate()).append("\n");
        }
        builder.append("Family List:\n");
        for (Family family : familyList) {
            builder.append("\t- Relation Name: ").append(family.getRelationName())
                   .append(", Citizen Name: ").append(family.getCitizenName())
                   .append(", Citizen Reg Num: ").append(family.getCitizenRegNum())
                   .append(", Report Date: ").append(family.getReportDate())
                   .append(", Change Reason Name: ").append(family.getChangeReasonName()).append("\n");
        }
        return builder.toString();
    }
}
