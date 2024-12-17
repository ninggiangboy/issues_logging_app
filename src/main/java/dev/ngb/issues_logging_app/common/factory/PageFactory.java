package dev.ngb.issues_logging_app.common.factory;

import dev.ngb.issues_logging_app.common.model.PageData;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageFactory {

    private static final int EMPTY_PAGE_NUMBER = 0;
    private static final int EMPTY_PAGE_SIZE = 0;
    private static final int EMPTY_TOTAL_PAGE = 0;
    private static final int EMPTY_TOTAL_ELEMENT = 0;

    public static <T> PageData<T> createEmptyPage() {
        return PageData.<T>builder()
                .data(List.of())
                .metadata(PageData.PageMetadata.builder()
                        .page(EMPTY_PAGE_NUMBER)
                        .size(EMPTY_PAGE_SIZE)
                        .totalPage(EMPTY_TOTAL_PAGE)
                        .totalElement(EMPTY_TOTAL_ELEMENT)
                        .build())
                .build();
    }

    public static <T> PageData<T> createPage(Page<T> page) {
        return PageData.<T>builder()
                .data(page.getContent())
                .metadata(PageData.PageMetadata.builder()
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalPage(page.getTotalPages())
                        .totalElement(page.getTotalElements())
                        .build()
                )
                .build();
    }
}
