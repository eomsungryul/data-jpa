package study.datajpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter // 실무에서 가급적 Setter는 사용하지 않기
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 막고 싶은데, JPA 스팩상 PROTECTED로 열어두어야 함
@ToString(of = { "id", "username", "age" }) // 연관관계 필드는 toString 별로.. team 추가하면 무한 루프 돌거임
@NamedQuery(name = "Member.findByUsername", query = "select m from Member m where m.username = :username")
@NamedEntityGraph(name = "Member.all", attributeNodes =
@NamedAttributeNode("team"))//이런식으로 바로 붙여서 가능 잘안씀
public class Member extends BaseEntity {
	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;
	private String username;
	private int age;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_id")
	private Team team;

	public Member(String username) {
		this(username, 0);
	}

	public Member(String username, int age) {
		this(username, age, null);
	}

	public Member(String username, int age, Team team) {
		this.username = username;
		this.age = age;
		if (team != null) {
			changeTeam(team); // changeTeam으로 양방향 연관관계 한번에 처리(연관관계 편의 메소드)
		}
	}

	public void changeTeam(Team team) {
		this.team = team;
		team.getMembers().add(this);
	}
}