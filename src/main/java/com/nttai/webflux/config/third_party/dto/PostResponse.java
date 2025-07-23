package com.nttai.webflux.config.third_party.dto;

import lombok.Data;

@Data
public class PostResponse {
    private int userId;
    private int id;
    private String title;
    private String body;
}