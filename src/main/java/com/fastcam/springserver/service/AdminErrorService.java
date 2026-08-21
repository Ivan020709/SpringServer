package com.fastcam.springserver.service;

import com.fastcam.springserver.entity.AdminError;
import com.fastcam.springserver.repository.AdminErrorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminErrorService {

    @Autowired
    AdminErrorRepository aer;

    public void saveError(AdminError error) {
        aer.save(error);
    }

    public List<AdminError> getErrorList() {
        return aer.findAllByOrderByErrornumDesc();
    }
}
