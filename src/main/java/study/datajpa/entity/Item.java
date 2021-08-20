package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue
    private Long id;

}
