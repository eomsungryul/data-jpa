package study.datajpa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberDto;
import study.datajpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {
	private final MemberRepository memberRepository;

	// 도메인 클래스 컨버터 사용전
	@GetMapping("/members/{id}")
	public String findMember(@PathVariable("id") Long id) {
		Member member = memberRepository.findById(id).get();
		return member.getUsername();
	}

	// 도메인 클래스 컨버터 사용후
	// 근데 권장안함.. 간단할때는 씀 복잡할때는 못씀
	@GetMapping("/members2/{id}")
	public String findMember2(@PathVariable("id") Member member) { // 조회용으로만 써야함 그냥 쓰지마라!
		return member.getUsername();
	}

	@GetMapping("/members")
	// 파라미터 정보 /members?page=0&size=3&sort=id,desc&sort=username,desc
	public Page<Member> list(Pageable pageable) {
		Page<Member> page = memberRepository.findAll(pageable);
		return page;
	}
	

	@GetMapping("/members2")
	// 파라미터 정보 /members?page=0&size=3&sort=id,desc&sort=username,desc
	public Page<MemberDto> list2(@PageableDefault(size = 5) Pageable pageable) {
//		Page<Member> page = memberRepository.findAll(pageable);
//		Page<MemberDto> pageDto = page.map(MemberDto::new);
//		return pageDto;
		return memberRepository.findAll(pageable).map(MemberDto::new);
	}
	//페이지 인덱스는 0인게 불만이면 1로 바꿀수는 있지만 Pageable에서와 데이터가 다르기때문에 그냥 0으로 쓰는게 좋음
}