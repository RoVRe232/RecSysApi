package com.upverasmusproject.ro.recsysrestapi;

import org.springframework.data.jpa.repository.JpaRepository;

interface VideoRepository extends JpaRepository<Video, Long> {
}
