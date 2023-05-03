package com.daofab.restapi;

import com.daofab.restapi.dto.Child;
import com.daofab.restapi.dto.Parent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @GetMapping("/parent")
    public List<Parent> getParentTransactions() {
        System.out.println("working");
        return null;
    }

    @GetMapping("/child/{parentId}")
    public List<Child> getChildTransactions(@PathVariable Long parentId) {
        System.out.println("working");
        return null;
    }
}