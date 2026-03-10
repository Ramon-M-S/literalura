package com.ramon.literalura.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> tClass);
}
