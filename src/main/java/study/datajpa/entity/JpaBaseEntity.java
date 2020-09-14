package study.datajpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Getter;

//구식
@MappedSuperclass //상속전에 필수적으로 해야함!
@Getter
public class JpaBaseEntity {
	@Column(updatable = false)
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdDate = now;
		updatedDate = now;
	}

	@PreUpdate
	public void preUpdate() {
		updatedDate = LocalDateTime.now();
	}
	
	//주요 이벤트 애노테이션
	//@PrePersist, @PostPersist
	//@PreUpdate, @PostUpdate
	
}
