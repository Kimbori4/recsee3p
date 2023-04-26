package com.furence.recsee.wooribank.script.util.stream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.furence.recsee.wooribank.script.util.file.pdf.PDFHtmlConfig;

public class PDFDocumentTest {

	
	@Test(expected = NullPointerException.class)
	public void testBuilderException() {
		assertNull(PDFHtmlConfig.builder().build()); 
	}

	@Test
	public void testBuilder() {
		assertNotNull("Fail create pdfdocument", PDFHtmlConfig.builder().build()); 
	}
}
