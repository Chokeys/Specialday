package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.Administrator;
import com.example.dto.ClassProduct;

@Mapper
public interface AdministratorMapper {

    // 1. 관리자 등록
    public int insertAdminOne(Administrator obj);

    // 2. 관리자 정보 조회
    public Administrator selectAdminOne(String id);

    // 3. 클래스 등록 신청 정보 조회
    public List<ClassProduct> selectClassProductList();

    // 4. 클래스 승인(chk : 0 -> 1)
    public int updateClassProductChk(long classcode);
    
}