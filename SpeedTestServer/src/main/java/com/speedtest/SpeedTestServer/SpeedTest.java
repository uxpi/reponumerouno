package com.speedtest.SpeedTestServer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@RestController
public class SpeedTest {
    
    @RequestMapping("/")
    public String index() {
    	return "Up and running again";
    }
        
    @Value("gs://axiomatic-lamp-176915/file_200_MB.bin")
    private Resource gcsFile;
    
    @RequestMapping(value="/download", method=RequestMethod.GET)
	public String download(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (gcsFile.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(gcsFile.getFilename());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + gcsFile.getFilename() + "\""));
			response.setContentLength((int) gcsFile.contentLength());
			FileCopyUtils.copy(gcsFile.getInputStream(), response.getOutputStream());

			return "Download";
		}
		return "File not found";
	}
		
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public String upload(@RequestBody MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws EOFException {
		if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            byte[] bytes = file.getBytes();
            //Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            //Files.write(path, bytes);
            
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        }  catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }
    
}
