package com.fortis.MiniProject.service;

import com.fortis.MiniProject.dto.RegisterDto;
import com.fortis.MiniProject.dto.UnitDto;
import com.fortis.MiniProject.entity.UnitEntity;
import com.fortis.MiniProject.entity.UserEntity;
import com.fortis.MiniProject.repository.UnitRepository;
import com.fortis.MiniProject.repository.UserRepository;
import com.fortis.MiniProject.request.UnitRequest;
import com.fortis.MiniProject.utils.Utilities;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MainServiceImpl implements MainService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    Utilities utilities;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public RegisterDto createUser(RegisterDto initDto) {
        String publicUserId;
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(initDto,userEntity);
        publicUserId = utilities.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(initDto.getPassword()));
        UserEntity storedUserDetails = userRepository.save(userEntity);
        RegisterDto returnValue = new RegisterDto();
        BeanUtils.copyProperties(storedUserDetails,returnValue);
        return returnValue;
    }

    @Override
    public UnitDto createUnit(UnitDto unitDto) {
        String publicUnitId;
        UnitEntity unitEntity = new UnitEntity();
        BeanUtils.copyProperties(unitDto,unitEntity);
        unitEntity.setUserEntity(userRepository.findUserByUserId(unitDto.getUserId()));
        publicUnitId = utilities.generateUserId(35);
        unitEntity.setUnitId(publicUnitId);
        unitEntity.setRating(5d);
        UnitEntity storedUnitDetails = unitRepository.save(unitEntity);
        UnitDto returnValue = new UnitDto();
        BeanUtils.copyProperties(storedUnitDetails,returnValue);
        return returnValue;
    }

    @Override
    public List<UnitDto> getUnitsOwned(String id,int page, int limit) {
        UserEntity user = userRepository.findUserByUserId(id);
        if (user==null) throw new NoSuchElementException();
        Page<UnitEntity> unitPage = unitRepository.findAllByUserEntity(user,PageRequest.of(page,limit));
        List<UnitEntity> unitList = unitPage.getContent();
        List<UnitDto> returnValue = StreamSupport.stream(unitList.spliterator(),false).map(UnitEntity ->{
            UnitDto dto = new UnitDto();
            BeanUtils.copyProperties(UnitEntity,dto);
            return dto;
        }).collect(Collectors.toList());
        returnValue.sort(Comparator.comparingDouble(UnitDto::getRating).reversed().thenComparing(Comparator.comparing(UnitDto::getUnitName)));
        return returnValue;
    }
    @Override
    public int getNumberOfUnitsOwned(String id) {
        UserEntity user = userRepository.findUserByUserId(id);
        if (user==null) throw new NoSuchElementException();
        Page<UnitEntity> unitPage = unitRepository.findAllByUserEntity(user,PageRequest.of(0,1000));
        List<UnitEntity> unitList = unitPage.getContent();
        int returnValue = unitList.size();
        return returnValue;
    }

    @Override
    public List<UnitDto> getUnitsDash(int page, int limit) {
        List<UnitEntity> allUnits = unitRepository.findAll();
        allUnits.sort(Comparator.comparingDouble(UnitEntity::getRating).reversed().thenComparing(Comparator.comparing(UnitEntity::getUnitName)));
        Page<UnitEntity> unitPage = new PageImpl<>(allUnits, PageRequest.of(page,limit), allUnits.size());
        List<UnitEntity> unitList = unitPage.getContent();
        List<UnitDto> returnValue = StreamSupport.stream(unitList.spliterator(),false).map(UnitEntity ->{
            UnitDto dto = new UnitDto();
            BeanUtils.copyProperties(UnitEntity,dto);
            return dto;
        }).collect(Collectors.toList());
        returnValue.sort(Comparator.comparingDouble(UnitDto::getRating).reversed().thenComparing(Comparator.comparing(UnitDto::getUnitName)));
        return returnValue;
    }

    @Override
    public UnitDto getUnit(String id) {
        UnitDto returnValue = new UnitDto();
        UnitEntity entity = unitRepository.findUserByUnitId(id);
        if(entity==null) throw new NoSuchElementException();
        BeanUtils.copyProperties(entity,returnValue);
        return returnValue;
    }

    @Override
    public RegisterDto getUser(String email) {
        UserEntity userEntity = userRepository.findUserByEmail(email);
        if(userEntity==null) throw new UsernameNotFoundException(email);
        RegisterDto returnValue = new RegisterDto();
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public RegisterDto getUserByUserId(String id) {
        RegisterDto returnValue = new RegisterDto();
        UserEntity userEntity = userRepository.findUserByUserId(id);
        if(userEntity==null) throw new UsernameNotFoundException(id);
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
    @Override
    public List<UnitDto> getRecommended(String category, int limit) {
        List<UnitEntity> units = unitRepository.findAll();
        List<UnitDto> unitsDto = new ArrayList<>();
        List<UnitEntity> filteredUnits = units.stream().filter(unit-> unit.getCategory().equals(category)).
                collect(Collectors.toList());
        for (UnitEntity unit: filteredUnits){
            UnitDto unitDto = new UnitDto();
            BeanUtils.copyProperties(unit, unitDto);
            unitsDto.add(unitDto);
        }
        unitsDto.sort(Comparator.comparingDouble(UnitDto::getRating).reversed().thenComparing(Comparator.comparing(UnitDto::getUnitName)));
        int filteredSize = unitsDto.size();
        int returnSize = 0;
        returnSize += Math.min(filteredSize, limit);

        return unitsDto.stream().limit(returnSize).collect(Collectors.toList());
    }

    @Override
    public UnitDto updateUnit(String id, UnitRequest unitRequest) throws IllegalAccessError{
        UnitDto returnValue = new UnitDto();
        UnitEntity unitEntity = unitRepository.findByUnitId(id);
        if (unitEntity == null) throw new NoSuchElementException();

        if (!Objects.isNull(unitRequest.getUnitName())){
            unitEntity.setUnitName(unitRequest.getUnitName());
        }
        if (!Objects.isNull(unitRequest.getUnitDescription())){
            unitEntity.setUnitDescription(unitRequest.getUnitDescription());
        }
        if (!Objects.isNull(unitRequest.getCategory())){
            unitEntity.setCategory(unitRequest.getCategory());
        }
        if (!Objects.isNull(unitRequest.getUserId())){
            unitEntity.setUserEntity(userRepository.findUserByUserId(unitRequest.getUserId()));
        }
        if (!Objects.isNull(unitRequest.getPrice())){
            unitEntity.setPrice(unitRequest.getPrice());
        }
        if (!Objects.isNull(unitRequest.getPhoto1())){
            unitEntity.setPhoto1(unitRequest.getPhoto1());
        }
        if (!Objects.isNull(unitRequest.getPhoto2())){
            unitEntity.setPhoto2(unitRequest.getPhoto2());
        }
        if (!Objects.isNull(unitRequest.getPhoto3())){
            unitEntity.setPhoto3(unitRequest.getPhoto3());
        }
        if (!Objects.isNull(unitRequest.getPhoto4())){
            unitEntity.setPhoto4(unitRequest.getPhoto4());
        }
        if (!Objects.isNull(unitRequest.getPhoto5())){
            unitEntity.setPhoto5(unitRequest.getPhoto5());
        }
        if (!Objects.isNull(unitRequest.getRating())){
            unitEntity.setRating(unitRequest.getRating());
        }
        UnitEntity updatedUnitDetails = unitRepository.save(unitEntity);
        BeanUtils.copyProperties(updatedUnitDetails, returnValue);
        return returnValue;
    }

//    @Override
//    public List<UnitDto> getUnitsByName(String name, int page, int limit) {
//        List<UnitEntity> units = unitRepository.findAll();
//        List<UnitEntity> unitList = units.stream().filter(unit -> unit.getUnitName().equals(name))
//                .collect(Collectors.toList());
//        List<UnitDto> returnValue = StreamSupport.stream(unitList.spliterator(),false).map(UnitEntity ->{
//            UnitDto dto = new UnitDto();
//            BeanUtils.copyProperties(UnitEntity,dto);
//            return dto;
//        }).collect(Collectors.toList());
//        return returnValue;
//    }
//    @Override
//    public List<UnitDto> getUnitsByName(String name, int page, int limit) {
//        Page<UnitEntity> units = unitRepository.findByUnitName(name, PageRequest.of(page, limit));
//        List<UnitEntity> unitList = units.getContent();
//        List<UnitDto> returnValue = StreamSupport.stream(unitList.spliterator(),false)
//                .map(UnitEntity ->{
//                    UnitDto dto = new UnitDto();
//                    BeanUtils.copyProperties(UnitEntity,dto);
//                    return dto;
//                }).collect(Collectors.toList());
//        return returnValue;
//    }
    @Override
    public List<UnitDto> getUnitsByName(String name, int page, int limit) {
        List<UnitEntity> units = unitRepository.findAll();
        List<String> names = units.stream().filter(unit -> unit.getUnitName().toLowerCase().contains(name.toLowerCase())).map(UnitEntity::getUnitName).collect(Collectors.toList());
        List<UnitEntity> matchedUnits = new ArrayList<>();

        for (String match: names){
            matchedUnits.addAll(unitRepository.findByUnitName(match));
        }

        matchedUnits.sort(Comparator.comparingDouble(UnitEntity::getRating).reversed().thenComparing(Comparator.comparing(UnitEntity::getUnitName)));
        Page<UnitEntity> unitPage = new PageImpl<>(matchedUnits, PageRequest.of(page,limit), matchedUnits.size());
        List<UnitEntity> unitList = unitPage.getContent();
        List<UnitDto> returnValue = StreamSupport.stream(unitList.spliterator(),false)
                .map(UnitEntity ->{
                    UnitDto dto = new UnitDto();
                    BeanUtils.copyProperties(UnitEntity,dto);
                    return dto;
                }).collect(Collectors.toList());
        return returnValue;
    }

    @Override
    public void deleteUnit(String id) {
        UnitEntity unit = unitRepository.findByUnitId(id);
        unitRepository.delete(unit);
    }
}
