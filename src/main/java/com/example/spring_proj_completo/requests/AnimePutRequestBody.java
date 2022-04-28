package com.example.spring_proj_completo.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnimePutRequestBody {

    private Long id;
    private String name;
}
