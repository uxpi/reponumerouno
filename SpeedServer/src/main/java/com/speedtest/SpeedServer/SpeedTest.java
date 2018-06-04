package com.speedtest.SpeedServer;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
public class SpeedTest {
    
    @RequestMapping("/")
    public String index() {
        return "Up and running again";
    }
    
    private static final String EXTERNAL_FILE_PATH = "/home/belluxpi/reponumerouno/file_200_MB.bin";
    
    //home/belluxpi/gs-spring-boot/complete
    // /home/belluxpi/reponumerouno/file_200_MB.bin

    @RequestMapping(value="/download", method=RequestMethod.GET)
	public String download(HttpServletRequest request, HttpServletResponse response) throws IOException {

		File file = new File(EXTERNAL_FILE_PATH);
		if (file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());

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
