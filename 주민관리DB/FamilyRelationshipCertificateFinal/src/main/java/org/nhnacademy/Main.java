package org.nhnacademy;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nhnacademy.certificate.BirthCertificate;
import org.nhnacademy.certificate.CitizenCertificate;
import org.nhnacademy.certificate.DeadCertificate;
import org.nhnacademy.certificate.FamilyCertificate;
import org.nhnacademy.controller.DatabaseController;

public class Main {
    public static void main(String[] args) {
        String deadCertificateString = "------------------------------사망신고서------------------------------\n";
        String birthCertificateString = "------------------------------출생신고서------------------------------\n";
        String citizenCertificateString = "------------------------------주민등록등본------------------------------\n";
        String familyCertificateString = "------------------------------가족관계 증명서------------------------------\n";

        Logger logger = LogManager.getLogger();
        DatabaseController databaseController = new DatabaseController();

        databaseController.buildTable();
        databaseController.makeData();

        logger.log(Level.INFO, deadCertificateString + new DeadCertificate(7, databaseController));
        logger.log(Level.INFO, citizenCertificateString + new CitizenCertificate(1, databaseController));
        logger.log(Level.INFO, familyCertificateString + new FamilyCertificate(1, databaseController));
        logger.log(Level.INFO, birthCertificateString + new BirthCertificate(5, databaseController));
    }
}