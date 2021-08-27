package net.artux.pdanetwork.service.util;

import lombok.RequiredArgsConstructor;
import net.artux.pdanetwork.models.QueryPage;
import net.artux.pdanetwork.models.SortedBy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SortService {

    private final PageService pageService;
    private final AnnotationService annotationService;

    //  Установка атрибутов для сортировки и получение Pageable для выборки
    public <T> Pageable getSortInfo(Class<T> dtoClazz, QueryPage page, String defaultSortField) {
        Pageable resultPage = null;
        String sortByFromRequest = page.getSortBy();
        // Если поле id - устанавливаем поле по умолчанию defaultSortField
        if (sortByFromRequest.equals("id")) {
            if (!Objects.isNull(defaultSortField)) {
                page.setSortBy(defaultSortField);
                sortByFromRequest = defaultSortField;
            }
        }

        SortedBy sortedBy = (SortedBy) annotationService.getAnnotationFromClassByAnnotationClass(dtoClazz, sortByFromRequest, SortedBy.class);
        String[] annotationValues = {};
        // Проверка аннотации SortedBy над полем
        if (!Objects.isNull(sortedBy)) {
            annotationValues = sortedBy.values();
        }
        // Аннотации нет
        if (annotationValues.length == 0) {
            resultPage = pageService.getPageable(page);
        }
        // Аннотация с одним значением
        if (annotationValues.length == 1) {
            page.setSortBy(annotationValues[0]);
            resultPage = pageService.getPageable(page);
        }
        // Аннотация с несколькими значениями
        if (annotationValues.length > 1) {
            resultPage = pageService.getPageableWithMultipleSort(sortedBy.values(), page);
        }
        return resultPage;
    }
}
