package com.alr.products.controller;

import com.alr.products.service.FileProcessorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequestMapping(path = "/file")
@Api(value ="File Upload", tags = "Frontend to upload the product lists and process by RabbitMQ")
public class FileUploadController {

  private FileProcessorService fileProcessorService;

  public FileUploadController(@Autowired FileProcessorService fileProcessorService) {
    this.fileProcessorService = fileProcessorService;
  }

  @ApiOperation(value = "Upload the Product List", response = String.class)
  @ApiResponses(value = {
    @ApiResponse(code = 200, message = "Successfully retrieved product"),
    @ApiResponse(code = 400, message = "Bad argument, check the values and try again")
  })
  @PostMapping(value = "/upload", produces = MediaType.APPLICATION_JSON_VALUE)
  public String productList(final @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    if (file == null) {
      redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
      return "redirect:/file/uploadStatus";
    }

    try {
      fileProcessorService.processFile(file);

      redirectAttributes.addFlashAttribute("message","File uploaded '" + file.getOriginalFilename() + "'");
    } catch (IOException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }

    return "redirect:/file/uploadStatus";
  }

  @GetMapping("/")
  public String index() {
    return "upload";
  }

  @GetMapping("/uploadStatus")
  public String uploadStatus() {
    return "uploadStatus";
  }

  @ExceptionHandler(MultipartException.class)
  public String handleError1(MultipartException e, RedirectAttributes redirectAttributes) {

    redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
    return "redirect:/file/uploadStatus";
  }
}
