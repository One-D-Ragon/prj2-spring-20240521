package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(
            Authentication authentication,
            Board board,
            @RequestParam(value = "files[]", required = false) MultipartFile[] files) throws IOException {

        // 제목, 본문이 비어있으면 저장이 안되게 검사
        if (service.validate(board)) {
            service.add(board, files, authentication);
            return ResponseEntity.ok().build();
        } else {
            // 입력한 값이 비었을 경우 400 코드를 리턴함
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("list")
    public Map<String, Object> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(value = "type", required = false) String searchType,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
//        System.out.println("page = " + page);
        return service.list(page, searchType, keyword);
    }

    // /api/board/5
    // /api/board/6
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id,
                              Authentication authentication) {
        // 조회된 것이 없으면 null을 응답
        Map<String, Object> result = service.get(id, authentication);

        if (result.get("board") == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity delete(@PathVariable Integer id
            , Authentication authentication) {
        if (service.hasAccess(id, authentication)) {
            service.remove(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PutMapping("edit")
    public ResponseEntity edit(Board board,
                               @RequestParam(value = "removeFileList[]", required = false)
                               List<String> removeFileList,
                               @RequestParam(value = "addFileList[]", required = false)
                               MultipartFile[] addFileList,
                               Authentication authentication) throws IOException {

        if (!service.hasAccess(board.getId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.validate(board)) {
            service.edit(board, removeFileList, addFileList);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("like")
    @PreAuthorize("isAuthenticated()")
    public Map<String, Object> like(@RequestBody Map<String, Object> req,
                                    Authentication authentication) {

        return service.like(req, authentication);

    }
}
