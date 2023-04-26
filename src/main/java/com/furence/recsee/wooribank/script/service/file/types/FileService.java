package com.furence.recsee.wooribank.script.service.file.types;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import com.furence.recsee.wooribank.script.param.request.FileDownloadParam;
import com.furence.recsee.wooribank.script.param.request.base.FileParam;
import com.furence.recsee.wooribank.script.repository.dao.FileContentDao;

import lombok.Getter;
import lombok.Setter;


public abstract class FileService<T1, T2 extends FileParam> {
	
	
	@Value("#{scriptManageProperties['file.path']}")
	protected String filePath;
	
	@Value("#{scriptManageProperties['pdf.font.path']}")
	protected String fontPath;
	
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	protected FileContentDao fileContentDao;	
		
	public FileService(FileContentDao fileContentDao) {
		this.fileContentDao = fileContentDao;
	}
	/**
	 * 엑셀 파일 만들기
	 * @param t1
	 * @param t2
	 * @return
	 */
	protected abstract File makeExcel(T1 t1, T2 t2); 
	
	/**
	 * PDF 파일 만들기
	 * @param t1
	 * @param t2
	 * @return
	 */
	protected abstract File makePDF(T1 t1, T2 t2);
	
	/**
	 * 파일 내용을 가져옴
	 * @param param
	 * @return
	 */
	protected abstract T1 getFileContent(T2 param);
	
	
	/**
	 * 타입별로 구분
	 * @param fileType
	 * @return
	 */
	private FileMakerFuntion<T1, T2> getFileMaker(T2 param) {
		
		switch(param.getFileType()) {
		case EXCEL:			
			return ( a , b) -> { return makeExcel(a, b); }; 	
			
		case PDF:			
			return ( a , b) -> { return makePDF(a, b); };
		}
		return null;
	}
	
	/**
	 * 파일 생성 인터페이스
	 * @param param
	 * @return
	 */
	public File createFile(T2 param) throws RuntimeException {		
		
		T1 content = Optional.ofNullable(getFileContent(param))
						.orElseThrow(() -> new RuntimeException("Content is null"));

		return Optional.ofNullable(getFileMaker(param))
				.orElseThrow(() -> new RuntimeException("Doesn't support this resource type"))
				.makeFile(content, param);
	}
	
	@Getter
	@Setter
	public FileServiceType type;
	
	/**
	 * 파일 타입이 추가되어야하면 여기에서 우선 추가되어야 함
	 * enum을 통해 전달될 인자들의 클래스 타입을 지정함.
	 * @author woori
	 *
	 */
	@Getter
	public enum FileServiceType{
		
		History		( FileDownloadParam.VersionSnapshot.class ),		
		ScriptInfo	( FileDownloadParam.ScriptCurrentInfo.class ),
		CallScript	( FileDownloadParam.ScriptCallInfo.class );
		
		private Class<? extends FileParam> clazz;
		
		FileServiceType (Class<? extends FileParam> clazz){
			this.clazz = clazz;
		}
		
	}
	
	/**
	 * 파일 서비스에 파라미터를 전달하기 위한 Bean
	 * 해당 클래스를 인스턴스화해서 createFile 메소드의 인자로 전달하면 됨
	 * @author woori
	 *
	 */	
	public static class ProviderParam<T extends FileParam> {		
		/**
		 * 파일 서비스 타입
		 */
		@Getter
		private FileServiceType type;
		
		
		@Getter
		private T paramter;
		
		/**
		 * 
		 * @param type
		 * @throws Exception
		 */
		private ProviderParam(FileServiceType type) throws Exception{
			
			this.type = type;
			this.paramter = createParameter();
		}
		
		/**
		 * 생성자 팩토리 메서드
		 * @param <U>
		 * @param type
		 * @return
		 * @throws Exception
		 */
		public static <U extends FileParam> ProviderParam<U> of(FileServiceType type) 
				throws Exception {
			return new ProviderParam<U>(type);
		}
		
		/**
		 * 파라미터 생성(내부)
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("unchecked")
		private T createParameter() throws Exception {
			Class<T> clazz = (Class<T>) this.type.getClazz();
			return clazz.getConstructor().newInstance();
		}
	}
}



