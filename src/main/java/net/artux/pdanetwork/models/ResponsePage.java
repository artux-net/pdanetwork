package net.artux.pdanetwork.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
public class ResponsePage<T> {

    private int lastPage;
    private List<T> data;
    private int dataSize;
    private Long queryDataSize;
    private int number;
    private int size;
    private Sort.Direction sortDirection;
    private String sortBy;
}
