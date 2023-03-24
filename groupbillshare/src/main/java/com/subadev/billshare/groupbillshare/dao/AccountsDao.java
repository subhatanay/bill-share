package com.subadev.billshare.groupbillshare.dao;

import com.subadev.billshare.groupbillshare.entity.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccountsDao extends JpaRepository<Accounts,Integer> {

    @Query(nativeQuery = true,value = "UPDATE accounts SET processed= 't' WHERE id = ?1 ")
    @Transactional
    @Modifying
    public void updatedata(Integer id);

    @Query(nativeQuery = true,value = "SELECT pg_try_advisory_lock(?1)")

    public boolean tryLock(int lockKey);

    @Query(nativeQuery = true,value = "SELECT pg_advisory_unlock(?1)")
    public boolean releaseLock(int lockKey);


}
