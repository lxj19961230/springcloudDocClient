package springboot_demo.springcloud.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import springboot_demo.springcloud.handler.DocHelpUtils;

@RestController
public class FristController {

	@Value("${server.port}")
    String port;
	
	
    @RequestMapping("/hi")
    public String home(@RequestParam String name) {
        return "hi "+name+",i am from port:" +port;
    }
    
    @RequestMapping("/doc")
    public String getDoc(@RequestParam(value = "file") MultipartFile mfile) throws Exception{
    	
		File file = DocHelpUtils.mutifile2File(mfile);
		String name = file.getName().substring(0, file.getName().lastIndexOf("."));
    	String filePath = file.getPath().replaceAll(file.getName(), "");
		System.out.println(file.getName());
		if (file.getName().endsWith(".docx") || file.getName().endsWith(".DOCX")) {
			DocHelpUtils.docx(filePath ,file.getName(),name +".htm");
		}else{
			DocHelpUtils.dox(filePath ,file.getName(),name +".htm");
		}
		
    	DocHelpUtils.deleteFile(file);	
    	return "success";
    }
}
