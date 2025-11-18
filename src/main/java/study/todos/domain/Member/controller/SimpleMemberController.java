package study.todos.domain.Member.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.todos.common.dto.Api;
import study.todos.domain.Member.dto.SimpleMemberReq;
import study.todos.domain.Member.dto.SimpleMemberRes;
import study.todos.domain.Member.dto.UpdateMemberReq;
import study.todos.domain.Member.service.SimpleMemberService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class SimpleMemberController {
    private final SimpleMemberService simpleMemberService;

    public SimpleMemberController(SimpleMemberService simpleMemberService){
        this.simpleMemberService = simpleMemberService;
    }

    @PostMapping()
    public ResponseEntity<SimpleMemberRes> saveMember(@RequestBody SimpleMemberReq req) {
        return new ResponseEntity<>(simpleMemberService.saveMember(req), HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SimpleMemberRes> findMemberById(@PathVariable Long memberId) {
        return ResponseEntity.ok(simpleMemberService.findMember(memberId));
    }

    @GetMapping()
    public ResponseEntity<List<SimpleMemberRes>> findMembers() {
        return ResponseEntity.ok(simpleMemberService.findMembers());
    }

    @GetMapping("/todo/{todoId}")
    public ResponseEntity<Api<List<SimpleMemberRes>>> findMemberByTodoId(@PathVariable Long todoId,
                                                                         @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(simpleMemberService.findMembersByTodoId(todoId, pageable));
    }

    @PostMapping("/update/{memberId}")
    public ResponseEntity<SimpleMemberRes> updateMemberById(@PathVariable Long memberId,
                                                            @RequestBody UpdateMemberReq req) {
        return ResponseEntity.ok(simpleMemberService.updateMember(memberId, req));
    }

    @DeleteMapping("/delete/{memberId}")
    public  ResponseEntity<Map<String, String>> deleteMember(@PathVariable Long memberId){
        return ResponseEntity.ok(simpleMemberService.deleteMember(memberId));
    }
}
