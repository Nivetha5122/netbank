package com.netbank.repository;

import com.netbank.entity.Account;
import com.netbank.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.destinationAccount = :account ORDER BY t.createdAt DESC")
    List<Transaction> findAllByAccount(@Param("account") Account account);

    @Query("SELECT t FROM Transaction t WHERE t.sourceAccount = :account OR t.destinationAccount = :account ORDER BY t.createdAt DESC")
    Page<Transaction> findAllByAccountPageable(@Param("account") Account account, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE (t.sourceAccount = :account OR t.destinationAccount = :account) ORDER BY t.createdAt DESC")
    List<Transaction> findTop5ByAccount(@Param("account") Account account, Pageable pageable);

    Optional<Transaction> findByReferenceNumber(String referenceNumber);

    boolean existsByReferenceNumber(String referenceNumber);
}
