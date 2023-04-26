package com.furence.recsee.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dhtmlx.xml2excel.ExcelWriter;


@SuppressWarnings("serial")
public class ExcelGenerator extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String xml = req.getParameter("grid_xml");
		xml = URLDecoder.decode(xml, "UTF-8");

		xml = xml.replaceAll("\\x00", "");
		xml = xml.replaceAll("\\u0000", "");

		(new ExcelWriter()).generate(xml, resp, req);
	}

}