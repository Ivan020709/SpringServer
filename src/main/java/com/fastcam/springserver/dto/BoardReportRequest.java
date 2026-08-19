package com.fastcam.springserver.dto;

import lombok.Data;

@Data
public class BoardReportRequest {
    private String reason;
    private String detail;
}
