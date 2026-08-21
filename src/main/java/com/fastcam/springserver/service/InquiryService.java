package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.Paging;
import com.fastcam.springserver.entity.Inquiry;
import com.fastcam.springserver.repository.InquiryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class InquiryService {

    @Autowired
    InquiryRepository ir;

    public HashMap<String, Object> getInquiryList(int page) {

        if (page < 1) {
            page = 1;
        }

        HashMap<String, Object> result = new HashMap<>();

        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);
        paging.setTotalCount((int) ir.count());
        paging.calPaing();

        Pageable pageable = PageRequest.of(
                page - 1,
                10,
                Sort.by(
                        Sort.Direction.DESC,
                        "inquirynum"
                )
        );

        Page<Inquiry> inquiryPage = ir.findAll(pageable);
        List<Inquiry> inquiryList = inquiryPage.getContent();

        result.put("inquiryList", inquiryList);
        result.put("paging", paging);

        return result;
    }

    public Inquiry getInquiry(int inquirynum) {
        return ir.findByInquirynum(inquirynum);
    }

    public void insertInquiry(Inquiry inquiry) {

        if (inquiry.getStatus() == null
                || inquiry.getStatus().equals("")) {
            inquiry.setStatus("대기중");
        }

        ir.save(inquiry);
    }

    public boolean updateInquiry(Inquiry inquiry) {

        Inquiry oldInquiry =
                ir.findByInquirynum(inquiry.getInquirynum());

        if (oldInquiry == null) {
            return false;
        }

        oldInquiry.setTitle(inquiry.getTitle());
        oldInquiry.setContent(inquiry.getContent());

        ir.save(oldInquiry);

        return true;
    }

    public boolean deleteInquiry(int inquirynum) {

        Inquiry inquiry =
                ir.findByInquirynum(inquirynum);

        if (inquiry == null) {
            return false;
        }

        ir.delete(inquiry);

        return true;
    }
}