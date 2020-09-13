package study.datajpa.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = { "id", "name" }) //객체를 찍을때 바로 출력이 됨 
public class Team {
	
	@Id
	@GeneratedValue
	@Column(name = "team_id")
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "team") //무조건 fk 없는 곳에 mappedBy 사용!
	List<Member> members = new ArrayList<>();

	public Team(String name) {
		this.name = name;
	}
}