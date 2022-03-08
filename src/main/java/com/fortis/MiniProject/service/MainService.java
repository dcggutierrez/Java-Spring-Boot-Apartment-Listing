package com.fortis.MiniProject.service;

import com.fortis.MiniProject.dto.RegisterDto;
import com.fortis.MiniProject.dto.UnitDto;
import com.fortis.MiniProject.request.UnitRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MainService extends UserDetailsService {
    RegisterDto createUser(RegisterDto initDto);

    RegisterDto getUser(String email);

    RegisterDto getUserByUserId(String id);

    UnitDto createUnit(UnitDto unitDto);

    List<UnitDto> getUnitsOwned(String id,int page, int limit);

    int getNumberOfUnitsOwned(String id);

    UnitDto getUnit(String id);

    List<UnitDto> getUnitsDash(int page, int limit);

    List<UnitDto> getRecommended(String category, int limit);

    UnitDto updateUnit(String id, UnitRequest unitRequest);

    List<UnitDto> getUnitsByName(String name, int page, int limit);

    void deleteUnit(String id);
}