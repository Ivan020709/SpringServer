package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.Paging;
import com.fastcam.springserver.entity.AdminReport;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.entity.NoticeBoard;
import com.fastcam.springserver.repository.AdminRepository;
import com.fastcam.springserver.repository.BoardRepository;
import com.fastcam.springserver.repository.MemberRepository;
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
public class AdminService {

    @Autowired
    AdminRepository ar;

    @Autowired
    BoardRepository br;

    @Autowired
    MemberRepository mr;

    public void getReport(AdminReport areport) {
        Board board = br.findByBoardnum(areport.getBoardnum());
        areport.setStatus("N");
        Member member = mr.findByUserid(board.getUserid());
        if(member==null){
            areport.setCriminal("탈퇴한 회원");
        }
        areport.setCriminal(member.getNickname());
        ar.save(areport);
    }


    public HashMap<String, Object> getReportList(int page) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        Paging paging = new Paging();
        paging.setPage(page);
        List<AdminReport> list = ar.findAll();
        int count = list.size();

        paging.setTotalCount(count);
        paging.calPaing();
        paging.setDisplayPage(10);
        paging.setDisplayRow(10);



        Pageable pageable = PageRequest.of(page-1, paging.getDisplayRow(),
                Sort.by(Sort.Direction.DESC, "reportnum"));

        Page<AdminReport> pageList = ar.findAll(pageable);
        // Page<Qna> 에서 필요한 목록을 꺼냅니다.
        List<AdminReport> list2 = pageList.getContent();
        result.put("reportList", list2);
        result.put("paging", paging);

        return  result;

    }
}
