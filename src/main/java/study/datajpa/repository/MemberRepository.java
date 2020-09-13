package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

	@Query(name = "Member.findByUsername") // 네임드 쿼리 잘 안씀 ㅋㅋ
	// 우선 순위 1. @쿼리 찾고 2. 네임드쿼리 찾고 엔티티쪽 3. 그냥 관례에 따라 메서드 맞게 처리
	// 잘안쓰는 이유. 불~편~ 그래서 @query에서 함
	// 근데 큰 장점이 하나 있긴함 컴파일러에서 잡아줌
	List<Member> findByUsername(@Param("username") String username);

	@Query("select m from Member m where m.username= :username and m.age = :age")
	// 장점 > 오타 쳤을 경우 바로 컴파일 오류남
	// 동적 쿼리는 그냥 쿼리 dsl로 하셈 ㅎ
	List<Member> findUser(@Param("username") String username, @Param("age") int age);

	@Query("select m.username from Member m")
	List<String> findUsernameList();

	//new 생성하는거 불편하면 무조건 쿼리dsl
	@Query("select new study.datajpa.repository.MemberDto(m.id, m.username, t.name)" 
			+ "from Member m join m.team t")
	List<MemberDto> findMemberDto();
	
	//무조건 이름기반으로해라 ...
	@Query("select m from Member m where m.username = :name")
	 Member findMembers(@Param("name") String username);
	
	//컬렉션 기반하면 in으로 알아서 잘 됨 ㅎㅎ
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") List<String> names);
	
	List<Member> findListByUsername(String name); //컬렉션
	Member findMemberByUsername(String name); //단건
	Optional<Member> findOptionalByUsername(String name); //단건 Optional
	
}