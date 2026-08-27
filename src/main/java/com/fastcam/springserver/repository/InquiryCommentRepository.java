package com.fastcam.springserver.repository;

import com.fastcam.springserver.entity.InquiryComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryCommentRepository
        extends JpaRepository<InquiryComment, Integer> {

    // 선택한 문의글의 댓글을 작성 순서대로 조회합니다.
    List<InquiryComment> findAllByInquiryIdOrderByCreatedAtAsc(int inquiryId);

    // 문의글을 삭제하기 전에 연결된 댓글을 모두 삭제합니다.
    void deleteAllByInquiryId(int inquiryId);

    // 댓글이 여러 개여도 가장 최근에 등록된 답변 하나를 조회합니다.
    InquiryComment findFirstByInquiryIdOrderByCreatedAtDesc(int inquiryId);
}
