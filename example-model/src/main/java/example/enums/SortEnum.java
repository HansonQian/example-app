package example.enums;

import lombok.Getter;

/**
 * 排序方式
 */
@Getter
public enum SortEnum {

    /**
     * 升序
     */
    SORT_ASC(0, "ASC"),
    /**
     * 降序
     **/
    SORT_DESC(1, "DESC");

    private int code;

    private String sort;

    public static SortEnum getEnumByCode(int code) {
        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.getCode() == code) {
                return sortEnum;
            }
        }
        return null;
    }

    private SortEnum(int code, String sort) {
        this.code = code;
        this.sort = sort;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
