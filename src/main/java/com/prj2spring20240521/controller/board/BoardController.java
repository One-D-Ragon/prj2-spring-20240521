package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity add(
            Authentication authentication,
            @RequestBody Board board) {
        // 제목, 본문이 비어있으면 저장이 안되게 검사
        if (service.validate(board)) {
            service.add(board, authentication);
            return ResponseEntity.ok().build();
        } else {
            // 입력한 값이 비었을 경우 400 코드를 리턴함
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("list")
    public List<Board> list(@RequestParam(defaultValue = "1") Integer page) {
        System.out.println("page = " + page);
        return service.list(page);
    }

    // /api/board/5
    // /api/board/6
    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        // 조회된 것이 없으면 null을 응답
        Board board = service.get(id);

        if (board == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(board);
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
    public ResponseEntity edit(@RequestBody Board board
            , Authentication authentication) {
        if (!service.hasAccess(board.getId(), authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (service.validate(board)) {
            service.edit(board);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}
