package com.camilasoares.cursomc.dto;



import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;

public class EmailDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "Preenchimento obrigatório")
    @Email(message = "Informe um email válido")
    private String email;

    public EmailDTO(){}

    public String getEmail() {return email; }

    public void setEmail(String email) {
        this.email = email;
    }
}
