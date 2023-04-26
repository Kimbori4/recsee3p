package com.furence.recsee.wooribank.script.util.file.pdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import lombok.Getter;
import lombok.ToString;
import lombok.var;

@var
@Getter
@ToString
public class PDFHtmlDocument {
	private static final Logger logger = LoggerFactory.getLogger(PDFHtmlDocument.class);
	final private static Rectangle SZIE_OF_DOCUMENT = PageSize.A4;
	final private static String FONT_ALIAS = "PDF-FONT";
	
	@Getter
	private PDFHtmlConfig config;
	
	private String content;
	
	private String cssStr;
	/**
	 * 
	 * @param pdf
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 * @throws DOMException 
	 * @throws UnsupportedEncodingException 
	 */
	private PDFHtmlDocument(PDFHtmlConfig config, String content) 
			throws UnsupportedEncodingException, DOMException, 
			SAXException, ParserConfigurationException, IOException {
		
		Objects.requireNonNull(config);
		Objects.requireNonNull(content);
		
		this.config = config;
		this.content = content;
	}
	
	/**
	 * 새로운 PDFDocument 를 생성
	 * @param pdf
	 * @param content
	 * @return
	 * @throws RuntimeException
	 */
	public static PDFHtmlDocument newDocument(PDFHtmlConfig pdf, String content) throws RuntimeException {
		try {
			return new PDFHtmlDocument(pdf, content);
		} catch (Exception e) {
			throw new RuntimeException("Can't create new PDF document.", e);
		}
	}
	
	
	
	/**
	 * tn
	 * @param content
	 * @throws UnsupportedEncodingException
	 * @throws SAXException
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	private HtmlDocument getContentHtml() 
			throws UnsupportedEncodingException, SAXException, DOMException, 
			ParserConfigurationException, IOException  {
		HtmlDocument htmlDocument = new HtmlDocument();
		htmlDocument.getBody().setAttribute("style", "font-family:"+FONT_ALIAS+";");
		htmlDocument.appendChildBody(this.content);		
		return htmlDocument;
	}
	
	
	/**
	 * Document 생성
	 * @param padding
	 */
	private Document createDocument() {
		
		PDFHtmlConfig.Padding margin = Optional.ofNullable(this.config.getPadding())
				.orElse(new PDFHtmlConfig.Padding(50,50,50,50));
		
		return new Document( SZIE_OF_DOCUMENT, 
				margin.getLeft(), 
				margin.getRight(), 
				margin.getTop(), 
				margin.getBottom()) ;
	}
	
	/**
	 * css  설정
	 * @param cssPath
	 */
	private StyleAttrCSSResolver getCssResolver() {
		
			String cssPath = this.config.getCssPath();
			FileInputStream fis = null;
			if(null == cssPath) return null;
					
			StyleAttrCSSResolver cssResolver = null;
			
			try {
				 	fis = new FileInputStream(cssPath);
					cssResolver = new StyleAttrCSSResolver();
					CssFile css = XMLWorkerHelper.getCSS(fis);
					cssResolver.addCss(css);
				
		}catch(Exception e) {
			logger.error("error", e.getMessage());
		}finally {
			if(fis != null) {
				try {
						fis.close();
				}catch(Exception e) {
					logger.error("error",e.getMessage());
				}
			}
		}
		
		return cssResolver;
	}
	
	/**
	 * 폰트 설정 
	 * @param fontPath
	 */
	private XMLWorkerFontProvider getFontProvider() {
		
		if(this.config.getFontPath() == null ) return null;
		
		XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
		fontProvider.register( this.config.getFontPath(), FONT_ALIAS);		
		
		return fontProvider;
	}
	
	
	private HtmlPipelineContext getHtmlContext() {
		
		CssAppliers cssAppliers = new CssAppliersImpl(getFontProvider());
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
		htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
		
		return htmlContext;
		
	}
	
	
	public void setStyle(String cssStr) {
		this.cssStr = cssStr;
	}
	
	/**
	 * 파일로 저장함
	 * @throws RuntimeException
	 */
	public void write(String savePath) throws RuntimeException {
		
		PdfWriter writer = null;
		Document document = null;
		StringReader reader = null;
		FileOutputStream fos = null;
		try {
			
			document = createDocument();
			fos =new FileOutputStream(savePath);
			writer = PdfWriter.getInstance(document,fos); 
			
			writer.setInitialLeading(12.5f);
			
			document.open();
			
			PdfWriterPipeline pdfPipeline = new PdfWriterPipeline(document, writer);
			HtmlPipeline htmlPipeline = new HtmlPipeline(getHtmlContext(), pdfPipeline);
			
			XMLWorker worker = null;
			CSSResolver cssResolver = getCssResolver();			
			if(cssResolver != null) {
				CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
				worker = new XMLWorker(cssPipeline, true);
			} else {
				worker = new XMLWorker(htmlPipeline, true);
			}
			
			XMLParser parser = new XMLParser(true, worker, Charset.forName("UTF-8"));			
			HtmlDocumentWriter htmlWriter = new HtmlDocumentWriter(getContentHtml());
			
			if(this.cssStr != null ) {
				htmlWriter.getHtmlDoc().addStyleSheet(cssStr);
			}
			
			String htmlText = htmlWriter.writeAsString(true);
			logger.info(htmlText);
			reader = new StringReader(htmlText);
			parser.parse(reader);
			
			document.close();
		} catch (Exception e) {
			logger.error("error", e);
			throw new RuntimeException("Can't create pdf",e);
		} finally {
			try {
				if(fos!=null) {
					fos.close();
				}
			}catch(IOException e) {
				logger.error("error",e.getMessage());
			}
			Optional.ofNullable(reader).ifPresent(StringReader::close);
			Optional.ofNullable(document).ifPresent(Document::close);
			Optional.ofNullable(writer).ifPresent(PdfWriter::close);
		}
	}
}
