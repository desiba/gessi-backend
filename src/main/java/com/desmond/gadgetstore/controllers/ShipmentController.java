package com.desmond.gadgetstore.controllers;

import com.desmond.gadgetstore.entities.ShipmentEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipment")
public class ShipmentController {
    @GetMapping("{id}")
    public ResponseEntity<ShipmentEntity> findById(@PathVariable("id") long id){


        return null; //ResponseEntity.ok().body();
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update (@PathVariable("id") long id){
        //Boolean result = studentService.updateStudent(id, student);


        return ResponseEntity.ok().body("Student Updated Successfully");
    }

}
