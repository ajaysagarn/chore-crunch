package com.ajsa.dyrepo.repository.content.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.Resource;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReadContentResponse {

    String fileName;
    Resource content;

}
