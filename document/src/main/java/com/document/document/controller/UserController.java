package com.document.document.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.document.document.entity.DocumentType;
import com.document.document.entity.Response;
import com.document.document.entity.User;
import com.document.document.entity.UserDocument;
import com.document.document.repository.UserDocumentRepository;
import com.document.document.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

    public final String UPLOAD_DIR = "E:\\neofruition\\document\\src\\main\\resources\\static\\image";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDocumentRepository userDocumentRepository;

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {

        if (user == null) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } else {
            User user1 = this.userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(user1);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {

        User user = this.userRepository.findByUid(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }

    }

    @PostMapping("/add-document/{id}")
    public ResponseEntity<?> add_document(@PathVariable int id, @RequestParam MultipartFile file,
            @ModelAttribute DocumentType documentType) {

        try {
            User user = this.userRepository.findByUid(id);
            UserDocument userDocument = new UserDocument();

            if (!file.isEmpty()) {

                if (user.getUserDocuments() == null) {

                    // LocalTime localTime = LocalTime.now();

                    // String filename = localTime.toString() + file.getOriginalFilename();

                    String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                            + file.getOriginalFilename();

                    userDocument.setImage(filename);

                    // File savefile = new ClassPathResource("/static/image").getFile();
                    // Path path = Paths.get(savefile.getAbsolutePath() + File.separator +
                    // file.getOriginalFilename());
                    Path path = Paths.get(UPLOAD_DIR + File.separator + filename);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                    userDocument.setD_name(documentType.getName());
                    userDocument.setFilesize((int) file.getSize());
                    userDocument.setUser(user);
                    userDocument.setContentType(file.getContentType());
                    // userDocument.setTime(localTime);
                    user.setUserDocuments(userDocument);

                    this.userRepository.save(user);

                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/download/" + id).toUriString();

                    return ResponseEntity.ok().body(new Response(file.getOriginalFilename(), fileDownloadUri,
                            file.getContentType(), file.getSize()));
                } else {

                    // File filedelet = new File(UPLOAD_DIR, oldDocument.getImage());
                    // filedelet.delete();

                    // oldDocument.setImage(file.getOriginalFilename());

                    // Path path = Paths.get(UPLOAD_DIR + File.separator +
                    // file.getOriginalFilename());
                    // Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    // oldDocument.setD_name(documentType.getName());
                    // oldDocument.setFilesize((int) file.getSize());
                    // oldDocument.setContentType(file.getContentType());
                    // LocalTime localTime = LocalTime.now();
                    // userDocument.setTime(localTime);
                    // oldDocument.setUser(user);

                    // user.setUserDocuments(oldDocument);

                    // this.userRepository.save(user);

                    // String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    // .path("/download/" + id).toUriString();

                    // return ResponseEntity.ok().body(new Response(file.getOriginalFilename(),
                    // fileDownloadUri,
                    // file.getContentType(), file.getSize()));

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("file is already uploded");
                }

            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("file is empty");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }

        // return ResponseEntity.status(HttpStatus.OK).body("file uploded");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestParam MultipartFile file,
            @ModelAttribute DocumentType documentType) {

        User user = this.userRepository.findByUid(id);
        UserDocument userDocument = this.userDocumentRepository.findUserDocumentByUser(id);

        if (user == null) {

            return ResponseEntity.status(HttpStatus.OK).body("user not uploded any document so you can't update");
        } else {

            try {

                File filedelet = new File(UPLOAD_DIR, userDocument.getImage());
                filedelet.delete();

                // LocalTime localTime = LocalTime.now();

                // String filename = localTime + file.getOriginalFilename();
                String filename = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                        + file.getOriginalFilename();
                userDocument.setImage(filename);
                Path path = Paths.get(UPLOAD_DIR + File.separator + filename);

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                userDocument.setD_name(documentType.getName());
                userDocument.setFilesize((int) file.getSize());
                userDocument.setUser(user);
                userDocument.setContentType(file.getContentType());
                // userDocument.setTime(localTime);
                user.setUserDocuments(userDocument);

                this.userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }

            return ResponseEntity.status(HttpStatus.OK).body("file is successfully updated");

        }

    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable int id, HttpServletRequest request) {

        UserDocument userDocument = this.userDocumentRepository.findUserDocumentByUser(id);
        File file = new File(UPLOAD_DIR, userDocument.getImage());

        Path path = Paths.get(UPLOAD_DIR).toAbsolutePath().resolve(userDocument.getImage());
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.ok().body("issue in reading the file");

        }

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;" + file.getName() + resource.getFilename())
                .body(resource);

        // return ResponseEntity.status(HttpStatus.OK).body(file);
    }

}
