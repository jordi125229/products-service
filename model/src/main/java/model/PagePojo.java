package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagePojo<T>  {
    private List<T> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Long total;
    private Integer totalPages;
}
