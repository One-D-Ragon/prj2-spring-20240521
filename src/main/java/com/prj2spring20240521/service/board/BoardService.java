package com.prj2spring20240521.service.board;

import com.prj2spring20240521.domain.board.Board;
import com.prj2spring20240521.mapper.board.BoardMapper;
import com.prj2spring20240521.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper mapper;
    private final MemberMapper memberMapper;

    // authentication에 사용자 정보가 들어있음, sub로 넘겨준 이메일이 들어있음
    public void add(Board board, Authentication authentication) {
        board.setMemberId(Integer.valueOf(authentication.getName()));
        mapper.insert(board);
    }

    public boolean validate(Board board) {
        // 제목, 본문, 작성자가 비어있으면 리턴값을 false로 줌
        if (board.getTitle() == null || board.getTitle().isBlank()) {
            return false;
        }
        if (board.getContent() == null || board.getContent().isBlank()) {
            return false;
        }

        return true;
    }

    public Map<String, Object> list(Integer page, String searchType, String keyword) {
        Map pageInfo = new HashMap();
        Integer countAll = mapper.countAllWithSearch(searchType, keyword);

        Integer offset = (page - 1) * 10;
        Integer lastPageNumber = (countAll - 1) / 10 + 1;
        Integer leftPageNumber = (page - 1) / 10 * 10 + 1;
        Integer rightPageNUmber = leftPageNumber + 9;
        rightPageNUmber = Math.min(rightPageNUmber, lastPageNumber);
        // 마지막 페이지네이션이 10개가 안되면 왼쪽을 채워 넣음
        leftPageNumber = rightPageNUmber - 9;
        leftPageNumber = Math.max(leftPageNumber, 1);

        // 이전, 처음, 다음, 맨끝 버튼 만들기
        Integer prevPageNumber = leftPageNumber - 1;
        Integer nextPageNumber = rightPageNUmber + 1;
        if (prevPageNumber > 0) {
            pageInfo.put("prevPageNumber", prevPageNumber);
        }
        if (nextPageNumber <= lastPageNumber) {
            pageInfo.put("nextPageNumber", nextPageNumber);
        }

        // 현재, 마지막 페이지 넘버
        pageInfo.put("currentPageNumber", page);
        pageInfo.put("lastPageNumber", lastPageNumber);
        pageInfo.put("leftPageNumber", leftPageNumber);
        pageInfo.put("rightPageNumber", rightPageNUmber);

        return Map.of("pageInfo", pageInfo, "boardList", mapper.selectAllPaging(offset, searchType, keyword));
    }

    public Board get(Integer id) {
        return mapper.selectById(id);
    }

    public void remove(Integer id) {
        mapper.deleteById(id);
    }

    public void edit(Board board) {
        mapper.update(board);
    }

    public boolean hasAccess(Integer id, Authentication authentication) {
        Board board = mapper.selectById(id);

        return board.getMemberId()
                .equals(Integer.valueOf(authentication.getName()));
    }
}
