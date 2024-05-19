package net.artux.pdanetwork.models.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String title;
    private List<ErrorModel> details;
}
