package com.document.document.repository;

import com.document.document.entity.UserDocument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserDocumentRepository extends JpaRepository<UserDocument, Integer> {

    @Query("from UserDocument as u where u.user.uid  =:id")
    public UserDocument findUserDocumentByUser(@Param("id") int id);

}
