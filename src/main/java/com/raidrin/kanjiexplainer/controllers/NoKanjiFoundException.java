package com.raidrin.kanjiexplainer.controllers;

public class NoKanjiFoundException extends Throwable {
    public NoKanjiFoundException(String s) {
        super(s);
    }
}
