package com.upverasmusproject.ro.recsysrestapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


interface VideoRepository extends JpaRepository<Video, Long> {
}
