package com.example.jpa.Controller;

import com.example.jpa.Repository.ShiftRepository;
import com.example.jpa.Repository.ShiftTypeRepository;
import com.example.jpa.Repository.ShiftUserRepository;
import com.example.jpa.Repository.UserRepository;
import com.example.jpa.entity.Shift;
import com.example.jpa.entity.ShiftType;
import com.example.jpa.entity.ShiftUser;
import com.example.jpa.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CrudController {

    private final ShiftTypeRepository shiftTypeRepo;
    private final ShiftRepository shiftRepo;
    private final UserRepository userRepo;
    private final ShiftUserRepository shiftUserRepo;

    public CrudController(
            ShiftTypeRepository shiftTypeRepo,
            ShiftRepository shiftRepo,
            UserRepository userRepo,
            ShiftUserRepository shiftUserRepo) {
        this.shiftTypeRepo = shiftTypeRepo;
        this.shiftRepo = shiftRepo;
        this.userRepo = userRepo;
        this.shiftUserRepo = shiftUserRepo;
    }

    @PostMapping("/shift-types")
    public ShiftType saveShiftType(@RequestBody ShiftType st) {
        return shiftTypeRepo.save(st);
    }

    @PostMapping("/shifts")
    public Shift saveShift(@RequestBody Shift s) {
        return shiftRepo.save(s);
    }

    @PostMapping("/users")
    public User saveUser(@RequestBody User u) {
        return userRepo.save(u);
    }

    @PostMapping("/shift-users")
    public ShiftUser saveShiftUser(@RequestBody ShiftUser su) {
        return shiftUserRepo.save(su);
    }
}
