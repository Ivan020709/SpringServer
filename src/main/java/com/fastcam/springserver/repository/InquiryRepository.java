package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository
        extends JpaRepository<Inquiry, Integer> {

    Inquiry findByInquirynum(int inquirynum);
}