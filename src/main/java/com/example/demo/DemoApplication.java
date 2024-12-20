package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {
    private static final String EXPIRED_DATE = "19-12-2024 13:37:00";
    private static boolean isLicenseExpired = false;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @PostConstruct
    public void checkLicenseOnStartup() {
        try {
            checkLicense();
            System.out.println("License is valid. Application is starting...");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void checkLicense() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime expirationDate = LocalDateTime.parse(EXPIRED_DATE, formatter);
        LocalDateTime currentDate = LocalDateTime.now();
        if (currentDate.isAfter(expirationDate)) {
            throw new RuntimeException("License expired on " + EXPIRED_DATE + ". Please renew your license.");
        }
    }

    @Scheduled(cron = "0 */4 * * * *")
    public void scheduledLicenseCheck() {
        System.out.println("Checking License...");
        try {
            checkLicense();
            isLicenseExpired = false;
        } catch (RuntimeException e) {
            isLicenseExpired = true;
            System.err.println(e.getMessage());
        }
    }

    public static boolean isLicenseExpired() {
        return isLicenseExpired;
    }
}
