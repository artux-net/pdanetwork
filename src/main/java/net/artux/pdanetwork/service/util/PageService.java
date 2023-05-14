package net.artux.pdanetwork.service.util;

import net.artux.pdanetwork.models.page.QueryPage;
import net.artux.pdanetwork.models.page.ResponsePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PageService {

    public Pageable getPageable(QueryPage page) {
        return createPageableWithSort(page);
    }

    private Pageable createPageableWithSort(QueryPage page){
        Sort sort = Sort.by(page.getSortDirection(), page.getSortBy());
        return PageRequest.of(subtractOne(page.getNumber()), page.getSize(), sort);
    }

    private int subtractOne(int pageNumber) {
        int result = pageNumber - 1;
        if (result < 0) {
            throw new RuntimeException("Номер страницы должен быть не меньше 1!");
        }
        return result;
    }

    // Сортировка по нескольким полям в одну сторону
    public Pageable getPageableWithMultipleSort(String[] sortFields, QueryPage page){
        List<Sort.Order> orders = new ArrayList<>();
        Arrays.stream(sortFields)
                .forEach(field -> orders.add(new Sort.Order(page.getSortDirection(), field)));
        Sort multipleSort = Sort.by(orders);
        return PageRequest.of(subtractOne(page.getNumber()), page.getSize(), multipleSort);
    }

    // Основной маппинг всех данных для возврата результирующей страницы
    public <T,R> ResponsePage<R> mapDataPageToResponsePage(Page<T> dataPage, List<R> data){
        return ResponsePage
                .<R>builder()
                .lastPage(dataPage.getTotalPages())
                .content(data)
                .contentSize(data.size())
                .totalSize(dataPage.getTotalElements())
                .number(dataPage.getNumber()+1) // +1 так как отсчет идет от 0
                .size(dataPage.getSize())
                .sortBy(dataPage.getSort().stream().iterator().next().getProperty())
                .sortDirection(dataPage.getSort().stream().iterator().next().getDirection())
                .build();
    }
}
