package example.enums;

import lombok.ToString;

@ToString
public enum TXMessageStatusEnum {
    /**
     * 等待消费
     */
    WAITING(0),

    /**
     * 已消费
     */
    OVER(1),

    /**
     * 死亡
     */
    DIE(2);

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private TXMessageStatusEnum(int status) {
        this.status = status;
    }

    public static TXMessageStatusEnum getEnumByCode(int status) {
        for (TXMessageStatusEnum statusEnum : TXMessageStatusEnum.values()) {
            if (statusEnum.getStatus() == status) {
                return statusEnum;
            }
        }
        return null;
    }


}
