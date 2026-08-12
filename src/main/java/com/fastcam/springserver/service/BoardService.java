package com.fastcam.springserver.service;

import com.fastcam.springserver.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BoardService {
    @Autowired
    BoardRepository br;
}
