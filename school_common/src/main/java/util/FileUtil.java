package util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	/**
	 * uploadFile
	 * @param uploadFile 为上传的文件
	 * @param rootPath  为文件存放的路径 如"E:\\images\\work";
	 * @param only  你自己唯一的东西
	 */

	public  static String  FileUpload(MultipartFile uploadFile,String rootPath,Object only) {
		
		String fileName = uploadFile.getOriginalFilename();//上传的文件名字 如a.jpg
		System.out.println(fileName);
		String suffix = fileName.substring(fileName.lastIndexOf(".")); //文件格式
		
		String tempFileName = UUID.randomUUID().toString()+DateToStringUtil.dataToString(new Date())+only+suffix;  //数据库存的名字
		//String tempFileName = UUID.randomUUID().toString()+suffix;
		System.out.println("tempFileName:"+tempFileName);
		

		File fileTemp = new File(rootPath);
		if(!fileTemp.exists()) {//判断目录是否存在，不存在则创建目录
			fileTemp.mkdir();
		}
		String fileAbPath = rootPath+"\\"+tempFileName;//存放文件的绝对路径
		//String fileAbPath = rootPath+tempFileName;
		File file = new File(fileAbPath);
		
		try {
			uploadFile.transferTo(file);//将上传的文件对象移到指定的位置去
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return tempFileName;
		
	}
}
