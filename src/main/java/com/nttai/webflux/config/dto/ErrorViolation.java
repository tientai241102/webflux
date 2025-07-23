package com.nttai.webflux.config.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.util.ObjectUtils;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SuperBuilder
public class ErrorViolation {

    private String field;

    @JsonIgnore
    private String code;

    private String description;

    private String message;

    @JsonIgnore
    public String getErrorMessage() {
        if(!ObjectUtils.isEmpty(description)) {
            return description;
        }
        return message;
    }
}