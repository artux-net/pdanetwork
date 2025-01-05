package net.artux.pdanetwork.dto.page;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@Builder
public class ResponsePage<T> {

    private int lastPage;
    private List<T> content;
    private int contentSize;
    private Long totalSize;
    private int number;
    private int size;
    private Sort.Direction sortDirection;
    private String sortBy;

    public static <T> ResponsePage<T> of(Page<T> page) {
        var pageBuilder = ResponsePage
                .<T>builder()
                .lastPage(page.getTotalPages())
                .content(page.getContent())
                .contentSize(page.getContent().size())
                .totalSize(page.getTotalElements())
                .number(page.getNumber() + 1) // +1 так как отсчет идет от 0
                .size(page.getSize());

        var sortIterator = page.getSort().stream().iterator();
        if (sortIterator.hasNext()) {
            var nextItem = sortIterator.next();
            pageBuilder.sortBy(nextItem.getProperty())
                    .sortDirection(nextItem.getDirection());
        }

        return pageBuilder.build();
    }
}
