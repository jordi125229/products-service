package adapters.mappers;

import dto.PageableDto;
import model.PagePojo;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public class PageMapper {
    public static <E, P> PagePojo<P> toPojo(Page<E> page, Function<E, P> mapper) {
        return new PagePojo<>(
                page.getContent()
                        .stream()
                        .map(mapper)
                        .toList(),
                page.getSize(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public static <E, P> PageableDto<P> toDto(PagePojo<E> page, Function<E, P> mapper) {
        return new PageableDto<>(
                page.getContent()
                        .stream()
                        .map(mapper)
                        .toList(),
                page.getPageSize(),
                page.getPageNumber(),
                page.getTotal(),
                page.getTotalPages()
        );
    }
}
