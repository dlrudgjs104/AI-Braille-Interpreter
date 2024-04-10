package org.nhnacademy.certificate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.nhnacademy.controller.DatabaseController;

import lombok.Data;

@Data
public class DeadCertificate {
    private int deadCitizenId;
    private DatabaseController controller;
    private String reportDate;
    private String deadCitizenName;
    private String deadCitizenRegNum;
    private String deadDate;
    private String placeDeadDivision;
    private String addressName;
    private String reportCitizenName;
    private String reportCitizenRegNum;
    private String reporterQualifications;
    private String email;
    private String phoneNumber;

    public DeadCertificate(int deadCitizenId, DatabaseController controller) {
        this.deadCitizenId = deadCitizenId;
        this.controller = controller;
        setReportDate();
        setDeadCitizenName();
        setDeadCitizenRegNum();
        setDeadDate();
        setplaceDeadDivision();
        setAddressName();
        setReportCitizenName();
        setReportCitizenRegNum();
        setReporterQualifications();
        setEmail();
        setPhoneNumber();
    }

    private void setReportDate() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery(getReposrtDateQuery());
            while (rs.next()) {
                reportDate = rs.getString("report_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDeadCitizenName() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery(getDeadCitizenNameQuery());
            while (rs.next()) {
                deadCitizenName = rs.getString("citizen_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDeadCitizenRegNum() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery(getDeadCitizenRegNumQuery());
            while (rs.next()) {
                deadCitizenRegNum = rs.getString("주민등록번호");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDeadDate() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery(getDeadDateQuery());
            while (rs.next()) {
                deadDate = rs.getString("dead_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setplaceDeadDivision() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery(getPlaceDeadDivisionQuery());
            while (rs.next()) {
                placeDeadDivision = rs.getString("place_dead_division");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setAddressName() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT a.address_name FROM dead_registration JOIN address a on dead_registration.address_id = a.address_id WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
            if (rs.next()) {
                addressName = rs.getString("address_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setReportCitizenName() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT c.citizen_name FROM dead_registration JOIN citizen c on dead_registration.citizen_id = c.citizen_id WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
            if (rs.next()) {
                reportCitizenName = rs.getString("citizen_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setReportCitizenRegNum() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT SUBSTRING(AES_DECRYPT(unhex(c.citizen_reg_num), 'p@ssW@rd'), 1, 6) AS 주민등록번호 FROM dead_registration JOIN citizen c on dead_registration.citizen_id = c.citizen_id WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
            if (rs.next()) {
                reportCitizenRegNum = rs.getString("주민등록번호");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setReporterQualifications() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT reporter_Qualifications FROM dead_registration WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
            if (rs.next()) {
                reporterQualifications = rs.getString("reporter_Qualifications");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setEmail() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT email FROM dead_registration WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPhoneNumber() {
        try (Statement stmt = controller.getConnector().getConn().createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT phone_number FROM dead_registration WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
            if (rs.next()) {
                phoneNumber = rs.getString("phone_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getReposrtDateQuery() {
        return ("SELECT report_date FROM dead_registration WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
    }

    private String getDeadCitizenNameQuery() {
        return ("SELECT citizen_name FROM dead_registration JOIN citizen c on c.citizen_id = dead_registration.person_dead_id WHERE dead_registration.person_dead_id =" + deadCitizenId + ";");
    }

    private String getDeadCitizenRegNumQuery() {
        return ("SELECT SUBSTRING(AES_DECRYPT(unhex(citizen_reg_num), \'p@ssW@rd\'), 1, 6) AS 주민등록번호 FROM dead_registration JOIN citizen c on c.citizen_id = dead_registration.person_dead_id WHERE dead_registration.person_dead_id =" + deadCitizenId + ";");
    }

    private String getDeadDateQuery() {
        return ("SELECT dead_date FROM dead_registration JOIN citizen c on c.citizen_id = dead_registration.citizen_id WHERE dead_registration.person_dead_id = " + deadCitizenId + ";");
    }

    private String getPlaceDeadDivisionQuery() {
        return ("SELECT pd.`place_dead_division` FROM dead_registration JOIN place_dead pd on dead_registration.place_dead_id = pd.place_dead_id WHERE dead_registration.person_dead_id =" + deadCitizenId + ";");
    }

    @Override
    public String toString() {
        return "DeadCertificate Details: \n" +
                "Report Date: " + reportDate + "\n" +
                "Dead Citizen Name: " + deadCitizenName + "\n" +
                "Dead Citizen Registration Number: " + deadCitizenRegNum + "\n" +
                "Dead Date: " + deadDate + "\n" +
                "Place Dead Division: " + placeDeadDivision + "\n" +
                "Address Name: " + addressName + "\n" +
                "Report Citizen Name: " + reportCitizenName + "\n" +
                "Report Citizen Registration Number: " + reportCitizenRegNum + "\n" +
                "Reporter Qualifications: " + reporterQualifications + "\n" +
                "Email: " + email + "\n" +
                "Phone Number: " + phoneNumber + "\n";
    }
}
