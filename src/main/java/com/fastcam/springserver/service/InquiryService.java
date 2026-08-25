package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.Paging;
import com.fastcam.springserver.entity.Inquiry;
import com.fastcam.springserver.entity.InquiryComment;
import com.fastcam.springserver.entity.Member;
import com.fastcam.springserver.repository.InquiryCommentRepository;
import com.fastcam.springserver.repository.InquiryRepository;
import com.fastcam.springserver.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class InquiryService {

    @Autowired
    InquiryRepository ir;

    @Autowired
    InquiryCommentRepository inquiryCommentRepository;

    @Autowired
    MemberRepository memberRepository;

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

        // FK 오류가 나지 않도록 문의글에 달린 댓글을 먼저 삭제합니다.
        inquiryCommentRepository.deleteAllByInquiryId(inquirynum);
        ir.delete(inquiry);

        return true;
    }


    // =====================================================
    // 문의 댓글 조회
    // =====================================================

//    @Transactional(readOnly = true)
//    public List<HashMap<String, Object>> getInquiryComments(int inquiryId) {
//
//        // 존재하지 않는 문의글의 댓글을 조회하지 않도록 먼저 확인합니다.
//        if (ir.findByInquirynum(inquiryId) == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    "문의글을 찾을 수 없습니다."
//            );
//        }
//
//        return inquiryCommentRepository
//                .findAllByInquiryIdOrderByCreatedAtAsc(inquiryId)
//                .stream()
//                .map(this::toCommentData)
//                .toList();
//    }


    // =====================================================
    // 문의 댓글 등록
    // =====================================================

//    public HashMap<String, Object> insertInquiryComment(
//            int inquiryId,
//            int userId,
//            String content
//    ) {
//
//        if (ir.findByInquirynum(inquiryId) == null) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    "문의글을 찾을 수 없습니다."
//            );
//        }
//
//        requireMember(userId);
//
//        InquiryComment comment = new InquiryComment();
//        comment.setInquiryId(inquiryId);
//        comment.setUserId(userId);
//        comment.setContent(requireCommentContent(content));
//
//        return toCommentData(
//                inquiryCommentRepository.save(comment)
//        );
//    }


    // =====================================================
    // 문의 댓글 수정 - PUT 대신 POST 사용
    // =====================================================

//    public HashMap<String, Object> updateInquiryComment(
//            int commentId,
//            int userId,
//            String content
//    ) {
//
//        InquiryComment comment = requireComment(commentId);
//        requireCommentOwner(comment, userId);
//        comment.setContent(requireCommentContent(content));
//
//        return toCommentData(
//                inquiryCommentRepository.save(comment)
//        );
//    }


    // =====================================================
    // 문의 댓글 삭제
    // =====================================================

//    public void deleteInquiryComment(int commentId, int userId) {
//        InquiryComment comment = requireComment(commentId);
//        requireCommentOwner(comment, userId);
//        inquiryCommentRepository.delete(comment);
//    }


//    // 댓글 작성자 이름까지 프론트에 보내기 위한 변환 함수입니다.
//    private HashMap<String, Object> toCommentData(InquiryComment comment) {
//        HashMap<String, Object> data = new HashMap<>();
//        Member member = memberRepository.findByUserid(comment.getUserId());
//
//        data.put("id", comment.getId());
//        data.put("inquiryId", comment.getInquiryId());
//        data.put("userId", comment.getUserId());
//        data.put("userName", member != null ? member.getName() : "알 수 없음");
//        data.put("content", comment.getContent());
//        data.put("createdAt", comment.getCreatedAt());
//
//        return data;
//    }

    private Member requireMember(int userId) {
        Member member = memberRepository.findByUserid(userId);
        if (member == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "로그인 회원을 찾을 수 없습니다."
            );
        }
        return member;
    }

    private InquiryComment requireComment(int commentId) {
        return inquiryCommentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "댓글을 찾을 수 없습니다."
                ));
    }

//    private void requireCommentOwner(InquiryComment comment, int userId) {
//        if (comment.getUserId() != userId) {
//            throw new ResponseStatusException(
//                    HttpStatus.FORBIDDEN,
//                    "본인의 댓글만 변경할 수 있습니다."
//            );
//        }
//    }

    private String requireCommentContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "댓글 내용을 입력해주세요."
            );
        }
        return content.trim();
    }
}
