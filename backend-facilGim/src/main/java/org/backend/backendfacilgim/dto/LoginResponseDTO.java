package org.backend.backendfacilgim.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String mensaje;
    private String token;
    private String username;
    private List<String> authorities;
    private Integer userId;
}
