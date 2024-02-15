package ru.sberbank.jd.controller.dto;

import lombok.Getter;

@Getter
public class ChangeAccountBalance {
    private String numberAccount;
    private double changeBalance;
}
