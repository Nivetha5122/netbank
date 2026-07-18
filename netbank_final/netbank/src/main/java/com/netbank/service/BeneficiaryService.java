package com.netbank.service;

import com.netbank.entity.Beneficiary;
import com.netbank.entity.User;
import java.util.List;

public interface BeneficiaryService {
    List<Beneficiary> getBeneficiaries(User user);
    Beneficiary addBeneficiary(User user, String name, String accountNumber, String bankName, String ifscCode);
    void deleteBeneficiary(Long id, User user);
}
