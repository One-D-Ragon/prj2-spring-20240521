package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;

    @PostMapping("add")
    public ResponseEntity add(@RequestBody Board board) {

        // 제목, 본문, 작성자가 비어있으면 저장이 안되게 검사
        if (service.validate(board)) {
            service.add(board);
            return ResponseEntity.ok().build();
        } else {
            // 입력한 값이 비었을 경우 400 코드를 리턴함
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("list")
    public List<Board> list() {
        return service.list();
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

        return ResponseEntity.ok().build();
    }
}
