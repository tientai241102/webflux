package com.nttai.webflux.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "Post response DTO")
public class PostResponse {
    @NotNull
    @Schema(description = "Post ID", example = "1")
    private Integer id;

    @NotNull
    @Schema(description = "Title of the post", example = "Hello World")
    private String title;

    @Schema(description = "Content of the post", example = "This is a post content.")
    private String content;
} 