package dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
@Builder
public class PageableDto<T> {
    private List<T> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Long total;
    private Integer totalPages;
}
