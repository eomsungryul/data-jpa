package study.datajpa.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import study.datajpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

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

	// new 생성하는거 불편하면 무조건 쿼리dsl
	@Query("select new study.datajpa.repository.MemberDto(m.id, m.username, t.name)" + "from Member m join m.team t")
	List<MemberDto> findMemberDto();

	// 무조건 이름기반으로해라 ...
	@Query("select m from Member m where m.username = :name")
	Member findMembers(@Param("name") String username);

	// 컬렉션 기반하면 in으로 알아서 잘 됨 ㅎㅎ
	@Query("select m from Member m where m.username in :names")
	List<Member> findByNames(@Param("names") List<String> names);

	List<Member> findListByUsername(String name); // 컬렉션

	Member findMemberByUsername(String name); // 단건

	Optional<Member> findOptionalByUsername(String name); // 단건 Optional

	// 페이징
	Page<Member> findByAge(int age, Pageable pageable);

	// count 쿼리를 다음과 같이 분리할 수 있음
	@Query(value = "select m from Member m", countQuery = "select count(m.username) from Member m")
	Page<Member> findMemberAllCountBy(Pageable pageable);

	@Modifying(clearAutomatically = true) // 쿼리를 실행하고 나서 영속성 컨텍스트 초기화 default false //엥간하면 초기화 하는게 좋음
	@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
	int bulkAgePlus(@Param("age") int age);

	@Query("select m from Member m left join fetch m.team") // JPQL 페치조인 하기 귀찮으니 엔티디 그래프를 이용하여 fetch조인을 가능하게함
	List<Member> findMemberFetchJoin();

	// entityGraph
	// 공통 메서드 오버라이드
	@Override
	@EntityGraph(attributePaths = { "team" })
	List<Member> findAll();

	// JPQL + 엔티티 그래프
	@EntityGraph(attributePaths = { "team" })
	@Query("select m from Member m")
	List<Member> findMemberEntityGraph();

	// 메서드 이름으로 쿼리에서 특히 편리하다.
	@EntityGraph(attributePaths = { "team" })
	List<Member> findEntityGraphByUsername(@Param("username") String username);

//	@EntityGraph("Member.all")
//	@Query("select m from Member m")
//	List<Member> findMemberEntityGraph();
	// 간단한건 엔티디 그래프 쓰고 복잡하면 그냥 jpql 페치조인

	// 리드온니 하면 스냅샵 도 안해서 최적화가 좋음
	@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
	Member findReadOnlyByUsername(String username);

	@QueryHints(value = { @QueryHint(name = "org.hibernate.readOnly", value = "true") }, forCounting = true)
	// 반환 타입으로 Page 인터페이스를 적용하면 추가로 호출하는 페이징을 위한 count 쿼리도 쿼리 힌트 적용 기본값true
	Page<Member> findByUsername(String name, Pageable pageable);
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	List<Member> findLockByUsername(String name); //잘 안쓸거임.. 대게 실시간 서비스면 락걸면안댐 

}