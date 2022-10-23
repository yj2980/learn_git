package board_example.board_01.controller;

import board_example.board_01.dto.BoardDto;
import board_example.board_01.dto.FileDto;
import board_example.board_01.service.BoardService;
import board_example.board_01.service.FileService;
import board_example.board_01.util.MD5Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
public class BoardController {
    private BoardService boardService;
    private FileService fileService;

    public BoardController(BoardService boardService, FileService fileService)
    {
        this.boardService = boardService;
        this.fileService = fileService;
    }
    @GetMapping("/")
    public String list(Model model) {
        //글 목록 가져오기
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/list.html";
    }

    @GetMapping("/post")
    public String post()
    {
        return "board/post.html";
    }

    //http post 요청을 처리
    //http post? : 서버로 데이터를 전송 post 요청은 보통 html 양식을 통해 서버로 전송하며, 서버에 변경사항을 만듦.
    @PostMapping("/post")
    public String write(@RequestParam("file") MultipartFile files, BoardDto boardDto) {
        try {
            String origFilename = files.getOriginalFilename();
            log.info("empty? " + origFilename);
            if (!origFilename.isEmpty())
            {
                log.info("insert into file table");
                String filename = new MD5Generator(origFilename).toString();
                log.info("filename is " + filename);
                /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
                String savePath = System.getProperty("user.dir") + "\\files";
                /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath + "\\" + filename;
                files.transferTo(new File(filePath));

                FileDto fileDto = new FileDto();
                fileDto.setOrigFilename(origFilename);
                fileDto.setFilename(filename);
                fileDto.setFilePath(filePath);

                Long fileId = fileService.saveFile(fileDto);
                boardDto.setFileId(fileId);
            }
            log.info("inset into board table");
            boardService.savePost(boardDto);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    //게시판 글 보기
    @GetMapping("/post/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);

        if (boardDto.getFileId() != null) {
            FileDto fileDto = fileService.getFile(boardDto.getFileId());
            model.addAttribute("file", fileDto);
        }
        return "board/detail.html";
    }

    //HTTP GET : 특정한 리소스를 가져오도록 요청
    //조회, 요청시 사용
    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/edit.html";
    }

    //주어진 URI 표현식과 일치하는 HTTP PUT 요청을 처리
    //HTTP PUT : 요청 페이로드를 사용해 새로운 리소스를 생성하거나, 대상 리소스를 나타내는 데이터를 대체
    // 수정시 사용한다.
    @PutMapping("/post/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/";
    }
    //삭제 시
    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "file_id", required = false) Long fileId) {
        log.info("cal check >> " + fileId);
        boardService.deletePost(id);
        if(fileId != null)
            fileService.deletePost(fileId);
        return "redirect:/";
    }
    //파일 다운로드
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
                .body(resource);
    }
}

