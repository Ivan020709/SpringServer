package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.Paging;
import com.fastcam.springserver.entity.NoticeBoard;
import com.fastcam.springserver.repository.NoticeBoardRepository;
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
public class NoticeBoardService {
    @Autowired
    NoticeBoardRepository nbr;


    public HashMap<String, Object> getBoardList(int page, String key, String searchType) {
        HashMap<String, Object> result = new HashMap<>();
        Paging paging = new Paging();
        paging.setPage(page);
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);

        if(key.equals("")) {
            int count = nbr.findAll().size();
            paging.setTotalCount(count);
            paging.calPaing();

            Pageable pageable = PageRequest.of(
                    page - 1,
                    10,
                    Sort.by(
                            Sort.Order.desc("fixed"),
                            Sort.Order.desc("noticenum")
                    )
            );

            Page<NoticeBoard> obj = nbr.findAll(pageable);
            List<NoticeBoard> list = obj.getContent();
            result.put("noticeList", list);
        }else if(searchType.equals("title")){
            int count = nbr.findByTitleContaining(key).size();
            paging.setTotalCount(count);
            paging.calPaing();
            Pageable pageable = PageRequest.of(page - 1, 10,
                    Sort.by(Sort.Direction.DESC, "noticenum"));
            Page<NoticeBoard> obj = nbr.findAllByTitleContaining(key, pageable);
            List<NoticeBoard> list = obj.getContent();
            result.put("noticeList", list);

        }else if(searchType.equals("content")){
            int count = nbr.findByContentContaining(key).size();
            paging.setTotalCount(count);
            paging.calPaing();
            Pageable pageable = PageRequest.of(page - 1, 10,
                    Sort.by(Sort.Direction.DESC, "noticenum"));
            Page<NoticeBoard> obj = nbr.findAllByContentContaining(key, pageable);
            List<NoticeBoard> list = obj.getContent();
            result.put("noticeList", list);

        }
        result.put("paging", paging);

        return  result;
    }

    public void insertBoard(NoticeBoard nboard) {
        if ("Y".equals(nboard.getFixed())) {

            List<NoticeBoard> fixedList =
                    nbr.findByFixedOrderByIndateAsc("Y");
            // 이미 3개라면 가장 오래된 고정 공지 해제
            if (fixedList.size() >= 3) {
                NoticeBoard oldNotice = fixedList.get(0);
                oldNotice.setFixed("N");
                nbr.save(oldNotice);
            }
        }

        // 새 공지 등록
        nbr.save(nboard);
    }
}
