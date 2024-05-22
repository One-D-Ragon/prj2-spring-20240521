package com.prj2spring20240521.controller.member;

import com.prj2spring20240521.domain.member.Member;
import com.prj2spring20240521.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    final MemberService service;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody Member member) {
        // 입력한 회원가입정보가 유효한지 확인
        if (service.validate(member)) {
            service.add(member);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "check", params = "email")
    public ResponseEntity checkEmail(@RequestParam("email") String email) {
        System.out.println("email = " + email);
        Member member = service.getByEmail(email);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member.getEmail());
    }

    @GetMapping(value = "check", params = "nickName")
    public ResponseEntity checkNickName(@RequestParam("nickName") String nickName) {
        System.out.println("nickName = " + nickName);
        Member member = service.getByNickName(nickName);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member.getNickName());

        /* practice/nickNameCheck에서 200 <-> 404 바꿔서 확인할 때 사용하는 코드
        if (member == null) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        */
    }

    @GetMapping("list")
    public List<Member> list() {
        return service.list();
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        Member member = service.getById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(member);
        }
    }
}
