package com.fortis.MiniProject.controller;

import com.fortis.MiniProject.dto.UnitDto;
import com.fortis.MiniProject.request.UnitRequest;
import com.fortis.MiniProject.response.DeleteResponse;
import com.fortis.MiniProject.response.DetailedUnitResponse;
import com.fortis.MiniProject.response.UnitResponse;
import com.fortis.MiniProject.service.MainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("units")
public class UnitsController {

    @Autowired
    MainService mainService;

    @PostMapping
    public UnitResponse registerUnit(@RequestBody UnitRequest unitRequest){
        UnitResponse returnValue = new UnitResponse();
        UnitDto unitDto = new UnitDto();
        BeanUtils.copyProperties(unitRequest,unitDto);
        UnitDto returnDto = mainService.createUnit(unitDto);
        BeanUtils.copyProperties(returnDto,returnValue);
        return returnValue;
    }

    @GetMapping(path = "/{id}")
    public DetailedUnitResponse getUnit(@PathVariable String id){
        DetailedUnitResponse returnValue = new DetailedUnitResponse();
        UnitDto dto = mainService.getUnit(id);
        System.out.println(dto.getUnitName());
        BeanUtils.copyProperties(dto,returnValue);
        return returnValue;
    }

    @GetMapping(path = "/owned/{id}")
    public List<UnitResponse> getUnitsOwned(@PathVariable String id,@RequestParam (value="page", defaultValue = "0") int page, @RequestParam (value="limit", defaultValue = "1000")int limit){
        List<UnitDto> returnDto = mainService.getUnitsOwned(id,  page, limit);
        List<UnitResponse> returnValue = StreamSupport.stream(returnDto.spliterator(),false).map(UnitDto ->{
            UnitResponse response = new UnitResponse();
            BeanUtils.copyProperties(UnitDto,response);
            return response;
        }).collect(Collectors.toList());
        return returnValue;
    }

    @GetMapping(path = "/owned/number/{id}")
    public int getNumberOfUnitsOwned(@PathVariable String id){
        int returnValue = mainService.getNumberOfUnitsOwned(id);
        return returnValue;
    }

    @GetMapping(path = "/dashboard")
    public List<UnitResponse> getUnitsDash(@RequestParam (value="page", defaultValue = "0") int page, @RequestParam (value="limit", defaultValue = "1000") int limit){
        List<UnitDto> returnDto = mainService.getUnitsDash(page, limit);
        List<UnitResponse> returnValue = StreamSupport.stream(returnDto.spliterator(),false).map(UnitDto ->{
            UnitResponse response = new UnitResponse();
            BeanUtils.copyProperties(UnitDto,response);
            return response;
        }).collect(Collectors.toList());
        return returnValue;
    }

    @GetMapping(path = "/recommended")
    public List<UnitResponse> getUnitsRecommended (@RequestParam (value="category", defaultValue = "Apartment") String category, @RequestParam (value="limit", defaultValue = "5") int limit){
        List<UnitDto> returnDto = mainService.getRecommended(category, limit);
        List<UnitResponse> returnValue = StreamSupport.stream(returnDto.spliterator(),false)
                .map(UnitDto ->{
                    UnitResponse response = new UnitResponse();
                    BeanUtils.copyProperties(UnitDto,response);
                    return response;
                }).collect(Collectors.toList());
        return returnValue;
    }

//    @GetMapping(path = "/search/{name}")
//    public List<UnitResponse> getUnitsSearch(@PathVariable String name, @RequestParam (value="page", defaultValue = "0") int page, @RequestParam (value="limit", defaultValue = "1000") int limit){
//        List<UnitDto> returnDto = mainService.getUnitsByName(name, page, limit);
//        if (returnDto.size()==0){
//            return new ArrayList<>();
//        } else {
//            List<UnitResponse> returnValue = StreamSupport.stream(returnDto.spliterator(),false)
//                    .map(UnitDto ->{
//                        UnitResponse response = new UnitResponse();
//                        BeanUtils.copyProperties(UnitDto,response);
//                        return response;
//                    }).collect(Collectors.toList());
//            return returnValue;
//        }
//    }

    @GetMapping(path = "/search/{name}")
    public List<UnitResponse> getUnitsByName(@PathVariable String name,@RequestParam (value="page", defaultValue = "0") int page, @RequestParam (value="limit", defaultValue = "1000")int limit){
        List<UnitDto> returnDto = mainService.getUnitsByName(name,  page, limit);
        List<UnitResponse> returnValue = StreamSupport.stream(returnDto.spliterator(),false).map(UnitDto ->{
            UnitResponse response = new UnitResponse();
            BeanUtils.copyProperties(UnitDto,response);
            return response;
        }).collect(Collectors.toList());
        return returnValue;
    }

    @PutMapping(path = "/{id}")
    public UnitResponse editUnit(@PathVariable String id, @RequestBody UnitRequest unitRequest)  {
        UnitResponse returnValue = new UnitResponse();
        UnitDto updateUnit = mainService.updateUnit(id,unitRequest);
        BeanUtils.copyProperties(updateUnit, returnValue);
        return returnValue;
    }

    @DeleteMapping(path = "/{id}")
    public void deleteUnit(@PathVariable String id){
        mainService.deleteUnit(id);
    }
}
