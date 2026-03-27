package com.example.aop.Service;
import com.example.aop.Annotations.RequiresRole;
import org.springframework.stereotype.Service;

@Service
public class SmartLockService {

    @RequiresRole("Guest")
    public void unlock(String user){
        System.out.println("The door is now open for user "+ user);
    }
}
