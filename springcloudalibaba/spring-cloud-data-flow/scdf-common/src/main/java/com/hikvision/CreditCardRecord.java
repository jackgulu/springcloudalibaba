package com.hikvision;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CreditCardRecord {

    public static Map<String, String> cardTypes = new HashMap<>();
    public static Map<String, String> users = new HashMap<>();

    static {
        cardTypes.put("0", "CMB");  // 招行银行
        cardTypes.put("1", "ICBC"); // 工商银行
        cardTypes.put("2", "ABC");  // 农业银行
        cardTypes.put("3", "CCB");  // 建设银行
        cardTypes.put("4", "BCM");  // 交通银行
        cardTypes.put("5", "CMBC"); // 民生银行

        users.put("0", "jim");
        users.put("1", "jerry");
        users.put("2", "tom");
    }

    private String user;
    private BigDecimal cost;
    private String cardType;

    public CreditCardRecord() {
    }

    public CreditCardRecord(String user, BigDecimal cost, String cardType) {
        this.user = user;
        this.cost = cost;
        this.cardType = cardType;
    }

    public static Map<String, String> getCardTypes() {
        return cardTypes;
    }

    public static void setCardTypes(Map<String, String> cardTypes) {
        CreditCardRecord.cardTypes = cardTypes;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @Override
    public String toString() {
        return "CreditCardRecord{" +
                "user='" + user + '\'' +
                ", cost=" + cost +
                ", cardType='" + cardType + '\'' +
                '}';
    }
}
