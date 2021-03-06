package springboot_demo.springcloud.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

public class DocHelpUtils {
	
	
	public static File mutifile2File(MultipartFile mfile) throws Exception{
		// 获取文件名 
    	String fileName = mfile.getOriginalFilename(); 
    	// 获取文件后缀 
    	String prefix=fileName.substring(fileName.lastIndexOf(".")); 
    	// 用uuid作为文件名，防止生成的临时文件重复 
    	final File file = File.createTempFile(UUID.randomUUID().toString(), prefix); 
    	// MultipartFile to File 
    	mfile.transferTo(file); 
    	return file;
	}

	
	/**
	 * 转换docx
	 * @param filePath
	 * @param fileName
	 * @param htmlName
	 * @throws Exception
	 */
	public static void docx(String filePath ,String fileName,String htmlName) throws Exception{
		final String file = filePath + fileName;
		File f = new File(file);  
		// ) 加载word文档生成 XWPFDocument对象
		InputStream in = new FileInputStream(f);
		XWPFDocument document = new XWPFDocument(in);
		// ) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
		File imageFolderFile = new File(filePath);
		XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
		options.setExtractor(new FileImageExtractor(imageFolderFile));
		options.setIgnoreStylesIfUnused(false);
		options.setFragment(true);
		// ) 将 XWPFDocument转换成XHTML
		OutputStream out = new FileOutputStream(new File(filePath + htmlName));
		XHTMLConverter.getInstance().convert(document, out, options);
	}
	/**
	 * 转换doc
	 * @param filePath
	 * @param fileName
	 * @param htmlName
	 * @throws Exception
	 */
	public static void dox(String filePath ,String fileName,String htmlName) throws Exception{
        final String file = filePath + fileName;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
       //解析word文档
       wordToHtmlConverter.processDocument(wordDocument);
       org.w3c.dom.Document htmlDocument = wordToHtmlConverter.getDocument();
       
       File htmlFile = new File(filePath + htmlName);
       OutputStream outStream = new FileOutputStream(htmlFile);
 
       DOMSource domSource = new DOMSource(htmlDocument);
       StreamResult streamResult = new StreamResult(outStream);
 
       TransformerFactory factory = TransformerFactory.newInstance();
       Transformer serializer = factory.newTransformer();
       serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
       serializer.setOutputProperty(OutputKeys.INDENT, "yes");
       serializer.setOutputProperty(OutputKeys.METHOD, "html");
       
       serializer.transform(domSource, streamResult);
       outStream.close();
   }
	
	/** 
	 * 
	 *删除
	 * @param files 
	 *
	 */
	public static void deleteFile(File... files) {
		for (File file : files) {
			if (file.exists()) {
				file.delete(); 
			} 
		} 
	}

}
