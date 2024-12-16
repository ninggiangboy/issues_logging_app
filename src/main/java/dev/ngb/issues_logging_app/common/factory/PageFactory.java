package dev.ngb.issues_logging_app.common.factory;

import dev.ngb.issues_logging_app.common.model.PageData;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageFactory {
    public static <T> PageData<T> createEmptyPage() {
        return PageData.<T>builder()
                .data(List.of())
                .metadata(PageData.PageMetadata.builder()
                        .page(0)
                        .size(0)
                        .totalPage(0)
                        .totalElement(0)
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
