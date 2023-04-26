package com.furence.recsee.wooribank.script.service.file.types;

import java.io.File;

@FunctionalInterface
public interface FileMakerFuntion<T1, T2> {
	public abstract File makeFile(T1 data, T2 param);
}