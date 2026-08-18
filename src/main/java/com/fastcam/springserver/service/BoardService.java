package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.Paging;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.repository.BoardRepository;
import com.fastcam.springserver.repository.BoardLikeRepository;
import com.fastcam.springserver.repository.BoardReportRepository;
import com.fastcam.springserver.entity.BoardLike;
import com.fastcam.springserver.entity.BoardReport;
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
public class BoardService {
    @Autowired
    BoardRepository br;
    @Autowired BoardLikeRepository blr;
    @Autowired BoardReportRepository brr;


    public void plusCount(int boardnum) {
        Board board = br.findByBoardnum(boardnum);
        int pc = board.getViewcount() +1;
        board.setViewcount(pc);
    }

    public HashMap<String, Object> getBoardList(int page, Integer userId) {
        HashMap<String, Object> result = new HashMap<String, Object>();

        Paging paging = new Paging();
        paging.setPage(page);
        List<Board> list = br.findAll();
        int count = list.size();

        paging.setDisplayPage(5);
        paging.setDisplayRow(5);
        paging.setTotalCount(count);
        paging.calPaing();



        Pageable pageable = PageRequest.of(page-1, paging.getDisplayRow(),
                Sort.by(Sort.Direction.DESC, "boardnum"));


        Page<Board> pageList = br.findAll(pageable);

        List<Board> list2 = pageList.getContent();
        List<HashMap<String, Object>> enriched = list2.stream().map(board -> {
            HashMap<String, Object> item = new HashMap<>();
            item.put("boardnum", board.getBoardnum()); item.put("userid", board.getUserid());
            item.put("email", board.getEmail()); item.put("title", board.getTitle());
            item.put("content", board.getContent()); item.put("viewcount", board.getViewcount());
            item.put("indate", board.getIndate()); item.put("category", board.getCategory());
            item.put("isprivate", board.isIsprivate());
            item.put("likeCount", blr.countByBoardId(board.getBoardnum()));
            boolean likedByMe = userId != null && blr.findByBoardIdAndUserId(board.getBoardnum(), userId).isPresent();
            item.put("likedByMe", likedByMe);
            item.put("liked", likedByMe);
            return item;
        }).toList();
        result.put("boardList", enriched);
        result.put("paging", paging);

        return  result;
    }

    public HashMap<String, Object> toggleLike(int boardId, int userId) {
        var existing = blr.findByBoardIdAndUserId(boardId, userId);
        boolean liked;
        if (existing.isPresent()) { blr.delete(existing.get()); liked = false; }
        else { BoardLike like = new BoardLike(); like.setBoardId(boardId); like.setUserId(userId); blr.save(like); liked = true; }
        HashMap<String, Object> result = new HashMap<>();
        result.put("liked", liked); result.put("likeCount", blr.countByBoardId(boardId));
        return result;
    }

    public void reportBoard(int boardId, int reporterId, String reason, String detail) {
        if (brr.findByBoardIdAndReporterId(boardId, reporterId).isPresent())
            throw new IllegalArgumentException("이미 신고한 게시글입니다.");
        BoardReport report = new BoardReport(); report.setBoardId(boardId); report.setReporterId(reporterId);
        report.setReason(reason); report.setDetail(detail); brr.save(report);
    }

    public Board getBoard(int boardnum) {
        Board board = br.findByBoardnum(boardnum);
        return board;
    }

    public void deleteBoard(int boardnum) {
        Board board = br.findByBoardnum(boardnum);
        br.delete(board);
    }

    public void updateBoard(Board board) {
        Board oldBoard = br.findByBoardnum(board.getBoardnum());

        oldBoard.setEmail(board.getEmail());
        oldBoard.setTitle(board.getTitle());
        oldBoard.setContent(board.getContent());

    }

    // 게시글 등록
    public void insertBoard(Board board) {
        br.save(board);
    }
}
