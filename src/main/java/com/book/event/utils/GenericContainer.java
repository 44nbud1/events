package com.book.event.utils;

import lombok.Data;

@Data
public class GenericContainer<T> {
    private  T value;

}