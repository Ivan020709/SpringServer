package com.fastcam.springserver.service;

import com.fastcam.springserver.dto.Paging;
import com.fastcam.springserver.entity.Board;
import com.fastcam.springserver.repository.BoardRepository;
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


    public void plusCount(int boardnum) {
        Board board = br.findByBoardnum(boardnum);
        int pc = board.getViewcount() +1;
        board.setViewcount(pc);
    }

    public HashMap<String, Object> getBoardList(int page) {
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
        result.put("boardList", list2);
        result.put("paging", paging);

        return  result;
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
