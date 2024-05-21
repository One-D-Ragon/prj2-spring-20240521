package com.prj2spring20240521.controller.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
