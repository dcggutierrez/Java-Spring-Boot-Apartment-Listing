package com.fortis.MiniProject.controller;

import com.fortis.MiniProject.dto.RegisterDto;
import com.fortis.MiniProject.dto.UnitDto;
import com.fortis.MiniProject.request.RegisterRequest;
import com.fortis.MiniProject.request.UnitRequest;
import com.fortis.MiniProject.response.RegisterResponse;
import com.fortis.MiniProject.response.UnitResponse;
import com.fortis.MiniProject.service.MainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class MainController {

    @Autowired
    MainService mainService;

    @PostMapping
    public RegisterResponse registerUser(@RequestBody RegisterRequest registerRequest){
        RegisterResponse returnValue = new RegisterResponse();
        RegisterDto initDto = new RegisterDto();
        BeanUtils.copyProperties(registerRequest,initDto);
        RegisterDto returnDto = mainService.createUser(initDto);
        BeanUtils.copyProperties(returnDto,returnValue);
        return returnValue;
    }

    @GetMapping(path = "/{id}")
    public RegisterResponse getUser(@PathVariable String id){
        RegisterResponse returnValue = new RegisterResponse();
        RegisterDto registerDto = mainService.getUserByUserId(id);
        BeanUtils.copyProperties(registerDto,returnValue);
        return returnValue;
    }

    @PutMapping
    public String updateUser(){
        return "put was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "delete was called";
    }
}
