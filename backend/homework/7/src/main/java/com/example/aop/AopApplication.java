package com.example.aop;
import com.example.aop.Exception.HardwareFailureException;
import com.example.aop.Service.BatteryService;
import com.example.aop.Service.SmartLockService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import java.util.Scanner;

@EnableAspectJAutoProxy
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }
    @Bean
    @Order(1)
    CommandLineRunner testLock(SmartLockService smartLockService) {
        return args -> {
            smartLockService.unlock("Alice");
        };
    }

    @Order(2)
    @Bean
    CommandLineRunner testBattery(BatteryService batteryService) {
        return args -> {
            batteryService.checkBattery();
        };
    }

    @Order(3)
    @Bean
    CommandLineRunner testAccess(SmartLockService smartLockService) {
        return args -> {
            Scanner sc=new Scanner(System.in);
            String user=sc.nextLine();
            try {
                smartLockService.unlock(user);
            } catch (HardwareFailureException e) {
                System.out.println("SYSTEM ERROR: " + e.getMessage());
            }
        };
    }

}
