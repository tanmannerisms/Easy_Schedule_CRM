package com.utils;

public abstract class Query {
    abstract void select();
    abstract int insert();
    abstract int update();
    abstract int delete();
}
