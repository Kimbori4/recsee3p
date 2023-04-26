package com.furence.recsee.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.furence.recsee.common.model.Log;

public class CompreasZip {
//	/**
//	 * * @description 압축 메소드 *
//	 * 
//	 * @param path
//	 *            압축할 폴더 경로 *
//	 * @param outputFileName
//	 *            출력파일명
//	 */
//	public static boolean compress(String path, String outputPath, String outputFileName) throws Throwable {
//		// 파일 압축 성공 여부
//		boolean isChk = false;
//		File file = new File(path);
//		// 파일의 .zip이 없는 경우, .zip 을 붙여준다.
//		int pos = outputFileName.lastIndexOf(".") == -1 ? outputFileName.length() : outputFileName.lastIndexOf(".");
//		// outputFileName .zip이 없는 경우
//		if (!outputFileName.substring(pos).equalsIgnoreCase(".zip")) {
//			outputFileName += ".zip";
//		} // 압축 경로 체크
//		if (!file.exists()) {
//			throw new Exception("Not File!");
//		}
//		// 출력 스트림
//		FileOutputStream fos = null;
//		// 압축 스트림
//		ZipOutputStream zos = null;
//		try {
//			fos = new FileOutputStream(new File(outputPath +"/" +outputFileName));
//			zos = new ZipOutputStream(fos);
//			// 디렉토리 검색를 통한 하위 파일과 폴더 검색
//			searchDirectory(file, file.getPath(), zos);
//			// 압축 성공.
//			isChk = true;
//		} catch (Throwable e) {
//			throw e;
//		} finally {
//			if (zos != null)
//				zos.close();
//			if (fos != null)
//				fos.close();
//		}
//		return isChk;
//	}
//
//	/**
//	 * * @description 디렉토리 탐색 * @param file 현재 파일 * @param root 루트 경로 * @param zos
//	 * 압축 스트림
//	 */
//	private static void searchDirectory(File file, String root, ZipOutputStream zos) throws Exception {
//		// 지정된 파일이 디렉토리인지 파일인지 검색
//		if (file.isDirectory()) {
//			// 디렉토리일 경우 재탐색(재귀)
//			File[] files = file.listFiles();
//			for (File f : files) {
//				//System.out.println("file = > " + f);
//				searchDirectory(f, root, zos);
//			}
//		} else {
//			// 파일일 경우 압축을 한다.
//			try {
//				compressZip(file, root, zos);
//			} catch (Throwable e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/**
//	 * * @description압축 메소드 * @param file * @param root * @param zos * @throws
//	 * Throwable
//	 */
//	public static void compressZip(File file, String root, ZipOutputStream zos) throws Throwable {
//		FileInputStream fis = null;
//		try {
//			String zipName = file.getPath().replace(root + "\\", "");
//			// 파일을 읽어드림
//			fis = new FileInputStream(file);
//			// Zip엔트리 생성(한글 깨짐 버그)
//			ZipEntry zipentry = new ZipEntry(zipName);
//			// 스트림에 밀어넣기(자동 오픈)
//			zos.putNextEntry(zipentry);
//			int length = (int) file.length();
//			byte[] buffer = new byte[length];
//			// 스트림 읽어드리기
//			fis.read(buffer, 0, length);
//			// 스트림 작성
//			zos.write(buffer, 0, length);
//			// 스트림 닫기
//			zos.closeEntry();
//		} catch (Throwable e) {
//			throw e;
//		} finally {
//			if (fis != null)
//				fis.close();
//		}
//	}
//	
//	/**
//     * 디렉토리 및 파일을 압축한다.
//     * @param path 압축할 디렉토리 및 파일
//     * @param toPath 압축파일을 생성할 경로
//     * @param fileName 압축파일의 이름
//     */
//    public static void createZipFile(String path, String toPath, String fileName) {
// 
//        File dir = new File(path);
//        String[] list = dir.list();
//        String _path;
// 
//        if (!dir.canRead() || !dir.canWrite())
//            return;
// 
//        int len = list.length;
// 
//        if (path.charAt(path.length() - 1) != '/')
//            _path = path + "/";
//        else
//            _path = path;
// 
//        try {
//            ZipOutputStream zip_out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(toPath+"/"+fileName), 2048));
// 
//            for (int i = 0; i < len; i++)
//                zip_folder("",new File(_path + list[i]), zip_out, path);
// 
//            zip_out.close();
// 
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
// 
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
// 
// 
//        }
//    }
// 
//    /**
//     * ZipOutputStream를 넘겨 받아서 하나의 압축파일로 만든다.
//     * @param parent 상위폴더명
//     * @param file 압축할 파일
//     * @param zout 압축전체스트림
//     * @throws IOException
//     */
//    private static void zip_folder(String parent, File file, ZipOutputStream zout, String ReplacePath) throws IOException {
//        byte[] data = new byte[2048];
//        int read;
// 
//        if (file.isFile()) {
//            ZipEntry entry = new ZipEntry(parent + file.getName());
//            zout.putNextEntry(entry);
//            BufferedInputStream instream = new BufferedInputStream(new FileInputStream(file));
// 
//            while ((read = instream.read(data, 0, 2048)) != -1)
//                zout.write(data, 0, read);
// 
//            zout.flush();
//            zout.closeEntry();
//            instream.close();
// 
//        } else if (file.isDirectory()) {
//            String parentString = file.getPath().replaceAll("\\\\", "/").replace(ReplacePath,"");
//            parentString = parentString.substring(0,parentString.length() - file.getName().length());
//            ZipEntry entry = new ZipEntry(parentString+file.getName()+"/");
//            zout.putNextEntry(entry);
// 
//            String[] list = file.list();
//            if (list != null) {
//                int len = list.length;
//                for (int i = 0; i < len; i++) {
//                    zip_folder(entry.getName(),new File(file.getPath() + "/" + list[i]), zout, ReplacePath);
//                }
//            }
//        }
//    }
// 
//    /**
//     * 압축을 해제 한다
//     *
//     * @param zip_file
//     * @param directory
//     */
//    public static boolean extractZipFiles(String zip_file, String directory) {
//        boolean result = false;
// 
//        byte[] data = new byte[2048];
//        ZipEntry entry = null;
//        ZipInputStream zipstream = null;
//        FileOutputStream out = null;
// 
//        if (!(directory.charAt(directory.length() - 1) == '/'))
//            directory += "/";
// 
//        File destDir = new File(directory);
//        boolean isDirExists = destDir.exists();
//        boolean isDirMake = destDir.mkdirs();
// 
//        try {
//            zipstream = new ZipInputStream(new FileInputStream(zip_file));
// 
//            while ((entry = zipstream.getNextEntry()) != null) {
// 
//                int read = 0;
//                File entryFile;
// 
//                //디렉토리의 경우 폴더를 생성한다.
//                if (entry.isDirectory()) {
//                    File folder = new File(directory+entry.getName());
//                    if(!folder.exists()){
//                        folder.mkdirs();
//                    }
//                    continue;
//                }else {
//                    entryFile = new File(directory + entry.getName());
//                }
// 
//                if (!entryFile.exists()) {
//                    boolean isFileMake = entryFile.createNewFile();
//                }
// 
//                out = new FileOutputStream(entryFile);
//                while ((read = zipstream.read(data, 0, 2048)) != -1)
//                    out.write(data, 0, read);
// 
//                zipstream.closeEntry();
// 
//            }
// 
//            result = true;
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            result = false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            result = false;
//        } finally {
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
// 
//            if (zipstream != null) {
//                try {
//                    zipstream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
// 
//        return result;
//    }
}
