package net.artux.pdanetwork.models.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorModel {
    private String field;
    private String errorMessage;
}
