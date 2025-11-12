package study.todos.common.dto;

//API 응답 포맷
public class Api<T> {
    private T body;
    private Pagination pagination;

    public Api() {}
    public Api(T body, Pagination pagination) {
        this.body = body;
        this.pagination = pagination;
    }

    public T getBody() {
        return body;
    }

    public Pagination getPagination() {
        return pagination;
    }
}
