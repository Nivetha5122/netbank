package com.netbank.service.impl;

import com.netbank.entity.Beneficiary;
import com.netbank.entity.User;
import com.netbank.exception.NetBankException;
import com.netbank.repository.BeneficiaryRepository;
import com.netbank.service.BeneficiaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;

    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Beneficiary> getBeneficiaries(User user) {
        return beneficiaryRepository.findByUser(user);
    }

    @Override
    @Transactional
    public Beneficiary addBeneficiary(User user, String name, String accountNumber, String bankName, String ifscCode) {
        if (beneficiaryRepository.existsByUserAndAccountNumber(user, accountNumber)) {
            throw new NetBankException("Beneficiary with this account number already exists");
        }
        Beneficiary b = Beneficiary.builder()
                .user(user)
                .name(name)
                .accountNumber(accountNumber)
                .bankName(bankName)
                .ifscCode(ifscCode)
                .build();
        return beneficiaryRepository.save(b);
    }

    @Override
    @Transactional
    public void deleteBeneficiary(Long id, User user) {
        Beneficiary b = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new NetBankException("Beneficiary not found"));
        if (!b.getUser().getId().equals(user.getId())) {
            throw new NetBankException("Access denied");
        }
        beneficiaryRepository.delete(b);
    }
}
