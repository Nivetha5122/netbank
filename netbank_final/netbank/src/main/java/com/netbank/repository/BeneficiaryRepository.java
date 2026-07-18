package com.netbank.repository;

import com.netbank.entity.Beneficiary;
import com.netbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByUser(User user);
    Optional<Beneficiary> findByUserAndAccountNumber(User user, String accountNumber);
    boolean existsByUserAndAccountNumber(User user, String accountNumber);
}
