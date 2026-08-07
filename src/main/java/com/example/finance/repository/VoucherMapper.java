package com.example.finance.repository;

import com.example.finance.entity.Voucher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VoucherMapper {
    void insert(Voucher voucher);
    List<Voucher> findByCompanyAndPeriod(@Param("companyId") String companyId,
                                         @Param("period")    String period);
    List<Voucher> findAll();
}
