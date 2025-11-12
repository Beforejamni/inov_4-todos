package study.todos.common.dto;

//Pagination 객체 정의
public class Pagination {
    //현재 페이지
    private Integer page;
    //페이지 크기
    private Integer size;
    //현재 페이지에 있는 요소 개수
    private Integer currentElement;
    //총 페이지 수
    private Integer totalPage;
    //전체 데이터 수
    private Long totalElement;

    protected Pagination() {}

    public Pagination(Integer page, Integer size, Integer currentElement, Integer totalPage, Long totalElement) {
        this.page = page;
        this.size = size;
        this.currentElement = currentElement;
        this.totalPage = totalPage;
        this.totalElement = totalElement;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getCurrentElement() {
        return currentElement;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Long getTotalElement() {
        return totalElement;
    }
}
