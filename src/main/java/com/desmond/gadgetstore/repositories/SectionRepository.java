package com.desmond.gadgetstore.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.desmond.gadgetstore.entities.SectionEntity;


@Repository
public interface SectionRepository extends JpaRepository<SectionEntity, UUID> {

}
